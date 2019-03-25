package com.yz.aac.common.controller;

import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.exception.RpcException;
import com.yz.aac.common.exception.SerializationException;
import com.yz.aac.common.model.request.LoginInfo;
import com.yz.aac.common.model.response.RootResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yz.aac.common.Constants.Misc.PLACEHOLDER;
import static com.yz.aac.common.Constants.Misc.REQUEST_INFO_KEY;
import static com.yz.aac.common.Constants.ResponseMessageInfo.*;
import static com.yz.aac.common.Constants.ResponseStatusInfo.HTTP_OK;
import static org.apache.commons.lang3.StringUtils.isBlank;

@SuppressWarnings("unused")
@Slf4j
@ControllerAdvice
public abstract class BaseController {

    @Getter
    @Autowired
    private HttpServletRequest request;

    @Getter
    @Autowired
    private HttpServletResponse response;

    @Value("${spring.http.multipart.max-file-size}")
    private String maxFileSize;

    @Value("${spring.http.multipart.max-request-size}")
    private String maxRequestSize;

    protected LoginInfo getLoginInfo() {
        return (LoginInfo) request.getAttribute(REQUEST_INFO_KEY.value());
    }

    protected void validateRequired(String fieldName, Object fieldValue) throws BusinessException {
        boolean isValid = true;
        if (null == fieldValue) {
            isValid = false;
        } else if (fieldValue instanceof String && isBlank((String) fieldValue)) {
            isValid = false;
        } else if (fieldValue instanceof MultipartFile && ((MultipartFile) fieldValue).isEmpty()) {
            isValid = false;
        }
        if (!isValid) {
            throw new BusinessException(
                    MSG_REQUIRED_FILED.code(),
                    MSG_REQUIRED_FILED.message().replaceFirst(PLACEHOLDER.value(), fieldName)
            );
        }
    }

    protected void validateSpecialChar(String fieldName, String fieldValue) throws BusinessException {
        if (StringUtils.isBlank(fieldValue)) {
            return;
        }
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(fieldValue);
        if (m.find()) {
            throw new BusinessException(
                    MSG_INVALID_CHAR.code(),
                    MSG_INVALID_CHAR.message()
                            .replaceFirst(PLACEHOLDER.value(), fieldName)
                            .replaceFirst(PLACEHOLDER.value(), fieldValue)
            );
        }
    }

    protected void validateStringLength(String fieldName, String fieldValue, int minLength, int maxLength) throws BusinessException {
        if (fieldValue.trim().length() < minLength || fieldValue.trim().length() > maxLength) {
            throw new BusinessException(
                    MSG_INVALID_FILED_LENGTH.code(),
                    MSG_INVALID_FILED_LENGTH.message()
                            .replaceFirst(PLACEHOLDER.value(), fieldName)
                            .replaceFirst(PLACEHOLDER.value(), String.valueOf(minLength))
                            .replaceFirst(PLACEHOLDER.value(), String.valueOf(maxLength))
            );
        }
    }

    protected void validateIntRange(String fieldName, Integer fieldValue, int minValue, int maxValue) throws BusinessException {
        if (fieldValue < minValue || fieldValue > maxValue) {
            throw new BusinessException(
                    MSG_INVALID_FILED.code(),
                    MSG_INVALID_FILED.message()
                            .replaceFirst(PLACEHOLDER.value(), fieldName)
                            .replaceFirst(PLACEHOLDER.value(), new BigDecimal(fieldValue).toPlainString())
            );
        }
    }

    protected void validateDoubleRange(String fieldName, Double fieldValue, double minValue, double maxValue) throws BusinessException {
        if (fieldValue < minValue || fieldValue > maxValue) {
            throw new BusinessException(
                    MSG_INVALID_FILED.code(),
                    MSG_INVALID_FILED.message()
                            .replaceFirst(PLACEHOLDER.value(), fieldName)
                            .replaceFirst(PLACEHOLDER.value(), new BigDecimal(fieldValue).toPlainString())
            );
        }
    }

    protected void validateBigDecimalRange(String fieldName, BigDecimal fieldValue, BigDecimal minValue, BigDecimal maxValue) throws BusinessException {
        if (fieldValue.compareTo(minValue) < 0 || fieldValue.compareTo(maxValue) > 0) {
            throw new BusinessException(
                    MSG_INVALID_FILED.code(),
                    MSG_INVALID_FILED.message()
                            .replaceFirst(PLACEHOLDER.value(), fieldName)
                            .replaceFirst(PLACEHOLDER.value(), fieldValue.toPlainString())
            );
        }
    }

    protected <T> void validateValueScope(String fieldName, T fieldValue, Set<T> scope) throws BusinessException {
        if (!scope.contains(fieldValue)) {
            throw new BusinessException(
                    MSG_INVALID_FILED.code(),
                    MSG_INVALID_FILED.message()
                            .replaceFirst(PLACEHOLDER.value(), fieldName)
                            .replaceFirst(PLACEHOLDER.value(), fieldValue.toString())
            );
        }
    }

    protected void validateTimePeriod(String beginTimeFieldName, Long beginTime, String endTimeFieldName, Long endTime) throws BusinessException {
        if (null != beginTime && null != endTime && beginTime > endTime) {
            throw new BusinessException(
                    MSG_INVALID_TIME_PERIOD.code(),
                    MSG_INVALID_TIME_PERIOD.message()
                            .replaceFirst(PLACEHOLDER.value(), beginTimeFieldName)
                            .replaceFirst(PLACEHOLDER.value(), endTimeFieldName)
            );
        }
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    protected RootResponse<?> handleException(Exception e) {
        String messageCode, message;
        if (e instanceof BusinessException) {
            BusinessException be = (BusinessException) e;
            messageCode = be.getMessageCode();
            message = be.getMessage();
        } else if (e instanceof MultipartException) {
            MultipartException me = (MultipartException) e;
            messageCode = MSG_INVALID_UPLOAD.code();
            message = MSG_INVALID_UPLOAD.message()
                    .replaceFirst(PLACEHOLDER.value(), maxFileSize)
                    .replaceFirst(PLACEHOLDER.value(), maxRequestSize);
        } else if (e instanceof SerializationException) {
            log.error(e.getMessage(), e);
            messageCode = MSG_SERVER_EXCEPTION.code();
            message = MSG_SERVER_EXCEPTION.message()
                    .replaceFirst(PLACEHOLDER.value(), SerializationException.class.getSimpleName())
                    .replaceFirst(PLACEHOLDER.value(), e.getMessage());
        } else if (e instanceof RpcException) {
            log.error(e.getMessage(), e);
            messageCode = MSG_SERVER_EXCEPTION.code();
            message = MSG_SERVER_EXCEPTION.message()
                    .replaceFirst(PLACEHOLDER.value(), RpcException.class.getSimpleName())
                    .replaceFirst(PLACEHOLDER.value(), e.getMessage());
        } else {
            log.error(e.getMessage(), e);
            messageCode = MSG_SERVER_EXCEPTION.code();
            message = e.getMessage();
        }
        return RootResponse.buildError(HTTP_OK.code(), messageCode, message);
    }

}

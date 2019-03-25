package com.yz.aac.common.interceptor;

import com.yz.aac.common.Constants;
import com.yz.aac.common.config.SecurityConfig;
import com.yz.aac.common.model.request.LoginInfo;
import com.yz.aac.common.model.response.RootResponse;
import com.yz.aac.common.util.EncryptionUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.yz.aac.common.Constants.Misc.REQUEST_INFO_KEY;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_UNAUTHORIZED;
import static com.yz.aac.common.Constants.ResponseStatusInfo.HTTP_OK;
import static com.yz.aac.common.Constants.Token.TOKEN_CLAIM_LOGIN_ID;
import static com.yz.aac.common.Constants.Token.TOKEN_KEY;
import static com.yz.aac.common.model.response.RootResponse.buildError;
import static com.yz.aac.common.util.PathUtil.match;

@SuppressWarnings("unused")
@Aspect
@Component
public class ApiInterceptor {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Around("execution(public * com.yz.aac.*.controller.*.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        LoginInfo loginUser = parseToken(request.getHeader(TOKEN_KEY.value()));
        if (isUnregulatedRequest(securityConfig.getUnregulatedUriPatterns(), uri)) {
            if (null != loginUser) {
                request.setAttribute(REQUEST_INFO_KEY.value(), loginUser);
            }
            result = pjp.proceed();
        } else {
            if (null == loginUser || isRegulatedExternalRequest(securityConfig.getExternalUriPatterns(), uri, loginUser.getId())) {
                result = buildUnauthorizedResponse();
            } else {
                request.setAttribute(REQUEST_INFO_KEY.value(), loginUser);
                result = pjp.proceed();
            }
        }
        return result;
    }

    private boolean isRegulatedExternalRequest(String externalUriPatterns, String requestUri, Long loginUserId) {
        if (StringUtils.isEmpty(externalUriPatterns))
            return false;
        boolean flag = match(externalUriPatterns, requestUri);
        if (loginUserId > 0)
            return flag;
        if (loginUserId == 0 || loginUserId < Arrays.stream(Constants.ExternalServiceNumber.values()).map(x -> x.code()).min(Integer::compareTo).get())
            return true;
        return !flag;
    }

    private boolean isUnregulatedRequest(List<String> unregulatedUriPatterns, String requestUri) {
        boolean result = false;
        if (!CollectionUtils.isEmpty(unregulatedUriPatterns)) {
            for (String pattern : unregulatedUriPatterns) {
                if (match(pattern, requestUri)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private LoginInfo parseToken(String token) {
        LoginInfo result = null;
        if (StringUtils.isNotBlank(token)) {
            Map<String, Object> info = EncryptionUtil.parseToken(token, securityConfig.getTokenSecurityKey());
            Object loginId = null == info ? null : info.get(TOKEN_CLAIM_LOGIN_ID.value());
            result = (null == loginId ? null : new LoginInfo(Long.parseLong(loginId.toString())));
        }
        return result;
    }

    private RootResponse<?> buildUnauthorizedResponse() {
        return buildError(HTTP_OK.code(), MSG_UNAUTHORIZED.code(), MSG_UNAUTHORIZED.message());
    }
}

package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.opadmin.model.request.QuerySignInParticipatorRequest;
import com.yz.aac.opadmin.model.response.QueryQuestionParticipatorResponse;
import com.yz.aac.opadmin.model.response.QuerySignInParticipatorResponse;
import com.yz.aac.opadmin.repository.UserMiningRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.yz.aac.common.Constants.Misc.USER_CODE_PREFIX;
import static com.yz.aac.common.Constants.ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION;
import static com.yz.aac.opadmin.Constants.ErrorMessage.INVALID_USER_CODE;

@Service
@Slf4j
public class SignInServiceImpl implements SignInService {

    @Autowired
    private UserMiningRecordRepository userMiningRecordRepository;

    @Override
    public QuerySignInParticipatorResponse queryParticipators(QuerySignInParticipatorRequest request) throws Exception {
        Long userId;
        try {
            userId = StringUtils.isBlank(request.getCode()) ? null : Long.parseLong(request.getCode().trim().replaceFirst(USER_CODE_PREFIX.value(), ""));
        } catch (Exception e) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_USER_CODE.value());
        }
        request.setId(userId);
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<QuerySignInParticipatorResponse.Item> page = (Page<QuerySignInParticipatorResponse.Item>) userMiningRecordRepository.querySignInParticipators(request);
        List<QuerySignInParticipatorResponse.Item> items = page.getResult().stream()
                .map(x -> new QuerySignInParticipatorResponse.Item(
                        USER_CODE_PREFIX.value() + x.getUserCode(),
                        x.getUserName(),
                        x.getCrtTime(),
                        x.getTotalSignIn(),
                        x.getPowerPointBonus()
                )).collect(Collectors.toList());
        return new QuerySignInParticipatorResponse(page.getTotal(), items);
    }
}

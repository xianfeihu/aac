package com.yz.aac.opadmin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.opadmin.model.request.QueryInvitationParticipatorRequest;
import com.yz.aac.opadmin.model.response.QueryInvitationParticipatorResponse;
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
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    private UserMiningRecordRepository userMiningRecordRepository;

    @Override
    public QueryInvitationParticipatorResponse queryParticipators(QueryInvitationParticipatorRequest request) throws Exception {
        Long userId;
        try {
            userId = StringUtils.isBlank(request.getCode()) ? null : Long.parseLong(request.getCode().trim().replaceFirst(USER_CODE_PREFIX.value(), ""));
        } catch (Exception e) {
            throw new BusinessException(MSG_CUSTOMIZED_EXCEPTION.code(), INVALID_USER_CODE.value());
        }
        request.setId(userId);
        request.setName(StringUtils.isBlank(request.getName()) ? null : request.getName().trim());
        PageHelper.startPage(request.getPaging().getPageNumber(), request.getPaging().getPageSize());
        Page<QueryInvitationParticipatorResponse.Item> page = (Page<QueryInvitationParticipatorResponse.Item>) userMiningRecordRepository.queryInvitationParticipators(request);
        List<QueryInvitationParticipatorResponse.Item> items = page.getResult().stream()
                .map(x -> new QueryInvitationParticipatorResponse.Item(
                        USER_CODE_PREFIX.value() + x.getUserCode(),
                        x.getUserName(),
                        x.getInvitingNumber(),
                        x.getTotalBonus(),
                        x.getInviteeTotalBonus()
                )).collect(Collectors.toList());
        return new QueryInvitationParticipatorResponse(page.getTotal(), items);
    }
}

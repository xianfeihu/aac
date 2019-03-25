package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.QueryInvitationParticipatorRequest;
import com.yz.aac.opadmin.model.response.QueryInvitationParticipatorResponse;

public interface InvitationService {

    /**
     * 查询参与用户
     */
    QueryInvitationParticipatorResponse queryParticipators(QueryInvitationParticipatorRequest request) throws Exception;


}

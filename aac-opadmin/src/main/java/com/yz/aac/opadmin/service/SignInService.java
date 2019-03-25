package com.yz.aac.opadmin.service;

import com.yz.aac.opadmin.model.request.QuerySignInParticipatorRequest;
import com.yz.aac.opadmin.model.response.QuerySignInParticipatorResponse;

public interface SignInService {

    /**
     * 查询参与用户
     */
    QuerySignInParticipatorResponse queryParticipators(QuerySignInParticipatorRequest request) throws Exception;


}

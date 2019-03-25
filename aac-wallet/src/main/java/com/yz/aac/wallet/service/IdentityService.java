package com.yz.aac.wallet.service;

import com.yz.aac.wallet.model.response.IdCardResponse;

/**
 * 身份证实名认证服务
 * @author Xian.FeiHu
 *
 */
public interface IdentityService {

	IdCardResponse Authentication(String code, String personName) throws Exception;
}

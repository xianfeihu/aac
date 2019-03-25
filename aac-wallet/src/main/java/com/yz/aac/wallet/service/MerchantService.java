package com.yz.aac.wallet.service;

import com.yz.aac.wallet.model.request.AABOrderInfoRequest;
import com.yz.aac.wallet.model.response.AABOrderInfoResponse;
import com.yz.aac.wallet.model.response.MerchantDepositStatusResponse;


/**
 * APP商户押金服务
 *
 */
public interface MerchantService {

	/**
	 * 获取押金订单信息
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	AABOrderInfoResponse getDepositMes(Long userId) throws Exception;
	
	/**
	 * 提交押金订单
	 * @param aabOrderInfoRequest
	 * @return
	 * @throws Exception
	 */
	AABOrderInfoResponse addDepositOrder(AABOrderInfoRequest aabOrderInfoRequest) throws Exception ;
	
	/**
	 * 查询商户押金状态
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	MerchantDepositStatusResponse getDepositStatus(Long userId) throws Exception ;
	
}

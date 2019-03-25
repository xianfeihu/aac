package com.yz.aac.mining.service;

import com.yz.aac.mining.model.PageResult;
import com.yz.aac.mining.model.request.GrowthRequest;
import com.yz.aac.mining.model.request.WechatCheckCodeRequest;
import com.yz.aac.mining.model.response.FocusOnWechatInfoResponse;
import com.yz.aac.mining.model.response.HomeInfoResponse;
import com.yz.aac.mining.model.response.InviteFriendsParamResponse;
import com.yz.aac.mining.model.response.LevelInfoResponse;
import com.yz.aac.mining.model.response.MiningRecordResponse;
import com.yz.aac.mining.model.response.PlatformCurrencyInfoResponse;
import com.yz.aac.mining.model.response.PowerPointInfoResponse;
import com.yz.aac.mining.model.response.SignInPointResponse;



/**
 * APP用户基本服务
 *
 */
public interface AccountService {

	/**
	 * 首页数据详情
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	HomeInfoResponse getMyHomeInfo(Long userId) throws Exception;
	
	/**
	 * 平台币能量收取
	 * @param growthRequest
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String collectingEnergy(GrowthRequest growthRequest, Long userId) throws Exception;
	
	/**
	 * 获取用户元力详情
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	PowerPointInfoResponse getPowerPointInfo(Long userId) throws Exception;
	
	/**
	 * 挖矿记录列表
	 * @param pageNo 当前页
	 * @param pageSize 每页条数
	 * @param userId 用户ID
	 * @param bonusType 奖励类型
	 * @return
	 * @throws Exception
	 */
	PageResult<MiningRecordResponse> getMiningRecordList(Integer pageNo, Integer pageSize, Long userId, Integer bonusType) throws Exception;
	
	/**
	 * 获取平台币简介
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	PlatformCurrencyInfoResponse getPlatformCurrencyInfo(Long userId) throws Exception;
	
	/**
	 * 获取当前用户等级信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	LevelInfoResponse getLevelInfo(Long userId) throws Exception;
	
	/**
	 * 获取关注公众号详情
	 * @return
	 * @throws Exception
	 */
	FocusOnWechatInfoResponse getFocusOnWechatInfo(Long userId) throws Exception;
	
	/**
	 * 校验微信公众验证码
	 * @param wechatCheckCodeRequest
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	boolean wechatCheckCode(WechatCheckCodeRequest wechatCheckCodeRequest, Long userId) throws Exception;
	
	/**
	 * 签到
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	SignInPointResponse signInPoint(Long userId) throws Exception;
	
	/**
	 * 邀请好友-获取分享参数
	 * @return
	 * @throws Exception
	 */
	InviteFriendsParamResponse getInviteFriendsParam() throws Exception;
	
}

package com.yz.aac.wallet.service.impl;

import com.yz.aac.common.exception.RpcException;
import com.yz.aac.common.util.EncodeUtil;
import com.yz.aac.common.util.HttpUtil;
import com.yz.aac.common.util.JsonUtil;
import com.yz.aac.wallet.Constants.YunXinErrorCodeEnum;
import com.yz.aac.wallet.config.YunXinConfig;
import com.yz.aac.wallet.model.response.SmsResponse;
import com.yz.aac.wallet.repository.UserSmsCodeRepository;
import com.yz.aac.wallet.repository.domain.UserSmsCode;
import com.yz.aac.wallet.service.SmsService;
import okhttp3.Headers;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class SmsServiceImpl implements SmsService{
	
	//验证码短信模板
	private static final int TEMPLATE_ID = 9374568;
	
	//默认验证码长度
	private static final int DEFAULT_CODE_LEN = 4;
	
	@Autowired
	private YunXinConfig yunXinConfig;
	
	@Autowired
	private UserSmsCodeRepository userSmsCodeRepository;

	@Override
	public SmsResponse sendCode(String mobile, Integer authCode) throws Exception {
		
		Map<String, Object> postMap = new HashMap<String, Object>();
		
		postMap.put("mobile", mobile);
//		postMap.put("templateid", TEMPLATE_ID);
		postMap.put("codeLen", DEFAULT_CODE_LEN);
		if (null != authCode) {
			postMap.put("authCode", authCode);
		}
		
		Pair<Boolean, String> pair = buildSmsHttpParam(postMap, yunXinConfig.getSendCodeUrl());
		
		return JsonUtil.jsonToBean(pair.getRight().toString(), SmsResponse.class);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public SmsResponse verifycode(String mobile, String code, Integer type) throws Exception{
		
//		Map<String, Object> postMap = new HashMap<String, Object>();
//		
//		postMap.put("mobile", mobile);
//		postMap.put("code", code);
//		
//		Pair<Boolean, String> pair = buildSmsHttpParam(postMap, yunXinConfig.getVerfycodeUrl());
//		
//		return JsonUtil.jsonToBean(pair.getRight().toString(), SmsResponse.class);
		
		int sendCode = YunXinErrorCodeEnum.CODE_OK.errorCode();
		
		//本地校验验证码
		UserSmsCode smsCode = userSmsCodeRepository.getByMobile(Long.valueOf(mobile), type);
		if (null == smsCode) {
			sendCode = YunXinErrorCodeEnum.CODE_414.errorCode();
		} else {
			if (((System.currentTimeMillis() - smsCode.getSendTime()) / (1000 * 60)) >= 10) {
				sendCode = YunXinErrorCodeEnum.CODE_414.errorCode();
			} else {
				sendCode = (smsCode.getCode().equals(code) ? sendCode : YunXinErrorCodeEnum.CODE_404.errorCode());
			}
		}

		//删除
		userSmsCodeRepository.deleteByMobile(Long.valueOf(mobile), type);

		return new SmsResponse(sendCode, "-1", code);
		
	}
	
	/**
	 * 构建封装Client
	 * @param postMap
	 * @param url
	 * @return
	 * @throws RpcException 
	 */
	public Pair<Boolean, String> buildSmsHttpParam(Map<String, Object> postMap, String url) throws RpcException{
		Map<String, String> headerMap = new HashMap<String, String>();
		
		String nonce =  EncodeUtil.getMD5(String.valueOf(Math.random()*1000));
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = EncodeUtil.getSha1(yunXinConfig.getSecret() + nonce + curTime);//参考 计算CheckSum
        
		headerMap.put("AppKey", yunXinConfig.getApikey());
		headerMap.put("Nonce", nonce);
		headerMap.put("CurTime", String.valueOf(curTime));
		headerMap.put("CheckSum", checkSum);
		
		@SuppressWarnings("rawtypes")
		Function<Map<String, Object>,String> fun = ma -> {
			StringBuilder sb = new StringBuilder();
	        for (Map.Entry i : ma.entrySet()) {
	            sb.append(i.getKey()).append("=").append(i.getValue()).append("&");
	        }
	        return sb.toString();
		};
		
		return HttpUtil.post(HttpUtil.createClient(), MediaType.APPLICATION_FORM_URLENCODED_VALUE, url, fun.apply(postMap), Headers.of(headerMap));
	}

}

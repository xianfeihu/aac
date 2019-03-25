package com.yz.aac.wallet.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.yz.aac.common.Constants.ResponseMessageInfo;
import com.yz.aac.common.exception.BusinessException;
import com.yz.aac.common.util.HttpUtil;
import com.yz.aac.common.util.JsonUtil;
import com.yz.aac.wallet.Constants.JuheErrorCodeEnum;
import com.yz.aac.wallet.config.IdCardConfig;
import com.yz.aac.wallet.model.request.IdCardRpcRequest;
import com.yz.aac.wallet.model.response.IdCardResponse;
import com.yz.aac.wallet.service.IdentityService;

@Slf4j
@Service
public class IdentityServiceImpl implements IdentityService{
	
	@Autowired
	private IdCardConfig idCardConfig;

	@Override
	public IdCardResponse Authentication(String code, String personName) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", idCardConfig.getApikey());
		map.put("idcard", code);
		map.put("realname", URLEncoder.encode(personName,"UTF-8"));
		
		@SuppressWarnings("rawtypes")
		Function<Map<String, Object>,String> fun = ma -> {
			StringBuilder sb = new StringBuilder();
	        for (Map.Entry i : ma.entrySet()) {
	            sb.append(i.getKey()).append("=").append(i.getValue()).append("&");
	        }
	        return sb.toString();
		};
		
		Pair<Boolean, String> pair = HttpUtil.post(HttpUtil.createClient(), MediaType.APPLICATION_FORM_URLENCODED_VALUE, idCardConfig.getQueryUrl(), fun.apply(map), null);
		
		IdCardRpcRequest cardDate = JsonUtil.jsonToBean(pair.getRight(),IdCardRpcRequest.class); 
		
		if (JuheErrorCodeEnum.CODE_OK.errorCode() == Integer.valueOf(cardDate.getError_code())) {
			IdCardResponse idCardResponse =cardDate.getResult();
			if (idCardResponse.getRes() == 2) {
				throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "实名认证不匹配");
			}
			return cardDate.getResult();
		} else {
			log.error("身份证实名认证第三方接口参数错误：" + Integer.valueOf(cardDate.getError_code()));
	        throw new BusinessException(ResponseMessageInfo.MSG_CUSTOMIZED_EXCEPTION.code(), "实名认证不匹配");
		}
	}

}

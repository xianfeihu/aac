package com.yz.aac.mining.service.impl;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yz.aac.common.util.RandomUtil;
import com.yz.aac.mining.model.WeiObj;
import com.yz.aac.mining.repository.WeChatVerificationCodeRepository;
import com.yz.aac.mining.service.WeChatService;
import com.yz.aac.mining.util.WeChatXmlUtil;

@Slf4j
@Service
public class WeChatServiceImpl implements WeChatService{
	
	@Autowired
	private WeChatVerificationCodeRepository weChatVerificationCodeRepository;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public String PushManageXml(InputStream is, HttpServletRequest request)
			throws Exception {

		WeiObj weiObj = new WeiObj();
		String returnStr = ""; // 返回Servlet字符串
		weiObj = WeChatXmlUtil.getWeiXml(is, weiObj);// 解析获得微信推送的xml
		if (weiObj.getMsgType().equals("event")) { // 此为事件
			if (weiObj.getEvent().equals("subscribe")) {// 用户为关注点击关注事件
				// 验证码生成
				boolean condition = true;
				String code = null;
				while (condition) {
					try {
						code = RandomUtil.createNumber(6);
						weChatVerificationCodeRepository
								.saveVerificationCode(code);
						condition = false;
					} catch (DuplicateKeyException du) {
						condition = true;
						log.info("微信公众号验证码重复，重新生成！");
					}
				}
				returnStr = WeChatXmlUtil.getBackXMLTypeText(
						weiObj.getToUserName(), weiObj.getFromUserName(), code);
			}
			return returnStr;
		}
		return null;
	}
}

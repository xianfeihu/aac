package com.yz.aac.mining.service;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;



/**
 *微信公众号基本服务
 *
 */
public interface WeChatService {

	String PushManageXml(InputStream is,HttpServletRequest request) throws Exception;
	
}

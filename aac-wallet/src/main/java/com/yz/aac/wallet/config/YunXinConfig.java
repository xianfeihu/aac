package com.yz.aac.wallet.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 身份实名认证信息
 * @author Xian.FeiHu
 *
 */

@Configuration
@ConfigurationProperties("yunxin")
@Order(1)
@Data
public class YunXinConfig {

	private String apikey;
	
	private String secret;
	
	private String sendCodeUrl;
	
	private String verfycodeUrl;
	
}

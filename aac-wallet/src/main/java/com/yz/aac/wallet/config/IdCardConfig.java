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
@ConfigurationProperties("idcard")
@Order(1)
@Data
public class IdCardConfig {

	private String queryUrl;
	
	private String apikey;

}

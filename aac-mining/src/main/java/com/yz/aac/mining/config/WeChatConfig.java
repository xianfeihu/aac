package com.yz.aac.mining.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 微信公众号平台信息
 *
 */

@Configuration
@ConfigurationProperties("weChat")
@Order(1)
@Data
public class WeChatConfig {

	private String token;

}

package com.yz.aac.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ConfigurationProperties("api")
@Order(1)
@Data
public class ApiConfig {

    private String basePackage;

    private boolean enableDocument;

}

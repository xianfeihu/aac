package com.yz.aac.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ConfigurationProperties("fileStorage")
@Order(1)
@Data
public class FileStorageConfig {

    private boolean enabled;

    private String host;

    private int port;

    private int httpPort;

    private String securityKey;

}

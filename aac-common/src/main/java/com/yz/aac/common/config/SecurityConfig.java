package com.yz.aac.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Configuration
@ConfigurationProperties("security")
@Order(1)
@Data
public class SecurityConfig {

    private List<String> disabledUriPatterns;

    private List<String> unregulatedUriPatterns;

    private String externalUriPatterns;

    private String tokenSecurityKey;

    @PostConstruct
    private void init() {
        disabledUriPatterns = Arrays.stream(new String[]{
                "/druid/**"
        }).collect(Collectors.toList());
    }

}

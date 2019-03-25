package com.yz.aac.common.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

@Data
@Order(1)
@Configuration
@EnableScheduling
@ConfigurationProperties("job")
@ConditionalOnProperty(name = "job.enabled", havingValue = "true")
public class SchedulerConfig implements SchedulingConfigurer {

    private Integer corePoolSize = 1;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(corePoolSize));
    }
}

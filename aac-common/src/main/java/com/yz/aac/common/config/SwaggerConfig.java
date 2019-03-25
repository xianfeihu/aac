package com.yz.aac.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.yz.aac.common.Constants.Misc.API_DOC_TITLE;


@SuppressWarnings("unused")
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Autowired
    private ApiConfig apiConfig;

    @Bean
    public Docket buildDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(apiConfig.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(buildApiInfo())
                .enable(apiConfig.isEnableDocument());
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title(API_DOC_TITLE.value())
                .build();
    }
}

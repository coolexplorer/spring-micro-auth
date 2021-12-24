package io.coolexplorer.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    private static final String BASE_PACKAGE = "io.coolexplorer.auth.controller";
    private static final String API_INFO_TITLE = "Auth ";
    private static final String API_INFO_DESCRIPTION = "Auth API document";
    private static final String API_VERSION = "v1";
    private final String version;

    public SwaggerConfig(@Value("${server.version}") String version) {
        this.version = version;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(String.format("%s %s", API_INFO_TITLE, version))
                .description(API_INFO_DESCRIPTION)
                .version(API_VERSION)
                .build();
    }
}

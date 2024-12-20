package com.furkantufan.courier.tracking.common.config.swagger;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:open-api.properties")
@ConfigurationProperties("swagger")
public class SwaggerProperties {

    private String apiPath;

    private Info info;

    private Contact contact;

    @Data
    static class Contact {

        private String name;

        private String email;
    }


    @Data
    static class Info {

        private String title;

        private String description;

        private String version;
    }
}

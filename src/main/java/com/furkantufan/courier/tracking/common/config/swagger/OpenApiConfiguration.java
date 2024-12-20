package com.furkantufan.courier.tracking.common.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SwaggerProperties.class)
@RequiredArgsConstructor
public class OpenApiConfiguration {

    private final SwaggerProperties swaggerProperties;

    @Bean
    public OpenAPI openApiInfo() {
        return new OpenAPI().info(info());
    }

    private Info info() {
        return new Info()
                .contact(contact())
                .title(swaggerProperties.getInfo().getTitle())
                .version(swaggerProperties.getInfo().getVersion())
                .description(swaggerProperties.getInfo().getDescription());
    }

    private Contact contact() {
        return new Contact()
                .email(swaggerProperties.getContact().getEmail())
                .name(swaggerProperties.getContact().getName());
    }
}

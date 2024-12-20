package com.furkantufan.courier.tracking.common.config.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class MessageSourceConfiguration {

    @Bean
    public static MessageSource messageSource() {

        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    public static String getMessage(String message, String... dynamicValues) {
        var locale = new Locale.Builder().setLanguage("tr").setRegion("TR").build();
        return messageSource().getMessage(message, dynamicValues, locale);
    }
}

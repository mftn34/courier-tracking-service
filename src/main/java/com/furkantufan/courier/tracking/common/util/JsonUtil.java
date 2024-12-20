package com.furkantufan.courier.tracking.common.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class JsonUtil {

    public static String getJsonPropertyName(Class<?> clazz, String fieldName) {
        try {
            var field = clazz.getDeclaredField(fieldName);
            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            if (jsonProperty != null) {
                return jsonProperty.value();
            }
        } catch (Throwable t) {
            log.error("No such property: {} in class: {}", fieldName, clazz.getName());
        }
        return fieldName;
    }
}

package com.furkantufan.courier.tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy
public class CourierTrackingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourierTrackingServiceApplication.class, args);
    }

}

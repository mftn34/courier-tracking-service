package com.furkantufan.courier.tracking.common.async;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThreadProperties {

    private String name;

    private int corePoolSize;

    private int queueCapacity;

    private int maxPoolSize;
}
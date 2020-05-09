package com.avisow.spring5.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "bean")
@Data
public class MyConfig {
    private String message = "a message that can be changed live";
}

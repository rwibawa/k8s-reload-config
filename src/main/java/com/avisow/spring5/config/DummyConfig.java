package com.avisow.spring5.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "dummy")
@Data
public class DummyConfig {
    private String message = "this is a dummy message";
}

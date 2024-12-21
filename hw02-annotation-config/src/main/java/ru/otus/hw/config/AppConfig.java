package ru.otus.hw.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@PropertySource(value = "classpath:testConfig.yaml", factory = YamlPropertySourceFactory.class)
@ComponentScan(basePackages = "ru.otus.hw")
public class AppConfig {
}

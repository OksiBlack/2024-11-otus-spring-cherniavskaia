package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.config.AppProperties;

@Configuration
@ComponentScan(basePackages = "ru.otus.hw")
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class Hw03Application {
    public static void main(String[] args) {
        SpringApplication.run(Hw03Application.class, args);
    }
}
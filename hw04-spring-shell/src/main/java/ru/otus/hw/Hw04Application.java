package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.annotation.CommandScan;
import ru.otus.hw.config.AppProperties;

@Configuration
@ComponentScan(basePackages = "ru.otus.hw")
@EnableConfigurationProperties(AppProperties.class)
@CommandScan
@SpringBootApplication
public class Hw04Application {

    public static void main(String[] args) {
        SpringApplication.run(Hw04Application.class, args);

    }
}
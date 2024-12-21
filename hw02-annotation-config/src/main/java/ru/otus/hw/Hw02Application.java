package ru.otus.hw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.service.TestRunnerService;

@Slf4j
@ComponentScan(basePackages = "ru.otus.hw")
@Configuration
public class Hw02Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Hw02Application.class);

        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}
package ru.otus.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.hw.config.YamlPropertySourceFactory;
import ru.otus.hw.service.TestRunnerService;

@Configuration
@ComponentScan(basePackages = "ru.otus.hw")
@PropertySource("classpath:application.properties")
@PropertySource(value = "classpath:testConfig.yaml", factory = YamlPropertySourceFactory.class)
public class Hw02Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Hw02Application.class);

        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();
    }
}
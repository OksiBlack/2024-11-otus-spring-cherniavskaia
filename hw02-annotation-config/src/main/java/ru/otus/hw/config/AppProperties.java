package ru.otus.hw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppProperties implements TestConfig, TestFileNameProvider {

    private int rightAnswerPercentToPass;

    private String testFileName;

    public AppProperties(
        @Value("${test.rightAnswerPercentToPass}") int rightAnswerPercentToPass,
        @Value("${test.fileName}") String testFileName) {
        this.testFileName = testFileName;
        this.rightAnswerPercentToPass = rightAnswerPercentToPass;
    }
}

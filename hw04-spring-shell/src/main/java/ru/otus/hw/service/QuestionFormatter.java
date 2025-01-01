package ru.otus.hw.service;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

public class QuestionFormatter {
    private QuestionFormatter() {
    }


    public static String formatQuestion(Question question) {
        StringBuilder result = new StringBuilder();

        List<Answer> answers = question.answers();
        for (int num = 0; num < answers.size(); num++) {
            Answer answer = answers.get(num);
            result.append(String.format("%d) %s%n", num + 1, answer.text()));
        }
        return result.toString();
    }
}

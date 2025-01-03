package ru.otus.hw.service;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@Component
public class QuestionFormatterImpl implements QuestionFormatter {

    @Override
    public String formatQuestion(Question question) {
        StringBuilder result = new StringBuilder();
        result.append(System.lineSeparator()).append(question.text()).append("\n\nOptions: \n");

        List<Answer> answers = question.answers();
        for (int num = 0; num < answers.size(); num++) {
            Answer answer = answers.get(num);
            result.append(String.format("%d) %s%n", num + 1, answer.text()));
        }

        return result.toString();
    }
}

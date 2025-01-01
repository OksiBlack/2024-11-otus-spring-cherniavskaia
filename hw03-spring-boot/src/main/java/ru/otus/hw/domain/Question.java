package ru.otus.hw.domain;

import java.util.ArrayList;
import java.util.List;

public record Question(String text, List<Answer> answers) {
    public List<AnswerWithPosition> getCorrectAnswersWithPositions() {

        List<AnswerWithPosition> result = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {

            Answer answer = answers.get(i);
            if (answer.isCorrect()) {
                AnswerWithPosition answerWithPosition = new AnswerWithPosition(answer, i + 1);
                result.add(answerWithPosition);
            }

        }
        return result;
    }
}

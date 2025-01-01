package ru.otus.hw.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class TestResult {
    private final Student student;

    private final List<Question> answeredQuestions;

    private int rightAnswersCount;

    public TestResult(Student student) {
        this.student = student;
        this.answeredQuestions = new ArrayList<>();
    }

    public void applyAnswer(Question question, boolean isRightAnswer) {
        answeredQuestions.add(question);
        if (isRightAnswer) {
            rightAnswersCount++;
        }
    }

    public boolean isAnswersCorrect(Question question, Set<Integer> answers) {

        return question.getCorrectAnswersWithPositions().stream()

            .map(AnswerWithPosition::position)
            .collect(Collectors.toSet())
            .equals(answers);
    }
}

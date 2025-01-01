package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {

            ioService.printLine(formatQuestion(question));
            Set<Integer> answerNumbers = ioService.readAllIntsForRange(1,
                question.answers().size(),
                "Exception out of range"
            );

            var isAnswerValid = testResult.isAnswersCorrect(question, answerNumbers);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private String formatQuestion(Question question) {
        StringBuilder result = new StringBuilder();

        List<Answer> answers = question.answers();
        for (int num = 0; num < answers.size(); num++) {
            Answer answer = answers.get(num);
            result.append(String.format("%d) %s%n", num + 1, answer.text()));
        }
        return result.toString();
    }

}

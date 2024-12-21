package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionFormatter questionFormatter;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please insert your first name");
        ioService.printFormattedLine("Please answer the questions below:%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printFormattedLine(questionFormatter.formatQuestion(question));

            Set<Integer> answerNumbers = ioService.readAllIntsForRange(1, question.answers().size());

            var isAnswerValid = testResult.isAnswersCorrect(question, answerNumbers);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
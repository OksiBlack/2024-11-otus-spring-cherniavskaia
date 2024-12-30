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

            Set<Integer> answerNumbers = ioService.readAllIntsForRangeWithPrompt(1,
                question.answers().size(),
                QuestionFormatter.formatQuestion(question),
                "Exception.out.of.range"
            );

            var isAnswerValid = testResult.isAnswersCorrect(question, answerNumbers);
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

}

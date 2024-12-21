package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final TestConfig testConfig;

    private final IOService ioService;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printLine("");
        ioService.printLine("Test results: ");
        ioService.printFormattedLine("Student: %s", testResult.getStudent().getFullName());
        int answersOverall = testResult.getAnsweredQuestions().size();
        ioService.printFormattedLine("Answered questions count: %d", answersOverall);
        int rightAnswersCount = testResult.getRightAnswersCount();
        ioService.printFormattedLine("Right answers count: %d", rightAnswersCount);

        if (getActualRightAnswersPercent(rightAnswersCount, answersOverall) >=
            testConfig.getRightAnswerPercentToPass()
        ) {
            ioService.printLine("Congratulations, you have passed the test!!!");
            return;
        }
        ioService.printLine("Sorry. You have failed the test.");
    }

    private double getActualRightAnswersPercent(int rightAnswersCount, int answersOverall) {
        return 100.0 * rightAnswersCount / answersOverall;
    }
}

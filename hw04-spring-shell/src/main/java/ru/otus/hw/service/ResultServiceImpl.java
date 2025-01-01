package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final TestConfig testConfig;

    private final LocalizedIOService ioService;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printLine("");
        ioService.printLineLocalized("ResultService.test.results");
        ioService.printFormattedLineLocalized("ResultService.student", testResult.getStudent().getFullName());

        int answersOverall = testResult.getAnsweredQuestions().size();

        ioService.printFormattedLineLocalized("ResultService.answered.questions.count", answersOverall);

        int rightAnswersCount = testResult.getRightAnswersCount();
        ioService.printFormattedLineLocalized("ResultService.right.answers.count",
            rightAnswersCount);

        double rightAnswersPercent = getActualRightAnswersPercent(rightAnswersCount, answersOverall);

        ioService.printFormattedLineLocalized("ResultService.right.answers.percent",
            rightAnswersPercent + "%");

        if (rightAnswersPercent >= testConfig.getRightAnswerPercentToPass()) {
            ioService.printLineLocalized("ResultService.passed.test");
            return;
        }
        ioService.printLineLocalized("ResultService.fail.test");
    }

    private double getActualRightAnswersPercent(int rightAnswersCount, int answersOverall) {
        return 100.0 * rightAnswersCount / answersOverall;
    }
}

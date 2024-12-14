package ru.otus.hw.service;

import static ru.otus.hw.constant.FormattingConstants.EMPTY;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

public class QuestionPrinterImpl implements QuestionPrinter {
    private final IOService ioService;

    public QuestionPrinterImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public void printQuestion(Question question) {
        ioService.printLine(EMPTY);
        ioService.printFormattedLine(question.text());
        ioService.printLine(EMPTY);
        ioService.printLine("Options:");
        ioService.printLine(EMPTY);

        question.answers().forEach(this::printAnswerText);
    }

    private void printAnswerText(Answer answer) {
        ioService.printLine("- " + answer.text());
    }
}

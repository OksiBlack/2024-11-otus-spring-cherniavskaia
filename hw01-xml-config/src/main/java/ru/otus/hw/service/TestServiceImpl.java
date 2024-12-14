package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;

import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionFormatter questionFormatter;

    @Override
    public void executeTest() {
        ioService.printFormattedLine("Please answer the questions below:");
        List<Question> all = questionDao.findAll();

        List<String> questionsFormatted =
                all.stream().map(questionFormatter::formatQuestion).toList();

        questionsFormatted.forEach(ioService::printFormattedLine);
    }
}

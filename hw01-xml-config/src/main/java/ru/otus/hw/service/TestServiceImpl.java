package ru.otus.hw.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionPrinter questionPrinter;

    @Override
    public void executeTest() {
        ioService.printFormattedLine("Please answer the questions below:");
        List<Question> all = questionDao.findAll();

        all.forEach(questionPrinter::printQuestion);
    }
}

package ru.otus.hw.dao;

import java.util.List;
import ru.otus.hw.domain.Question;

public interface QuestionDao {
    List<Question> findAll();
}

package ru.otus.hw.dao.dto;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@Data
public class QuestionDto {

    @CsvBindByPosition(position = 0)
    private String text;

    @CsvBindAndSplitByPosition(
            position = 1,
            collectionType = ArrayList.class,
            elementType = Answer.class,
            converter = AnswerCsvConverter.class,
            splitOn = "\\|")
    private List<Answer> answers;

    public Question toDomainObject() {
        return new Question(text, answers);
    }
}

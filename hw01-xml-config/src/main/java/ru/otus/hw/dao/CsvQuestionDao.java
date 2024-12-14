package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;

import lombok.RequiredArgsConstructor;

import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException Про ресурсы:
        // https://mkyong.com/java/java-read-a-file-from-resources-folder
        ClassLoader classLoader = getClass().getClassLoader();
        String fileName = fileNameProvider.getTestFileName();

        try (InputStream is = classLoader.getResourceAsStream(fileNameProvider.getTestFileName())) {

            List<QuestionDto> questionDtos = new CsvToBeanBuilder<QuestionDto>(new InputStreamReader(is))
                            .withType(QuestionDto.class)
                            .withSeparator(';')
                            .withSkipLines(1)
                            .build()
                            .parse();

            return questionDtos.stream().map(QuestionDto::toDomainObject).toList();
        } catch (IOException exception) {
            throw new QuestionReadException(
                    String.format("Error on reading resource %s", fileName), exception);
        }
    }
}

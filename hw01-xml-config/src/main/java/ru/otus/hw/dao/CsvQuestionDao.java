package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

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
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new QuestionReadException(
                    String.format("Unable to load resource:  %s", fileName));
        } else {
            try {
                File file = new File(resource.toURI());
                List<QuestionDto> questionDtos = readQuestionsFromCsv(file);

                return questionDtos.stream().map(QuestionDto::toDomainObject).toList();
            } catch (URISyntaxException e) {
                throw new QuestionReadException(
                        String.format("Incorrect uri syntax for %s", fileName), e);
            } catch (FileNotFoundException e) {
                throw new QuestionReadException(String.format("File not found %s", fileName), e);
            }
        }
    }

    private static List<QuestionDto> readQuestionsFromCsv(File file) throws FileNotFoundException {
        return new CsvToBeanBuilder<QuestionDto>(new FileReader(file))
                .withType(QuestionDto.class)
                .withSeparator(';')
                .withSkipLines(1)
                .build()
                .parse();
    }
}

package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.exception.QuestionReadException;

import static org.assertj.core.api.Assertions.assertThatList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    @Mock
    private AppProperties appProperties;

    private QuestionDao dao;

    @BeforeEach
    void setUp() {
        dao = new CsvQuestionDao(appProperties);
    }

    @DisplayName("Should throw exception when test file name property is missing")
    @Test
    void shouldThrowExceptionExceptionWhenTestFileNamePropertyIsMissing() {
        assertThrows(IllegalArgumentException.class, dao::findAll);
    }

    @DisplayName("Should throw exception when InputStream is null")
    @Test
    void shouldThrowQuestionReadExceptionException() {
        given(appProperties.getTestFileName()).willReturn("notExisting.csv");
        assertThrows(QuestionReadException.class, dao::findAll);
    }

    @DisplayName("Should throw exception on wrong question format")
    @Test
    void shouldThrowUnsupportedQuestionFormatException() {
        given(appProperties.getTestFileName()).willReturn("error.test.questions.csv");
        assertThrows(QuestionReadException.class, dao::findAll);
    }

    @DisplayName("Should execute successfully")
    @Test
    void shouldNotThrowExceptions() {
        given(appProperties.getTestFileName()).willReturn("test.questions.csv");
        assertThatList(dao.findAll()).isNotNull().isNotEmpty()
            .hasSize(2);
    }
}
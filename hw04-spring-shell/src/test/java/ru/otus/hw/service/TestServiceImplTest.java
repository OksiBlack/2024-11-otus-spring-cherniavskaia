package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;

@SpringBootTest
class TestServiceImplTest {

    @MockitoBean
    private LocalizedIOService ioService;

    @MockitoBean
    private QuestionDao questionDao;

    @MockitoBean
    private QuestionFormatter questionFormatter;

    private TestServiceImpl testService;

    private Student student;

    @BeforeEach
    void setUp() {
        testService = new TestServiceImpl(ioService, questionDao);

        student = new Student("Frodo", "Baggins");
    }

    @DisplayName("No results for null student")
    @Test
    void shouldNotHaveResultsForNullStudent() {
        TestResult testResult = testService.executeTestFor(null);
        assertThat(testResult).isNotNull();
        assertThat(testResult.getStudent()).isNull();
        assertThatList(testResult.getAnsweredQuestions()).isEmpty();
        assertThat(testResult.getRightAnswersCount()).isZero();
    }

    @DisplayName("Should ask question")
    @Test
    void shouldAskOneQuestion() {
        given(questionDao.findAll()).willReturn(List.of(new Question("Test question",
            List.of(new Answer("yep", true)))));
        given(ioService.readAllIntsForRange(anyInt(), anyInt(), anyString()))
            .willReturn(new HashSet<>(Collections.singletonList(1))); //

        given(ioService.readAllIntsForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString()))
            .willReturn(new HashSet<>(Collections.singletonList(1))); //

        TestResult testResult = testService.executeTestFor(student);

        assertThat(testResult).isNotNull();
        assertThat(testResult.getStudent()).isNotNull().isEqualTo(student);
        assertThatList(testResult.getAnsweredQuestions()).isNotEmpty();
        assertThat(testResult.getAnsweredQuestions()).hasSize(1);
        assertThat(testResult.getRightAnswersCount()).isEqualTo(1);
    }
}

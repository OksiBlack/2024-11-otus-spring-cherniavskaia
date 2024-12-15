package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;
    @Mock
    private QuestionFormatter questionFormatter;

    @InjectMocks
    private TestServiceImpl target;

    @DisplayName("Test service calls needed integrations")
    @Test
    void executeTest() {

        // Given
        Question expectedQuestion = new Question("Some question", Collections.emptyList());
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Who are the founders of Google",
            Collections.emptyList())
        );
        questions.add(expectedQuestion);

        given(questionDao.findAll()).willReturn(questions);
        String expectedAnswer = "Expected answer";
        given(questionFormatter.formatQuestion(any())).willReturn(expectedAnswer);
        given(questionDao.findAll()).willReturn(questions);
        doNothing().when(ioService).printFormattedLine(any());

        // When
        target.executeTest();

        // Then
        verify(questionDao, times(1)).findAll();
        verify(questionFormatter, times(questions.size())).formatQuestion(any());
    }
}
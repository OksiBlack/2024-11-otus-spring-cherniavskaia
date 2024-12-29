package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;
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
    private TestServiceImpl testService;

    private Student student;
    private List<Question> questions;

    @BeforeEach
    void setUp() {
        student = new Student("Frodo", "Baggins");
        questions = new ArrayList<>();

        // Add sample questions to the list
        questions.add(new Question("What is 2+2?", Arrays.asList(
            new Answer("1", false), new Answer("2", false),
            new Answer("3", false),
            new Answer("4", true)))
        );
        questions.add(new Question("What is the capital of France?", Arrays.asList(
            new Answer("London", false),
            new Answer("Berlin", false),
            new Answer("Paris", true),
            new Answer("Madrid", false)))
        );
    }

    @Test
    void testExecuteTestFor() {
        // Mock methods
        given(questionDao.findAll()).willReturn(questions);
        given(ioService.readAllIntsForRange(1, 4)).willReturn(new HashSet<>(Arrays.asList(4, 2))); // Correct answers for the questions

        String formattedQuestion1 = "1: What is 2+2?";
        String formattedQuestion2 = "2: What is the capital of France?";
        given(questionFormatter.formatQuestion(questions.get(0))).willReturn(formattedQuestion1);
        given(questionFormatter.formatQuestion(questions.get(1))).willReturn(formattedQuestion2);

        // Execute test
        TestResult result = testService.executeTestFor(student);

        // Assertions (you can adjust based on the real implementation of TestResult)
        assertNotNull(result);
        assertEquals(student, result.getStudent());
        assertFalse(result.getAnsweredQuestions().isEmpty());

        // Verify interactions with IOService
        verify(ioService, times(4)).printFormattedLine(anyString()); // Verifying print calls
        verify(ioService, times(1)).printLine(anyString()); // Initial print call
        verify(ioService, times(1)).printFormattedLine("Please insert your first name"); // First name prompt
        verify(ioService, times(1)).printFormattedLine("Please answer the questions below:%n"); // Answer prompt


        // Verify interactions with QuestionFormatter
        verify(questionFormatter, times(1)).formatQuestion(questions.get(0)); // Ensure question is formatted
        verify(questionFormatter, times(1)).formatQuestion(questions.get(1)); // Ensure second question is formatted

        // Verify that QuestionDao was called once to retrieve all questions
        verify(questionDao, times(1)).findAll();

        // Verify that readAllIntsForRange was called correctly
        verify(ioService, times(2)).readAllIntsForRange(1, 4); // Checking range for answer input
    }
}

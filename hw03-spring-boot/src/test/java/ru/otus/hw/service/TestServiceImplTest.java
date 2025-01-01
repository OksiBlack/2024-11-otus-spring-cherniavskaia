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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private LocalizedIOService ioService;

    @Mock
    private QuestionDao questionDao;

    private Student student;
    private List<Question> questions;

    @InjectMocks
    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        student = new Student("Frodo", "Baggins");
        questions = new ArrayList<>();

        // Add sample questions to the list
        questions.add(new Question("What is 2+2?", Arrays.asList(
                new Answer("1", false),
                new Answer("2", false),
                new Answer("3", false),
                new Answer("4", true)
            ))
        );
        questions.add(new Question("What is the capital of France?", Arrays.asList(
                new Answer("London", false),
                new Answer("Berlin", false),
                new Answer("Madrid", false),
                new Answer("Paris", true)
            ))
        );
    }

    @Test
    void executeTestFor_ShouldReturnCorrectTestResult() {
        // Given

        Answer correctAnswer = new Answer("Correct Answer", true);
        Answer wrongAnswer = new Answer("Wrong Answer", false);
        List<Answer> answers = Arrays.asList(correctAnswer, wrongAnswer);
        Question question = new Question("Sample Question?", answers);

        given(questionDao.findAll()).willReturn(List.of(question));

        // Mocking IO interactions
        given(ioService.readAllIntsForRange(anyInt(), anyInt(), anyString()))
            .willReturn(new HashSet<>(Set.of(1))); // Assume user selected the correct answer

        // When
        TestResult testResult = testService.executeTestFor(student);

        // Then
        assertEquals(student, testResult.getStudent());
        assertEquals(1, testResult.getAnsweredQuestions().size()); // One question, one correct answer
        assertEquals(question, testResult.getAnsweredQuestions().get(0)); // The question asked
        then(ioService).should(times(3)).printLine(anyString()); // Verify printLine is called
        then(ioService).should(times(1)).printLineLocalized("TestService.answer.the.questions");
        then(ioService).should(times(1)).readAllIntsForRange(1, 2, "Exception out of range");
    }

    @Test
    void executeTestFor_ShouldHandleMultipleQuestions_SomeCorrect() {
        // Given

        Question question1 = questions.get(0);
        Question question2 = questions.get(1);

        given(questionDao.findAll()).willReturn(List.of(question1, question2));

        // Mocking IO interactions for both questions
        given(ioService.readAllIntsForRange(anyInt(), anyInt(), anyString()))
            .willReturn(new HashSet<>(Set.of(4))) // Answer "Question 1?" Correct
            .willReturn(new HashSet<>(Set.of(2))); // Answer "Question 2?" Incorrect

        // When
        TestResult testResult = testService.executeTestFor(student);

        // Then
        assertEquals(1, testResult.getRightAnswersCount()); // 1 correct answer from two questions
        then(ioService).should(times(2)).readAllIntsForRange(anyInt(), anyInt(), anyString());
    }

    @Test
    void executeTestFor_ShouldHandleMultipleQuestions_AllCorrect() {
        // Given

        Question question1 = questions.get(0);
        Question question2 = questions.get(1);

        given(questionDao.findAll()).willReturn(List.of(question1, question2));

        // Mocking IO interactions for both questions
        given(ioService.readAllIntsForRange(anyInt(), anyInt(), anyString()))
            .willReturn(new HashSet<>(Set.of(4))) // Answer Correct
            .willReturn(new HashSet<>(Set.of(4))); // Answer Correct

        // When
        TestResult testResult = testService.executeTestFor(student);

        // Then
        assertEquals(2, testResult.getRightAnswersCount()); // 1 correct answer from two questions
        then(ioService).should(times(2)).readAllIntsForRange(anyInt(), anyInt(), anyString());
    }

    @Test
    void executeTestFor_ShouldHandleEmptyQuestions() {
        // Given
        Student student = new Student("Frodo", "Baggins");
        given(questionDao.findAll()).willReturn(List.of()); // No questions available

        // When
        TestResult testResult = testService.executeTestFor(student);

        // Then
        assertEquals(student, testResult.getStudent());
        assertEquals(0, testResult.getAnsweredQuestions().size()); // No questions, so no correct answers
    }
}

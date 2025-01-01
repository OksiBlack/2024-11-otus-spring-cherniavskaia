package ru.otus.hw.shell;

import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Getter
@Setter
public class TestContext {

    private Student student;

    private TestResult testResult;

    public boolean isStudentLoggedIn() {
        return student != null;
    }

    public boolean isTestCompleted() {
        return testResult != null;
    }
}

package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.Availability;
import org.springframework.shell.AvailabilityProvider;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.CommandAvailability;
import org.springframework.shell.command.annotation.Option;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

@Command(group = "Test Runner")
@Slf4j
@RequiredArgsConstructor
public class TestRunner {
    public static final String EMPTY = "";

    private final StudentService studentService;

    private final TestService testService;

    private final ResultService resultService;

    private final ShellCommandResultFormatter shellCommandResultFormatter;

    private TestContext testContext = new TestContext();

    @Command(description = "Determine student", command = "login", alias = "lgn")
    public String login() {
        testContext = new TestContext();
        Student student = studentService.determineCurrentStudent();
        testContext.setStudent(student);
        log.info("Logged in: {}", student);

        return shellCommandResultFormatter.format("Currently logged in user: ", student);
    }

    @Command(description = "Execute test", command = "test", alias = "tst")
    @CommandAvailability(provider = "testAvailability")
    public String test(@Option(longNames = "show-result", defaultValue = "false")
                       boolean showResultAfterCompletion) {

        TestResult testResult = testService.executeTestFor(testContext.getStudent());
        testContext.setTestResult(testResult);

        if (showResultAfterCompletion) {
            return showResult(false);
        } else {
            return "Test completed. Call show-result command to view result.";
        }
    }

    @Bean
    public AvailabilityProvider testAvailability() {
        return () -> testContext.isStudentLoggedIn()
            ? Availability.available()
            : Availability.unavailable("login required.");
    }

    @Bean
    public AvailabilityProvider testResultAvailability() {
        return () -> testContext.isTestCompleted()
            ? Availability.available()
            : Availability.unavailable("test execution required.");
    }

    @CommandAvailability(provider = "testResultAvailability")
    @Command(description = "Show result")
    public String showResult(@Option(longNames = "verbose", defaultValue = "false") boolean verbose) {

        TestResult testResult = testContext.getTestResult();
        resultService.showResult(testResult);
        if (verbose) {
            return shellCommandResultFormatter.format(EMPTY, testResult);
        } else {
            return "";
        }
    }

    @Command(description = "Reset test context", command = "reset", alias = "rst")
    public String resetContext() {
        testContext = new TestContext();

        return "Test context cleared";
    }
}
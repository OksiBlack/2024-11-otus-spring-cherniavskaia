package ru.otus.hw.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StreamIOService implements IOService {
    private static final int MAX_ATTEMPTS = 10;

    private final PrintStream printStream;

    private final Scanner scanner;

    public StreamIOService(@Value("#{T(System).out}") PrintStream printStream,
                           @Value("#{T(System).in}") InputStream inputStream) {

        this.printStream = printStream;
        this.scanner = new Scanner(inputStream);
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }

    @Override
    public String readString() {
        return scanner.nextLine();
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        printLine(prompt);
        return scanner.nextLine();
    }

    @Override
    public Set<Integer> readAllIntsForRange(int min, int max) {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            try {
                var stringValue = scanner.nextLine();
                Set<Integer> results = Arrays.stream(stringValue.split(",|\\s+"))
                    .map(Integer::parseInt).collect(Collectors.toSet());

                if (results.stream().anyMatch(it -> it < min || it > max)) {
                    throw new IllegalArgumentException();
                }
                return results;

            } catch (IllegalArgumentException e) {
                printLine("Some results are out of range.");
            }
        }
        throw new IllegalArgumentException("Error during reading int values.");
    }

    @Override
    public int readIntForRange(int min, int max, String errorMessage) {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            try {
                var stringValue = scanner.nextLine();
                int intValue = Integer.parseInt(stringValue);
                if (intValue < min || intValue > max) {
                    throw new IllegalArgumentException();
                }
                return intValue;
            } catch (IllegalArgumentException e) {
                printLine(errorMessage);
            }
        }
        throw new IllegalArgumentException("Error during reading int value");
    }

    @Override
    public int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
        printLine(prompt);
        return readIntForRange(min, max, errorMessage);
    }
}

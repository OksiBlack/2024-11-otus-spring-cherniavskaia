package ru.otus.hw.service;

import java.util.Set;

public interface IOService {
    void printLine(String s);

    void printFormattedLine(String s, Object ...args);

    String readString();

    String readStringWithPrompt(String prompt);

    Set<Integer> readAllIntsForRange(int min, int max);

    int readIntForRange(int min, int max, String errorMessage);

    int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage);
}

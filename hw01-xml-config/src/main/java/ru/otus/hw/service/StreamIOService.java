package ru.otus.hw.service;

import java.io.PrintStream;

public class StreamIOService implements IOService {
    private final PrintStream printStream;

    public StreamIOService(PrintStream printStream) {

        this.printStream = printStream;
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }
}

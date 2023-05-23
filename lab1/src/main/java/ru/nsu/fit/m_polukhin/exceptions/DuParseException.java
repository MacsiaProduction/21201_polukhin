package ru.nsu.fit.m_polukhin.exceptions;

public class DuParseException extends Exception {

    public DuParseException(Throwable e) {
        super(e);
    }

    public DuParseException(String message) {
        super(message);
    }
}

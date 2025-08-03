package br.edu.infnet.carlos_araujo.application.exception;

public class DuplicateCategoryNameException extends RuntimeException {
    public DuplicateCategoryNameException(String message) {
        super(message);
    }

    public DuplicateCategoryNameException(String message, Throwable cause) {
        super(message, cause);
    }
}

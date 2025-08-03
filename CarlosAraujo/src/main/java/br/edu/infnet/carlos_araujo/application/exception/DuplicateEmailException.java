package br.edu.infnet.carlos_araujo.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String message) {
        super(message);
    }
    public DuplicateEmailException(String message, Throwable cause) {
        super(message);
    }
}

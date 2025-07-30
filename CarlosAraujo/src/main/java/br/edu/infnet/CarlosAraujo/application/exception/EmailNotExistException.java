package br.edu.infnet.CarlosAraujo.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class EmailNotExistException extends RuntimeException {

    public EmailNotExistException(String message) {
        super(message);
    }
}

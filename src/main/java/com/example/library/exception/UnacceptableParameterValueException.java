package com.example.library.exception;

public class UnacceptableParameterValueException extends RuntimeException{

    public UnacceptableParameterValueException(String message) {
        super(message);
    }
}

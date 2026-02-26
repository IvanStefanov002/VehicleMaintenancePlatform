package com.example.demo.exception;

/* custom exception - default for project */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
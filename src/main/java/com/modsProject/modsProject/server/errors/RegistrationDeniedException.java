package com.modsProject.modsProject.server.errors;

public class RegistrationDeniedException extends RuntimeException {
    public RegistrationDeniedException(String message) {
        super(message);
    }
}

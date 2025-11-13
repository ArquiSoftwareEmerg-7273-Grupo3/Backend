package com.drawnet.artcollab.CollaborativeProjects.domain.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

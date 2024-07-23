package com.example.demo.config.exception.comment;

public class NotFoundCommentException extends RuntimeException {

    public NotFoundCommentException(String message) {
        super(message);
    }
}

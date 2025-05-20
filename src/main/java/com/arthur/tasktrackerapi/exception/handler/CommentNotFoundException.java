package com.arthur.tasktrackerapi.exception.handler;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(Long id) {
        super("Comment not found with id" + id);
    }
}

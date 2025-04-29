package com.arthur.tasktrackerapi.exception.handler;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id) {
        super("User not found with " + id);
    }
}

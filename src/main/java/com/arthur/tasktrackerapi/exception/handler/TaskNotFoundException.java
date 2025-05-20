package com.arthur.tasktrackerapi.exception.handler;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id){
        super("Task not found with id" + id);
    }
}

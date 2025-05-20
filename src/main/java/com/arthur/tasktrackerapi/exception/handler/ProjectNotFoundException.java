package com.arthur.tasktrackerapi.exception.handler;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(Long id) {
        super("Project not found with id " + id);
    }
}

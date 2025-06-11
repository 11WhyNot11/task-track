package com.arthur.tasktrackerapi.task.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Priority {
    LOW, MEDIUM, HIGH
}

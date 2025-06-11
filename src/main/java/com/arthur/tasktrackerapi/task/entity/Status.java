package com.arthur.tasktrackerapi.task.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Status {
    TODO, IN_PROGRESS, DONE
}

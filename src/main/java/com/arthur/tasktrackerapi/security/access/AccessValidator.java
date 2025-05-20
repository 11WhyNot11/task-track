package com.arthur.tasktrackerapi.security.access;

import com.arthur.tasktrackerapi.comment.entity.Comment;
import com.arthur.tasktrackerapi.project.entity.Project;
import com.arthur.tasktrackerapi.task.entity.Task;
import com.arthur.tasktrackerapi.user.entity.Role;
import com.arthur.tasktrackerapi.user.entity.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class AccessValidator {

    public void validateAccessToTask(Task task, User user) {
        if(!task.getProject().getOwner().equals(user) && !user.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    public  void validateCanModifyComment(Comment comment, User user) {
        if(!comment.getAuthor().equals(user) && !user.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    public void validateAccessToProject(Project project, User user) {
        if(!project.getOwner().equals(user) && !user.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Access denied");
        }
    }
}

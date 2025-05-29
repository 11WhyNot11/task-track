package com.arthur.tasktrackerapi.security.access;

import com.arthur.tasktrackerapi.comment.entity.Comment;
import com.arthur.tasktrackerapi.project.entity.Project;
import com.arthur.tasktrackerapi.task.entity.Task;
import com.arthur.tasktrackerapi.user.entity.Role;
import com.arthur.tasktrackerapi.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AccessValidator {

    public void validateAccessToTask(Task task, User user) {
        if (!isProjectOwner(task.getProject(), user) && !isAdmin(user)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    public void validateCanModifyComment(Comment comment, User user) {
        if (!comment.getAuthor().getId().equals(user.getId()) && !isAdmin(user)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    public void validateAccessToProject(Project project, User user) {
        if (!isProjectOwner(project, user) && !isAdmin(user)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    private boolean isProjectOwner(Project project, User user) {
        return project.getOwner().getId().equals(user.getId());
    }

    private boolean isAdmin(User user) {
        return user.getRole().equals(Role.ADMIN);
    }
}


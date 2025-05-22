package com.arthur.tasktrackerapi.user.repository;

import com.arthur.tasktrackerapi.user.entity.User;
import com.arthur.tasktrackerapi.user.dto.filter.UserFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<User> findAllFiltered(UserFilterRequest filter, Pageable pageable);
}

package com.kcymerys.java.todowithcognito.repository;

import com.kcymerys.java.todowithcognito.model.Task;
import com.kcymerys.java.todowithcognito.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByUser(Pageable pageable, User user);

    Optional<Task> findById(Long id);

}

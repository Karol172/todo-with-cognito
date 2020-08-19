package com.kcymerys.java.todowithcognito.repository;

import com.kcymerys.java.todowithcognito.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}

package com.kcymerys.java.todowithcognito.repository;

import com.kcymerys.java.todowithcognito.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

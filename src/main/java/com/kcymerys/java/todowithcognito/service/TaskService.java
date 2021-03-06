package com.kcymerys.java.todowithcognito.service;

import com.kcymerys.java.todowithcognito.dto.TaskDTO;
import com.kcymerys.java.todowithcognito.model.Status;
import com.kcymerys.java.todowithcognito.model.Task;
import com.kcymerys.java.todowithcognito.model.User;
import com.kcymerys.java.todowithcognito.repository.TaskRepository;
import com.kcymerys.java.todowithcognito.repository.UserRepository;
import com.kcymerys.java.todowithcognito.security.model.Role;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public void create(TaskDTO taskDTO) {
        User user = extractUsername();
        user.getTasks().add(new Task(null,
                                taskDTO.getTitle().trim(),
                                Optional.ofNullable(taskDTO.getDescription()).orElse("").trim(),
                                Status.CREATED,
                                user));
        userRepository.save(user);
    }

    public void update(Long id, TaskDTO taskDTO) {
        Task task = getTask(id);
        task.setTitle(taskDTO.getTitle().trim());
        task.setDescription(Optional.ofNullable(taskDTO.getDescription())
                .orElse("").trim());
        taskRepository.save(task);
    }

    public void changeStatus(Long id, Status status) {
        Task task = getTask(id);
        task.setStatus(status);
        taskRepository.save(task);
    }

    public Task get(Long id) {
        User user = extractUsername();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.ADMIN.value()));
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Defined task not found"));
        if (task.getUser().hashCode() != user.hashCode() && !isAdmin) {
            throw new AccessDeniedException("Access denied");
        }
        return task;
    }

    public Page<Task> getTasks (Pageable pageable) {
        return taskRepository.findByUser(pageable, extractUsername());
    }

    public Page<Task> getUsersTasks (Pageable pageable, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with given filename not found."));
        return taskRepository.findByUser(pageable, user);
    }

    public void delete (Long id) {
        taskRepository.delete(getTask(id));
    }

    private User extractUsername () {
        String username = (String) ((JWTClaimsSet) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getClaim("username");
        return userRepository.findByUsername(username)
                .orElse(new User(null, username, new ArrayList<>()));
    }

    private Task getTask(Long id) {
        User user = extractUsername();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Defined task not found"));
        if (task.getUser().hashCode() != user.hashCode()) {
            throw new AccessDeniedException("Access denied");
        }
        return task;
    }

}

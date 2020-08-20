package com.kcymerys.java.todowithcognito.controller;

import com.kcymerys.java.todowithcognito.dto.TaskDTO;
import com.kcymerys.java.todowithcognito.model.Status;
import com.kcymerys.java.todowithcognito.service.TaskService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/task")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void create (@RequestBody @Valid TaskDTO task) {
        taskService.create(task);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void update (@PathVariable Long id, @RequestBody @Valid TaskDTO task) {
        taskService.update(id, task);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void changeStatus (@PathVariable Long id, @RequestParam(name = "status") Status status) {
        taskService.changeStatus(id, status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public TaskDTO get (@PathVariable Long id) {
        return modelMapper.map(taskService.get(id), TaskDTO.class);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Page<TaskDTO> all (Pageable pageable) {
        return taskService.getTasks(pageable)
                .map(task -> modelMapper.map(task, TaskDTO.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void delete (@PathVariable Long id) {
        taskService.delete(id);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handle404 (EntityNotFoundException exc) {
        return Map.of("status", HttpStatus.NOT_FOUND.value(),
                "message", exc.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handle403 (AccessDeniedException exc) {
        return Map.of("status", HttpStatus.FORBIDDEN.value(),
                "message", exc.getMessage());
    }

}

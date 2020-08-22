package com.kcymerys.java.todowithcognito.service;

import com.kcymerys.java.todowithcognito.dto.TaskDTO;
import com.kcymerys.java.todowithcognito.model.Status;
import com.kcymerys.java.todowithcognito.model.Task;
import com.kcymerys.java.todowithcognito.model.User;
import com.kcymerys.java.todowithcognito.repository.TaskRepository;
import com.kcymerys.java.todowithcognito.repository.UserRepository;
import com.kcymerys.java.todowithcognito.security.model.Role;
import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    TaskService taskService;

    @Mock
    UserRepository userRepository;

    @Mock
    TaskRepository taskRepository;

    @Test
    void shouldCreateTaskWhenUserExists() {
        User user = new User(1L, "user-test", new ArrayList<>());
        TaskDTO taskDTO = new TaskDTO(null, "test-task", null, null);

        mockSecurityContext(user.getUsername(), Optional.of(user), false);

        taskService.create(taskDTO);
    }

    @Test
    void shouldCreateTaskWhenUserNotExist(@Mock SecurityContext context, @Mock Authentication authentication) {
        User user = new User(1L, "user-test", new ArrayList<>());
        TaskDTO taskDTO = new TaskDTO(null, "test-task", null, null);

        mockSecurityContext(user.getUsername(), Optional.empty(), false);

        taskService.create(taskDTO);
    }

    @Test
    void shouldUpdateTask() {
        User user = new User(1L, "user-test", new ArrayList<>());
        Task task = new Task(1L, "test-title", null, Status.CREATED, user);
        TaskDTO taskDTO = new TaskDTO(null, "updated-test-title", "description", null);

        mockSecurityContext(user.getUsername(), Optional.of(user), false);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        taskService.update(task.getId(), taskDTO);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUpdatingTask() {
        Long id = 2L;
        User user = new User(1L, "user-test", new ArrayList<>());
        TaskDTO taskDTO = new TaskDTO(null, "updated-test-title", "description", null);

        mockSecurityContext(user.getUsername(), Optional.of(user), false);
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.update(id, taskDTO));
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenUpdatingTask() {
        User user = new User(1L, "user-test", new ArrayList<>());
        User user2 = new User(2L, "user2-test", new ArrayList<>());
        Task task = new Task(1L, "test-title", null, Status.CREATED, user);
        TaskDTO taskDTO = new TaskDTO(null, "updated-test-title", "description", null);

        mockSecurityContext(user2.getUsername(), Optional.of(user2), false);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> taskService.update(task.getId(), taskDTO));
    }

    @Test
    void shouldChangeStatusOfTask() {
        User user = new User(1L, "user-test", new ArrayList<>());
        Task task = new Task(1L, "test-title", null, Status.CREATED, user);

        mockSecurityContext(user.getUsername(), Optional.of(user), false);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.changeStatus(task.getId(), Status.PROCESS);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenChangingStatusOfTask() {
        Long id = 2L;
        User user = new User(1L, "user-test", new ArrayList<>());

        mockSecurityContext(user.getUsername(), Optional.of(user), false);
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.changeStatus(id, Status.PROCESS));
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenChangingStatusOfTask() {
        User user = new User(1L, "user-test", new ArrayList<>());
        User user2 = new User(2L, "user2-test", new ArrayList<>());
        Task task = new Task(1L, "test-title", null, Status.CREATED, user);

        mockSecurityContext(user2.getUsername(), Optional.of(user2), false);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> taskService.changeStatus(task.getId(), Status.PROCESS));
    }

    @Test
    void shouldReturnTaskAsOwner() {
        User user = new User(1L, "user-test", new ArrayList<>());
        Task task = new Task(1L, "test-title", null, Status.CREATED, user);

        mockSecurityContext(user.getUsername(), Optional.of(user), false);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Task output = taskService.get(task.getId());

        assertAll(
                () -> assertEquals(task.getId(), output.getId()),
                () -> assertEquals(task.getTitle(), output.getTitle()),
                () -> assertEquals(task.getDescription(), output.getDescription()),
                () -> assertEquals(task.getStatus(), output.getStatus())
        );
    }

    @Test
    void shouldReturnTaskAsAdmin() {
        User user = new User(1L, "user-test", new ArrayList<>());
        User admin = new User(2L, "admin-test", new ArrayList<>());

        Task task = new Task(1L, "test-title", null, Status.CREATED, user);

        mockSecurityContext(admin.getUsername(), Optional.of(admin), true);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Task output = taskService.get(task.getId());

        assertAll(
                () -> assertEquals(task.getId(), output.getId()),
                () -> assertEquals(task.getTitle(), output.getTitle()),
                () -> assertEquals(task.getDescription(), output.getDescription()),
                () -> assertEquals(task.getStatus(), output.getStatus())
        );
    }

    @Test
    void shouldThrowEntityNotFoundWhenGettingTask() {
        User user = new User(1L, "user-test", new ArrayList<>());
        Long taskId = 2L;

        mockSecurityContext(user.getUsername(), Optional.of(user), false);
        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.get(taskId));
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenGettingTaskAsAnotherUser() {
        User user = new User(1L, "user-test", new ArrayList<>());
        User user2 = new User(2L, "user2-test", new ArrayList<>());

        Task task = new Task(1L, "test-title", null, Status.CREATED, user);

        mockSecurityContext(user2.getUsername(), Optional.of(user2), false);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> taskService.get(task.getId()));
    }

    @Test
    void shouldReturnPageOfTasksAsOwner() {
        User user = new User(1L, "user-test", new ArrayList<>());
        List<Task> items = Collections.singletonList(new Task(1L, "test-title", null, Status.CREATED, user));
        Pageable pageable = PageRequest.of(0,20);

        mockSecurityContext(user.getUsername(), Optional.of(user), false);
        Mockito.when(taskRepository.findByUser(pageable, user))
                .thenReturn(new PageImpl<>(items, pageable, items.size()));

        Page<Task> output = taskService.getTasks(pageable);
        for (int i=0; i<items.size(); i++) {
            final int index = i;
            assertAll(
                    () -> assertEquals(items.get(index).getId(), output.getContent().get(index).getId()),
                    () -> assertEquals(items.get(index).getTitle(), output.getContent().get(index).getTitle()),
                    () -> assertEquals(items.get(index).getDescription(), output.getContent().get(index).getDescription()),
                    () -> assertEquals(items.get(index).getStatus(), output.getContent().get(index).getStatus())
            );
        }
    }

    @Test
    void shouldReturnPageOfTasksAsAdmin() {
        User user = new User(1L, "user-test", new ArrayList<>());
        List<Task> items = Collections.singletonList(new Task(1L, "test-title", null, Status.CREATED, user));
        Pageable pageable = PageRequest.of(0,20);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(taskRepository.findByUser(pageable, user))
                .thenReturn(new PageImpl<>(items, pageable, items.size()));

        Page<Task> output = taskService.getUsersTasks(pageable, user.getUsername());
        for (int i=0; i<items.size(); i++) {
            final int index = i;
            assertAll(
                    () -> assertEquals(items.get(index).getId(), output.getContent().get(index).getId()),
                    () -> assertEquals(items.get(index).getTitle(), output.getContent().get(index).getTitle()),
                    () -> assertEquals(items.get(index).getDescription(), output.getContent().get(index).getDescription()),
                    () -> assertEquals(items.get(index).getStatus(), output.getContent().get(index).getStatus())
            );
        }
    }

    @Test
    void shouldThrowEntityNotFoundWhenGttingPageOfTasksAsAdmin() {
        String username = "non-existing user";
        Pageable pageable = PageRequest.of(0,20);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.getUsersTasks(pageable, username));
    }

    @Test
    void shouldDeleteTask() {
        User user = new User(1L, "user-test", new ArrayList<>());
        Task task = new Task(1L, "test-title", null, Status.CREATED, user);

        mockSecurityContext(user.getUsername(), Optional.of(user), false);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.delete(task.getId());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenDeletingTask() {
        Long id = 2L;
        User user = new User(1L, "user-test", new ArrayList<>());

        mockSecurityContext(user.getUsername(), Optional.of(user), false);
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.delete(id));
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenDeletingTask() {
        User user = new User(1L, "user-test", new ArrayList<>());
        User user2 = new User(2L, "user2-test", new ArrayList<>());
        Task task = new Task(1L, "test-title", null, Status.CREATED, user);

        mockSecurityContext(user2.getUsername(), Optional.of(user2), false);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        assertThrows(AccessDeniedException.class, () -> taskService.delete(task.getId()));
    }

    private void mockSecurityContext(String username, Optional<User> user, boolean isAdmin) {
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        JWTClaimsSet principal = new JWTClaimsSet.Builder()
                .claim("username", username)
                .build();

        SecurityContextHolder.setContext(context);
        Mockito.when(context.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(principal);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
        if (isAdmin) {
            Mockito.when(authentication.getAuthorities())
                    .thenReturn(new ArrayList(Collections
                            .singleton(new SimpleGrantedAuthority(Role.ADMIN.value()))
                            ));
        }
    }



}

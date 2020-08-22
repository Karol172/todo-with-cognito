package com.kcymerys.java.todowithcognito.controller;

import com.kcymerys.java.todowithcognito.dto.TaskDTO;
import com.kcymerys.java.todowithcognito.model.Status;
import com.kcymerys.java.todowithcognito.service.TaskService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/task")
@CrossOrigin("*")
@AllArgsConstructor
@Api(value = "Tasks", description = "REST API for tasks' management", tags = "Task")
@ApiResponses({
        @ApiResponse(code = 400, message = "Bad request."),
        @ApiResponse(code = 401, message = "Unauthorized."),
        @ApiResponse(code = 403, message = "Forbidden."),
})
public class TaskController {

    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation(value = "Create new task")
    @ApiResponses(@ApiResponse(code = 201, message = "The task was successfully created."))
    public void create (@ApiParam(value = "The object of task to create.", required = true)
                            @RequestBody @Valid TaskDTO task) {
        taskService.create(task);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation(value = "Update existing task")
    @ApiResponses({@ApiResponse(code = 204, message = "The task was successfully updated."),
                   @ApiResponse(code = 404, message = "Task not found.")})
    public void update (@ApiParam(value = "Id of task to update.", required = true) @PathVariable Long id,
                        @ApiParam(value = "The object of task to update.", required = true)
                            @RequestBody @Valid TaskDTO task) {
        taskService.update(id, task);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation(value = "Change status of existing task")
    @ApiResponses({@ApiResponse(code = 204, message = "The status of task was successfully changed."),
            @ApiResponse(code = 404, message = "Task not found.")})
    public void changeStatus (@ApiParam(value = "Id of task to status change.", required = true) @PathVariable Long id,
                              @ApiParam(value = "The status of task. CREATED|PENDING|PROCESS|FINISHED", required = true)
                              @RequestParam(name = "status") Status status) {
        taskService.changeStatus(id, status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation(value = "Retrieve existing task")
    @ApiResponses({@ApiResponse(code = 200, message = "Task was successfully loaded."),
            @ApiResponse(code = 404, message = "Task not found.")})
    public TaskDTO get (@ApiParam(value = "Id of task to status change.", required = true) @PathVariable Long id) {
        return modelMapper.map(taskService.get(id), TaskDTO.class);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation(value = "Retrieve page of logged user's tasks.")
    @ApiResponses(@ApiResponse(code = 200, message = "The request was successfully processed."))
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N).", defaultValue = "0"),
            @ApiImplicitParam(name = "size", paramType = "query", dataType = "integer",
                    value = "Number of records per page.", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. Multiple sort criteria are supported.")
    })
    public Page<TaskDTO> all (@ApiIgnore Pageable pageable) {
        return taskService.getTasks(pageable)
                .map(task -> modelMapper.map(task, TaskDTO.class));
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Retrieve page of specified user's tasks.")
    @ApiResponses(@ApiResponse(code = 200, message = "The request was successfully processed."))
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N).", defaultValue = "0"),
            @ApiImplicitParam(name = "size", paramType = "query", dataType = "integer",
                    value = "Number of records per page.", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. Multiple sort criteria are supported.")
    })
    public Page<TaskDTO> usersTasks (@ApiIgnore Pageable pageable,
                                     @ApiParam(value = "Username.", required = true) @PathVariable String username) {
        return taskService.getUsersTasks(pageable, username)
                .map(task -> modelMapper.map(task, TaskDTO.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @ApiOperation(value = "Delete existing task")
    @ApiResponses({@ApiResponse(code = 204, message = "Task was successfully deleted."),
            @ApiResponse(code = 404, message = "Task not found.")})
    public void delete (@ApiParam(value = "Id of task to delete.", required = true) @PathVariable Long id) {
        taskService.delete(id);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handle404 (EntityNotFoundException exc) {
        return Map.of("status", HttpStatus.NOT_FOUND.value(),
                "message", exc.getMessage());
    }

}

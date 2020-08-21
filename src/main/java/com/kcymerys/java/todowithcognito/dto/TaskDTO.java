package com.kcymerys.java.todowithcognito.dto;

import com.kcymerys.java.todowithcognito.model.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Task object stored in database.")
public class TaskDTO {

    @ApiModelProperty(notes = "Id of task.")
    private Long id;

    @NotBlank(message = "This field is required.")
    @ApiModelProperty(notes = "Title of task.", required = true)
    private String title;

    @ApiModelProperty(notes = "Description of task.")
    private String description;

    @ApiModelProperty(notes = "Status of task. (CREATED|PENDING|PROCESS|FINISHED)")
    private Status status;

}

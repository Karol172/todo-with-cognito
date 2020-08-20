package com.kcymerys.java.todowithcognito.dto;

import com.kcymerys.java.todowithcognito.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long id;

    @NotBlank(message = "This field is required.")
    private String title;
    private String description;
    private Status status;

}

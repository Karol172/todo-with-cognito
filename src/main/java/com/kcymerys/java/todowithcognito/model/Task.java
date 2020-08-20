package com.kcymerys.java.todowithcognito.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "TASK")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION", length = 1024)
    private String description;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false, referencedColumnName = "ID")
    private User user;

}

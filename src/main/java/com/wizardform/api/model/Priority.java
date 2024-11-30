package com.wizardform.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "Priority")
public class Priority {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private int priorityCode;
    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "priority")
    private List<Request> requests;
}

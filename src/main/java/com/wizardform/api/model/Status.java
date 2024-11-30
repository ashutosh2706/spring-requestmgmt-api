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
@Table(name = "Status")
public class Status {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private int statusCode;
    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "status")
    private List<Request> requestList;

}

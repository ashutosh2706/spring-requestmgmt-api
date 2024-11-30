package com.wizardform.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "Status")
public class Status {

    @Id
    private int statusCode;
    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "status")
    private List<Request> requestList;

}

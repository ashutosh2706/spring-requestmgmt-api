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
@Table(name = "Roles")
public class Role {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private int roleId;
    @Column(nullable = false)
    private String roleType;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<User> users;
}

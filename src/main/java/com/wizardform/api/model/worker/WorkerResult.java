package com.wizardform.api.model.worker;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_worker_result")
public class WorkerResult {

    @Id
    @Column(nullable = false, updatable = false)
    private int resultId;

    @Column(nullable = false, unique = true)
    private String resultType;
}

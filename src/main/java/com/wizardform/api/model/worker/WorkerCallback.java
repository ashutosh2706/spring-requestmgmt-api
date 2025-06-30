package com.wizardform.api.model.worker;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_worker_callbacks")
public class WorkerCallback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String callbackUrl;

    @Column(updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "resultId", nullable = false)
    private WorkerResult workerResult;

}

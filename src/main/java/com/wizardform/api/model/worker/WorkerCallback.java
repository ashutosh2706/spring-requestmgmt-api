package com.wizardform.api.model.worker;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_worker_callbacks")
public class WorkerCallback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String callbackUrl;

    @CreatedDate
    @Column(updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "resultId", nullable = false)
    private WorkerResult workerResult;

}

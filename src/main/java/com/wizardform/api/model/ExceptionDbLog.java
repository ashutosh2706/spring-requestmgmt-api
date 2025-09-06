package com.wizardform.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_exception_logs")
public class ExceptionDbLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String enquiryId;
    @Column(nullable = false)
    private String error;
    @Column(nullable = false)
    private String status;
    private String details;
    private String path;
    @CreatedDate
    @Column(name = "time_stamp", nullable = false)
    private LocalDateTime timestamp;
}

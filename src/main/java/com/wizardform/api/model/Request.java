package com.wizardform.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "Requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long requestId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String guardianName;
    private String phone;
    @Column(nullable = false)
    private LocalDate requestDate;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "statusCode", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "priorityCode", nullable = false)
    private Priority priority;

    @OneToOne
    @JoinColumn(name = "fileId")
    private FileDetail fileDetail;

}

package com.wizardform.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "FileDetails")
public class FileDetail {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fileId;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String checksum;
}

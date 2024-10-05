package com.wizardform.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "FileDetails")
public class FileDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fileId;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String checksum;
}

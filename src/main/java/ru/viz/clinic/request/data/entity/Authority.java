package ru.viz.clinic.request.data.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "authorities")
@Data
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String authority;
}
package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class Principle {
    @Id
    private String username;
    private String password;
    private boolean enabled = true;
}

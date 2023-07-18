package ru.viz.clinic.request.data.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    private String username;
    private String password;
    private String email;
    private boolean enabled;
}



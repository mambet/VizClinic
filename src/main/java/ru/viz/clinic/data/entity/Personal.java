package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.viz.clinic.data.Gender;

import java.time.LocalDate;

@Setter
@Getter
@MappedSuperclass
public class Personal extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgenerator")
    @SequenceGenerator(name = "idgenerator", initialValue = 3000)
    private Long id;
    @NotBlank(message = "username is required")
    private String username;
    private String tempPass;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String email;
    private String phone;
}



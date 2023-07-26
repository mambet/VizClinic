package ru.viz.clinic.data.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.viz.clinic.data.Gender;
import ru.viz.clinic.data.Role;

import java.time.LocalDate;

@Data
@SuperBuilder
public class PersonalDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String email;
    private String phone;
    private Role role;
}


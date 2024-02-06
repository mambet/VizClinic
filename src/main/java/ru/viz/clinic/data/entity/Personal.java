package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import ru.viz.clinic.converter.EntityToStringConverter;
import ru.viz.clinic.data.Gender;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@MappedSuperclass
public abstract class Personal extends AbstractEntity {
    @NotBlank(message = "username is required")
    private String username;
    private String tempPass;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String email;
    private String phone;

    @Override
    public String getEntityName() {
        return StringUtils.joinWith(" ", firstName, middleName, lastName);
    }
}
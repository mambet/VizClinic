package ru.viz.clinic.data.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.viz.clinic.data.entity.Hospital;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class EngineerPersonalDTO extends PersonalDTO {
    private Hospital hospital;
}

package ru.viz.clinic.data.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.viz.clinic.data.entity.Department;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class MedicPersonalDTO extends PersonalDTO {
    private Department department;
}

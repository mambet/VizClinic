package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Table(name = "medic_person")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class MedicPersonal extends Personal {
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}

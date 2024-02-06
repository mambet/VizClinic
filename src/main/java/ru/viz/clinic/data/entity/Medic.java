package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Setter
@Getter
@Table(name = "medic")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Medic extends Personal {
    private static final String MEDIC_ID_PREFIX = "МД";
    @Id
    @GeneratedValue(generator = "hospital-generator")
    @GenericGenerator(name = "hospital-generator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = MEDIC_ID_PREFIX),
            type = ru.viz.clinic.data.IdGenerator.class)
    private String id;
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
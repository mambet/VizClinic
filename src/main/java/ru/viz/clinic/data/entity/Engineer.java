package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Setter
@Getter
@Table(name = "engineer")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Engineer extends Personal {
    private static final String ENGINEER_ID_PREFIX = "ИЖ";

    @Id
    @GeneratedValue(generator = "hospital-generator")
    @GenericGenerator(name = "hospital-generator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = ENGINEER_ID_PREFIX),
            type = ru.viz.clinic.data.IdGenerator.class)
    private String id;
    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;
}
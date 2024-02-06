package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Setter
@Getter
@Table(name = "admin")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Admin extends Personal {
    private static final String ADMIN_ID_PREFIX = "АМ";
    @Id
    @GeneratedValue(generator = "hospital-generator")
    @GenericGenerator(name = "hospital-generator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = ADMIN_ID_PREFIX),
            type = ru.viz.clinic.data.IdGenerator.class)
    private String id;
}
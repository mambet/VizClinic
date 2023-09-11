package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.viz.clinic.help.Translator;

@Entity
@Setter
@Getter
@Table(name = "engineer")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Engineer extends Personal {
    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Override
    public String getEntityDesignation() {
        return Translator.ENTITY_NAME_ENGINEER;
    }
}

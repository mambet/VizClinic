package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.viz.clinic.help.Translator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "medic")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Medic extends Personal {
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    @Override
    public String getEntityDesignation() {
        return Translator.ENTITY_NAME_MEDIC;
    }
}
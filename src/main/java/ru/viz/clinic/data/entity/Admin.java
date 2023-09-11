package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.viz.clinic.help.Translator;

@Entity
@Setter
@Getter
@Table(name = "admin")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Admin extends Personal {
    @Override
    public String getEntityDesignation() {
        return Translator.ENTITY_NAME_ADMIN;
    }
}
package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.viz.clinic.help.Translator;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "address")
@Data
@ToString(includeFieldNames = false)
public class Address extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    private Long id;
    private String street;
    private String city;
    private Integer postalCode;
    private String region;

    @Override
    public String getEntityDesignation() {
        return Translator.ENTITY_NAME_ADDRESS;
    }

    @Override
    public String getEntityName() {
        return toString();
    }
}
package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "address")
@Data
public class Address extends AbstractEntity {
    private static final String ADDRESS_ID_PREFIX = "АС";
    @Id
    @GeneratedValue(generator = "hospital-generator")
    @GenericGenerator(name = "hospital-generator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = ADDRESS_ID_PREFIX),
            type = ru.viz.clinic.data.IdGenerator.class)
    private String id;
    private String street;
    private String city;
    private Integer postalCode;
    private String region;

    @Override
    public String getEntityName() {
        return StringUtils.join(List.of(street, postalCode + " " + city, region), ", ");
    }
}
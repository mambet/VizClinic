package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "address")
@Data
@ToString(includeFieldNames = false)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    private Long id;
    private String street;
    private String city;
    private Integer postalCode;
    private String region;
    @OneToOne(mappedBy = "address")
    @ToString.Exclude
    private Hospital hospital;
}
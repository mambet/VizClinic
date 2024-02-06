package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department extends AbstractEntity {
    private static final String DEPARTMENT_ID_PREFIX = "ОД";

    @Id
    @GeneratedValue(generator = "hospital-generator")
    @GenericGenerator(name = "hospital-generator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = DEPARTMENT_ID_PREFIX),
            type = ru.viz.clinic.data.IdGenerator.class)
    private String id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;
    @OneToMany(mappedBy = "department", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<Medic> medics = new HashSet<>();
    @OneToMany(mappedBy = "department", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<Equipment> equipment = new HashSet<>();

    @Override
    public String getEntityName() {
        return name;
    }
}
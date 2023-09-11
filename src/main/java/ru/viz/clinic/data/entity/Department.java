package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.viz.clinic.help.Translator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @OneToMany(mappedBy = "department", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<Medic> medics = new HashSet<>();

    @OneToMany(mappedBy = "department", cascade =  CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<Equipment> equipment = new HashSet<>();

    @Override
    public String getEntityDesignation() {
        return Translator.ENTITY_NAME_DEPARTMENT;
    }

    @Override
    public String getEntityName() {
        return name;
    }
}

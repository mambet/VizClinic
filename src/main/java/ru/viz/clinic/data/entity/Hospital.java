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
@Table(name = "hospital")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hospital extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Department> departments = new HashSet<>();
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Engineer> engineers = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Override
    public String getEntityDesignation() {
        return Translator.ENTITY_NAME_HOSPITAL;
    }

    @Override
    public String getEntityName() {
        return name;
    }
}
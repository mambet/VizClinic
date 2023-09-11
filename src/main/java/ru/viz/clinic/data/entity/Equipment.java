package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.viz.clinic.help.Translator;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "Equipment")
@AllArgsConstructor
@NoArgsConstructor
public class Equipment extends AbstractEntity {
    public Equipment(
            @NotNull final Medic medic,
            @NotNull final Department department
    ) {
        this.medic = Objects.requireNonNull(medic);
        this.department = Objects.requireNonNull(department);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    private Long id;
    private String name;
    private String inventoryNumber;
    private String factoryNumber;
    private String description;
    private LocalDate createDate;
    private LocalDate commissioningDate;
    @ManyToOne
    @JoinColumn(name = "medic_id")
    private Medic medic;
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<Order> orders=new HashSet<>();

    @Override
    public String getEntityDesignation() {
        return Translator.ENTITY_NAME_EQUIPMENT;
    }

    @Override
    public String getEntityName() {
        return name;
    }
}

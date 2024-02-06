package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    private static final String EQUIPMENT_ID_PREFIX = "ОБ";

    public Equipment(
            @NotNull final Medic medic,
            @NotNull final Department department
    ) {
        this.medic = Objects.requireNonNull(medic);
        this.department = Objects.requireNonNull(department);
    }

    @Id
    @GeneratedValue(generator = "hospital-generator")
    @GenericGenerator(name = "hospital-generator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = EQUIPMENT_ID_PREFIX),
            type = ru.viz.clinic.data.IdGenerator.class)
    private String id;
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
    private Set<Order> orders = new HashSet<>();

    @Override
    public String getEntityName() {
        return name;
    }
}

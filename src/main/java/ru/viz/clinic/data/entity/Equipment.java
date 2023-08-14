package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
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
    private String number;
    private String numberNext;
    private String description;
    private LocalDate createDate;
    @ManyToOne
    @JoinColumn(name = "medic_id")
    private Medic medic;
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Order> orders;
}

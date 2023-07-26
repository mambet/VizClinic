package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Table(name = "Equipment")
public class Equipment  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    private Long id;
    private String number;
    private String numberNext;
    private LocalDate createDate;
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}

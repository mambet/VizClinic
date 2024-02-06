package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hospital")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hospital extends AbstractEntity {
    private static final String HOSPITAL_ID_PREFIX = "КЛ";

    @Id
    @GeneratedValue(generator = "hospital-generator")
    @GenericGenerator(name = "hospital-generator",
            parameters = @Parameter(name = "prefix", value = HOSPITAL_ID_PREFIX),
            type = ru.viz.clinic.data.IdGenerator.class)
    private String id;
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
    public String getEntityName() {
        return name;
    }
}
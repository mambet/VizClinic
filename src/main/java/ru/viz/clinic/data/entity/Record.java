package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import ru.viz.clinic.data.RecordType;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "record")
@NoArgsConstructor
@AllArgsConstructor

public class Record extends AbstractEntity {
    private static final String RECORD_ID_PREFIX = "ПР";
    @Id
    @GeneratedValue(generator = "hospital-generator")
    @GenericGenerator(name = "hospital-generator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = RECORD_ID_PREFIX),
            type = ru.viz.clinic.data.IdGenerator.class)
    private String id;
    private RecordType recordType;
    @ManyToOne
    @JoinColumn(name = "engineer_id")
    private Engineer engineer;
    @ManyToOne
    @JoinColumn(name = "medic_id")
    private Medic medic;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
    @Column(name = "comment", length = 1000)
    private String comment;
    @CreationTimestamp
    private LocalDateTime recordTime;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Override
    public String getEntityName() {
        return String.valueOf(id);
    }
}
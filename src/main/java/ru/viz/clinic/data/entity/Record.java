package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.viz.clinic.data.EventType;
import ru.viz.clinic.help.Translator;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "record")
@NoArgsConstructor
@AllArgsConstructor

public class Record extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private EventType eventType;
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
    public String getEntityDesignation() {
        return Translator.ENTITY_NAME_RECORD;
    }

    @Override
    public String getEntityName() {
        return String.valueOf(id);
    }
}
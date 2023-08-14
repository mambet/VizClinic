package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.viz.clinic.data.EventType;

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
    @Column(name = "comment", length = 1000)
    private String comment;
    @CreationTimestamp
    private LocalDateTime recordTime;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}


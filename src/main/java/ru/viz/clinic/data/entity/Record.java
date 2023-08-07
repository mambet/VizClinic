package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
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
    private String person;
    @Column(name = "comment", length = 1000)
    private String comment;
    @UpdateTimestamp
    private LocalDateTime eventTime;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}


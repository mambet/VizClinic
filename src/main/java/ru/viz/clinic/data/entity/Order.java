package ru.viz.clinic.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import ru.viz.clinic.data.OrderState;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "order_table")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends AbstractEntity {
    private static final String ORDER_ID_PREFIX = "ЗВ";
    //immutable fields
    @Id
    @GeneratedValue(generator = "hospital-generator")
    @GenericGenerator(name = "hospital-generator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = ORDER_ID_PREFIX),
            type = ru.viz.clinic.data.IdGenerator.class)
    private String id;
    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;
    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medic_id", nullable = false)
    private Medic medic;

    @Column(name = "create_time")
    @CreationTimestamp
    private LocalDateTime createTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_engineer_id", nullable = false)
    private Set<Engineer> destinationEngineers = new HashSet<>();
    //mutable fields
    private OrderState orderState;
    @ManyToOne
    @JoinColumn(name = "owner_engineer_id")
    private Engineer ownerEngineer;
    @ManyToOne
    @JoinColumn(name = "finish_engineer_id")
    private Engineer finishEngineer;
    @Column(name = "update_time")
    @UpdateTimestamp
    private LocalDateTime updateTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Record> records = new HashSet<>();

    @Override
    public String getEntityName() {
        return String.valueOf(id);
    }
}
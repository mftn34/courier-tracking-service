package com.furkantufan.courier.tracking.entity;

import com.furkantufan.courier.tracking.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "store_entrance",
        indexes = {@Index(name = "ix_storeEntrance_courierIdDate", columnList = "courier_id,date")})
public class StoreEntrance extends BaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = 1495071743121301712L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Instant date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", referencedColumnName = "id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", referencedColumnName = "id", nullable = false)
    private Courier courier;

    @Column(name = "entrance_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Instant entranceTime;

    @Column(name = "total_distance", nullable = false)
    private Double totalDistance;
}
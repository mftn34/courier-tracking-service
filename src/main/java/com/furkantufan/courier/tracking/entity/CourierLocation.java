package com.furkantufan.courier.tracking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furkantufan.courier.tracking.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.time.Instant;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "courier_location",
        indexes = {
                @Index(name = "ix_courierLocation_courierIdTime", columnList = "courier_id,time")
        }
)
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class CourierLocation extends BaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = -8065612678687664875L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Instant time;

    @ToString.Include
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "courier_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Courier courier;

    @NotNull(message = "Latitude value must not be null")
    @Column(name = "latitude", nullable = false)
    @JsonProperty("lat")
    private Double latitude;

    @NotNull(message = "Longitude value must not be null")
    @Column(name = "longitude", nullable = false)
    @JsonProperty("lng")
    private Double longitude;
}
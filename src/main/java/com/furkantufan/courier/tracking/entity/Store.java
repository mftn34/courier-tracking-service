package com.furkantufan.courier.tracking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furkantufan.courier.tracking.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Table(name = "store", indexes = {
        @Index(name = "idx_store_name", columnList = "name")
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Store extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", length = 200, nullable = false)
    @NotBlank
    private String name;

    @NotNull(message = "Latitude value must not be null")
    @Column(name = "latitude", nullable = false)
    @JsonProperty("lat")
    private Double latitude;

    @NotNull(message = "Longitude value must not be null")
    @Column(name = "longitude", nullable = false)
    @JsonProperty("lng")
    private Double longitude;
}
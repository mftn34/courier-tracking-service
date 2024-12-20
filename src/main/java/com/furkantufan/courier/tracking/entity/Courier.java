package com.furkantufan.courier.tracking.entity;

import com.furkantufan.courier.tracking.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "courier", indexes = {
        @Index(name = "idx_courier_phone_number", columnList = "phone_number")
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Courier extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", length = 150, nullable = false)
    @NotBlank
    private String fullName;

    @Column(name = "phoneNumber", length = 11, nullable = false)
    @NotBlank
    private String phoneNumber;
}
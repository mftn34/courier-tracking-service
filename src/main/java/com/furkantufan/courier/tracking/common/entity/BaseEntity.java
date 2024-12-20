package com.furkantufan.courier.tracking.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEntity<T> implements Serializable {

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Instant createdAt;

    @CreatedBy
    @Column(name = "created_by")
    @Builder.Default
    private String createdBy = "courier-tracking-service";

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    private Instant modifiedAt;

    @LastModifiedBy
    @Column(name = "modified_by")
    @Builder.Default
    private String modifiedBy = "courier-tracking-service";

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;

    public boolean isNew() {
        return getId() == null;
    }

    @ToString.Include
    @EqualsAndHashCode.Include
    public abstract T getId();

    public abstract void setId(T id);
}
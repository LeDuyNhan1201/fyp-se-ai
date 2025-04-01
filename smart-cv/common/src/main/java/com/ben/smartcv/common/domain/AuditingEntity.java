package com.ben.smartcv.common.domain;

import com.ben.smartcv.common.infrastructure.database.CustomAuditingEntityListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@MappedSuperclass
@EntityListeners(CustomAuditingEntityListener.class)
public abstract class AuditingEntity {

    @CreatedBy
    @Column(name = "created_by")
    String createdBy;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    Instant createdAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    String updatedBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    Instant updatedAt;

    @Column(name = "is_deleted")
    boolean isDeleted = false;

    @Column(name = "deleted_by")
    String deletedBy;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    Instant deletedAt;

    public void softDelete() {
        isDeleted = true;
    }

}

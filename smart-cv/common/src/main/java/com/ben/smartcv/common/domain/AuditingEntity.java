package com.ben.smartcv.common.domain;

import com.ben.smartcv.common.infrastructure.data.CustomAuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.Version;
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

    @JsonProperty(value = "created_by")
    @CreatedBy
    @Column(name = "created_by")
    String createdBy;

    @JsonProperty(value = "created_at")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    Instant createdAt;

    @JsonProperty(value = "updated_by")
    @LastModifiedBy
    @Column(name = "updated_by")
    String updatedBy;

    @JsonProperty(value = "updated_at")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    Instant updatedAt;

    @JsonProperty(value = "is_deleted")
    @Column(name = "is_deleted")
    boolean isDeleted = false;

    @JsonProperty(value = "deleted_by")
    @Column(name = "deleted_by")
    String deletedBy;

    @JsonProperty(value = "deleted_at")
    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    Instant deletedAt;

    @JsonProperty(value = "version")
    @Version
    @Column(name = "version")
    Long version;

    public void softDelete() {
        isDeleted = true;
    }

}

package com.ben.smartcv.common.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class MongoAuditingDocument {

    @Id
    @Field(name = "_id", targetType = FieldType.OBJECT_ID)
    String id;

    @CreatedBy
    @Field(name = "created_by", targetType = FieldType.STRING)
    String createdBy;

    @CreatedDate
    @Field(name = "created_at", targetType = FieldType.DATE_TIME)
    Instant createdAt;

    @LastModifiedBy
    @Field(name = "updated_by", targetType = FieldType.STRING)
    String updatedBy;

    @LastModifiedDate
    @Field(name = "updated_at", targetType = FieldType.DATE_TIME)
    Instant updatedAt;

    @Field(name = "is_deleted", targetType = FieldType.BOOLEAN)
    Boolean isDeleted;

    @Field(name = "deleted_by", targetType = FieldType.STRING)
    String deletedBy;

    @Field(name = "deleted_at", targetType = FieldType.DATE_TIME)
    Instant deletedAt;

    @Version
    @Field(name = "version", targetType = FieldType.INT64)
    Long version;

}
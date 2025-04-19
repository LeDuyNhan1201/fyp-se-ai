package com.ben.smartcv.job.infrastructure.debezium;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;


public class RequestDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ConnectionRequest<TConfigType> {

        String name;

        TConfigType config;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ConfigForPostgres {

        @JsonProperty("connector.class")
        String connectorClass;

        @JsonProperty("database.hostname")
        String databaseHostname;

        @JsonProperty("database.port")
        String databasePort;

        @JsonProperty("database.user")
        String databaseUser;

        @JsonProperty("database.password")
        String databasePassword;

        @JsonProperty("database.dbname")
        String databaseDbname;

        @JsonProperty("topic.prefix")
        String topicPrefix;

        @JsonProperty("schema.include.list")
        String schemaIncludeList;

        @JsonProperty("table.include.list")
        String tableIncludeList;

        @JsonProperty("slot.name")
        String slotName;

        @JsonProperty("key.converter")
        String keyConverter;

        @JsonProperty("value.converter")
        String valueConverter;

        @JsonProperty("key.converter.schemas.enable")
        String keyConverterSchemasEnable;

        @JsonProperty("value.converter.schemas.enable")
        String valueConverterSchemasEnable;

    }

}
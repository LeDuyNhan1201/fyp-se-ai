package com.ben.smartcv.job.infrastructure.debezium;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CdcOperation {

        READ("r"),
        CREATE("c"),
        UPDATE("u"),
        DELETE("d");

        private final String name;

        CdcOperation(String name) {
            this.name = name;
        }

        @JsonValue
        public String getName() {
            return name;
        }
    }
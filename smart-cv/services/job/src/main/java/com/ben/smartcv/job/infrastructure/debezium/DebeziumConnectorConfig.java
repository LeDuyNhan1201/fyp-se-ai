package com.ben.smartcv.job.infrastructure.debezium;

import com.ben.smartcv.job.application.exception.JobError;
import com.ben.smartcv.job.application.exception.JobHttpException;
import com.ben.smartcv.job.infrastructure.DebeziumRestClient;
import feign.FeignException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DebeziumConnectorConfig {

    @Value("${spring.application.name}")
    private String microserviceName;

    private final DebeziumRestClient debeziumRestClient;

    @Value("${debezium.connectors[0].name}")
    private String connectionName;

    @Value("${debezium.connectors[0].config.connector.class}")
    private String connectorClass;

    @Value("${debezium.connectors[0].config.database.hostname}")
    private String databaseHostname;

    @Value("${debezium.connectors[0].config.database.port}")
    private String databasePort;

    @Value("${debezium.connectors[0].config.database.user}")
    private String databaseUser;

    @Value("${debezium.connectors[0].config.database.password}")
    private String databasePassword;

    @Value("${debezium.connectors[0].config.database.dbname}")
    private String databaseDbname;

    @Value("${debezium.connectors[0].config.topic.prefix}")
    private String topicPrefix;

    @Value("${debezium.connectors[0].config.schema.include.list}")
    private String schemaIncludeList;

    @Value("${debezium.connectors[0].config.table.include.list}")
    private String tableIncludeList;

    @Value("${debezium.connectors[0].config.slot.name}")
    private String slotName;

    @Value("${debezium.connectors[0].config.key.converter}")
    private String keyConverter;

    @Value("${debezium.connectors[0].config.value.converter}")
    private String valueConverter;

    @Value("${debezium.connectors[0].config.key.converter.schemas.enable}")
    private String keyConverterSchemasEnable;

    @Value("${debezium.connectors[0].config.value.converter.schemas.enable}")
    private String valueConverterSchemasEnable;

    @PostConstruct
    public void checkAndCreateConnection() {
        try {
            List<String> connections = debeziumRestClient.getConnections();
            if (!connections.contains(connectionName)) {
                ConnectionRequest request = ConnectionRequest.builder()
                        .name(connectionName)
                        .config(ConnectionRequest.Config.builder()
                                .connectorClass(connectorClass)
                                .databaseHostname(databaseHostname)
                                .databasePort(databasePort)
                                .databaseUser(databaseUser)
                                .databasePassword(databasePassword)
                                .databaseDbname(databaseDbname)
                                .topicPrefix(topicPrefix)
                                .schemaIncludeList(schemaIncludeList)
                                .tableIncludeList(tableIncludeList)
                                .slotName(slotName)
                                .keyConverter(keyConverter)
                                .valueConverter(valueConverter)
                                .keyConverterSchemasEnable(keyConverterSchemasEnable)
                                .valueConverterSchemasEnable(valueConverterSchemasEnable)
                                .build())
                        .build();
                createConnectionAndValidate(request);
            } else {
                log.info("[{}]: Connection '{}' already exists. No action needed.", microserviceName, connectionName);
            }
        } catch (Exception e) {
            log.error("[{}]: Failed to process Debezium connections", microserviceName, e);
            throw new JobHttpException(JobError.DEBEZIUM_CONNECT_FAILED, BAD_REQUEST);
        }
    }

    private void createConnectionAndValidate(ConnectionRequest request) {
        try {
            debeziumRestClient.createConnection(request);
            log.info("[{}]: Debezium connection created successfully.", microserviceName);

        } catch (FeignException e) {
            if (e.status() != 201) {
                log.error("[{}]: Failed to create connection: HTTP Status {}", microserviceName, e.status());
                throw new JobHttpException(JobError.DEBEZIUM_CONNECT_FAILED, BAD_REQUEST);
            }
        }
    }

}
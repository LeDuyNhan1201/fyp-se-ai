package com.ben.smartcv.common.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.Configurer;
import org.axonframework.config.ConfigurerModule;
import org.axonframework.config.MessageMonitorFactory;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.extensions.mongo.spring.SpringMongoTemplate;
import org.axonframework.micrometer.*;
import org.axonframework.monitoring.MultiMessageMonitor;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.axonframework.tracing.LoggingSpanFactory;
import org.axonframework.tracing.MultiSpanFactory;
import org.axonframework.tracing.SpanFactory;
import org.axonframework.tracing.attributes.*;
import org.axonframework.tracing.opentelemetry.OpenTelemetrySpanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
public class AxonConfig {

    @Bean
    @Primary
    public Serializer xStreamSerializer() {
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        xStream.addImmutableType(List.of().getClass(), false);

        return XStreamSerializer.builder()
                .xStream(xStream)
                .build();
    }

    @Bean
    public Serializer jacksonSerializer() {
        return JacksonSerializer.builder()
                .objectMapper(new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)) // Bỏ qua thuộc tính không xác định
                .build();
    }

    @Bean
    public SpanFactory spanFactory() {
        return new MultiSpanFactory(
                Arrays.asList(
                        LoggingSpanFactory.INSTANCE,
                        OpenTelemetrySpanFactory
                                .builder()
                                .addSpanAttributeProviders(
                                        Arrays.asList(
                                                new AggregateIdentifierSpanAttributesProvider(),
                                                new MessageIdSpanAttributesProvider(),
                                                new MessageNameSpanAttributesProvider(),
                                                new MessageTypeSpanAttributesProvider(),
                                                new PayloadTypeSpanAttributesProvider(),
                                                new MetadataSpanAttributesProvider()
                                        )
                                )
                                .build()
                )
        );
    }

    @Bean(name = "axonMongoDbFactory")
    public MongoDatabaseFactory axonMongoDbFactory(@Value("${spring.data.mongodb.axon.uri}") String uri) {
        return new SimpleMongoClientDatabaseFactory(uri);
    }

    @Bean(name = "axonMongoTemplate")
    public MongoTemplate axonMongoTemplate(@Qualifier("axonMongoDbFactory") MongoDatabaseFactory factory) {
        return SpringMongoTemplate.builder()
                .factory(factory)
                .build();
    }

    @Bean
    @Primary
    public EventStore axonEventStore(EventStorageEngine axonStorageEngine,
                                     GlobalMetricRegistry metricRegistry,
                                     @Value("axon.event-bus-name") String eventBusName) {
        return EmbeddedEventStore.builder()
                .storageEngine(axonStorageEngine)
                .messageMonitor(metricRegistry.registerEventBus(eventBusName))
                .spanFactory(spanFactory())
                .build();
    }

    @Bean
    public TokenStore axonTokenStore(MongoTemplate axonMongoTemplate,
                                     TransactionManager transactionManager) {
        return MongoTokenStore.builder()
                .mongoTemplate(axonMongoTemplate)
                .serializer(jacksonSerializer())
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public EventStorageEngine axonStorageEngine(MongoTemplate axonMongoTemplate,
                                            TransactionManager transactionManager) {
        return MongoEventStorageEngine.builder()
                .mongoTemplate(axonMongoTemplate)
                .transactionManager(transactionManager)
                .build();
    }

//    @Bean
//    public CommandBus configureCommandBus() {
//        CommandBus commandBus = SimpleCommandBus.builder().build();
//        commandBus.registerDispatchInterceptor(new CommandLoggingInterceptor());
//        commandBus.registerHandlerInterceptor(new CommandAuthorizationInterceptor());
//        return commandBus;
//    }

//    @Bean
//    public QueryBus configureQueryBus() {
//        QueryBus queryBus = SimpleQueryBus.builder().build();
//        queryBus.registerDispatchInterceptor(new QueryLoggingInterceptor());
//        queryBus.registerHandlerInterceptor(new QueryAuthorizationInterceptor());
//        return queryBus;
//    }

//    @Bean
//    public EventBus configureEventBus(EventStorageEngine eventStorageEngine) {
//        EventBus eventBus = EmbeddedEventStore.builder()
//                .storageEngine(eventStorageEngine)
//                .build();
//        eventBus.registerDispatchInterceptor(new EventLoggingInterceptor());
//        return eventBus;
//    }

    @Bean
    public ConfigurerModule metricConfigurer(MeterRegistry meterRegistry) {
        return configurer -> {
            configureMonitoring(meterRegistry, configurer, EventStore.class);
            configureMonitoring(meterRegistry, configurer, TrackingEventProcessor.class);
            configureMonitoring(meterRegistry, configurer, CommandBus.class);
            configureMonitoring(meterRegistry, configurer, QueryBus.class);
        };
    }

    private <T> void configureMonitoring(MeterRegistry meterRegistry, Configurer configurer, Class<T> componentClass) {
        configurer.configureMessageMonitor(componentClass, createMessageMonitorFactory(meterRegistry));
    }

    private MessageMonitorFactory createMessageMonitorFactory(MeterRegistry meterRegistry) {
        return (configuration, componentType, componentName) -> {
            MessageCountingMonitor messageCounter = MessageCountingMonitor.buildMonitor(
                    componentName, meterRegistry,
                    message -> Tags.of(TagsUtil.PAYLOAD_TYPE_TAG, message.getPayloadType().getSimpleName())
            );

            CapacityMonitor capacityMonitor = CapacityMonitor.buildMonitor(
                    componentName, meterRegistry,
                    message -> Tags.of(TagsUtil.PAYLOAD_TYPE_TAG, message.getPayloadType().getSimpleName())
            );

            MessageTimerMonitor messageTimer = MessageTimerMonitor.builder()
                    .meterNamePrefix(componentName)
                    .meterRegistry(meterRegistry)
                    .tagsBuilder(message -> Tags.of(TagsUtil.PAYLOAD_TYPE_TAG, message.getPayloadType().getSimpleName()))
                    .build();

            return new MultiMessageMonitor<>(messageCounter, messageTimer, capacityMonitor);
        };
    }

}

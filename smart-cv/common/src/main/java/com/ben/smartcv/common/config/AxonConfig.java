package com.ben.smartcv.common.config;

import com.ben.smartcv.common.axoninterceptor.CommandAuthorizationInterceptor;
import com.ben.smartcv.common.axoninterceptor.CommandLoggingInterceptor;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.config.Configurer;
import org.axonframework.config.ConfigurerModule;
import org.axonframework.config.MessageMonitorFactory;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.micrometer.CapacityMonitor;
import org.axonframework.micrometer.MessageCountingMonitor;
import org.axonframework.micrometer.MessageTimerMonitor;
import org.axonframework.micrometer.TagsUtil;
import org.axonframework.monitoring.MultiMessageMonitor;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.SimpleQueryBus;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.axonframework.tracing.LoggingSpanFactory;
import org.axonframework.tracing.MultiSpanFactory;
import org.axonframework.tracing.SpanFactory;
import org.axonframework.tracing.attributes.*;
import org.axonframework.tracing.opentelemetry.OpenTelemetrySpanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;

@Configuration
@Slf4j
public class AxonConfig {

    @Bean
    @Primary
    public Serializer defaultSerializer() {
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);

        return XStreamSerializer.builder()
                .xStream(xStream)
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

    @Bean
    public CommandBus configureCommandBus() {
        CommandBus commandBus = SimpleCommandBus.builder().build();
        commandBus.registerDispatchInterceptor(new CommandLoggingInterceptor());
        commandBus.registerHandlerInterceptor(new CommandAuthorizationInterceptor());
        return commandBus;
    }

    @Bean
    public QueryBus configureQueryBus() {
        QueryBus queryBus = SimpleQueryBus.builder().build();
//        queryBus.registerDispatchInterceptor(new QueryLoggingInterceptor());
//        queryBus.registerHandlerInterceptor(new QueryAuthorizationInterceptor());
        return queryBus;
    }

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

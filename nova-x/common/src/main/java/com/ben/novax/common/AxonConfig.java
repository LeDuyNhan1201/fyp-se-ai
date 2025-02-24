package com.ben.novax.common;

import com.ben.novax.common.axoninterceptor.CommandAuthorizationInterceptor;
import com.ben.novax.common.axoninterceptor.CommandLoggingInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.SimpleQueryBus;
import org.axonframework.tracing.LoggingSpanFactory;
import org.axonframework.tracing.MultiSpanFactory;
import org.axonframework.tracing.SpanFactory;
import org.axonframework.tracing.attributes.*;
import org.axonframework.tracing.opentelemetry.OpenTelemetrySpanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@Slf4j
public class AxonConfig {

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

}

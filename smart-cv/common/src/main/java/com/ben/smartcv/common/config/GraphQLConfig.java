package com.ben.smartcv.common.config;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {

//    @Bean
//    public RuntimeWiring runtimeWiring() {
//        return RuntimeWiring.newRuntimeWiring()
//                .scalar(ExtendedScalars.Date) // Định nghĩa scalar Date
//                .scalar(ExtendedScalars.DateTime) // Nếu cần DateTime
//                .build();
//    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.UUID);
    }

}

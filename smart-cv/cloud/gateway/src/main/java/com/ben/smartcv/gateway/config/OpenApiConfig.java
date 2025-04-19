package com.ben.smartcv.gateway.config;

import jakarta.annotation.PostConstruct;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OpenApiConfig {

    RouteDefinitionLocator locator;

    SwaggerUiConfigProperties swaggerUiConfigProperties;

    @PostConstruct
    public void init() {
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new HashSet<>();

        assert definitions != null;

        definitions.stream()
                .filter(routeDefinition -> routeDefinition.getId().matches(".*-service"))
                .forEach(routeDefinition -> {
                    String prefix = routeDefinition.getId().replaceAll("-service", "");
                    String name = routeDefinition.getId();
                    AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl =
                            new AbstractSwaggerUiConfigProperties.SwaggerUrl(
                                    name, "/" + prefix + "/api-docs/" + name + "-rest-api", convertToTitleCase(name));
                    urls.add(swaggerUrl);
                });
        swaggerUiConfigProperties.setUrls(urls);
    }

    private static String convertToTitleCase(String input) {
        return Arrays.stream(input.split("-"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

}
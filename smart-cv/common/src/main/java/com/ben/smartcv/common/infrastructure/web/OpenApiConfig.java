package com.ben.smartcv.common.infrastructure.web;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi(@Value("${openapi.service.api-docs}") String apiDocs,
                                    @Value("${openapi.service.name}") String serviceName) {
        return GroupedOpenApi.builder()
            .group(apiDocs)
            .packagesToScan("com.ben.smartcv." + serviceName + ".adapter")
            .build();
    }

    @Bean
    public OpenAPI openAPI(
        @Value("${openapi.service.title}") String title,
        @Value("${openapi.service.description}") String description,
        @Value("${openapi.service.version}") String version,
        @Value("${openapi.service.server-url}") String serverUrl,
        @Value("${openapi.service.server-description}") String serverDescription,
        @Value("${gateway.domain}") String gatewayDomain,
        @Value("${gateway.port}") String gatewayPort,
        @Value("${server.servlet.context-path}") String contextPath
    ) {
        return createOpenAPI(
            title,
            description,
            version,
            serverUrl,
            serverDescription,
            gatewayDomain,
            gatewayPort,
            contextPath
        );
    }

    private OpenAPI createOpenAPI(
            String title,
            String description,
            String version,
            String serverUrl,
            String serverDescription,
            String gatewayDomain,
            String gatewayPort,
            String contextPath
    ) {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url(serverUrl).description(serverDescription),
                        new Server().url(String.format("http://%s:%s%s", gatewayDomain, gatewayPort, contextPath))
                ))
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")
                        )
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new Contact()
                                .email("benlun99999@gmail.com")
                                .name("Ben's side projects")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Find out more about this service")
                        .url("http://abc.com")
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .security(List.of(
                        new SecurityRequirement()
                                .addList("bearerAuth")));
    }

}

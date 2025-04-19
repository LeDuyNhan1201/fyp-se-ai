package com.ben.smartcv.curriculum_vitae.infrastructure.data;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
    basePackages = "com.ben.smartcv.curriculum_vitae.infrastructure",
    mongoTemplateRef = "smartCvMongoTemplate"
)
public class SmartCvMongoConfig {

    @Primary
    @Bean(name = "smartCvMongoDbFactory")
    public MongoDatabaseFactory smartCvMongoDbFactory(@Value("${spring.data.mongodb.smart-cv.uri}") String uri) {
        return new SimpleMongoClientDatabaseFactory(uri);
    }

    @Primary
    @Bean(name = "smartCvMongoTemplate")
    public MongoTemplate smartCvMongoTemplate(@Qualifier("smartCvMongoDbFactory") MongoDatabaseFactory factory) {
        return new MongoTemplate(factory);
    }

}

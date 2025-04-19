package com.ben.smartcv.curriculum_vitae;

import com.ben.smartcv.common.infrastructure.web.CommonGrpcServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableDiscoveryClient
@ConfigurationPropertiesScan
@ComponentScan(basePackages = { "com.ben.smartcv.common", "com.ben.smartcv.curriculum_vitae" },
		excludeFilters = {
				@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CommonGrpcServerConfig.class)
})
public class CurriculumVitaeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurriculumVitaeApplication.class, args);
	}

}

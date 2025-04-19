package com.ben.smartcv.notification;

import com.ben.smartcv.common.infrastructure.web.CommonGrpcServerConfig;
import com.ben.smartcv.common.infrastructure.web.OpenApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableDiscoveryClient
@ConfigurationPropertiesScan
@ComponentScan(
		basePackages = { "com.ben.smartcv.common", "com.ben.smartcv.notification" },
		excludeFilters = {
				@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OpenApiConfig.class),
				@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CommonGrpcServerConfig.class),
})
public class NotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}

}

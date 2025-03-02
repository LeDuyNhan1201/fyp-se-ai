package com.ben.smartcv.orchestration;

import com.ben.smartcv.common.config.AxonConfig;
import com.ben.smartcv.common.config.OpenApiConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@SpringBootApplication
//@ComponentScan(
//		basePackages = { "com.ben.smartcv.common", "com.ben.smartcv.orchestration" },
//		excludeFilters = {
//				@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OpenApiConfig.class)
//		}
//)
@Import(AxonConfig.class)
public class OrchestrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrchestrationApplication.class, args);
	}

}

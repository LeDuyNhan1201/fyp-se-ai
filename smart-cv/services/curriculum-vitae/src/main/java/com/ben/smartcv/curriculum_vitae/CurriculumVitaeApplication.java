package com.ben.smartcv.curriculum_vitae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ben.smartcv.common", "com.ben.smartcv.curriculum_vitae" })
public class CurriculumVitaeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurriculumVitaeApplication.class, args);
	}

}

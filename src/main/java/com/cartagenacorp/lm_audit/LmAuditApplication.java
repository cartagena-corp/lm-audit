package com.cartagenacorp.lm_audit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class LmAuditApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(LmAuditApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(LmAuditApplication.class);
	}

}

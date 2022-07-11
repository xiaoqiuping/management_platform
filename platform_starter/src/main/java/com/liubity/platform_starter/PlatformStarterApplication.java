package com.liubity.platform_starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaAuditing
@SpringBootApplication
@ComponentScan("com.liubity")
@EntityScan("com.liubity")
@EnableJpaRepositories(basePackages = "com.liubity.*.service")
public class PlatformStarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlatformStarterApplication.class, args);
	}

}

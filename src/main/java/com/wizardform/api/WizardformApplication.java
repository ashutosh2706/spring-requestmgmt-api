package com.wizardform.api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EntityScan("com.wizardform.api.model")
@EnableAsync
@EnableJpaAuditing
public class WizardformApplication implements CommandLineRunner {
	@Value("${server.port}")
	String port;
	@Value("${spring.application.name}")
	String application;

	public static void main(String[] args) {
		Dotenv.configure().load();
		new SpringApplicationBuilder(WizardformApplication.class).profiles("api").run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.printf("%s started on port: %s\n", this.application, this.port);
	}
}

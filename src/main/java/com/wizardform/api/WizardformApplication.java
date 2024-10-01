package com.wizardform.api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.wizardform.api.model")
public class WizardformApplication {

	public static void main(String[] args) {
		// Configure & Load .env
		Dotenv dotenv = Dotenv.configure().load();
		SpringApplication.run(WizardformApplication.class, args);
	}

}

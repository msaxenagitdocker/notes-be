package com.example.svenskabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"app"})
@EnableJpaRepositories("dao")
@EntityScan("domain")
public class SvenskaBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(SvenskaBackendApplication.class, args);
	}
}


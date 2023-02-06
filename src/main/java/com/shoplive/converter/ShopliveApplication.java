package com.shoplive.converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ShopliveApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopliveApplication.class, args);
	}

}

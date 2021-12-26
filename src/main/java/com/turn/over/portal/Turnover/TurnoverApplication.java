package com.turn.over.portal.Turnover;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.turn.over.portal.*")
@OpenAPIDefinition(info = @Info(title = "Turn Over", version = "3.0", description = "Turn Over Microservice"))
public class TurnoverApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurnoverApplication.class, args);
	}

}

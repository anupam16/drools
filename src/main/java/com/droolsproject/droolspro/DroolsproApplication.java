package com.droolsproject.droolspro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.droolsproject.droolspro.repository")
@ComponentScan(basePackages = {"com.droolsproject.droolspro.service"})
public class DroolsproApplication {

	public static void main(String[] args) {
		SpringApplication.run(DroolsproApplication.class, args);
		System.out.println("successfully Executed....");
	}

}
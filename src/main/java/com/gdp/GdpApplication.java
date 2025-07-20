package com.gdp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GdpApplication {

	public static void main(String[] args) {
		SpringApplication.run(GdpApplication.class, args);
	}

}

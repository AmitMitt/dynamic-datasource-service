package com.example.dynamicdatasourceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class DynamicDatasourceServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(DynamicDatasourceServiceApplication.class, args);
	}
}

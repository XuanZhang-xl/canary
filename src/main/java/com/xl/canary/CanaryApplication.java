package com.xl.canary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.xl.canary.mapper")
public class CanaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanaryApplication.class, args);
	}
}

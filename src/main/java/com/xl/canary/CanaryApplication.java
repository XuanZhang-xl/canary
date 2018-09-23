package com.xl.canary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class CanaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanaryApplication.class, args);
	}
}

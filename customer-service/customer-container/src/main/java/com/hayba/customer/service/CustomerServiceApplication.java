package com.hayba.customer.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = { "com.hayba.customer.service.dataaccess", "com.hayba.dataaccess"})
@EntityScan(basePackages = { "com.hayba.customer.service.dataaccess", "com.hayba.dataaccess" })
@SpringBootApplication(scanBasePackages = "com.hayba")
public class CustomerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}

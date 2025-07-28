package com.infnet.simpleExpenseManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class SimpleExpenseManagerApplication {
    public static void main(String[] args) {
        Dotenv.configure().systemProperties().load();
        SpringApplication.run(SimpleExpenseManagerApplication.class, args);
    }
}

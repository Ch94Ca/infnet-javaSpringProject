package br.edu.infnet.CarlosAraujo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class CarlosAraujoApplication {
    public static void main(String[] args) {
        Dotenv.configure().systemProperties().load();
        SpringApplication.run(CarlosAraujoApplication.class, args);
    }
}

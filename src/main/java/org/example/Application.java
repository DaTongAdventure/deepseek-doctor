package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;


@SpringBootApplication
@MapperScan("org.example.mapper")
public class Application {
    public static void main(String[] args) throws IOException {
        System.out.printf("Hello and welcome!");
        SpringApplication.run(Application.class, args);

    }
}
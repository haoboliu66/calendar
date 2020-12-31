package com.haoboliu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.haoboliu")
public class CalendarApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(CalendarApplication.class, args);
        System.out.println("http://localhost:9999/");

    }

}

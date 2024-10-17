package ru.nedorezova;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoursesEnrollmentApplication {

    public static void main(String[] args) {

        try {
            SpringApplication.run(CoursesEnrollmentApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

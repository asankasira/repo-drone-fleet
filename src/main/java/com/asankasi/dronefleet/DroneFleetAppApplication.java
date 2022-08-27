package com.asankasi.dronefleet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class DroneFleetAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroneFleetAppApplication.class, args);
    }

}

package com.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
        System.out.println("Smart Inventory Management System - Authentication Module Started!");
        System.out.println("API running on: http://localhost:8080");
    }
}

package com.droolsproject.droolspro.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class checkdriver {
    
   
        public static void main(String[] args) {
           String url = "jdbc:mysql://localhost:3306/drools";
        String username = "root";
        String password = "admin@1234";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
    }
    
}

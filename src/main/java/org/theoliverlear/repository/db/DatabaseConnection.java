package org.theoliverlear.repository.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Deprecated
public class DatabaseConnection {
    static final String URL = "jdbc:h2:C:/Users/olive/GitHub/Crypto-Trader/db/crypto_trader_db/crypto_trader_db";
    static final String USER = System.getenv("H2_USER");
    static final String PASSWORD = System.getenv("H2_PW");
    Connection connection;
    public DatabaseConnection() {

    }
    public void start() {
        this.loadDriver();
        this.connectToDatabase();
    }
    public void loadDriver() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Error: Unable to load driver class.");
        }
    }
    public void connectToDatabase() {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        if (this.connection != null) {
            System.out.println("Connected to the database.");
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }
}

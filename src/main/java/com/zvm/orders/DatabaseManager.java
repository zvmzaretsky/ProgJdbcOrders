package com.zvm.orders;

import java.sql.*;

public class DatabaseManager {

    static final String DB_CONNECTION = "jdbc:mysql://localhost:3307/prog1db?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "Password";
    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    protected static void restartDB() throws SQLException {
        try (Statement st = connection.createStatement()) {

            st.execute("DROP TABLE IF EXISTS Clients");
            st.execute("""
                     CREATE
                     TABLE Clients 
                     (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
                     name VARCHAR(20) NOT NULL)
                     """);

            st.execute("DROP TABLE IF EXISTS Products");
            st.execute("""
                     CREATE
                     TABLE Products 
                     (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
                     name VARCHAR(20) NOT NULL)
                     """);

            st.execute("DROP TABLE IF EXISTS Orders");
            st.execute("""
                     CREATE 
                     TABLE Orders 
                     (client_id INT NOT NULL, 
                     product_id INT NOT NULL,
                     CONSTRAINT client_id FOREIGN KEY client_id REFERENCES Clients (id),
                     CONSTRAINT product_id FOREIGN KEY product_id REFERENCES Clients (id))
                     """);
        }
    }

    protected static void addClient(String name) throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement("""
                INSERT
                INTO Clients (name)
                VALUES (?)
                """)) {

            ps.setString(1, name);
            ps.execute();
        }
    }

    protected static void addProduct(String name) throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement("""
                INSERT
                INTO Products (name)
                VALUES (?)
                """)) {

            ps.setString(1, name);
            ps.execute();
        }
    }

    protected static void makeOrder(int client, int product) throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement("""
                INSERT 
                INTO Orders (client_id, product_id) 
                VALUES (?, ?)
                """)) {

            ps.setInt(1, client);
            ps.setInt(2, product);
            ps.execute();
        }
    }
}

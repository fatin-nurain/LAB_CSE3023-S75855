/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lab.dao;


import com.lab.model.Product;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProductDAO {
private String jdbcURL = "jdbc:mysql://localhost:3306/CSE3023";
private String jdbcUsername = "root";
private String jdbcPassword = "admin";

// Method untuk mendapatkan sambungan Database
protected Connection getConnection() {
    Connection connection = null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");

        System.out.println("Connecting to DB...");

        connection = DriverManager.getConnection(
            jdbcURL, jdbcUsername, jdbcPassword);

        System.out.println("✅ SUCCESS CONNECT");

    } catch (Exception e) {
        System.out.println("❌ CONNECTION FAILED");
        e.printStackTrace();
    }
    return connection;
}


// CREATE: Masukkan pengguna baharu
public void insertProduct(Product product) throws SQLException {
String sql = "INSERT INTO products (name, category,price,quantity) VALUES (?, ?, ?,?)";
try (Connection conn = getConnection()){ 
        
        if (conn == null) {
            System.out.println("❌ Conn is NULL - stop here");
            return;
        }
        PreparedStatement ps = conn.prepareStatement(sql) ;
        ps.setString(1, product.getName());
        ps.setString(2, product.getCategory());
        ps.setDouble(3, product.getPrice());
        ps.setInt(4, product.getQuantity());
        ps.executeUpdate();
} catch (SQLException e) {
e.printStackTrace();
}
}



public List<Product> selectAllProducts() {
        List<Product> list = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM products")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(rs.getInt("id"), rs.getString("name"), 
                        rs.getString("category"), rs.getDouble("price"), rs.getInt("quantity")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Product selectProduct(int id) {
        Product product = null;
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT * FROM products WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                product = new Product(id, rs.getString("name"), rs.getString("category"), 
                                      rs.getDouble("price"), rs.getInt("quantity"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return product;
    }

    public boolean updateProduct(Product p) {
        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement("UPDATE products SET name=?, category=?, price=?, quantity=? WHERE id=?")) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getQuantity());
            ps.setInt(5, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean deleteProduct(int id) {
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}
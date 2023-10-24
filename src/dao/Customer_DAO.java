/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import entity.Customer;
import java.util.ArrayList;
import database.ConnectDB;

/**
 *
 * @author Hoàng Khang
 */
public class Customer_DAO {

    public Customer_DAO() {
    }
    public ArrayList<Customer> getAllCustomer() {
        ArrayList<Customer> result = new ArrayList<>();
        try {
            Statement statement = ConnectDB.conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Customer");

            while (resultSet.next()) {
                String customerID = resultSet.getString("cutomerID");
                String name = resultSet.getString("name");
                int score = resultSet.getInt("score");
                boolean gender = resultSet.getBoolean("gender");
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                String phoneNumber = resultSet.getString("phoneNumber");
                String rank = resultSet.getString("rank");
                String address = resultSet.getString("address");

                Customer customer = new Customer(customerID, name, gender, dateOfBirth, score, phoneNumber, rank, address);
                result.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public Customer getCustomerById(String customerID) {
        Customer customer = null;
        try {
            String sql = "SELECT * FROM Customer WHERE cutomerID = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, customerID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int score = resultSet.getInt("score");
                boolean gender = resultSet.getBoolean("gender");
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                String phoneNumber = resultSet.getString("phoneNumber");
                String rank = resultSet.getString("rank");
                String address = resultSet.getString("address");

                customer = new Customer(customerID, name, gender, dateOfBirth, score, phoneNumber, rank, address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }
    
     public boolean addCustomer(Customer customer) {
        try {
            String sql = "INSERT INTO Customer (cutomerID, name, dateOfBirth, gender, phoneNumber, score, rank, address) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

            preparedStatement.setString(1, customer.getCustomerID());
            preparedStatement.setString(2, customer.getName());
            preparedStatement.setDate(3, new java.sql.Date(customer.getDateOfBirth().getTime()));
            preparedStatement.setBoolean(4, customer.isGender());
            preparedStatement.setString(5, customer.getPhoneNumber());
            preparedStatement.setInt(6, customer.getScore());
            preparedStatement.setString(7, customer.getRank());
            preparedStatement.setString(8, customer.getAddress());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     
     public boolean updateCustomer(Customer customer) {
        try {
            String sql = "UPDATE Customer SET name=?, dateOfBirth=?, gender=?, phoneNumber=?, score=?, rank=?, address=? " +
                         "WHERE cutomerID=?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

            preparedStatement.setString(1, customer.getName());
            preparedStatement.setDate(2, new java.sql.Date(customer.getDateOfBirth().getTime()));
            preparedStatement.setBoolean(3, customer.isGender());
            preparedStatement.setString(4, customer.getPhoneNumber());
            preparedStatement.setInt(5, customer.getScore());
            preparedStatement.setString(6, customer.getRank());
            preparedStatement.setString(7, customer.getAddress());
            preparedStatement.setString(8, customer.getCustomerID());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}


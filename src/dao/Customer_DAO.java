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
public class Customer_DAO implements interfaces.DAOBase<Customer> {

    public Customer_DAO() {
    }

    @Override
    public Customer getOne(String id) {
        Customer customer = null;
        try {
            String sql = "SELECT * FROM Customer WHERE cutomerID = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, id);
            
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                int score = resultSet.getInt("score");
                boolean gender = resultSet.getBoolean("gender");
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                String phoneNumber = resultSet.getString("phoneNumber");
                String rank = resultSet.getString("rank");
                String address = resultSet.getString("address");

                customer = new Customer(id, name, gender, dateOfBirth, score, phoneNumber, rank, address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public ArrayList<Customer> getAll() {
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

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(Customer object) {
        try {
            String sql = "INSERT INTO Customer (cutomerID, name, dateOfBirth, gender, phoneNumber, score, rank, address) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

            preparedStatement.setString(1, object.getCustomerID());
            preparedStatement.setString(2, object.getName());
            preparedStatement.setDate(3, new java.sql.Date(object.getDateOfBirth().getTime()));
            preparedStatement.setBoolean(4, object.isGender());
            preparedStatement.setString(5, object.getPhoneNumber());
            preparedStatement.setInt(6, object.getScore());
            preparedStatement.setString(7, object.getRank());
            preparedStatement.setString(8, object.getAddress());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean update(String id, Customer newObject) {
        try {
            String sql = "UPDATE Customer SET name=?, dateOfBirth=?, gender=?, phoneNumber=?, score=?, rank=?, address=? "
                    + "WHERE cutomerID=?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

            preparedStatement.setString(1, newObject.getName());
            preparedStatement.setDate(2, new java.sql.Date(newObject.getDateOfBirth().getTime()));
            preparedStatement.setBoolean(3, newObject.isGender());
            preparedStatement.setString(4, newObject.getPhoneNumber());
            preparedStatement.setInt(5, newObject.getScore());
            preparedStatement.setString(6, newObject.getRank());
            preparedStatement.setString(7, newObject.getAddress());
            preparedStatement.setString(8, id);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

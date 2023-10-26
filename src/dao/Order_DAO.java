/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.Customer;
import entity.Employee;
import entity.Order;
import entity.OrderDetail;
import entity.Promotion;
import interfaces.DAOBase;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author KienTran
 */
public class Order_DAO implements DAOBase<Order>{

    @Override
    public Order getOne(String id) {
        Order order = null;
        try {
            String sql = "SELECT * FROM Order WHERE orderID = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                boolean status = resultSet.getBoolean("status");
                Date orderAt = resultSet.getDate("orderAt");
                boolean payment = resultSet.getBoolean("payment");
                String employeeID = resultSet.getString("employeeID");
                String customerID = resultSet.getString("customerID");
                String promotionID = resultSet.getString("promotionID");
                Double totalDue = resultSet.getDouble("totalDue");
                Double subTotal = resultSet.getDouble("subTotal");
                Promotion promotion = new Promotion(promotionID);
                Customer customer = new Customer(customerID);
                Employee employee = new Employee(employeeID);

                order = new Order(id, orderAt, payment, status, promotion, employee, customer, new OrderDetail_DAO().getAll(id), subTotal, totalDue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public ArrayList<Order> getAll() {
        ArrayList<Order> result = new ArrayList<>();
        try {
            Statement statement = ConnectDB.conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Order");

            while (resultSet.next()) {
                String orderID = resultSet.getString("orderID");
                boolean status = resultSet.getBoolean("status");
                Date orderAt = resultSet.getDate("orderAt");
                boolean payment = resultSet.getBoolean("payment");
                String employeeID = resultSet.getString("employeeID");
                String customerID = resultSet.getString("customerID");
                String promotionID = resultSet.getString("promotionID");
                Double totalDue = resultSet.getDouble("totalDue");
                Double subTotal = resultSet.getDouble("subTotal");
                Promotion promotion = new Promotion(promotionID);
                Customer customer = new Customer(customerID);
                Employee employee = new Employee(employeeID);

                Order order = new Order(orderID, orderAt, payment, status, promotion, employee, customer, new OrderDetail_DAO().getAll(orderID), subTotal, totalDue);
                result.add(order);
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
    public Boolean create(Order object) {
        try {
                String sql = "INSERT INTO Order (orderID, payment, status, orderAt, employeeID, customerID, promotionID, totalDue, subTotal) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

                preparedStatement.setString(1, object.getOrderID());
                preparedStatement.setBoolean(2, object.isPayment());
                preparedStatement.setBoolean(3, object.isStatus());
                preparedStatement.setDate(4, new java.sql.Date(object.getOrderAt().getTime()));
                preparedStatement.setString(5, object.getEmployee().getEmployeeID());
                preparedStatement.setString(6, object.getCustomer().getCustomerID());
                preparedStatement.setString(7, object.getPromotion().getPromotionID());
                preparedStatement.setDouble(8, object.getTotalDue());
                preparedStatement.setDouble(9, object.getSubTotal());

                int rowsAffected = preparedStatement.executeUpdate();
                for (OrderDetail orderDetail : object.getOrderDetail()) {
                    orderDetail.setOrder(object);
                    new OrderDetail_DAO().create(orderDetail);
                }
                return rowsAffected > 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }    
    }

    @Override
    public Boolean update(String id, Order newObject) {
        try {
            String sql = "UPDATE Order SET payment=?, status=?, orderAt=?, employeeID=?, customerID=?, promotionID=?, totalDue=?, subTotal=?  " +
                         "WHERE orderID=?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setBoolean(1, newObject.isPayment());
            preparedStatement.setBoolean(2, newObject.isStatus());
            preparedStatement.setDate(3, new java.sql.Date(newObject.getOrderAt().getTime()));
            preparedStatement.setString(4, newObject.getEmployee().getEmployeeID());
            preparedStatement.setString(5, newObject.getCustomer().getCustomerID());
            preparedStatement.setString(6, newObject.getPromotion().getPromotionID());
            preparedStatement.setDouble(7, newObject.getTotalDue());
            preparedStatement.setDouble(8, newObject.getSubTotal());
            preparedStatement.setString(9, id);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean delete(String id) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("delete from Order where orderID = ?");
            st.setString(1, id);
            new OrderDetail_DAO().delete(id);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    
}

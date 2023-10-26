/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.ReturnOrder;
import interfaces.DAOBase;
import java.util.ArrayList;
import database.ConnectDB;
import entity.Employee;
import entity.Order;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Như Tâm
 */
public class ReturnOrder_DAO implements DAOBase<ReturnOrder>{

    @Override
    public ReturnOrder getOne(String id) {
       ReturnOrder returnOrder = null;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM ReturnOrder WHERE returnOrderID = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {                
                String returnOrderID = rs.getString("returnOrderID");
                String orderID = rs.getString("orderID");
                int status = rs.getInt("status");
                Date orderDate = rs.getDate("orderDate");
                String employeeID = rs.getString("employeeID");
                Order order = new Order(orderID);
                Employee employee = new Employee(employeeID);
                returnOrder = new ReturnOrder(orderDate, status, returnOrderID, employee, order, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnOrder;
    }

    @Override
    public ArrayList<ReturnOrder> getAll() {
        ArrayList<ReturnOrder> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ReturnOrder");
            
            while (rs.next()) {                
                String returnOrderID = rs.getString("returnOrderID");
                String orderID = rs.getString("orderID");
                int status = rs.getInt("status");
                Date orderDate = rs.getDate("orderDate");
                String employeeID = rs.getString("employeeID");
                //boolean type = rs.getBoolean("type");
                Order order = new Order(orderID);
                Employee employee = new Employee(employeeID);
                ReturnOrder returnOrder = new ReturnOrder(orderDate, status, returnOrderID, employee, order, true);
                result.add(returnOrder);
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
    public Boolean create(ReturnOrder returnOrder) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("INSERT INTO ReturnOrder "
                    + "VALUES (?,?,?,?,?)"); //+ "VALUES (?,?,?,?,?,?)");
            st.setString(1, returnOrder.getReturnOrderID());
            st.setString(2, returnOrder.getOrder().getOrderID());
            st.setInt(3, returnOrder.getStatus());
            st.setDate(4, new java.sql.Date(returnOrder.getOrderDate().getTime()));
            st.setString(5, returnOrder.getEmployee().getEmployeeID());
            //st.setBoolean(6, returnOrder.isType());
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, ReturnOrder returnOrder) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("UPDATE ReturnOrder "
                    + "SET status = ?, orderDate = ? "  //+ "SET status = ?, orderDate = ?, type = ?"
                    + "WHERE returnOrderID = ?"); 
            int i= 1;
            st.setInt(i++, returnOrder.getStatus());
            st.setDate(i++, new java.sql.Date(returnOrder.getOrderDate().getTime()));
            //st.setBoolean(i++, returnOrder.isType());
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

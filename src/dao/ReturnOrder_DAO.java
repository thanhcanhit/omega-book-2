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
import entity.ReturnOrderDetail;
import enums.ReturnOrderStatus;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 *
 * @author Như Tâm
 */
public class ReturnOrder_DAO implements DAOBase<ReturnOrder>{

    public static String getMaxSequence(String prefix) {
        try {
        prefix += "%";
        String sql = "  SELECT TOP 1  * FROM ReturnOrder WHERE returnOrderID LIKE '"+prefix+"' ORDER BY returnOrderID DESC;";
        PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            String returnOrderID = rs.getString("returnOrderID");
            return returnOrderID;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
    }

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
                boolean type = rs.getBoolean("type");
                int status = rs.getInt("status");
                Date orderDate = rs.getDate("orderDate");
                String employeeID = rs.getString("employeeID");
                Order order = new Order(orderID);
                Employee employee = new Employee(employeeID);
                returnOrder = new ReturnOrder(orderDate, ReturnOrderStatus.fromInt(status), returnOrderID, employee, order, type);
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
                ReturnOrder returnOrder = new ReturnOrder(orderDate, ReturnOrderStatus.fromInt(status), returnOrderID, employee, order, true);
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
            PreparedStatement st = ConnectDB.conn.prepareStatement("INSERT INTO ReturnOrder(returnOrderID, orderID, status, orderDate, employeeID, type) "
                    + "VALUES (?, ?, ?, ?, ?, ?)"); 
            st.setString(1, returnOrder.getReturnOrderID());
            st.setString(2, returnOrder.getOrder().getOrderID());
            st.setInt(3, returnOrder.getStatus().getValue());
            st.setDate(4, new java.sql.Date(returnOrder.getOrderDate().getTime()));
            st.setString(5, returnOrder.getEmployee().getEmployeeID());
            st.setBoolean(6, returnOrder.isType());
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
                    + "SET status = ?, orderDate = ?, type = ? "
                    + "WHERE returnOrderID = ?"); 
            int i = 1;
            st.setInt(i++, returnOrder.getStatus().getValue());
            st.setDate(i++, new java.sql.Date(returnOrder.getOrderDate().getTime()));
            st.setBoolean(i++, returnOrder.isType());
            st.setString(i++, id);
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

    public Order getOrder(String orderID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

//    public ArrayList<ReturnOrderDetail> getAllReturnOrderDetail(String returnOrderID) {
//        return new ReturnOrderDetail_DAO().getAllForOrderReturnID(returnOrderID);
//    }

    public ArrayList<ReturnOrder> findById(String returnOrderID) {
        ArrayList<ReturnOrder> result = new ArrayList<>();
        String query = """
                       SELECT * FROM ReturnOrder
                       where returnOrdeID LIKE ?
                       """;
        try {

            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            st.setString(1, returnOrderID + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (rs != null) {
                    result.add(getReturnOrderData(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private ReturnOrder getReturnOrderData(ResultSet rs) throws SQLException {
        ReturnOrder result = null;

        //Lấy thông tin tổng quát của lớp ReturnOrder
        String returnOderID = rs.getString("returnOrderID");
        boolean type = rs.getBoolean("type");
        int status = rs.getInt("status");
        Date orderDate = rs.getDate("orderDate");
        String orderID = rs.getString("orderID");
        String employeeID = rs.getString("employeeID");
        Order order = new Order(orderID);
        Employee employee = new Employee(employeeID);
        result = new ReturnOrder(orderDate, ReturnOrderStatus.fromInt(status), returnOderID, employee, order, type);
        return result;
    }
    
    public ArrayList<ReturnOrder> filter(int type, int status) {
        ArrayList<ReturnOrder> result = new ArrayList<>();
//        Index tự động tăng phụ thuộc vào số lượng biến số có
        int index = 1;
        String query = "select * from ReturnOrder WHERE returnOrderID like '%'";
//        Xét loại đơn đổi trả
        if (type != 0)
            query += " and type = ?";
//            Xét trạng thái 
        if (status != 0)
            query += " and status = ?";
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            if(type == 1)
                st.setInt(index++, 0);
            else if(type == 2)
                st.setInt(index++, 1);
            if(status == 1)
                st.setInt(index++, 0);
            else if(status == 2)
                st.setInt(index++, 1);
            else if(status == 3)
                st.setInt(index++, 2);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (rs != null) {
                    result.add(getReturnOrderData(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    public int getNumberOfReturnOrderInMonth(int month, int year){
        int result=0;


        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("select count(returnOrderID) as sl from [ReturnOrder] where YEAR(orderDate) = ? and Month(orderDate) = ? ");
            st.setInt(1, year);
            st.setInt(2, month);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int sl = rs.getInt("sl");
                result=sl;

                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}

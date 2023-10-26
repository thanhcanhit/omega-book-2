/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.OrderDetail;
import entity.Product;
import entity.Order;
import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author KienTran
 */
public class OrderDetail_DAO implements DAOBase<OrderDetail>{

    @Override
    public OrderDetail getOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<OrderDetail> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public ArrayList<OrderDetail> getAll(String id){
        ArrayList<OrderDetail> result = new ArrayList<OrderDetail>();
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("select * from OrderDetail where orderID = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String productID = rs.getString("productID");
                Product product = new Product(productID);
                String orderID = rs.getString("orderID");
                Order order = new Order(orderID);
                Double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                Double lineTotal = rs.getDouble("lineTotal");
                OrderDetail orderDetail = new OrderDetail(order, product, quantity, price, lineTotal);
                result.add(orderDetail);
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
    public Boolean create(OrderDetail object) {
         int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("insert into OrderDetail (orderID, productID, price, quantity, lineTotal) values (?, ?, ?, ?, ?)");
            st.setString(1, object.getOrder().getOrderID());
            st.setString(2, object.getProduct().getProductID());
            st.setDouble(3, object.getPrice());
            st.setInt(4,object.getQuantity() );
            st.setDouble(5, object.getLineTotal());
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, OrderDetail newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("delete from OrderDetail where orderID = ?");
            st.setString(1, id);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    
}

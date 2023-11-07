/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.ReturnOrderDetail;
import interfaces.DAOBase;
import java.util.ArrayList;
import database.ConnectDB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Như Tâm
 */
public class ReturnOrderDetail_DAO implements DAOBase<ReturnOrderDetail>{

    public ReturnOrderDetail getOne(String returnOrderID, String productID) {
        ReturnOrderDetail returnOrderDetail = null;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM ReturnOrderDetail "
                    + "WHERE returnOrderID = ? AND productID = ?");
            st.setString(1, returnOrderID);
            st.setString(2, productID);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                returnOrderID = rs.getString("returnOrderID");
                productID = rs.getString("productID");
                int quantity = rs.getInt("quantity");
                returnOrderDetail = new ReturnOrderDetail(returnOrderID, productID, quantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnOrderDetail;
    }

    @Override
    public ArrayList<ReturnOrderDetail> getAll() {
        ArrayList result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ReturnOrderDetail");
            
            while (rs.next()) {                
                String returnOrderID = rs.getString("returnOrderID");
                String productID = rs.getString("productID");
                int quantity = rs.getInt("quantity");
                ReturnOrderDetail returnOrderDetail = new ReturnOrderDetail(returnOrderID, productID, quantity);
                result.add(returnOrderDetail);
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
    public Boolean create(ReturnOrderDetail returnOrderDetail) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("INSERT INTO ReturnOrderDetail "
                    + "VALUES (?,?)"); // + "VALUES (?,?,?)");
            st.setString(1, returnOrderDetail.getReturnOrderID());
            st.setString(2, returnOrderDetail.getProductID());
            //st.setString(3, returnOrderDetail.getQuantity());
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public ArrayList<ReturnOrderDetail> getAllForOrderReturnID(String id) {
        System.out.println(id);
        ArrayList result = new ArrayList<>();
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM ReturnOrderDetail "
                    + "WHERE returnOrderID = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String productID = rs.getString("productID");
                int quantity = rs.getInt("quantity");
                ReturnOrderDetail returnOrderDetail = new ReturnOrderDetail(id, productID, quantity);
                result.add(returnOrderDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @Override
    public Boolean update(String id, ReturnOrderDetail newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ReturnOrderDetail getOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.PurchaseOrderDetail;
import interfaces.DAOBase;
import java.util.ArrayList;
import database.ConnectDB;
import entity.Product;
import entity.PurchaseOrder;
import java.sql.*;

/**
 *
 * @author KienTran
 */
public class PurchaseOrderDetail_DAO implements DAOBase<PurchaseOrderDetail>{

    @Override
    public PurchaseOrderDetail getOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<PurchaseOrderDetail> getAll() {
         ArrayList<PurchaseOrderDetail> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PurchaseOrderDetail;");
            while (rs.next()) {
                String id = rs.getString("purchaseOrderID");
                String productID = rs.getString("productID");
                int quantity = rs.getInt("Quantity");
                Double costPrice = rs.getDouble("costPrice");
                Double lineTotal = rs.getDouble("lineTotal");
                result.add(new PurchaseOrderDetail(new PurchaseOrder(id), new Product(productID), quantity, costPrice, lineTotal));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public ArrayList<PurchaseOrderDetail> getAll(String id) {
        ArrayList<PurchaseOrderDetail> result = new ArrayList<>();
        try{
            PreparedStatement st = ConnectDB.conn.prepareStatement("Select * from PurchaseOrderDetail where PurchaseOrderID= ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                String productID = rs.getString("productID");
                int quantity = rs.getInt("Quantity");
                Double costPrice = rs.getDouble("costPrice");
                Double lineTotal = rs.getDouble("lineTotal");
                result.add(new PurchaseOrderDetail(new PurchaseOrder(id), new Product(productID), quantity, costPrice, lineTotal));
            }
                   
        }catch(Exception e){
         e.printStackTrace();
        }
        return result;
        
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(PurchaseOrderDetail object) {
        int n = 0;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("INSERT INTO PurchaseOrderDetail(purchaseOrderID, productID, quantity, costPrice, lineTotal) VALUES (?, ?, ?, ?, ?);");
            st.setString(1, object.getPurchaseOrder().getPurchaseOrderID());
            st.setString(2, object.getProduct().getProductID());
            st.setInt(3, object.getQuantity()); 
            st.setDouble(4, object.getCostPrice());
            st.setDouble(5, object.getLineTotal());
          
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return n > 0;
    }

    @Override
    public Boolean update(String id, PurchaseOrderDetail newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("delete from PurchaseOrderDetail where PurchaseOrderID = ?");
            st.setString(1, id);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;}
    
}

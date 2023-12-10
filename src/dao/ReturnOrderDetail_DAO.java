package dao;

import entity.ReturnOrderDetail;
import interfaces.DAOBase;
import java.util.ArrayList;
import database.ConnectDB;
import entity.Product;
import entity.ReturnOrder;
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
                double price = rs.getDouble("price");
                ReturnOrder returnOrder = new ReturnOrder(returnOrderID);
                Product product = new Product(productID);
                returnOrderDetail = new ReturnOrderDetail(returnOrder, product, quantity, price);
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
                ReturnOrder returnOrder = new ReturnOrder(returnOrderID);
                Product product = new Product(productID);
                ReturnOrderDetail returnOrderDetail = new ReturnOrderDetail(returnOrder, product, quantity, product.getPrice());
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
            PreparedStatement st = ConnectDB.conn.prepareStatement("INSERT INTO ReturnOrderDetail(returnOrderID, productID, quantity, price)  "
                    + "VALUES (?,?, ?, ?)");
            st.setString(1, returnOrderDetail.getReturnOrder().getReturnOrderID());
            st.setString(2, returnOrderDetail.getProduct().getProductID());
            st.setInt(3, returnOrderDetail.getQuantity());
            st.setDouble(4, returnOrderDetail.getPrice());
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    public Boolean updateProduct(String id, int quantity) {
        int n = 0;
        Product product = new Product_DAO().getOne(id);
        int newQuantity = product.getInventory() - quantity;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("UPDATE Product "
                    + "SET inventory = ? "
                    + "WHERE productID = ?");
            st.setInt(1, newQuantity);
            st.setString(2, id);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    public Boolean updateRefund(ReturnOrderDetail returnOrderDetail) {
        int n = 0;
        
        return n > 0;
    }
    public ArrayList<ReturnOrderDetail> getAllForOrderReturnID(String id) {
        ArrayList result = new ArrayList<>();
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM ReturnOrderDetail "
                    + "WHERE returnOrderID = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String productID = rs.getString("productID");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                Product product = new Product_DAO().getOne(productID);
                ReturnOrder returnOrder = new ReturnOrder(id);
                ReturnOrderDetail returnOrderDetail = new ReturnOrderDetail(returnOrder, product, quantity, price);
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

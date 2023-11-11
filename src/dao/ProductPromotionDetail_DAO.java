/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.*;
import entity.Product;
import entity.ProductPromotionDetail;
import entity.Promotion;
import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author Như Tâm
 */
public class ProductPromotionDetail_DAO implements DAOBase<ProductPromotionDetail>{

    public ProductPromotionDetail getOne(String promotionID, String productID) throws Exception {
        ProductPromotionDetail productPromotionDetail = null;
        Promotion promotion = new Promotion(promotionID);
        Product product = new Product(productID);
//        try {
//            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM ProductPromotionDetail "
//                    + "WHERE promotionID = ? and productID = ?");
//            st.setString(1, promotionID);
//            st.setString(2, productID);
//            ResultSet rs = st.executeQuery();
//            while (rs.next()) {                
//                Promotion promotion = new Promotion_DAO().getOne(promotionID);
//                Product product = new Product_DAO().getOne(productID);
//                productPromotionDetail = new ProductPromotionDetail(promotion, product);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        productPromotionDetail = new ProductPromotionDetail(promotion, product);
        return productPromotionDetail;
    }
    public ArrayList<ProductPromotionDetail> getAllForProduct(String productID) {
        ArrayList<ProductPromotionDetail> result = new ArrayList<ProductPromotionDetail>();
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM ProductPromotionDetail "
                    + "WHERE productID = ?");
            st.setString(1, productID);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {                
                String promotionID = rs.getString("promotionID");
                Promotion promotion = new Promotion(promotionID);
                Product product = new Product(productID);
                
                ProductPromotionDetail productPromotionDetail = new ProductPromotionDetail(promotion, product);
                result.add(productPromotionDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
        public ArrayList<ProductPromotionDetail> getAllForPromotion(String promotionID) {
        ArrayList<ProductPromotionDetail> result = new ArrayList<ProductPromotionDetail>();
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM ProductPromotionDetail "
                    + "WHERE promotionID = ?");
            st.setString(1, promotionID);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {                
                String productID = rs.getString("productID");
                Promotion promotion = new Promotion(promotionID);
                Product product = new Product(productID);
                
                ProductPromotionDetail productPromotionDetail = getOne(promotionID, productID);
                System.out.println(productPromotionDetail.toString());
                result.add(productPromotionDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public ProductPromotionDetail getOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<ProductPromotionDetail> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(ProductPromotionDetail object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean update(String id, ProductPromotionDetail newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

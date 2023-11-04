/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;
import database.ConnectDB;
import entity.*;
import enums.PromotionType;
import java.util.Date;

/**
 *
 * @author Như Tâm
 */
public class Promotion_DAO implements DAOBase<Promotion>{

    @Override
    public Promotion getOne(String id) {
        Promotion promo = null;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM Promotion WHERE promotionID = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {                
                String promotionID = rs.getString("promotionID");
                int type = rs.getInt("type");
                double discount = rs.getDouble("discount");
                Date startedDate = rs.getDate("startedDate");
                Date endedDate = rs.getDate("startedEnd");
                promo = new Promotion(promotionID, startedDate, endedDate, PromotionType.fromInt(type), discount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return promo;
    }

    @Override
    public ArrayList<Promotion> getAll() {
        ArrayList<Promotion> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Promotion");
            
            while (rs.next()) {                
                String promotionID = rs.getString("promotionID");
                int type = rs.getInt("type");
                double discount = rs.getDouble("discount");
                Date startedDate = rs.getDate("startedDate");
                Date endedDate = rs.getDate("endedDate");
                Promotion promo = new Promotion(promotionID, startedDate, endedDate, PromotionType.fromInt(type), discount);
                result.add(promo);
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
    public Boolean delete(String id) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("DELETE FORM Promotion WHERE promotionID = ?");
            st.setString(1, id);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean create(Promotion promo) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("INSERT INTO Promotion"
                    + "VALUES(?,?,?,?,?)");
            st.setString(1, promo.getPromotionID());
            st.setInt(2, promo.getType().getValue());
            st.setDouble(3, promo.getDiscount());
            st.setDate(4, new java.sql.Date(promo.getStartedDate().getTime()));
            st.setDate(5, new java.sql.Date(promo.getEndedDate().getTime()));
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, Promotion newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

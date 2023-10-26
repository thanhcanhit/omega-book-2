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
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {                
                String promotionID = rs.getString("promotionID");
                int type = rs.getInt("type");
                double discount = rs.getDouble("discount");
                Date startedDate = rs.getDate("startedDate");
                Date endedDate = rs.getDate("endedDate");
                promo = new Promotion(startedDate, endedDate, type, discount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return promo;
    }

    @Override
    public ArrayList<Promotion> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(Promotion object) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean update(String id, Promotion newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

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
                Date endedDate = rs.getDate("endedDate");
                promo = new Promotion(promotionID, startedDate, endedDate, PromotionType.values()[type], discount);
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
                Promotion promo = new Promotion(promotionID, startedDate, endedDate, PromotionType.values()[type], discount);
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
            PreparedStatement st = ConnectDB.conn.prepareStatement("DELETE FROM Promotion WHERE promotionID = ?");
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
                    + "(promotionID, type, discount, startedDate, endedDate)"
                    + "VALUES(?, ?, ?, ?, ?)");
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

    public String getMaxSequence(String prefix) {
        try {
        prefix += "%";
        String sql = "  SELECT TOP 1  * FROM Promotion WHERE promotionID LIKE '"+prefix+"' ORDER BY promotionID DESC;";
        PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            String promotionID = rs.getString("promotionID");
            System.out.println(promotionID);
            return promotionID;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
    }

    public ArrayList<Promotion> findById(String searchQuery) {
        ArrayList<Promotion> result = new ArrayList<>();
        String query = """
                       SELECT * FROM Promotion
                       where promotionID LIKE ?
                       """;
        try {

            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            st.setString(1, searchQuery + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (rs != null) {
                    result.add(getPromotionData(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    private Promotion getPromotionData(ResultSet rs) throws SQLException, Exception {
        Promotion result = null;

        //Lấy thông tin tổng quát của lớp promotion
        String promotionID = rs.getString("promotionID");
        int type = rs.getInt("type");
        double discount = rs.getDouble("discount");
        Date startedDate = rs.getDate("startedDate");
        Date endedDate = rs.getDate("endedDate");
   
        result = new Promotion(promotionID, startedDate, endedDate, PromotionType.fromInt(type), discount);
        return result;
    }

    public ArrayList<Promotion> filter(int type, int status) {
        ArrayList<Promotion> result = new ArrayList<>();
//        Index tự động tăng phụ thuộc vào số lượng biến số có
        int index = 1;
        String query = "select * from Promotion WHERE promotionID like '%'";
//        Xét loại khuyến mãi
        if (type != 0)
            query += " and type = ?";
//            Xét trạng thái khuyến mãi
        if (status == 1)
            query += " and endedDate > GETDATE()";
        else if(status == 2)
            query += " and endedDate < GETDATE()";
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            
            if(type == 1)
                st.setInt(index++, 1);
            else if(type == 2)
                st.setInt(index++, 0);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (rs != null) {
                    result.add(getPromotionData(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}

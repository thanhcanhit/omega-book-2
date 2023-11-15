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
import enums.BookCategory;
import enums.DiscountType;
import enums.CustomerRank;
import enums.PromotionType;
import java.util.Date;

/**
 *
 * @author Như Tâm
 */
public class Promotion_DAO implements DAOBase<Promotion> {

    @Override
    public Promotion getOne(String id) {
        Promotion promo = null;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM Promotion WHERE promotionID = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int typeDiscount = rs.getInt("typeDiscount");
                int typePromotion = rs.getInt("promotionType");
                double discount = rs.getDouble("discount");
                Date startedDate = rs.getDate("startedDate");
                Date endedDate = rs.getDate("endedDate");
                int rankCustomer = rs.getInt("condition");
                ArrayList<ProductPromotionDetail> listDetail = new ProductPromotionDetail_DAO().getAllForPromotion(id);
                promo = new Promotion(id, startedDate, endedDate, PromotionType.fromInt(typePromotion), DiscountType.fromInt(typeDiscount), discount, CustomerRank.fromInt(rankCustomer), listDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return promo;
    }

    public ArrayList<Promotion> getAllForOrder() {
        ArrayList<Promotion> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Promotion WHERE promotionType = 1");

            while (rs.next()) {
                String promotionID = rs.getString("promotionID");
                int typeDiscount = rs.getInt("typeDiscount");
                double discount = rs.getDouble("discount");
                Date startedDate = rs.getDate("startedDate");
                Date endedDate = rs.getDate("endedDate");
                int rankCustomer = rs.getInt("condition");
                Promotion promo = new Promotion(promotionID, startedDate, endedDate, PromotionType.ORDER, DiscountType.fromInt(typeDiscount), discount, CustomerRank.fromInt(rankCustomer));
                result.add(promo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Promotion> getAllForOrderFilterRank(int rank) {
        ArrayList<Promotion> result = new ArrayList<>();
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM Promotion WHERE promotionType = 1 "
                    + "and condition = ?");
            st.setInt(1, rank);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String promotionID = rs.getString("promotionID");
                int typeDiscount = rs.getInt("typeDiscount");
                double discount = rs.getDouble("discount");
                Date startedDate = rs.getDate("startedDate");
                Date endedDate = rs.getDate("endedDate");
                Promotion promo = new Promotion(promotionID, startedDate, endedDate, PromotionType.ORDER, DiscountType.fromInt(typeDiscount), discount, CustomerRank.fromInt(rank));
                result.add(promo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Promotion> getAllForProduct() {
        ArrayList<Promotion> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Promotion WHERE promotionType = 0");

            while (rs.next()) {
                String promotionID = rs.getString("promotionID");
                int typeDiscount = rs.getInt("typeDiscount");
                int promotionType = rs.getInt("promotionType");
                double discount = rs.getDouble("discount");
                Date startedDate = rs.getDate("startedDate");
                Date endedDate = rs.getDate("endedDate");
                ArrayList<ProductPromotionDetail> listDetail = new ProductPromotionDetail_DAO().getAllForPromotion(promotionID);
                Promotion promo = new Promotion(promotionID, startedDate, endedDate, PromotionType.fromInt(promotionType), DiscountType.fromInt(typeDiscount), discount, listDetail);
                result.add(promo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Promotion> getAllForProductFilterProduct(String productID) {
        ArrayList<Promotion> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Promotion WHERE promotionType = 0");

            while (rs.next()) {
                String promotionID = rs.getString("promotionID");
                int typeDiscount = rs.getInt("typeDiscount");
                int promotionType = rs.getInt("promotionType");
                double discount = rs.getDouble("discount");
                Date startedDate = rs.getDate("startedDate");
                Date endedDate = rs.getDate("endedDate");
                ArrayList<ProductPromotionDetail> listDetail = new ProductPromotionDetail_DAO().getAllForProduct(productID);
                Promotion promo = new Promotion(promotionID, startedDate, endedDate, PromotionType.fromInt(promotionType), DiscountType.fromInt(typeDiscount), discount, listDetail);
                result.add(promo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Promotion> getAll() {
        ArrayList<Promotion> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Promotion");

            while (rs.next()) {
                String promotionID = rs.getString("promotionID");
                int typeDiscount = rs.getInt("typeDiscount");
                int promotionType = rs.getInt("promotionType");
                double discount = rs.getDouble("discount");
                Date startedDate = rs.getDate("startedDate");
                Date endedDate = rs.getDate("endedDate");
                int rankCustomer = rs.getInt("condition");
                ArrayList<ProductPromotionDetail> listDetail = new ProductPromotionDetail_DAO().getAllForPromotion(promotionID);

                Promotion promo = new Promotion(promotionID, startedDate, endedDate, PromotionType.fromInt(promotionType), DiscountType.fromInt(typeDiscount), discount, CustomerRank.fromInt(rankCustomer), listDetail);
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
            PreparedStatement st = ConnectDB.conn.prepareStatement("insert into Promotion"
                    + "([promotionID], [typeDiscount], [promotionType], [discount], [startedDate], [endedDate], [condition])"
                    + "VALUES(?, ?, ?, ?, ?, ?, ?)");
            st.setString(1, promo.getPromotionID());
            st.setInt(2, promo.getTypeDiscount().getValue());
            st.setInt(3, promo.getTypePromotion().getValue());
            st.setDouble(4, promo.getDiscount());
            st.setDate(5, new java.sql.Date(promo.getStartedDate().getTime()));
            st.setDate(6, new java.sql.Date(promo.getEndedDate().getTime()));
            st.setInt(7, promo.getCondition().getValue());
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public Boolean createForProduct(Promotion promo) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("insert into Promotion"
                    + "([promotionID], [typeDiscount], [promotionType], [discount], [startedDate], [endedDate])"
                    + "VALUES(?, ?, ?, ?, ?, ?)");
            st.setString(1, promo.getPromotionID());
            st.setInt(2, promo.getTypeDiscount().getValue());
            st.setInt(3, promo.getTypePromotion().getValue());
            st.setDouble(4, promo.getDiscount());
            st.setDate(5, new java.sql.Date(promo.getStartedDate().getTime()));
            st.setDate(6, new java.sql.Date(promo.getEndedDate().getTime()));
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, Promotion newObject) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareCall("UPDATE Promotion "
                    + "SET endedDate = GETDATE() " 
                    + "WHERE promotionID = ?");
            st.setString(1, id);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    public Boolean updateDate(String id) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareCall("UPDATE Promotion "
                    + "SET endedDate = GETDATE() " 
                    + "WHERE promotionID = ?");
            st.setString(1, id);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public String getMaxSequence(String prefix) {
        try {
            prefix += "%";
            String sql = "  SELECT TOP 1  * FROM Promotion WHERE promotionID LIKE '" + prefix + "' ORDER BY promotionID DESC;";
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String promotionID = rs.getString("promotionID");
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
        int typePromotion = rs.getInt("promotionType");
        int typeDiscount = rs.getInt("typeDiscount");
        double discount = rs.getDouble("discount");
        Date startedDate = rs.getDate("startedDate");
        Date endedDate = rs.getDate("endedDate");
        int rankCustomer = rs.getInt("condition");
        ArrayList<ProductPromotionDetail> listDetail = new ProductPromotionDetail_DAO().getAllForPromotion(promotionID);

        result = new Promotion(promotionID, startedDate, endedDate, PromotionType.fromInt(typePromotion), DiscountType.fromInt(typeDiscount), discount, CustomerRank.fromInt(rankCustomer), listDetail);
        return result;
    }

    public ArrayList<Promotion> filter(int type, int status) {
        ArrayList<Promotion> result = new ArrayList<>();
//        Index tự động tăng phụ thuộc vào số lượng biến số có
        int index = 1;
        String query = "select * from Promotion WHERE promotionID like '%'";
//        Xét loại khuyến mãi
        if (type != 0) {
            query += " and typeDiscount = ?";
        }
//            Xét trạng thái khuyến mãi
        if (status == 1) {
            query += " and endedDate > GETDATE()";
        } else if (status == 2) {
            query += " and endedDate < GETDATE()";
        }
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);

            if (type == 1) {
                st.setInt(index++, 1);
            } else if (type == 2) {
                st.setInt(index++, 0);
            }

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

    public ArrayList<Promotion> findForOrderById(String searchQuery) {
        ArrayList<Promotion> result = new ArrayList<>();
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM Promotion WHERE promotionType = 1 "
                    + "and promotionID like '%?'");
            st.setString(1, searchQuery);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String promotionID = rs.getString("promotionID");
                int typeDiscount = rs.getInt("typeDiscount");
                double discount = rs.getDouble("discount");
                Date startedDate = rs.getDate("startedDate");
                Date endedDate = rs.getDate("endedDate");
                int rankCustomer = rs.getInt("condition");
                Promotion promo = new Promotion(promotionID, startedDate, endedDate, PromotionType.ORDER, DiscountType.fromInt(typeDiscount), discount, CustomerRank.fromInt(rankCustomer));
                result.add(promo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Promotion> getPromotionOrderAvailable(int customerPoint) {
        ArrayList<Promotion> result = new ArrayList<>();
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("select * from Promotion where promotionType = 1 and endedDate > GETDATE() and condition <= ?");
            st.setInt(1, customerPoint);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String promotionID = rs.getString("promotionID");
                int typeDiscount = rs.getInt("typeDiscount");
                int promotionType = rs.getInt("promotionType");
                double discount = rs.getDouble("discount");
                Date startedDate = rs.getDate("startedDate");
                Date endedDate = rs.getDate("endedDate");
                int rankCustomer = rs.getInt("condition");
                ArrayList<ProductPromotionDetail> listDetail = new ProductPromotionDetail_DAO().getAllForPromotion(promotionID);

                Promotion promo = new Promotion(promotionID, startedDate, endedDate, PromotionType.fromInt(promotionType), DiscountType.fromInt(typeDiscount), discount, CustomerRank.fromInt(rankCustomer), listDetail);
                result.add(promo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean updateDateStart(Promotion pm) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareCall("UPDATE Promotion "
                    + "SET startedDate = GETDATE()" 
                    + "WHERE promotionID = ?");
            st.setString(1, pm.getPromotionID());
            n = st.executeUpdate();
            if(updateDateEnd(pm) == false)
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    private boolean updateDateEnd(Promotion pm) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareCall("UPDATE Promotion "
                    + "SET endedDate = GETDATE()" 
                    + "WHERE promotionID = ?");
            st.setString(1, pm.getPromotionID());
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean createForOrder(Promotion promo) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("insert into Promotion"
                    + "([promotionID], [typeDiscount], [promotionType], [discount], [startedDate], [endedDate], [condition])"
                    + "VALUES(?, ?, ?, ?, ?, ?, ?)");
            st.setString(1, promo.getPromotionID());
            st.setInt(2, promo.getTypeDiscount().getValue());
            st.setInt(3, promo.getTypePromotion().getValue());
            st.setDouble(4, promo.getDiscount());
            st.setDate(5, new java.sql.Date(promo.getStartedDate().getTime()));
            st.setDate(6, new java.sql.Date(promo.getEndedDate().getTime()));
            st.setInt(7, promo.getCondition().getValue());
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public ArrayList<Promotion> filterForProduct(int type, int status) {
        ArrayList<Promotion> result = new ArrayList<>();
//        Index tự động tăng phụ thuộc vào số lượng biến số có
        int index = 1;
        String query = "select * from Promotion WHERE promotionType = 0 and promotionID like '%'";
//        Xét loại khuyến mãi
        if (type != 0) {
            query += " and typeDiscount = ?";
        }
//            Xét trạng thái khuyến mãi
        if (status == 1) {
            query += " and endedDate > GETDATE()";
        } else if (status == 2) {
            query += " and endedDate < GETDATE()";
        }
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);

            if (type == 1) {
                st.setInt(index++, 1);
            } else if (type == 2) {
                st.setInt(index++, 0);
            }

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
    
        public ArrayList<Promotion> filterForOrder(int type, int status) {
        ArrayList<Promotion> result = new ArrayList<>();
//        Index tự động tăng phụ thuộc vào số lượng biến số có
        int index = 1;
        String query = "select * from Promotion WHERE promotionType = 1 and promotionID like '%'";
//        Xét loại khuyến mãi
        if (type != 0) {
            query += " and typeDiscount = ?";
        }
//            Xét trạng thái khuyến mãi
        if (status == 1) {
            query += " and endedDate > GETDATE()";
        } else if (status == 2) {
            query += " and endedDate < GETDATE()";
        }
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);

            if (type == 1) {
                st.setInt(index++, 1);
            } else if (type == 2) {
                st.setInt(index++, 0);
            }

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

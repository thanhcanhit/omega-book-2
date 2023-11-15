/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.AcountingVoucher;
import entity.CashCountSheet;
import entity.Order;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Hoàng Khang
 */
public class AcountingVoucher_DAO implements interfaces.DAOBase<AcountingVoucher> {

    private CashCountSheet_DAO cashCountSheet_DAO = new CashCountSheet_DAO();

    public AcountingVoucher_DAO() {
    }

    @Override
    public AcountingVoucher getOne(String acountingVoucherID) {
        AcountingVoucher acountingVoucher = null;
        try {
            String sql = "SELECT * FROM AcountingVoucher WHERE acountingVoucherID = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, acountingVoucherID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                Timestamp startTimestamp = resultSet.getTimestamp("startedDate");
                Timestamp endTimestamp = resultSet.getTimestamp("endedDate");

                Date startDate = new java.sql.Date(startTimestamp.getTime());
                Date endDate = new java.sql.Date(endTimestamp.getTime());
                String cashCountSheetID = resultSet.getString("cashCountSheetID");

                acountingVoucher = new AcountingVoucher(acountingVoucherID, startDate, endDate, new CashCountSheet(cashCountSheetID), new Order_DAO().getAllOrderInAcountingVoucher(acountingVoucherID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acountingVoucher;
    }

    public String getMaxSequence(String code) {
        try {
            code += "%";
            String sql = "SELECT TOP 1  * FROM AcountingVoucher WHERE acountingVoucherID LIKE '" + code + "' ORDER BY acountingVoucherID DESC";
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String acountingVoucherID = rs.getString("acountingVoucherID");
                return acountingVoucherID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<AcountingVoucher> getAll() {
        ArrayList<AcountingVoucher> acountingVouchers = new ArrayList<>();

        try {
            String sql = "SELECT * FROM AcountingVoucher";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String acountingVoucherID = resultSet.getString("acountingVoucherID");
                Timestamp startTimestamp = resultSet.getTimestamp("startedDate");
                Timestamp endTimestamp = resultSet.getTimestamp("endedDate");

                Date startDate = new java.sql.Date(startTimestamp.getTime());
                Date endDate = new java.sql.Date(endTimestamp.getTime());

                String cashCountSheetID = resultSet.getString("cashCountSheetID");
                CashCountSheet cashCountSheet = cashCountSheet_DAO.getOne(cashCountSheetID);
                
                 ArrayList<Order> orList = new Order_DAO().getAllOrderInAcountingVoucher(acountingVoucherID);
                 
                AcountingVoucher acountingVoucher = new AcountingVoucher(acountingVoucherID, startDate, endDate, cashCountSheet, new Order_DAO().getAllOrderInAcountingVoucher(acountingVoucherID));

                acountingVouchers.add(acountingVoucher);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return acountingVouchers;
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(AcountingVoucher acountingVoucher) {
        try {
            String sql = "INSERT INTO AcountingVoucher (acountingVoucherID, startedDate, endedDate, cashCountSheetID) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

            preparedStatement.setString(1, acountingVoucher.getAcountingVoucherID());
            Timestamp end = new Timestamp(acountingVoucher.getEndedDate().getTime());
            preparedStatement.setTimestamp(3, end);

            Timestamp start = new Timestamp(acountingVoucher.getCreatedDate().getTime());
            preparedStatement.setTimestamp(2, start);

            preparedStatement.setString(4, acountingVoucher.getCashCountSheet().getCashCountSheetID());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true; // Thành công
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Thất bại
    }

    @Override
    public Boolean update(String id, AcountingVoucher newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

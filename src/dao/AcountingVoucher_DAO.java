/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.AcountingVoucher;
import entity.CashCountSheet;
import entity.Customer;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Hoàng Khang
 */
public class AcountingVoucher_DAO implements interfaces.DAOBase<AcountingVoucher> {

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
                Date createdDate = resultSet.getDate("createdDate");
                Date endedDate = resultSet.getDate("endedDate");
                String cashCountSheetID = resultSet.getString("cashCountSheetID");

                acountingVoucher = new AcountingVoucher(acountingVoucherID, createdDate, endedDate, new CashCountSheet(cashCountSheetID), new Order_DAO().getAllOrderInAcountingVoucher(acountingVoucherID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acountingVoucher;
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
                Date createdDate = resultSet.getDate("createdDate");
                Date endedDate = resultSet.getDate("endedDate");
                String cashCountSheetID = resultSet.getString("cashCountSheetID");

                AcountingVoucher acountingVoucher = new AcountingVoucher(acountingVoucherID, createdDate, endedDate, new CashCountSheet(cashCountSheetID), new Order_DAO().getAllOrderInAcountingVoucher(acountingVoucherID));

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
            String sql = "INSERT INTO AcountingVoucher (acountingVoucherID, createdDate, endedDate, cashCountSheetID) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

            preparedStatement.setString(1, acountingVoucher.getAcountingVoucherID());
            preparedStatement.setDate(2, new java.sql.Date(acountingVoucher.getCreatedDate().getTime()));
            preparedStatement.setDate(3, new java.sql.Date(acountingVoucher.getEndedDate().getTime()));
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

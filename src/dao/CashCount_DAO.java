/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.CashCount;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author Hoàng Khang
 */
public class CashCount_DAO implements interfaces.DAOBase<CashCount> {

    public CashCount_DAO() {
    }

    public CashCount getCashCount(double value, String cashCountSheetID) {
        CashCount cashCount = null;

        try {
            String sql = "SELECT * FROM CashCount WHERE cashCountSheetID = ? AND value = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, cashCountSheetID);
            preparedStatement.setDouble(2, value);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int quantity = resultSet.getInt("quantity");
                double total = resultSet.getDouble("total");

                cashCount = new CashCount(quantity, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cashCount;
    }

    @Override
    public CashCount getOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<CashCount> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Phương thức lấy toàn các CashCount nằm trong CashCountSheet
     * @param cashCountSheetID mã phiếu kết toán cần lấy danh sách kiểm tiền
     * @return ArrayList<CashCount>
     */
    public ArrayList<CashCount> getAll(String cashCountSheetID) {
        ArrayList<CashCount> cashCounts = new ArrayList<>();

        try {
            String sql = "SELECT * FROM CashCount WHERE cashCountSheetID = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, cashCountSheetID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                double value = resultSet.getDouble("value");
                int quantity = resultSet.getInt("quantity");

                CashCount cashCount = new CashCount(quantity, value);
                cashCounts.add(cashCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cashCounts;
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(CashCount cashCount) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public Boolean create(CashCount cashCount, String cashCountSheetID) {
    try {
        String sql = "INSERT INTO CashCount (cashCountSheetID, value, quantity) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

        preparedStatement.setString(1, cashCountSheetID);
        preparedStatement.setDouble(2, cashCount.getValue());
        preparedStatement.setInt(3, cashCount.getQuantity());

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
    public Boolean update(String id, CashCount newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

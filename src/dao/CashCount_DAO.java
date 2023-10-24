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
public class CashCount_DAO {
    public ArrayList<CashCount> getCashCountsBySheetID(String cashCountSheetID) {
        ArrayList<CashCount> result = new ArrayList<>();
        try {
            String sql = "SELECT * FROM CashCount WHERE cashCountSheetID = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, cashCountSheetID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int quantity = resultSet.getInt("quantity");
                double value = resultSet.getDouble("value");

                // Thêm đối tượng CashCount vào danh sách kết quả
                result.add(new CashCount(quantity, value));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
     public boolean saveCashCounts(String cashCountSheetID, ArrayList<CashCount> cashCounts) {
        try {
            // Tiến hành thêm mới dữ liệu
            String sql = "INSERT INTO CashCount (cashCountSheetID, quantity, value) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            for (CashCount cashCount : cashCounts) {
                preparedStatement.setString(1, cashCountSheetID);
                preparedStatement.setInt(2, cashCount.getQuantity());
                preparedStatement.setDouble(3, cashCount.getValue());

                preparedStatement.executeUpdate();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

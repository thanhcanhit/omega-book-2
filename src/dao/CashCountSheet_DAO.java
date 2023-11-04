/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.CashCount;
import entity.CashCountSheet;
import entity.CashCountSheetDetail;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author Hoàng Khang
 */
public class CashCountSheet_DAO implements interfaces.DAOBase<CashCountSheet> {

    private CashCount_DAO cashCount_DAO = new CashCount_DAO();
    private CashCountSheetDetail_DAO cashCountSheetDetail_DAO = new CashCountSheetDetail_DAO();

    @Override
    public CashCountSheet getOne(String id) {
        CashCountSheet cashCountSheet = null;

        try {
            String sql = "SELECT * FROM CashCountSheet WHERE cashCountSheetID = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Date startedDate = resultSet.getDate("startedDate");
                Date endedDate = resultSet.getDate("endedDate");
                cashCountSheet = new CashCountSheet(id, new CashCount_DAO().getAll(id), new CashCountSheetDetail_DAO().getAllCashCountSheetDetailInCashCountSheet(id), endedDate, endedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cashCountSheet;
    }

    @Override
    public ArrayList<CashCountSheet> getAll() {
        ArrayList<CashCountSheet> cashCountSheets = new ArrayList<>();

        try {
            String sql = "SELECT * FROM CashCountSheet";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String cashCountSheetID = resultSet.getString("cashCountSheetID");
                Date startedDate = resultSet.getDate("startedDate");
                Date endedDate = resultSet.getDate("endedDate");

                CashCountSheet cashCountSheet = new CashCountSheet(cashCountSheetID, new CashCount_DAO().getAll(cashCountSheetID), new CashCountSheetDetail_DAO().getAllCashCountSheetDetailInCashCountSheet(cashCountSheetID), endedDate, endedDate);

                cashCountSheets.add(cashCountSheet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cashCountSheets;
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getMaxSequence(String code) {
        try {
            code += "%";
            String sql = "SELECT TOP 1  * FROM CashCountSheet WHERE cashCountSheetID LIKE '" + code + "' ORDER BY cashCountSheetID DESC";
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String cashCountSheetID = rs.getString("cashCountSheetID");
                return cashCountSheetID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean create(CashCountSheet cashCountSheet
    ) {
        try {
            // Thêm bản ghi vào bảng CashCountSheet
            String cashCountSheetSql = "INSERT INTO CashCountSheet (cashCountSheetID, startedDate, endedDate) VALUES (?, ?, ?)";
            PreparedStatement cashCountSheetStatement = ConnectDB.conn.prepareStatement(cashCountSheetSql);

            cashCountSheetStatement.setString(1, cashCountSheet.getCashCountSheetID());
            cashCountSheetStatement.setDate(2, new java.sql.Date(cashCountSheet.getCreatedDate().getTime()));
            cashCountSheetStatement.setDate(3, new java.sql.Date(cashCountSheet.getEndedDate().getTime()));

            int cashCountSheetRowsAffected = cashCountSheetStatement.executeUpdate();

            // Thêm bản ghi vào bảng CashCount
            for (CashCount cashCount : cashCountSheet.getCashCountList()) {
                cashCount_DAO.create(cashCount, cashCountSheet.getCashCountSheetID());
            }

            // Thêm bản ghi vào bảng CashCountSheetDetail
            for (CashCountSheetDetail cashCountSheetDetail : cashCountSheet.getCashCountSheetDetailList()) {
                cashCountSheetDetail_DAO.create(cashCountSheetDetail);
            }

            // Nếu tất cả các bảng đều thêm bản ghi thành công
            if (cashCountSheetRowsAffected > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean update(String id, CashCountSheet newObject
    ) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id
    ) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.CashCountSheet;
import entity.CashCountSheetDetail;
import entity.Employee;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author Hoàng Khang
 */
public class CashCountSheetDetail_DAO implements interfaces.DAOBase<CashCountSheetDetail> {

    public CashCountSheetDetail_DAO() {
    }

    @Override
    public CashCountSheetDetail getOne(String cashCountSheetID) {
        CashCountSheetDetail cashCountSheetDetail = null;

        try {
            String sql = "SELECT * FROM CashCountSheetDetail WHERE cashCountSheetID = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, cashCountSheetID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                boolean index = resultSet.getBoolean("index");
                String employeeID = resultSet.getString("employeeID");

                cashCountSheetDetail = new CashCountSheetDetail(index, new Employee(employeeID), new CashCountSheet(cashCountSheetID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cashCountSheetDetail;
    }

    @Override
    public ArrayList<CashCountSheetDetail> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ArrayList<CashCountSheetDetail> getAllCashCountSheetDetailInCashCountSheet(String cashCountSheetID) {
        ArrayList<CashCountSheetDetail> cashCountSheetDetails = new ArrayList<>();

        try {
            String sql = "SELECT * FROM CashCountSheetDetail WHERE cashCountSheetID = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, cashCountSheetID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                boolean index = resultSet.getBoolean("auditorIndex");
                String employeeID = resultSet.getString("employeeID");

                Employee employee = new Employee(employeeID);
                CashCountSheet cashCountSheet = new CashCountSheet(cashCountSheetID);

                CashCountSheetDetail cashCountSheetDetail = new CashCountSheetDetail(index, employee, cashCountSheet);
                cashCountSheetDetails.add(cashCountSheetDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cashCountSheetDetails;
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
   public Boolean create(CashCountSheetDetail cashCountSheetDetail) {
    try {
        String sql = "INSERT INTO CashCountSheetDetail (auditorIndex, cashCountSheetID, employeeID) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

        preparedStatement.setBoolean(1, cashCountSheetDetail.getIndex());
        preparedStatement.setString(2, cashCountSheetDetail.getCashCountSheet().getCashCountSheetID());
        preparedStatement.setString(3, cashCountSheetDetail.getEmployee().getEmployeeID());
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
    public Boolean update(String id, CashCountSheetDetail newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

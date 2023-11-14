/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.Account;
import entity.Employee;
import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thanhcanhit
 */
public class Account_DAO implements DAOBase<Account> {

//    Lấy tài khoản dựa vào mã nhân viên
    @Override
    public Account getOne(String id) {
        Account result = null;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("Select * from Account where employeeID = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String employeeID = rs.getString("employeeID");
                String password = rs.getString("password");
                result = new Account(password, new Employee(employeeID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(Account_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean validateAccount(String id, String password) {
        boolean isValid = false;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("Select * from Account where employeeID = ? and password = ?");
            st.setString(1, id);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                isValid = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(Account_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isValid;
    }

    @Override
    public ArrayList<Account> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(Account object) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("insert into Account "
                    + " values(?,?)");
            st.setString(1, object.getEmployee().getEmployeeID());
            st.setString(2, object.getPassWord());

            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, Account newObject) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("update Account set "
                    + "password = ? where employeeID = ?");
            st.setString(1, id);
            st.setString(2, newObject.getPassWord());

            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public Boolean updatePass(String id, String newPass) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("update Account set "
                    + "password = ? where employeeID = ?");
            st.setString(1, newPass);
            st.setString(2, id);

            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

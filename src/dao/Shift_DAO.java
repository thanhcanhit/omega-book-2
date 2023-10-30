/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.Account;
import entity.Employee;
import entity.Shift;
import interfaces.DAOBase;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author thanhcanhit
 */
public class Shift_DAO implements DAOBase<Shift> {
    
    @Override
    public Shift getOne(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public ArrayList<Shift> getAll() {
        ArrayList<Shift> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("Select * from Shift");
            while (rs.next()) {
                String employeeID = rs.getString("employeeID");
                String shiftID = rs.getString("shiftID");
                Date started = rs.getDate("startedAt");
                Date ended = rs.getDate("endedAt");
                
                Shift shift = new Shift(shiftID, started, ended, new Account(new Employee(employeeID)));
                result.add(shift);
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
    public Boolean create(Shift object) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("insert into Shift(employeeID, startedAt, endedAt, shiftID) "
                    + " values(?,?,?,?)");
            st.setString(1, object.getAccount().getEmployee().getEmployeeID());
            st.setDate(2, new java.sql.Date(object.getStartedAt().getTime()));
            st.setDate(3, new java.sql.Date(object.getEndedAt().getTime()));
            st.setString(4, object.getShiftID());
            
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    
    @Override
    public Boolean update(String id, Shift newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

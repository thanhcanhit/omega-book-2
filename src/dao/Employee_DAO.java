/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.Employee;
import entity.Store;
import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;

/**
 *
 * @author thanhcanhit
 */
public class Employee_DAO implements DAOBase<Employee> {

    @Override
    public Employee getOne(String id) {
        Employee employee = null;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("Select * from Employee where employeeID = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String role = rs.getString("role");
                Boolean status = rs.getBoolean("status");
                Date date = rs.getDate("dateOfBirth");
                Boolean gender = rs.getBoolean("gender");
                String phoneNumber = rs.getString("phoneNumber");
                String citizenID = rs.getString("citizenIdentification");
                String address = rs.getString("address");
                String storeID = rs.getString("storeID");

                employee = new Employee(id, citizenID, role, status, name, phoneNumber, gender, date, address, new Store(storeID));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return employee;
    }

    @Override
    public ArrayList<Employee> getAll() {
        ArrayList<Employee> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("Select * from Employee");
            while (rs.next()) {
                String employeeID = rs.getString("employeeID");
                String name = rs.getString("name");
                String role = rs.getString("role");
                Boolean status = rs.getBoolean("status");
                Date date = rs.getDate("dateOfBirth");
                Boolean gender = rs.getBoolean("gender");
                String phoneNumber = rs.getString("phoneNumber");
                String citizenID = rs.getString("citizenIdentification");
                String address = rs.getString("address");
                String storeID = rs.getString("storeID");

                Employee employee = new Employee(employeeID, citizenID, role, status, name, phoneNumber, gender, date, address, new Store(storeID));
                result.add(employee);
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
    public Boolean create(Employee object) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("INSERT INTO [dbo].[Employee]"
                    + "([employeeID],[name],[role],[status],[dateOfBirth],[gender],[phoneNumber],[citizenIddentification],[address],[storeID])"
                    + "VALUES (? ,?  ,?  ,?  ,?  ,?  ,?)");
            st.setString(1, object.getEmployeeID());
            st.setString(2, object.getName());
            st.setString(3, object.getRole());
            st.setBoolean(4, object.isStatus());
            st.setDate(5, new java.sql.Date(object.getDateOfBirth().getTime()));
            st.setBoolean(6, object.isGender());
            st.setString(7, object.getPhoneNumber());
            st.setString(8, object.getCitizenIdentification());
            st.setString(9, object.getAddress());
            st.setString(10, object.getStore().getStoreID());

            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, Employee newObject) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("UPDATE [dbo].[Employee]"
                    + "   SET [employeeID] = ?"
                    + "      ,[name] = ?"
                    + "      ,[role] = ?"
                    + "      ,[status] = ?"
                    + "      ,[dateOfBirth] = ?"
                    + "      ,[gender] = ?"
                    + "      ,[phoneNumber] = ?"
                    + "      ,[citizenIddentification] = ?"
                    + "      ,[address] = ?"
                    + "      ,[storeID] = ?"
                    + " WHERE employeeID = ?");
            st.setString(1, id);
            st.setString(2, newObject.getName());
            st.setString(3, newObject.getRole());
            st.setBoolean(4, newObject.isStatus());
            st.setDate(5, new java.sql.Date(newObject.getDateOfBirth().getTime()));
            st.setBoolean(6, newObject.isGender());
            st.setString(7, newObject.getPhoneNumber());
            st.setString(8, newObject.getCitizenIdentification());
            st.setString(9, newObject.getAddress());
            st.setString(10, newObject.getStore().getStoreID());
            st.setString(11, id);

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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.*;
import interfaces.DAOBase;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Như Tâm
 */
public class Supplier_DAO implements DAOBase<Supplier>{

    @Override
    public Supplier getOne(String id) {
        Supplier supplier = null;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM Supplier WHERE supplierID = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                String supplierID = rs.getString("supplierID");
                String name = rs.getString("name");
                String address = rs.getString("address");
                supplier = new Supplier(supplierID, name, address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplier;
    }

    @Override
    public ArrayList<Supplier> getAll() {
        ArrayList<Supplier> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Supplier");
            
            while (rs.next()) {                
                String storeID = rs.getString("supplierID");
                String name = rs.getString("name");
                String address = rs.getString("address");
                Supplier supplier = new Supplier(storeID, name, address);
                result.add(supplier);
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
    public Boolean create(Supplier supplier) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("INSERT INTO Supplier "
                    + "VALUES (?,?,?)");
            st.setString(1, supplier.getSupplierID());
            st.setString(2, supplier.getName());
            st.setString(3, supplier.getAddress());
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, Supplier supplier) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("UPDATE Supplier "
                    + "SET name = ?, address = ?"
                    + "WHERE supplierID = ?");
            int i = 1;
            st.setString(i++, supplier.getName());
            st.setString(i++, supplier.getAddress());
            st.setString(i++, id);
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

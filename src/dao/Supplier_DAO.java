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
        String result = "NCC";

        String query = """
                       select top 1 * from Supplier
                       where supplierID like ?
                       order by supplierID desc
                       """;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            st.setString(1, result + "%");
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String lastID = rs.getString("supplierID");
                String sNumber = lastID.substring(lastID.length() - 2);
                int num = Integer.parseInt(sNumber) + 1;
                result += String.format("%04d", num);
            } else {
                result += String.format("%04d", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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
                    + "SET name = ?, address = ? "
                    + "WHERE supplierID = ?");
            
            st.setString(1, supplier.getName());
            st.setString(2, supplier.getAddress());
            st.setString(3, id);
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

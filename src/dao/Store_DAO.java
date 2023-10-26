/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.Store;
import interfaces.DAOBase;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Như Tâm
 */
public class Store_DAO implements DAOBase<Store>{

    @Override
    public Store getOne(String id) {
        Store store = null;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM Store WHERE storeID = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                String storeID = rs.getString("storeID");
                String name = rs.getString("name");
                String address = rs.getString("address");
                store = new Store(storeID, name, address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return store;
    }

    @Override
    public ArrayList<Store> getAll() {
        ArrayList<Store> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Store");
            
            while (rs.next()) {                
                String storeID = rs.getString("storeID");
                String name = rs.getString("name");
                String address = rs.getString("address");
                Store store = new Store(storeID, name, address);
                result.add(store);
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
    public Boolean create(Store store) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("INSERT INTO Store "
                    + "VALUES (?,?,?)");
            st.setString(1, store.getStoreID());
            st.setString(2, store.getName());
            st.setString(3, store.getAddress());
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, Store newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

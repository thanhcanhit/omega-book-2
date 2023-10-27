/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.Brand;
import entity.Supplier;
import interfaces.DAOBase;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Như Tâm
 */
public class Brand_DAO implements DAOBase<Brand> {

    @Override
    public Brand getOne(String id) {
        Brand brand = null;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM Brand WHERE brandID = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                String brandID = rs.getString("brandID");
                String name = rs.getString("name");
                String country = rs.getString("country");
                brand = new Brand(brandID, name, country);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brand;
    }

    @Override
    public ArrayList<Brand> getAll() {
        ArrayList result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Brand");
            
            while (rs.next()) {                
                String brandID = rs.getString("brandID");
                String name = rs.getString("name");
                String country = rs.getString("country");
                Brand brand = new Brand(brandID, name, country);
                result.add(brand);
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
    public Boolean create(Brand brand) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("INSERT INTO Brand "
                    + "VALUES (?,?,?)");
            st.setString(1, brand.getBrandID());
            st.setString(2, brand.getName());
            st.setString(3, brand.getCountry());
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, Brand brand) {
         int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("UPDATE Brand "
                    + "SET name = ?, country = ?"
                    + "WHERE brandID = ?");
            int i = 1;
            st.setString(i++, brand.getName());
            st.setString(i++, brand.getCountry());
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

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
import java.sql.SQLException;
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
        String result = "TH";

        String query = """
                       select top 1 * from Brand 
                       where brandID like ?
                       order by brandID desc
                       """;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            st.setString(1, result + "%");
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String lastID = rs.getString("brandID");
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
                    + "SET name = ?, country = ? "
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

    public String getMaxSequence(String code) {
        try {
        code += "%";
        String sql = "  SELECT TOP 1  * FROM Brand WHERE brandID LIKE '"+code+"' ORDER BY brandID DESC;";
        PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            String brandID = rs.getString("brandID");
            return brandID;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
    }
    
}

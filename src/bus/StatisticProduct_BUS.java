/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Product_DAO;
import entity.Product;
import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author KienTran
 */
public class StatisticProduct_BUS {
    Product_DAO productDAO = new Product_DAO();
    
    public double getTotalProduct(String productID, String date){
        return productDAO.getTotalProduct(productID, date);
    }
    public int getQuantitySale(String productID,String date){
        return productDAO.getQuantitySale(productID, date);
    }
    public ArrayList<Product> getTop10Product(String date){
        return productDAO.getTop10Product(date);
    }
    public ArrayList<Product> getTopProductInDay(String date){
        return productDAO.getTopProductInDay(date);
    }
    public Product getProduct(String ID){
        return productDAO.getOne(ID);
    }
}

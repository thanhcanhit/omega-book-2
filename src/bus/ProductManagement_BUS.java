/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Product_DAO;
import entity.Product;
import java.util.ArrayList;

/**
 *
 * @author thanhcanhit
 */
public class ProductManagement_BUS {

    private final Product_DAO productDAO = new Product_DAO();

    public ArrayList<Product> getDataOfPage(int page) {
        return productDAO.getPage(page);
    }

    public Product getProduct(String ID) {
        return productDAO.getOne(ID);
    }

    public int getLastPage() {
//        Tổng số sản phẩm / số sản phẩm 1 trang sau đó làm tròn lên
        int result = (int) Math.ceil(Double.valueOf(productDAO.getLength()) / 50);
        return result;
    }

}

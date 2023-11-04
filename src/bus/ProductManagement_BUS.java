/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Product_DAO;
import entity.Product;
import enums.BookCategory;
import enums.StationeryType;
import enums.Type;
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

    public ArrayList<Product> searchById(String searchQuery) {
        return productDAO.findById(searchQuery);
    }

    public ArrayList<Product> filter(String name, Boolean isEmpty, int type, int detailType) {
//      Nếu không lọc loại sản phẩm
        if (type == 0) {
            return productDAO.filter(name, isEmpty, null, null, null);
        }

//      Nếu không lọc loại chi tiết sản phẩm      
        if (detailType == 0) {
            return productDAO.filter(name, isEmpty, Type.fromInt(type), null, null);
        }

//      Có loại sản phẩm và loại chi tiết
        if (Type.fromInt(type) == Type.BOOK) {
            return productDAO.filter(name, isEmpty, Type.BOOK, BookCategory.fromInt(detailType), null);
        } else {
            return productDAO.filter(name, isEmpty, Type.STATIONERY, null, StationeryType.fromInt(detailType));
        }

    }
}

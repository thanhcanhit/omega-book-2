/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Brand_DAO;
import entity.Brand;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Như Tâm
 */
public class BrandManagement_BUS {
    private Brand_DAO brand_DAO = new Brand_DAO();
    
    public ArrayList<Brand> getALLBrand(){
        ArrayList<Brand> brandList = new Brand_DAO().getAll();
        return brandList;
    }
    
    public Brand getOne(String brandID) {
        return brand_DAO.getOne(brandID);
    }
    
    public String generateID() {
        //Khởi tạo mã Thương hiệu TH
        String prefix = "TH";
        //Tìm mã có tiền tố là code và xxxx lớn nhất
        String maxID = brand_DAO.getMaxSequence(prefix);
        if (maxID == null) {
            prefix += "0000";
        } else {
            String lastFourChars = maxID.substring(maxID.length() - 4);
            int num = Integer.parseInt(lastFourChars);
            num++;
            prefix += String.format("%04d", num);
        }
        return prefix;
    }
    
    public void createCustomer(String name, String country) throws Exception {
        Brand brand = new Brand(generateID(), name, country);
        brand_DAO.create(brand);
    }
    
    public void updateCustomer(Brand brand, String brandID) throws Exception {
        brand_DAO.update(brandID, brand);
    }
}

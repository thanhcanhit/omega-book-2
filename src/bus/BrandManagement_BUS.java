/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Brand_DAO;
import entity.Brand;
import java.util.ArrayList;

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
    
    public ArrayList<Brand> search(String id){
            ArrayList<Brand> list = new ArrayList<>();
            for(Brand brand:brand_DAO.getAll()){
                if(brand.getBrandID().equals(id)){
                    list.add(brand);
                }
            }
            return list;
        }
       
    public void create(String name, String country) throws Exception {
        Brand brand = new Brand(brand_DAO.generateID(), name, country);
        brand_DAO.create(brand);
    }
    
    public void update(Brand brand, String brandID) throws Exception {
        brand_DAO.update(brandID, brand);
    }
}

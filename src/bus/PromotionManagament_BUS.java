/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Product_DAO;
import dao.Promotion_DAO;
import entity.Product;
import entity.Promotion;
import enums.DiscountType;
import enums.PromotionType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Như Tâm
 */
public class PromotionManagament_BUS {
    private Promotion_DAO promotion_DAO = new Promotion_DAO();

    public PromotionManagament_BUS() {
    }
    
    public ArrayList<Promotion> getAllPromotion(){
        ArrayList<Promotion> promotionList = new Promotion_DAO().getAll();
        return promotionList;
    }
    public ArrayList<Promotion> getAllPromotionForOrder(){
        ArrayList<Promotion> promotionList = new Promotion_DAO().getAllForOrder();
        return promotionList;
    }
    public ArrayList<Promotion> getAllPromotionForProduct() {
        ArrayList<Promotion> promotionList = new Promotion_DAO().getAllForProduct();
        return promotionList;
    }
    
    public Promotion getOne(String promotionID) {
        return promotion_DAO.getOne(promotionID);
    }
    
    public String generateID(PromotionType promotionType, DiscountType typeDiscount, Date ended) {
        //Khởi tạo mã khuyến mãi KM
        String prefix = "KM";
        //Kí tự tiếp theo là loại giảm giá
        if(typeDiscount.compare(1))
            prefix += 1;
        else
            prefix += 0;
        //Kí tự tiếp theo là loại khuyến mãi
        if(promotionType.compare(1))
            prefix += 1;
        else
            prefix += 0;
        //8 kí tự tiếp theo là ngày tháng kết thúc
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("ddMMyyyy");
        String formatEnded = simpleDateFormat.format(ended);
        
        prefix += formatEnded;
        //Tìm mã có tiền tố là code và xxxx lớn nhất
        String maxID = promotion_DAO.getMaxSequence(prefix);
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
    
    public void createPromotion() throws Exception {
    
    }

    public Promotion getPromotion(String promotionID) {
        return promotion_DAO.getOne(promotionID);

    }

    public ArrayList<Promotion> searchById(String searchQuery) {
        return promotion_DAO.findById(searchQuery);
    }

    public ArrayList<Promotion> filter(int type, int status) {
        return promotion_DAO.filter(type, status);
    }

    public boolean addNewPromotion(Promotion newPromotion) {
        return promotion_DAO.create(newPromotion);
    }

    public boolean removePromotion(String promotionID) {
        return promotion_DAO.delete(promotionID);
    }

    public ArrayList<Product> searchProductById(String searchQuery) {
        return new Product_DAO().findById(searchQuery);
    }

    
}

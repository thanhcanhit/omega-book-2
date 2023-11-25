/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.ProductPromotionDetail_DAO;
import dao.Product_DAO;
import dao.Promotion_DAO;
import entity.Product;
import entity.ProductPromotionDetail;
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
        ArrayList<Promotion> promotionList = promotion_DAO.getAll();
        return promotionList;
    }
    public ArrayList<Promotion> getAllPromotionForOrder(){
        ArrayList<Promotion> promotionList = new Promotion_DAO().getAllForOrder();
        return promotionList;
    }
    public ArrayList<Promotion> getAllPromotionForProduct() {
        ArrayList<Promotion> promotionList = promotion_DAO.getAllForProduct();
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
    
//    public boolean createPromotion(Promotion promo) throws Exception {
//        return promotion_DAO.create(promo);
//    }

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
        return promotion_DAO.createForProduct(newPromotion);
    }

    public boolean removePromotion(String promotionID) {
        return promotion_DAO.updateDate(promotionID);
    }

    public Product searchProductById(String searchQuery) {
        return new Product_DAO().getOne(searchQuery);
    }
    public ArrayList<Promotion> searchForOrderById(String searchQuery) {
        return promotion_DAO.findForOrderById(searchQuery);
    }

    public Product getProduct(String productID) {
        return new Product_DAO().getOne(productID);
    }

    public void createProductPromotionDetail(Promotion newPromotion, ArrayList<ProductPromotionDetail> cart) {
        for (ProductPromotionDetail productPromotionDetail : cart) {
            productPromotionDetail.setPromotion(newPromotion);
            new ProductPromotionDetail_DAO().create(productPromotionDetail);
        }
    }

    public boolean removeProductPromotionDetail(String promotionID) {
        return new ProductPromotionDetail_DAO().delete(promotionID);
    }

    public boolean removeProductPromotionOther(Promotion pm) {
        return promotion_DAO.updateDateStart(pm);
    }

    public boolean removeOrderPromotionOther(Promotion pm) {
        return promotion_DAO.updateDateStart(pm);
    }

    public boolean addNewOrderPromotion(Promotion newPromotion) {
        return promotion_DAO.createForOrder(newPromotion);
    }

    public ArrayList<Promotion> filterForProduct(int type, int status) {
        return promotion_DAO.filterForProduct(type, status);
    }

    public ArrayList<Promotion> filterForOrder(int type, int status) {
        return promotion_DAO.filterForOrder(type, status);
    }

    public Product getOneProduct(String productID) {
        return new Product_DAO().getOne(productID);
    }


    
}

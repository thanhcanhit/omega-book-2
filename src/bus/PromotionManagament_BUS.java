/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Promotion_DAO;
import entity.Promotion;
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
    
    public ArrayList<Promotion> getALLPromotion(){
        ArrayList<Promotion> promotionList = new Promotion_DAO().getAll();
        return promotionList;
    }
    
    public Promotion getOne(String promotionID) {
        return promotion_DAO.getOne(promotionID);
    }
    
    public String generateID(PromotionType promotionType, Date started, Date ended) {
        //Khởi tạo mã khuyến mãi KM
        String prefix = "KM";
        //Kí tự tiếp theo là loại KM
        if(promotionType.compare(1))
            prefix += 1;
        else
            prefix += 0;
        //4 kí tự tiếp theo là ngày tháng bắt đầu
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("ddmm");
        String formatStared = simpleDateFormat.format(started);
        String formatEnded = simpleDateFormat.format(ended);
        
        prefix += formatStared;
        //4 kí tự tiếp theo là ngày tháng kết thúc
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
}

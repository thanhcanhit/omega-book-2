/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import enums.PromotionType;
import java.util.Date;

/**
 *
 * @author KienTran
 */
public final class Promotion {
    private final String PROMOTIONID_ERROR="Mã chương trình khuyến mãi không hợp lệ !";
    private final String STARTEDDATE_ERROR="Ngày tạo chương trình khuyến mãi không được rỗng !";
    private final String ENDEDDATE_ERROR="Ngày kết thúc chương trình khuyến mãi không được rỗng !";
    private final String DISCOUNT_ERROR="Giảm giá không hợp lệ !";
    
    private String promotionID; 
    private Date startedDate;
    private Date endedDate;
    private PromotionType type;
    private double discount;
    
     public String getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(String promotionID) throws Exception{
        
            this.promotionID = promotionID;
    }
    public Date getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Date startedDate) throws Exception {
        if(startedDate!=null)
            this.startedDate = startedDate;
        else
            throw new Exception(STARTEDDATE_ERROR);
    }

    public Date getEndedDate() {
        return endedDate;
    }

    public void setEndedDate(Date endedDate) throws Exception{
        if(endedDate!=null)
            this.endedDate = endedDate;
        else
            throw new Exception(ENDEDDATE_ERROR);
    }

    public PromotionType getType() {
        return type;
    }

    public void setType(PromotionType type) throws Exception{
            this.type = type;
      
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) throws Exception{
        if(discount>=0)
            this.discount = discount;
        else
            throw new Exception(DISCOUNT_ERROR);
    }

    public Promotion() {
    }
    public Promotion(String promotionID) throws Exception{
        setPromotionID(promotionID);
    }
    public Promotion(String promotionID,Date startedDate, Date endedDate, PromotionType type, double discount) throws Exception{
        setPromotionID(promotionID);
        setStartedDate(startedDate);
        setEndedDate(endedDate);
        setDiscount(discount);
        setType(type);
    }

    @Override
    public String toString() {
        return "Promotion{" + "PROMOTIONID_ERROR=" + PROMOTIONID_ERROR + ", STARTEDDATE_ERROR=" + STARTEDDATE_ERROR + ", ENDEDDATE_ERROR=" + ENDEDDATE_ERROR + ", DISCOUNT_ERROR=" + DISCOUNT_ERROR + ", promotionID=" + promotionID + ", startedDate=" + startedDate + ", endedDate=" + endedDate + ", type=" + type + ", discount=" + discount + '}';
    }
    
    
    
}

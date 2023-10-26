/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.util.Date;

/**
 *
 * @author KienTran
 */
public class Promotion {
    private String promotionID;

   
    private Date startedDate;
    private Date endedDate;
    private int type;
    private double discount;
    
     public String getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(String promotionID) {
        this.promotionID = promotionID;
    }
    public Date getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Date startedDate) throws Exception {
        this.startedDate = startedDate;
    }

    public Date getEndedDate() {
        return endedDate;
    }

    public void setEndedDate(Date endedDate) throws Exception{

        this.endedDate = endedDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) throws Exception{
        if(type>=0)
            this.type = type;
      
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) throws Exception{
        this.discount = discount;
    }

    public Promotion() {
    }
    public Promotion(String promotionID){
        setPromotionID(promotionID);
    }
    public Promotion(String promotionID,Date startedDate, Date endedDate, int type, double discount) throws Exception{
        setPromotionID(promotionID);
        setStartedDate(startedDate);
        setEndedDate(endedDate);
        setDiscount(discount);
        setType(type);
    }
    
    @Override
    public String toString() {
        return "Promotion{" + "startedDate=" + startedDate + ", endedDate=" + endedDate + ", type=" + type + ", discount=" + discount + '}';
    }
    
}

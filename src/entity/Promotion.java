package entity;

import enums.DiscountType;
import enums.CustomerRank;
import enums.PromotionType;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 *
 * @author Như Tâm
 */
public final class Promotion {
    public final String PROMOTIONID_ERROR="Mã chương trình khuyến mãi không hợp lệ!";
    public final String STARTEDDATE_ERROR="Ngày tạo chương trình khuyến mãi không được rỗng!";
    public final String ENDEDDATE_ERROR="Ngày kết thúc phải sau ngày tạo chương trình khuyến mãi!";
    public final String DISCOUNT_ERROR="Giảm giá phải là số dương!";
    
    private String promotionID; 
    private Date startedDate;
    private Date endedDate;
    private PromotionType typePromotion;
    private DiscountType typeDiscount;
    private double discount;
    private CustomerRank condition;
    private ArrayList<ProductPromotionDetail> listDetail;

    //constructor đầy đủ
    public Promotion(String promotionID, Date startedDate, Date endedDate, PromotionType typePromotion, DiscountType typeDiscount, double discount, CustomerRank condition, ArrayList<ProductPromotionDetail> listDetail) throws Exception {
        setPromotionID(promotionID);
        setStartedDate(startedDate);
        setEndedDate(endedDate);
        setTypePromotion(typePromotion);
        setTypeDiscount(typeDiscount);
        setDiscount(discount);
        setCondition(condition);
        setListDetail(listDetail);
    }
    //constructor khuyến mãi theo sản phẩm
    public Promotion(String promotionID, Date startedDate, Date endedDate, PromotionType typePromotion, DiscountType typeDiscount, double discount, ArrayList<ProductPromotionDetail> listDetail) throws Exception {
        setPromotionID(promotionID);
        setStartedDate(startedDate);
        setEndedDate(endedDate);
        setTypePromotion(typePromotion);
        setTypeDiscount(typeDiscount);
        setDiscount(discount);
        setListDetail(listDetail);
    }
    //constructor khuyến mãi theo hoá đơn
    public Promotion(String promotionID, Date startedDate, Date endedDate, PromotionType typePromotion, DiscountType typeDiscount, double discount, CustomerRank condition) throws Exception {
        setPromotionID(promotionID);
        setStartedDate(startedDate);
        setEndedDate(endedDate);
        setTypePromotion(typePromotion);
        setTypeDiscount(typeDiscount);
        setDiscount(discount);
        setCondition(condition);
    }

    public Promotion(String promotionID, Date startedDate, Date endedDate, PromotionType typePromotion, DiscountType typeDiscount, double discount) throws Exception {
        setPromotionID(promotionID);
        setStartedDate(startedDate);
        setEndedDate(endedDate);
        setTypePromotion(typePromotion);
        setTypeDiscount(typeDiscount);
        setDiscount(discount);
    }
    
    //constructor theo mã khuyến mãi
    public Promotion(String promotionID) throws Exception {
        setPromotionID(promotionID);
    }
    

    public String getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(String promotionID) throws Exception {
        String pattern = "^(KM)[0-9]{1}[0-9]{1}[0-9]{8}[0-9]{4}$";
        if(!Pattern.matches(pattern, promotionID))
            throw new Exception(PROMOTIONID_ERROR);
        this.promotionID = promotionID;
    }

    public Date getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    public Date getEndedDate() {
        return endedDate;
    }

    public void setEndedDate(Date endedDate) throws Exception {
        if(endedDate.before(startedDate))
            throw new Exception(ENDEDDATE_ERROR);
        this.endedDate = endedDate;
    }

    public PromotionType getTypePromotion() {
        return typePromotion;
    }

    public void setTypePromotion(PromotionType typePromotion) {
        this.typePromotion = typePromotion;
    }

    public DiscountType getTypeDiscount() {
        return typeDiscount;
    }

    public void setTypeDiscount(DiscountType typeDiscount) throws Exception {
        if(discount < 0)
            throw new Exception(DISCOUNT_ERROR);
        this.typeDiscount = typeDiscount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public CustomerRank getCondition() {
        return condition;
    }

    public void setCondition(CustomerRank condition) {
        this.condition = condition;
    }

    public ArrayList<ProductPromotionDetail> getListDetail() {
        return listDetail;
    }

    public void setListDetail(ArrayList<ProductPromotionDetail> listDetail) {
        this.listDetail = listDetail;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.promotionID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Promotion other = (Promotion) obj;
        return Objects.equals(this.promotionID, other.promotionID);
    }

    
    @Override
    public String toString() {
        return "Promotion{" + "PROMOTIONID_ERROR=" + PROMOTIONID_ERROR + ", STARTEDDATE_ERROR=" + STARTEDDATE_ERROR + ", ENDEDDATE_ERROR=" + ENDEDDATE_ERROR + ", DISCOUNT_ERROR=" + DISCOUNT_ERROR + ", promotionID=" + promotionID + ", startedDate=" + startedDate + ", endedDate=" + endedDate + ", typePromotion=" + typePromotion + ", typeDiscount=" + typeDiscount + ", discount=" + discount + ", condition=" + condition + ", listDetail=" + listDetail + '}';
    }
    
    
}

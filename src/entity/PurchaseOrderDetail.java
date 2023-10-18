/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Như Tâm
 */
public class PurchaseOrderDetail {
    
    /* Hằng báo lỗi*/
    public static final String QUANTITY_ERROR = "Số lượng không được bé hơn 0";
    public static final String ORDERID_EMPTY = "Đơn hàng không được rỗng";
    public static final String COSTPRICE_ERROR = "Giá nhập không được bé hơn 0";
    
    private int quantity;
    private double costPrice;
    private double lineTotal;
    private PurchaseOrder purchaseOrder;

    public PurchaseOrderDetail(PurchaseOrder purchaseOrder, int quantity, double costPrice, double lineTotal) throws Exception {
        setPurchaseOrder(purchaseOrder);
        setQuantity(quantity);
        setCostPrice(costPrice);
        setLineTotal();
    }

    public PurchaseOrderDetail() {
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws Exception{
        if(quantity >= 0)
            this.quantity = quantity;
        throw new Exception(QUANTITY_ERROR);
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) throws Exception{
        if(costPrice >= 0)
            this.costPrice = costPrice;
        throw new Exception(COSTPRICE_ERROR);
    }

    public double getLineTotal() {
        return lineTotal;
    }

    /*Tổng = giá trị * số lượng*/
    private void setLineTotal() {
        this.lineTotal = quantity*costPrice;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) throws Exception {
        if(purchaseOrder != null)
            this.purchaseOrder = purchaseOrder;
        throw new Exception(ORDERID_EMPTY);
    }

    @Override
    public String toString() {
        return purchaseOrder + "," + quantity + "," + costPrice + "," + lineTotal;
    }
     
   
}

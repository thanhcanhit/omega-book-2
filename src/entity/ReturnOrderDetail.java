/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Objects;

/**
 *
 * @author Như Tâm
 */
public class ReturnOrderDetail {
    
    /* Hằng báo lỗi*/
    public static final String ORDERID_EMPTY = "Mã hoá đơn không được phép rỗng";
    public static final String PRODUCT_EMPTY = "Mã sản phẩm không được phép rỗng";
    
    private String returnOrderID;
    private String productID;
    private int quantity;

    public ReturnOrderDetail(String returnOrderID, String productID, int quantity) throws Exception {
        setReturnOrderID(returnOrderID);
        setProductID(productID);
        setQuantity(quantity);
    }

    public ReturnOrderDetail() {
    }

    public String getReturnOrderID() {
        return returnOrderID;
    }

    public void setReturnOrderID(String returnOrderID) throws Exception {
        if(returnOrderID.equals(""))
            throw new Exception(ORDERID_EMPTY);
        this.returnOrderID = returnOrderID;
        
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) throws Exception {
        if(productID.equals(""))
            throw new Exception(PRODUCT_EMPTY);
        this.productID = productID;
        
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.returnOrderID);
        hash = 79 * hash + Objects.hashCode(this.productID);
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
        final ReturnOrderDetail other = (ReturnOrderDetail) obj;
        if (!Objects.equals(this.returnOrderID, other.returnOrderID)) {
            return false;
        }
        return Objects.equals(this.productID, other.productID);
    }

    @Override
    public String toString() {
        return "ReturnOrderDetail{" + "returnOrderID=" + returnOrderID + ", productID=" + productID + ", quantity=" + quantity + '}';
    }

    
    
}

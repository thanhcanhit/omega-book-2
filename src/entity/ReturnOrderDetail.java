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
    public static final String ORDERID_EMPTY = "Hoá đơn không được phép rỗng";
    public static final String PRODUCT_EMPTY = "Sản phẩm không được phép rỗng";
    
    private ReturnOrder returnOrder;
    private Product product;
    private int quantity;

    public ReturnOrderDetail(ReturnOrder returnOrder, Product product, int quantity) {
        this.returnOrder = returnOrder;
        this.product = product;
        this.quantity = quantity;
    }

    public ReturnOrderDetail(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }    

    public ReturnOrderDetail() {
    }

    public ReturnOrder getReturnOrder() {
        return returnOrder;
    }

    public void setReturnOrder(ReturnOrder returnOrder) {
        this.returnOrder = returnOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.returnOrder);
        hash = 67 * hash + Objects.hashCode(this.product);
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
        if (!Objects.equals(this.returnOrder, other.returnOrder)) {
            return false;
        }
        return Objects.equals(this.product, other.product);
    }

    @Override
    public String toString() {
        return "ReturnOrderDetail{" + "returnOrder=" + returnOrder + ", product=" + product + ", quantity=" + quantity + '}';
    }
    
}

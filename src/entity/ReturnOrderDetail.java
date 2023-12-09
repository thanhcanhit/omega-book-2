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
    public static final String QUANTITY_VALID = "Số lượng phải là số dương";

    
    private ReturnOrder returnOrder;
    private Product product;
    private int quantity;
    private double price;

    public ReturnOrderDetail(ReturnOrder returnOrder, Product product, int quantity, double price) throws Exception {
        setReturnOrder(returnOrder);
        setProduct(product);
        setQuantity(quantity);
        setPrice(price);
    }

    public ReturnOrderDetail(Product product, int quantity, double price) throws Exception {
        setProduct(product);
        setQuantity(quantity);
        setPrice(price);
    }

    public ReturnOrderDetail(ReturnOrder returnOrder, Product product) throws Exception {
        setReturnOrder(returnOrder);
        setProduct(product);        
    }
    

    public ReturnOrderDetail() {
    }

    public ReturnOrder getReturnOrder() {
        return returnOrder;
    }

    public void setReturnOrder(ReturnOrder returnOrder) throws Exception {
        if(returnOrder == null)
            throw new Exception(ORDERID_EMPTY);
        this.returnOrder = returnOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) throws Exception {
        if(product == null)
            throw new Exception(PRODUCT_EMPTY);
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws Exception {
        if(quantity < 0)
            throw new Exception(QUANTITY_VALID);
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price * quantity;
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
        return "ReturnOrderDetail{" + "returnOrder=" + returnOrder + ", product=" + product + ", quantity=" + quantity + ", price=" + price + '}';
    }    
}

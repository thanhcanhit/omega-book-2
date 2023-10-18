/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author macbookk
 */
public class OrderDetail {
    private final String QUANTITY_ERROR="Số lượng sản phẩm không được nhỏ hơn 1 !";
    private final String PRICE_ERROR="Giá bán phải lớn hơn giá nhập, không được rỗng và lớn hơn 0 !";
    
    private int quantity;
    private double price;
    private double lineTotal;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws Exception{
        if(quantity>0)
            this.quantity = quantity;
        else 
            throw new Exception(QUANTITY_ERROR);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws Exception{
        if(price>0)
            this.price = price;
        else
            throw new Exception(PRICE_ERROR);
    }

    public double getLineTotal() {
        return lineTotal;
    }

    private void setLineTotal() {
        this.lineTotal = this.price*this.quantity;
    }

    public OrderDetail() {
    }

    public OrderDetail(int quantity, double price) {
        this.quantity = quantity;
        this.price = price;
        setLineTotal();
    }

    @Override
    public String toString() {
        return "OrderDetail{" + "quantity=" + quantity + ", price=" + price + ", lineTotal=" + lineTotal + '}';
    }
    
    
}

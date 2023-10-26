/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author macbookk
 */
public final class OrderDetail {
    private final String QUANTITY_ERROR="Số lượng sản phẩm không được nhỏ hơn 1 !";
    private final String PRICE_ERROR="Giá bán phải lớn hơn giá nhập, không được rỗng và lớn hơn 0 !";
    
    private Order order;
    private Product product;
    private int quantity;
    private double price;
    private double lineTotal;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public OrderDetail(Order order, Product product, int quantity, double price) throws Exception{
        setOrder(order);
        setProduct(product);
        setQuantity(quantity);
        setPrice(price);
        setLineTotal();
    }
    public OrderDetail(Order order, Product product,int quantity, double price, double lineTotal) {
        this.order = order;
        this.product= product;       
        this.quantity = quantity;
        this.price = price;
        this.lineTotal=lineTotal;
    }
    @Override
    public String toString() {
        return "OrderDetail{" + "quantity=" + quantity + ", price=" + price + ", lineTotal=" + lineTotal + '}';
    }
    
    
}

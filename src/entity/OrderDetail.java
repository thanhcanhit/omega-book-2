/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author KienTran
 */
public final class OrderDetail {

    private final String QUANTITY_ERROR = "Số lượng sản phẩm không được nhỏ hơn 1 !";
    private final String PRICE_ERROR = "Giá bán phải lớn hơn giá nhập, không được rỗng và lớn hơn 0 !";
    private final String ORDER_ERROR = "Hoá đơn không được rỗng !";
    private final String PRODUCT_ERROR = "Sản phẩm không được rỗng !";

    private Order order;
    private Product product;
    private int quantity;
    private double price;
    private double lineTotal;
    private double VAT;
    private double seasonalDiscount;

    public double getVAT() {
        return VAT;
    }

    public void setVAT(double VAT) {
        this.VAT = VAT;
    }

    public double getSeasonalDiscount() {
        return seasonalDiscount;
    }

    public void setSeasonalDiscount(double seasonalDiscount) {
        this.seasonalDiscount = seasonalDiscount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) throws Exception {
        if (order != null) {
            this.order = order;
        } else {
            throw new Exception(ORDER_ERROR);
        }

    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) throws Exception {
        if (product != null) {
            this.product = product;
        } else {
            throw new Exception(PRODUCT_ERROR);
        }

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws Exception {
        if (quantity > 0) {
            this.quantity = quantity;
            setLineTotal();
        } else {
            throw new Exception(QUANTITY_ERROR);
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws Exception {
        if (price > 0) {
            this.price = price;
            setLineTotal();
        } else {
            throw new Exception(PRICE_ERROR);
        }
    }

    public double getLineTotal() {
        return lineTotal;
    }

    private void setLineTotal() {
        this.lineTotal = this.price * this.quantity;
    }

    public OrderDetail() {
    }

    public OrderDetail(Order order, Product product, int quantity, double price, double VAT, double seasonalDiscount) throws Exception {
        setOrder(order);
        setProduct(product);
        setQuantity(quantity);
        setPrice(price);
        setLineTotal();
        setSeasonalDiscount(seasonalDiscount);
        setVAT(VAT);
    }

    public OrderDetail(Order order, Product product, int quantity, double price, double lineTotal, double VAT, double seasonalDiscount) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.lineTotal = lineTotal;
        this.VAT = VAT;
        this.seasonalDiscount = seasonalDiscount;
    }

    @Override
    public String toString() {
        return "OrderDetail{" + "QUANTITY_ERROR=" + QUANTITY_ERROR + ", PRICE_ERROR=" + PRICE_ERROR + ", ORDER_ERROR=" + ORDER_ERROR + ", PRODUCT_ERROR=" + PRODUCT_ERROR + ", order=" + order + ", product=" + product + ", quantity=" + quantity + ", price=" + price + ", lineTotal=" + lineTotal + '}';
    }

}

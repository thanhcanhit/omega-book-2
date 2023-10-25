/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;


import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author macbookk
 */
public final class Order {
    
    
    private String orderID;
    private Date orderAt;
    private boolean status;
    private double subTotal;
    private double totalDue;
    private Promotion promotion;
    private Employee employee;
    private Customer customer;
    private ArrayList<OrderDetail> orderDetail;

    public ArrayList<OrderDetail> getOrderDetail() {
        return orderDetail;
    }

    
    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID){
        this.orderID = orderID;
        
    }

    public Date getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(Date orderAt) {
        this.orderAt = orderAt;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public double getTotalDue() {
        return totalDue;
    }

    /**
     * Tiền thanh toán = Tổng hóa đơn – Khuyến mãi + Phần trăm thuế*(Tổng hóa đơn-Khuyến mãi)
     */
    private void setTotalDue() {
        this.totalDue = subTotal - ((promotion.getType()==0) ? (promotion.getDiscount()*(subTotal)) : promotion.getDiscount()) ;
    }
    
    
    public double getSubTotal() {
        return subTotal;
    }

    private void setSubTotal() {
        double result = 0;
        for (OrderDetail cthd : orderDetail) {
            result += cthd.getLineTotal();
        }
        this.subTotal=result;
    }

    

    public void setOrderDetail(ArrayList<OrderDetail> orderDetail) {
        this.orderDetail = orderDetail;
    }


    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order(String orderID, Date orderAt, boolean status, Promotion promotion, Employee employee, Customer customer, ArrayList<OrderDetail> orderDetail) {
        setStatus(status);
        setSubTotal();
        setTotalDue();
        setOrderAt(orderAt);
        setCustomer(customer);
        setOrderDetail(orderDetail);
        setPromotion(promotion);
        setOrderID(orderID);
        
    }
    public Order(String orderID, Date orderAt, boolean status, Promotion promotion, Employee employee, Customer customer, ArrayList<OrderDetail> orderDetail, double  subTotal, double toTalDue) {
        this.orderID = orderID;
        this.orderAt = orderAt;
        this.status = status;
        this.subTotal= subTotal;
        this.totalDue= toTalDue;
        this.promotion = promotion;
        this.employee = employee;
        this.customer = customer;
        this.orderDetail=orderDetail;
    }

    public Order() {
    }

    public Order(String orderID) {
        this.orderID = orderID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.orderID);
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
        final Order other = (Order) obj;
        return Objects.equals(this.orderID, other.orderID);
    }

    @Override
    public String toString() {
        return "Order{" + "orderID=" + orderID + ", orderAt=" + orderAt + ", status=" + status + ", subTotal=" + subTotal + ", totalDue=" + totalDue + ", promotion=" + promotion + ", employee=" + employee + ", customer=" + customer + '}';
    }
    
}

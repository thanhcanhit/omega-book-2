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
 * @author KienTran
 */
public final class Order {

    private final String ORDERID_ERROR = "Mã hoá đơn không hợp lệ !";
    private final String ORDERAT_ERROR = "Ngày tạo hoá đơn không hợp lệ !";
    private final String PROMOTION_ERROR = "Khuyến mãi không được rỗng !";
    private final String EMPLOYEE_ERROR = "Nhân viên không được rỗng !";
    private final String CUSTOMER_ERROR = "Khách hàng không được rỗng !";
    private final String ORDERDETAIL_ERROR = "Chi tiết hoá đơn không được rỗng !";

    private String orderID;
    private Date orderAt;
    private boolean status;
    private double subTotal;
    private double totalDue;
    private boolean payment;
    private Promotion promotion;
    private Employee employee;
    private Customer customer;
    private ArrayList<OrderDetail> orderDetail;
    private double moneyGiven;

    public double getMoneyGiven() {
        return moneyGiven;
    }

    public void setMoneyGiven(double moneyGiven) {
        this.moneyGiven = moneyGiven;
    }
    
    

    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    public ArrayList<OrderDetail> getOrderDetail() {
        return orderDetail;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) throws Exception {

        this.orderID = orderID;

    }

    public Date getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(Date orderAt) throws Exception {
        if (orderAt != null) {
            this.orderAt = orderAt;
        } else {
            throw new Exception(ORDERAT_ERROR);
        }
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
     * Tiền thanh toán = Tổng hóa đơn – Khuyến mãi + Phần trăm thuế*(Tổng hóa
     * đơn-Khuyến mãi)
     */
    private void setTotalDue() {
        if (promotion == null) {
            this.totalDue = this.subTotal;
            return;
        }
        this.totalDue = subTotal - ((promotion.getType() == promotion.getType().PERCENT) ? (promotion.getDiscount() * (subTotal)) : promotion.getDiscount());
    }

    public double getSubTotal() {
        return subTotal;
    }

    private void setSubTotal() {
        double result = 0;
        for (OrderDetail cthd : orderDetail) {
            result += cthd.getLineTotal();
        }
        this.subTotal = result;
    }

    public void setOrderDetail(ArrayList<OrderDetail> orderDetail) throws Exception {
        if (!orderDetail.isEmpty()) {
            this.orderDetail = orderDetail;
        } else {
            throw new Exception(ORDERDETAIL_ERROR);
        }
        setSubTotal();
        setTotalDue();
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) throws Exception {
        this.promotion = promotion;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) throws Exception {
        if (employee != null) {
            this.employee = employee;
        } else {
            throw new Exception(EMPLOYEE_ERROR);
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) throws Exception {
        if (customer != null) {
            this.customer = customer;
        } else {
            throw new Exception(CUSTOMER_ERROR);
        }
    }

    public Order(String orderID, Date orderAt, boolean payment, boolean status, Promotion promotion, Employee employee, Customer customer, ArrayList<OrderDetail> orderDetail, double moneyGiven) throws Exception {
        setStatus(status);
        setSubTotal();
        setTotalDue();
        setOrderAt(orderAt);
        setCustomer(customer);
        setOrderDetail(orderDetail);
        setPayment(payment);
        setPromotion(promotion);
        setOrderID(orderID);
        setMoneyGiven(moneyGiven);

    }

    public Order(String orderID, Date orderAt, boolean payment, boolean status, Promotion promotion, Employee employee, Customer customer, ArrayList<OrderDetail> orderDetail, double subTotal, double toTalDue, double moneyGiven) {
        this.orderID = orderID;
        this.orderAt = orderAt;
        this.status = status;
        this.subTotal = subTotal;
        this.totalDue = toTalDue;
        this.promotion = promotion;
        this.payment = payment;
        this.employee = employee;
        this.customer = customer;
        this.orderDetail = orderDetail;
        this.moneyGiven = moneyGiven;
    }

    public Order(String orderID, Date orderAt, boolean status, double subTotal, double totalDue, boolean payment, Employee employee, Customer customer, ArrayList<OrderDetail> orderDetail, double moneyGiven) throws Exception {
        setOrderID(orderID);
        setOrderAt(orderAt);
        setStatus(status);
        setSubTotal();
        setTotalDue();
        setPayment(payment);
        setEmployee(employee);
        setCustomer(customer);
        setOrderDetail(orderDetail);
        setMoneyGiven(moneyGiven);
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
        return "Order{" + "orderID=" + orderID + ", orderAt=" + orderAt + ", status=" + status + ", subTotal=" + subTotal + ", totalDue=" + totalDue + ", payment=" + payment + ", promotion=" + promotion + ", employee=" + employee + ", customer=" + customer + ", orderDetail=" + orderDetail + '}';
    }
}

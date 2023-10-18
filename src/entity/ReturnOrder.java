/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author macbookk
 */
public class ReturnOrder {
    private final String STATUS_ERROR= "Trạng thái không hợp lệ !";
    private final String ORDER_ERROR="Order không được rỗng !";
    private final String EMPLOYEE_ERROR="Employee không được rỗng !";
    
    private Date orderDate;
    private int status;
    private String returnOrderID;
    private Employee employee;
    private Order order;
    private boolean type;

    @Override
    public String toString() {
        return "ReturnOrder{" + "orderDate=" + orderDate + ", status=" + status + ", returnOrderID=" + returnOrderID + ", employee=" + employee + ", order=" + order + ", type=" + type + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.returnOrderID);
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
        final ReturnOrder other = (ReturnOrder) obj;
        return Objects.equals(this.returnOrderID, other.returnOrderID);
    }

    public ReturnOrder(String returnOrderID) {
        this.returnOrderID = returnOrderID;
    }

    public ReturnOrder(Date orderDate, int status, String returnOrderID, Employee employee, Order order, boolean type) {
        this.orderDate = orderDate;
        this.status = status;
        this.returnOrderID = returnOrderID;
        this.employee = employee;
        this.order = order;
        this.type = type;
    }

    public ReturnOrder() {
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) throws Exception{
        if(status==1 || status==0 || status==2)
            this.status = status;
        else
            throw new Exception(STATUS_ERROR);
    }

    public String getReturnOrderID() {
        return returnOrderID;
    }

    public void setReturnOrderID(String returnOrderID) {
        this.returnOrderID = returnOrderID;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) throws Exception{
        if(employee!=null)
            this.employee = employee;
        else
            throw new Exception(EMPLOYEE_ERROR);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) throws Exception{
        if(order!=null)
            this.order = order;
        else
            throw new Exception(ORDER_ERROR);
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}

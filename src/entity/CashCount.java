/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Arrays;

/**
 *
 * @author Hoàng Khang
 */
public class CashCount {
    private int quantity;
    private double value;
    private double total;

    public CashCount() {
    }

    public CashCount(int quantity, double value) {
        setValue(value);
        setQuantity(quantity);
        setTotal();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) throws IllegalArgumentException {
    if (quantity < 0) {
        throw new IllegalArgumentException("Giá trị của quantity phải là số nguyên dương");
    }
    this.quantity = quantity;
}


    public double getValue() {
        return value;
    }

    public void setValue(double value) throws IllegalArgumentException {
//    double[] allowedValues = {1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000};
//    if (!Arrays.stream(allowedValues).anyMatch(val -> val == value)) {
//        throw new IllegalArgumentException("Giá trị của value không hợp lệ!");
//    }
    this.value = value;
}


    public double getTotal() {
        return total;
    }

    private void setTotal() {
        this.total = this.quantity*this.value;
    }

    @Override
    public String toString() {
        return "CashCount{" + "quantity=" + quantity + ", value=" + value + ", total=" + total + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
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
        final CashCount other = (CashCount) obj;
        return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(other.value);
    }
    
    
    
}

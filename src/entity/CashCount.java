/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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

    public CashCount(double value) {
        this.value = value;
    }

    public CashCount(int quantity, double value, double total) {
        this.quantity = quantity;
        this.value = value;
        this.total = total;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
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

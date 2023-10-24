/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Objects;

/**
 *
 * @author Hoàng Khang
 */
public class CashCountSheetDetail {
    private int index;
    private Employee employee;
    private CashCountSheet cashCountSheet;

    public CashCountSheetDetail() {
    }
    

    public CashCountSheetDetail(int index, Employee employee, CashCountSheet cashCountSheet) {
        this.index = index;
        this.employee = employee;
        this.cashCountSheet = cashCountSheet;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public CashCountSheet getCashCountSheet() {
        return cashCountSheet;
    }

    public void setCashCountSheet(CashCountSheet cashCountSheet) {
        this.cashCountSheet = cashCountSheet;
    }

    @Override
    public String toString() {
        return "CashCountSheetDetail{" + "employee=" + employee + ", cashCountSheet=" + cashCountSheet + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.employee);
        hash = 37 * hash + Objects.hashCode(this.cashCountSheet);
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
        final CashCountSheetDetail other = (CashCountSheetDetail) obj;
        if (!Objects.equals(this.employee, other.employee)) {
            return false;
        }
        return Objects.equals(this.cashCountSheet, other.cashCountSheet);
    }
    
    
}

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
public class AccountingVoucherDetail {
    private Employee employee;
    private AccountingVoucher accountingVoucher;

    public AccountingVoucherDetail() {
    }

    public AccountingVoucherDetail(Employee employee, AccountingVoucher accountingVoucher) {
        this.employee = employee;
        this.accountingVoucher = accountingVoucher;
    }

    @Override
    public String toString() {
        return "AccountingVoucherDetail{" + "employee=" + employee + ", accountingVoucher=" + accountingVoucher + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.employee);
        hash = 37 * hash + Objects.hashCode(this.accountingVoucher);
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
        final AccountingVoucherDetail other = (AccountingVoucherDetail) obj;
        if (!Objects.equals(this.employee, other.employee)) {
            return false;
        }
        return Objects.equals(this.accountingVoucher, other.accountingVoucher);
    }
    
    
}

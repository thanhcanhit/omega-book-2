/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Hoàng Khang
 */
public class AccountingVoucher {
    private String accountingVoucherID;
    private Date createdDate;
    private Date endedDate;
    private double sale;
    private double payViaATM;
    private double withDraw;
    private double difference;
    private CashCountSheet cashCountSheet;
    private ArrayList<Order> orderList;

    public AccountingVoucher() {
    }

    public AccountingVoucher(String accountingVoucherID, Date createdDate, Date endedDate, double sale, double payViaATM, double withDraw, double difference, CashCountSheet cashCountSheet,  ArrayList<Order> orderList) {
        setAccountingVoucherID(accountingVoucherID);
        setCreatedDate(createdDate);
        setEndedDate(endedDate);
        setOrderList(orderList);
        setPayViaATM(orderList);
        setWithDraw();
        setDifference();
        setCashCountSheet(cashCountSheet);
    }
    
    

    public AccountingVoucher(String accountingVoucherID) {
        this.accountingVoucherID = accountingVoucherID;
    }

    public String getAccountingVoucherID() {
        return accountingVoucherID;
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    
    public void setAccountingVoucherID(String accountingVoucherID) {
        this.accountingVoucherID = accountingVoucherID;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEndedDate() {
        return endedDate;
    }

    public void setEndedDate(Date endedDate) {
        this.endedDate = endedDate;
    }

    private void setSale(ArrayList<Order> orderList) {
        double sum = 0;
        for (Order order : orderList) {
            sum += order.getTotalDue();
        }
        this.sale = sum;
    }

    public double getSale() {
        return this.sale;
    }

    public double getPayViaATM() {
        return payViaATM;
    }

    public void setPayViaATM(ArrayList<Order> orderList) {
        double sum =0;
        for (Order order : orderList) {
//            if(order.)
        }
    }

    public double getWithDraw() {
        return withDraw;
    }

    public void setWithDraw() {
        this.withDraw = this.sale - this.payViaATM;
    }

    public double getDifference() {
        return difference;
    }

    private void setDifference() {
        this.difference = this.cashCountSheet.getTotal() - this.withDraw - 1765000;
    }

    public CashCountSheet getCashCountSheet() {
        return cashCountSheet;
    }

    public void setCashCountSheet(CashCountSheet cashCountSheet) {
        this.cashCountSheet = cashCountSheet;
    }



    @Override
    public String toString() {
        return "AccountingVoucher{" + "accountingVoucherID=" + accountingVoucherID + ", createdDate=" + createdDate + ", endedDate=" + endedDate + ", sale=" + sale + ", payViaATM=" + payViaATM + ", withDraw=" + withDraw + ", difference=" + difference + ", cashCountSheet=" + cashCountSheet + ", accoutinVoucherDetailList=" +  '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.accountingVoucherID);
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
        final AccountingVoucher other = (AccountingVoucher) obj;
        return Objects.equals(this.accountingVoucherID, other.accountingVoucherID);
    }

}

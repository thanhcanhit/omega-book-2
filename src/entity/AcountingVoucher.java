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
public class AcountingVoucher {

    private String accountingVoucherID;
    private Date createdDate;
    private Date endedDate;
    private double sale;
    private double payViaATM;
    private double withDraw;
    private double difference;
    private CashCountSheet cashCountSheet;
    private ArrayList<Order> orderList;

    public AcountingVoucher() {
    }

    public AcountingVoucher(String accountingVoucherID, Date createdDate, Date endedDate, CashCountSheet cashCountSheet, ArrayList<Order> orderList) {
        setAcountingVoucherID(accountingVoucherID);
        setCreatedDate(createdDate);
        setEndedDate(endedDate);
        setCashCountSheet(cashCountSheet);
        setOrderList(orderList);
        setSale();
        setPayViaATM();
        setWithDraw();
        setDifference();

        

    }

    public AcountingVoucher(String accountingVoucherID) {
        this.accountingVoucherID = accountingVoucherID;
    }

    public AcountingVoucher(Date endedDate) {
        this.endedDate = endedDate;
    }

    public String getAcountingVoucherID() {
        return accountingVoucherID;
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    public void setAcountingVoucherID(String accountingVoucherID) {
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

    private void setSale() {
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

    public void setPayViaATM() {
        double sum = 0;
        for (Order order : orderList) {
            if (!order.isPayment()) {
                sum += order.getTotalDue();
            }
        }
        this.payViaATM = sum;
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
        this.difference = cashCountSheet.getTotal() - sale + payViaATM - 1765000;
    }

    public CashCountSheet getCashCountSheet() {
        return cashCountSheet;
    }

    public void setCashCountSheet(CashCountSheet cashCountSheet) {
        this.cashCountSheet = cashCountSheet;
    }

    @Override
    public String toString() {
        return "AccountingVoucher{" + "accountingVoucherID=" + accountingVoucherID + ", createdDate=" + createdDate + ", endedDate=" + endedDate + ", sale=" + sale + ", payViaATM=" + payViaATM + ", withDraw=" + withDraw + ", difference=" + difference + ", cashCountSheet=" + cashCountSheet + ", accoutinVoucherDetailList=" + '}';
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
        final AcountingVoucher other = (AcountingVoucher) obj;
        return Objects.equals(this.accountingVoucherID, other.accountingVoucherID);
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

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
    private List<AccountingVoucherDetail> accoutinVoucherDetailList;

    public AccountingVoucher() {
    }

    public AccountingVoucher(String accountingVoucherID) {
        this.accountingVoucherID = accountingVoucherID;
    }

    public String getAccountingVoucherID() {
        return accountingVoucherID;
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

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public double getPayViaATM() {
        return payViaATM;
    }

    public void setPayViaATM(double payViaATM) {
        this.payViaATM = payViaATM;
    }

    public double getWithDraw() {
        return withDraw;
    }

    public void setWithDraw(double withDraw) {
        this.withDraw = withDraw;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }

    public CashCountSheet getCashCountSheet() {
        return cashCountSheet;
    }

    public void setCashCountSheet(CashCountSheet cashCountSheet) {
        this.cashCountSheet = cashCountSheet;
    }

    public List<AccountingVoucherDetail> getAccoutinVoucherDetailList() {
        return accoutinVoucherDetailList;
    }

    public void setAccoutinVoucherDetailList(List<AccountingVoucherDetail> accoutinVoucherDetailList) {
        this.accoutinVoucherDetailList = accoutinVoucherDetailList;
    }

    @Override
    public String toString() {
        return "AccountingVoucher{" + "accountingVoucherID=" + accountingVoucherID + ", createdDate=" + createdDate + ", endedDate=" + endedDate + ", sale=" + sale + ", payViaATM=" + payViaATM + ", withDraw=" + withDraw + ", difference=" + difference + ", cashCountSheet=" + cashCountSheet + ", accoutinVoucherDetailList=" + accoutinVoucherDetailList + '}';
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

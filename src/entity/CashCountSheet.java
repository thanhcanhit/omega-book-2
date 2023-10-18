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
public class CashCountSheet {

    private String cashCountSheetID;
    private List<CashCount> cashCountList;
    private Date createdDate;
    private Date endedDate;
    private double total;

    public CashCountSheet() {
    }

    public CashCountSheet(String cashCountSheetID) {
        this.cashCountSheetID = cashCountSheetID;
    }

    public CashCountSheet(String cashCountSheetID, List<CashCount> cashCountList, Date createdDate, Date endedDate, double total) {
        this.cashCountSheetID = cashCountSheetID;
        this.cashCountList = cashCountList;
        this.createdDate = createdDate;
        this.endedDate = endedDate;
        this.total = total;
    }

    public String getCashCountSheetID() {
        return cashCountSheetID;
    }

    public void setCashCountSheetID(String cashCountSheetID) {
        this.cashCountSheetID = cashCountSheetID;
    }

    public List<CashCount> getCashCountList() {
        return cashCountList;
    }

    public void setCashCountList(List<CashCount> cashCountList) {
        this.cashCountList = cashCountList;
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

    public double getTotal() {
        return total;
    }

    private void setTotal() {
        double sum = 0;
        for (CashCount cashCount : cashCountList) {
            sum += cashCount.getTotal();
        }
        this.total = sum;
    }

    @Override
    public String toString() {
        return "CashCountSheet{" + "cashCountSheetID=" + cashCountSheetID + ", cashCountList=" + cashCountList + ", createdDate=" + createdDate + ", endedDate=" + endedDate + ", total=" + total + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.cashCountSheetID);
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
        final CashCountSheet other = (CashCountSheet) obj;
        return Objects.equals(this.cashCountSheetID, other.cashCountSheetID);
    }
    
    

}

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
public class CashCountSheet implements Comparable<CashCountSheet>{

    private String cashCountSheetID;
    private ArrayList<CashCount> cashCountList;
    private Date createdDate;
    private Date endedDate;
    private double total;
    private double difference;

    public double getDifference() {
        return difference;
    }

    public void setDifference() {
        difference = total - 1765000;
    }
    private ArrayList<CashCountSheetDetail> cashCountSheetDetailList;

    public CashCountSheet() {
    }

    public CashCountSheet(String cashCountSheetID) {
        this.cashCountSheetID = cashCountSheetID;
    }

    public CashCountSheet(String cashCountSheetID, ArrayList<CashCount> cashCountList,ArrayList<CashCountSheetDetail> cashCountSheetDetailList, Date createdDate, Date endedDate) {
        this.cashCountSheetID = cashCountSheetID;
        this.cashCountList = cashCountList;
        this.createdDate = createdDate;
        this.endedDate = endedDate;
        setTotal();
        setDifference();
        this.cashCountSheetDetailList = cashCountSheetDetailList;
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

    public void setCashCountList(ArrayList<CashCount> cashCountList) {
        this.cashCountList = cashCountList;
        setTotal();
        setDifference();
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

    public ArrayList<CashCountSheetDetail> getCashCountSheetDetailList() {
        return cashCountSheetDetailList;
    }

    public void setCashCountSheetDetailList(ArrayList<CashCountSheetDetail> cashCountSheetDetailList) {
        this.cashCountSheetDetailList = cashCountSheetDetailList;
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

    @Override
    public int compareTo(CashCountSheet o) {
        return this.createdDate.compareTo(o.createdDate);
    }
    
    

}

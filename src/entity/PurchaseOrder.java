/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
import java.time.LocalDate;
import java.util.*;
/**
 *
 * @author Nhu Tam
 */
public class PurchaseOrder {
    
    /* Hằng báo lỗi*/
    public static final String ID_EMPTY = "Mã đơn nhập không được phép rỗng";
    public static final String ORDERDATE_ERORR = "Ngày tạo đơn nhập không hợp lệ";
    public static final String RECEIVEDATE_ERORR = "Ngày nhập không hợp lệ";
    
    private String purchaseOrderID;
    private Date orderDate;
    private Date receiveDate;
    private String note;
    private PurchaseOrderStatus status;
    private Supplier supplier;
    private Employee employee;
    private ArrayList<PurchaseOrderDetail> purchaseOrderDetailList;

    public PurchaseOrder(String purchaseOrderID, Date orderDate, Date receiveDate, String note, PurchaseOrderStatus status, Supplier supplier, Employee employee, ArrayList<PurchaseOrderDetail> purchaseOrderDetailList) throws Exception {
        setPurchaseOrderID(purchaseOrderID);
        setOrderDate(orderDate);
        setReceiveDate(receiveDate);
        setNote(note);
        setStatus(status);
        setSupplier(supplier);
        setEmployee(employee);
        setPurchaseOrderDetailList(purchaseOrderDetailList);
    }
    

    public PurchaseOrder(String purchaseOrderID) throws Exception {
        setPurchaseOrderID(purchaseOrderID);
    }

    public PurchaseOrder() {
    }

    public String getPurchaseOrderID() {
        return purchaseOrderID;
    }

    public void setPurchaseOrderID(String purchaseOrderID) throws Exception{
        if(!purchaseOrderID.trim().equals(""))
            this.purchaseOrderID = purchaseOrderID;
        else {
            throw new Exception(ID_EMPTY);
        }
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) throws Exception {
        if(orderDate.before(java.sql.Date.valueOf(LocalDate.now())))
            this.orderDate = orderDate;
        else{
            throw new Exception(ORDERDATE_ERORR);
        }
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) throws Exception {
        if(receiveDate.after(orderDate))
            this.receiveDate = receiveDate;
        else {
            throw new Exception(RECEIVEDATE_ERORR);
        }
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ArrayList<PurchaseOrderDetail> getPurchaseOrderDetailList() {
        return purchaseOrderDetailList;
    }

    public void setPurchaseOrderDetailList(ArrayList<PurchaseOrderDetail> purchaseOrderDetailList) {
        this.purchaseOrderDetailList = purchaseOrderDetailList;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.purchaseOrderID);
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
        final PurchaseOrder other = (PurchaseOrder) obj;
        return Objects.equals(this.purchaseOrderID, other.purchaseOrderID);
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" + "purchaseOrderID=" + purchaseOrderID + ", orderDate=" + orderDate + ", receiveDate=" + receiveDate + ", note=" + note + ", status=" + status + ", supplier=" + supplier + ", employee=" + employee + ", purchaseOrderDetailList=" + purchaseOrderDetailList + '}';
    }

    
    
}

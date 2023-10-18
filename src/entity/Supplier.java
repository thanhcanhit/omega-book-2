/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Objects;

/**
 *
 * @author Nhu Tam
 */
public class Supplier {
    
    /* Hằng báo lỗi*/
    public static final String ID_EMPTY = "Mã nhà cung cấp không được phép rỗng";
    public static final String NAME_EMPTY = "Tên nhà cung cấp không được phép rỗng";
    public static final String ADDRESS_EMPTY = "Địa chỉ không được phép rỗng";
    
    private String supplierID;
    private String name; 
    private String address;

    public Supplier(String supplierID, String name, String address) throws Exception {
        setSupplierID(supplierID);
        setName(name);
        setAddress(address);
    }

    public Supplier(String supplierID) throws Exception {
        setSupplierID(supplierID);
    }

    public Supplier() {
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) throws Exception {
        if(!supplierID.trim().equals("")) {
            this.supplierID = supplierID;
        } else {
            throw new Exception(ID_EMPTY);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if(!name.trim().equals("")) {
            this.name = name;
        } else {
            throw new Exception(NAME_EMPTY);
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) throws Exception {
        if(address != null) {
            this.address = address;
        } else {
            throw new Exception(ADDRESS_EMPTY);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.supplierID);
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
        final Supplier other = (Supplier) obj;
        return Objects.equals(this.supplierID, other.supplierID);
    }

    @Override
    public String toString() {
        return supplierID + "," + name + "," + address;
    }
    
}

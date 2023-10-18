/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
import java.util.*;

/**
 *
 * @author Admin
 */
public class Store {
    
     /* Hằng báo lỗi*/
    public static final String ID_EMPTY = "Mã cửa hàng không được phép rỗng";
    public static final String NAME_EMPTY = "Tên cửa hàng không được phép rỗng";
    public static final String ADDRESS_EMPTY = "Địa chỉ không được phép rỗng";
    
    private String storeID;
    private String name;
    private String address;

    public Store(String storeID, String name, String address) throws Exception {
        setStoreID(storeID);
        setName(name);
        setAddress(address);
    }

    public Store(String storeID) throws Exception {
        setStoreID(storeID);
    }

    public Store() {
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) throws Exception {
        if(!storeID.trim().equals("")) {
            this.storeID = storeID;
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
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.storeID);
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
        final Store other = (Store) obj;
        return Objects.equals(this.storeID, other.storeID);
    }

    @Override
    public String toString() {
        return storeID + "," + name + "," + address;
    }
}

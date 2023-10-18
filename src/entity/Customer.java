/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.Pattern;
/**
 *
 * @author Nhu Tam
 */
public class Customer implements Serializable{
    /*Hằng báo lỗi*/
    public static final String ID_EMPTY = "Mã khách hàng không được phép rỗng";
    public static final String NAME_EMPTY = "Họ tên không được phép rỗng";
    public static final String DATEBIRTH_ERROR = "Ngày sinh phải nhỏ hơn ngày hiện tại";
    public static final String PHONENUMBER_ERROR = "Số điện thoại gồm 10 chữ số, bắt đầu từ số 0";
    public static final String GENDER_EMPTY = "Giới tính không được phép rỗng";
    public static final String ADDRESS_EMPTY = "Mã địa chỉ không được phép rỗng";
    public static final String SCORE_ERROR = "Điểm tích luỹ không được nhỏ hơn không";
//    public static final String RANK_EMPTY = "Hạng không được phép rỗng";
    
    private String customerID;
    private String name;
    private int score;
    private boolean gender;
    private Date dateOfBirth;
    private String phoneNumber;
    private String rank;
    private String address;

    public Customer(String customerID, String name, boolean gender, Date dateOfBirth, int score, String phoneNumber, String rank, String address) throws Exception {
        setCustomerID(customerID);
        setName(name);
        setGender(gender);
        setDateOfBirth(dateOfBirth);
        setScore(score);
        setPhoneNumber(phoneNumber);
        setRank();
        setAddress(address);
    }

    public Customer(String customerID) throws Exception {
        setCustomerID(customerID);
    }

    public Customer() {
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) throws Exception {
        if(!customerID.trim().equals("")) {
            this.customerID = customerID;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) throws Exception {
        if(score >= 0) {
            this.score =+ score;
        } else {
            throw new Exception(SCORE_ERROR);
        }
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) throws Exception {
        Date current = new Date();
        if(dateOfBirth.compareTo(current) < 0) {
            this.dateOfBirth = dateOfBirth;
        } else {
            throw new Exception(DATEBIRTH_ERROR);
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws Exception {
        String patternPhone = "^(09|03|08|07|05|02)\\d{8}$";
        if(Pattern.matches(patternPhone, phoneNumber)) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new Exception(PHONENUMBER_ERROR);
        }
    }

    public String getRank() {
        return rank;
    }

    public void setRank() {
        if(score < 1000)
            rank = "Không";
        else if(score < 10000)
            rank = "Bạc";
        else if(score <  10000)
            rank = "Vàng";
        else
            rank = "Không";
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
        hash = 79 * hash + Objects.hashCode(this.customerID);
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
        final Customer other = (Customer) obj;
        return Objects.equals(this.customerID, other.customerID);
    }

    @Override
    public String toString() {
        String genderString = "Nam";
        if(gender)
            genderString = "Nữ";
        String dateFormat = DateFormat.getInstance().format(dateOfBirth);
        return customerID + "," + name + "," + genderString + "," +
                dateFormat + "," + phoneNumber + "," + score + "," + 
                rank + "," + address; 
    }
    
}

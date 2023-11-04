/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Hoàng Khang
 */
public class Employee {

    private String employeeID;
    private String citizenIdentification;
    private String role;
    private boolean status;
    private String name;
    private String phoneNumber;
    private boolean gender;
    private Date dateOfBirth;
    private String address;
    private Store store;

    public Employee() {
    }

    public Employee(String employeeID) {
        setEmployeeID(employeeID);
    }

    public Employee(String employeeID, String citizenIdentification, String role, boolean status, String name, String phoneNumber, boolean gender, Date dateOfBirth, String address, Store store) {
        setEmployeeID(employeeID);
        setCitizenIdentification(citizenIdentification);
        setRole(role);
        setStatus(status);
        setStore(store);
        setPhoneNumber(phoneNumber);
        setName(name);
        setDateOfBirth(dateOfBirth);
        setAddress(address);
        setGender(gender);
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getCitizenIdentification() {
        return citizenIdentification;
    }

    public void setCitizenIdentification(String citizenIdentification) throws IllegalArgumentException {
        citizenIdentification = citizenIdentification.trim();
        if (!citizenIdentification.matches("\\d{12}")) {
            throw new IllegalArgumentException("Mã định danh phải gồm đúng 12 chữ số!");
        }
        this.citizenIdentification = citizenIdentification;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được rỗng!");
        }
        if (!name.matches("^[\\p{L} ]+$")) {
            throw new IllegalArgumentException("Họ tên chỉ được chứa kí tự chữ và khoảng trắng!");
        }
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        phoneNumber = phoneNumber.trim();
        if (!phoneNumber.matches("^(02|03|05|07|08|09)\\d{8}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ!");
        }
        this.phoneNumber = phoneNumber;
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

    public void setDateOfBirth(Date dateOfBirth) throws IllegalArgumentException {
        if (dateOfBirth.after(new Date())) {
            throw new IllegalArgumentException("Ngày sinh không được sau ngày hiện tại!");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "Employee{" + "employeeID=" + employeeID + ", citizenIdentification=" + citizenIdentification + ", role=" + role + ", status=" + status + ", name=" + name + ", phoneNumber=" + phoneNumber + ", gender=" + gender + ", dateOfBirth=" + dateOfBirth + ", address=" + address + ", store=" + store + '}';
    }

    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.employeeID);
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
        final Employee other = (Employee) obj;
        return Objects.equals(this.employeeID, other.employeeID);
    }

}

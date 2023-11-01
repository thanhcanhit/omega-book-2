/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import entity.Customer;
import dao.Customer_DAO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Hoàng Khang
 */
public class CustomerManagement_BUS {

    private Customer_DAO customer_DAO = new Customer_DAO();

    public ArrayList<Customer> getAllCustomer() {
        ArrayList<Customer> customerList = new Customer_DAO().getAll();
        return customerList;
    }

    public Customer getOne(String customerID) {
        return customer_DAO.getOne(customerID);
    }

    public ArrayList<Customer> getCustomersByRank() {
        ArrayList<Customer> customerList = new Customer_DAO().getAll();
        return customerList;
    }

    public String generateID(Date date, boolean gender) {
        //Khởi tạo mã Khách hàng KH
        String prefix = "KH";
        //4 Kí tự kế tiếp là năm sinh khách hàng
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("yyyy");
        String format = simpleDateFormat.format(date);
        prefix += format;
        //Kí tự tiếp theo đại diện cho giới tính khách hàng
        if (gender) {
            prefix += "1";
        } else {
            prefix += "0";
        }
        //Tìm mã có tiền tố là code và xxxx lớn nhất
        String maxID = customer_DAO.getMaxSequence(prefix);
        if (maxID == null) {
            prefix += "0000";
        } else {
            String lastFourChars = maxID.substring(maxID.length() - 4);
            int num = Integer.parseInt(lastFourChars);
            num++;
            prefix += String.format("%04d", num);
        }
        return prefix;
    }

    public void createCustomer(String name, Date dateOfBirth, String numberPhone, String address, Boolean gender) throws Exception {
        Customer customer = new Customer(generateID(dateOfBirth, gender), name, gender, dateOfBirth, 0, numberPhone, address);
        customer_DAO.create(customer);
    }
    
    public void updateCustomer(Customer customer, String customerID) throws Exception {
        customer_DAO.update(customerID, customer);
    }
    
    
    public Customer searchByPhoneNumber(String phoneNumber){
        ArrayList<Customer> list = customer_DAO.getAll();
        for (Customer customer : list) {
            if(customer.getPhoneNumber().equals(phoneNumber))
                return customer;
        }
        return null;
    }
}

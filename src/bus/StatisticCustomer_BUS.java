package bus;

import dao.Customer_DAO;
import entity.Customer;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * StatisticCustomer_BUS
 * Tính toán thống kê khách hàng theo tuổi và giới tính
 */
public class StatisticCustomer_BUS {

    Customer_DAO customer_DAO = new Customer_DAO(); // Gợi ý: Bạn cần phải sử dụng đối tượng này để lấy danh sách khách hàng
    ArrayList<Customer> list = customer_DAO.getAll();
    
    public int getNumberCus(){
        return customer_DAO.getAll().size();
    }
    
    public int sumCustomer(){
        return list.size();
    }

    public int[] filterCustomers() {
        
        
        ArrayList<Customer> femaleUnder18 = new ArrayList<>();
        ArrayList<Customer> female18to40 = new ArrayList<>();
        ArrayList<Customer> femaleOver40 = new ArrayList<>();
        ArrayList<Customer> maleUnder18 = new ArrayList<>();
        ArrayList<Customer> male18to40 = new ArrayList<>();
        ArrayList<Customer> maleOver40 = new ArrayList<>();

        for (Customer customer : list) {
            int age = getAge(customer.getDateOfBirth());
            if (customer.isGender() == false) {
                if (age < 18) {
                    femaleUnder18.add(customer);
                } else if (age >= 18 && age <= 40) {
                    female18to40.add(customer);
                } else {
                    femaleOver40.add(customer);
                }
            } else {
                if (age < 18) {
                    maleUnder18.add(customer);
                } else if (age >= 18 && age <= 40) {
                    male18to40.add(customer);
                } else {
                    maleOver40.add(customer);
                }
            }
        }
        return new int[]{femaleUnder18.size(), female18to40.size(), femaleOver40.size(), maleUnder18.size(), male18to40.size(), maleOver40.size()}; 
    }

    public int getAge(Date dateOfBirth) {
        long diffInMillies = Math.abs(new Date().getTime() - dateOfBirth.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return (int) (diff / 365);
    }
}

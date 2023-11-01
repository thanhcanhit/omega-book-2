/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.AcountingVoucher_DAO;
import dao.CashCountSheetDetail_DAO;
import dao.CashCountSheet_DAO;
import dao.CashCount_DAO;
import entity.AcountingVoucher;
import entity.CashCountSheet;
import entity.Employee;
import java.util.ArrayList;

/**
 *
 * @author Hoàng Khang
 */
public class AcountingVoucher_BUS {
    private CashCount_DAO cashCount_DAO;
    private AcountingVoucher_DAO acountingVoucher_DAO = new AcountingVoucher_DAO();
    private CashCountSheet_DAO cashCountSheet_DAO = new CashCountSheet_DAO();
    private CashCountSheetDetail_DAO cashCountSheetDetail_DAO = new CashCountSheetDetail_DAO();
    private Employee employee1;
    private Employee employee2;
    private CashCountSheet cashCountSheet;
    private AcountingVoucher acountingVoucher;
    
    
    public Employee isEmployeePresent(String id){
        Employee emp = new Employee(id.trim());
        ArrayList<Employee> list = new ArrayList<>();
         for (Employee e : list) {
            if (e.getEmployeeID() == emp.getEmployeeID()) {
                return e; // Nhân viên được kiểm tra nằm trong danh sách
            }
        }
        return null; // Nhân viên không nằm trong danh sách
    }
    
}

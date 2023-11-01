/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.CashCountSheetDetail_DAO;
import dao.CashCountSheet_DAO;
import dao.CashCount_DAO;
import entity.Employee;
import entity.CashCount;
import entity.CashCountSheet;
import entity.CashCountSheetDetail;
import dao.Employee_DAO;
import java.util.ArrayList;

/**
 *
 * @author Hoàng Khang
 */
public class StatementCashCount_BUS {
    private Employee_DAO employee_DAO;
    private CashCount_DAO cashCount_DAO;
    private CashCountSheet_DAO cashCountSheet_DAO;
    private CashCountSheetDetail_DAO cashCountSheetDetail_DAO;
    
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

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Hoàng Khang
 */
public class StatementCashCount_BUS {
    
    private Employee_DAO employee_DAO = new Employee_DAO();
    private CashCount_DAO cashCount_DAO = new CashCount_DAO();
    private CashCountSheet_DAO cashCountSheet_DAO = new CashCountSheet_DAO();
    private CashCountSheetDetail_DAO cashCountSheetDetail_DAO;
    
    public Employee getEmployeeByID(String id) {
        return employee_DAO.getOne(id);
    }
    
    public Employee isEmployeePresent(String id) {
        System.out.println(id);
        Employee emp = new Employee(id.trim());
        ArrayList<Employee> list = employee_DAO.getAll();
        for (Employee e : list) {
            if (e.equals(emp)) {
                return e; // Nhân viên được kiểm tra nằm trong danh sách
            }
        }
        return null; // Nhân viên không nằm trong danh sách
    }
    
    public void createCashCountSheet(ArrayList<CashCount> cashCountList, ArrayList<Employee> employees, Date start) {
        Date end = new Date();
        CashCountSheet cashCountSheet = new CashCountSheet(generateID(start));
        //Set danh sách CashCount
        cashCountSheet.setCashCountList(cashCountList);
        //Khởi tạo CashCountSheetDetal
        ArrayList<CashCountSheetDetail> cashCountSheetDetails = new ArrayList<>();
        cashCountSheetDetails.add(new CashCountSheetDetail(true, employees.get(0), cashCountSheet));
        cashCountSheetDetails.add(new CashCountSheetDetail(false, employees.get(1), cashCountSheet));
        
        cashCountSheet.setCashCountSheetDetailList(cashCountSheetDetails);
        cashCountSheet.setCreatedDate(start);
        cashCountSheet.setEndedDate(end);
        System.out.println(cashCountSheet.getCashCountSheetDetailList().get(0));
        
        cashCountSheet_DAO.create(cashCountSheet);
        
//        for (CashCount cashCount : cashCountSheet.getCashCountList()) {
//            cashCount_DAO.create(cashCount, cashCountSheet.getCashCountSheetID());
//        }
//        for (CashCountSheetDetail cashCountSheetDetail : cashCountSheet.getCashCountSheetDetailList()) {
//            cashCountSheetDetail_DAO.create(cashCountSheetDetail);
//        }
        
    }
    
    public String generateID(Date date) {
        //Khởi tạo mã Khách hàng KH
        String prefix = "KTI";
        //8 Kí tự tiếp theo là ngày và giờ bắt đầu kiểm tiền
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String format = dateFormat.format(date);
        System.out.println(format);
        prefix += format;
        System.out.println(prefix);
        String maxID = cashCountSheet_DAO.getMaxSequence(prefix);
        if (maxID == null) {
            prefix += "0000";
        } else {
            String lastFourChars = maxID.substring(maxID.length() - 4);
            int num = Integer.parseInt(lastFourChars);
            num++;
            prefix += String.format("%04d", num);;
        }
        System.out.println(prefix);
        return prefix;
    }
}

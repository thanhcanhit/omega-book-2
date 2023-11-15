/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.AcountingVoucher_DAO;
import dao.CashCountSheet_DAO;
import dao.Employee_DAO;
import dao.Order_DAO;
import entity.AcountingVoucher;
import entity.CashCount;
import entity.CashCountSheet;
import entity.Employee;
import entity.Order;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Hoàng Khang
 */
public class StatementAcounting_BUS {

    private AcountingVoucher_DAO acountingVoucher_DAO = new AcountingVoucher_DAO();
    private CashCountSheet_DAO cashCountSheet_DAO = new CashCountSheet_DAO();
    private Employee_DAO employee_DAO = new Employee_DAO();
    private Order_DAO order_DAO = new Order_DAO();
    private StatementCashCount_BUS statementCashCount_BUS = new StatementCashCount_BUS();

    public AcountingVoucher getLastAcounting() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String format = dateFormat.format(date);
        String code = "KTO" + format;
        AcountingVoucher acountingVoucherLast = acountingVoucher_DAO.getOne(acountingVoucher_DAO.getMaxSequence(code));
        if (acountingVoucherLast == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            date = calendar.getTime();
            acountingVoucherLast = new AcountingVoucher(date);
        }
        return acountingVoucherLast;
    }

    /**
     * Phát sinh mã phiếu kết toán
     *
     * @param date
     * @return
     */
    public String generateID(Date date) {
        //Khởi tạo mã phiếu kết toán
        String prefix = "KTO";
        //8 Kí tự tiếp theo là ngày và giờ bắt đầu kết toán
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String format = dateFormat.format(date);
        prefix += format;
        String maxID = acountingVoucher_DAO.getMaxSequence(prefix);
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

    public void createAcountingVoucher(CashCountSheet cashCountSheet, Date end) {
        Date start = getLastAcounting().getEndedDate();
        ArrayList<Order> list = getAllOrderInAcounting(start, end);

        AcountingVoucher acountingVoucher = new AcountingVoucher(generateID(end), start, end, cashCountSheet, list);
        cashCountSheet_DAO.create(cashCountSheet);
        acountingVoucher_DAO.create(acountingVoucher);
        
        for (Order order : list) {
            order_DAO.updateOrderAcountingVoucher(order.getOrderID(), acountingVoucher.getAcountingVoucherID());
        }
    }

    public Employee getEmployeeByID(String id) {
        return employee_DAO.getOne(id);
    }

    public ArrayList<Order> getAllOrderInAcounting(Date start, Date end) {
        ArrayList<Order> allOrder = order_DAO.getAll();
        ArrayList<Order> list = new ArrayList<>();
        for (Order order : allOrder) {
            Date orderDate = order.getOrderAt();
            if (orderDate.after(start) && orderDate.before(end)) {
                list.add(order);
            }
        }
        return list;
    }

    public double getSale(ArrayList<Order> list) {
        double sum = 0;
        for (Order order : list) {
            sum += order.getTotalDue();
        }
        return sum;
    }

    public double getPayViaATM(ArrayList<Order> list) {
        double sum = 0;
        for (Order order : list) {
            if (!order.isPayment()) {
                sum += order.getTotalDue();
            }
        }
        return sum;
    }

    public double getTotal(ArrayList<CashCount> list) {
        double sum = 0;
        for (CashCount cashCount : list) {
            sum += cashCount.getTotal();
        }
        return sum;
    }
}

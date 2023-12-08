/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Shift_DAO;
import entity.Shift;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Hoàng Khang
 */
public class ShiftsManagemant_BUS {

    private Shift_DAO shift_DAO = new Shift_DAO();

    public Shift getOne(String id) {
        return shift_DAO.getOne(id);
    }

    public ArrayList<Shift> getAll() {
        return shift_DAO.getAll();
    }

    public boolean createShifts(Shift shift) {
        return shift_DAO.create(shift);
    }

    public String renderID() {
        return shift_DAO.generateID();
    }

    public ArrayList<Shift> getShiftsByDate(Date date) {
        return shift_DAO.getShiftsByDate(date);
    }

    public ArrayList<Shift> filter(String emloyeeID, String role, Date date) {
        ArrayList<Shift> list = shift_DAO.getShiftsByDate(date);
        ArrayList<Shift> listRemove = new ArrayList<>();

        if (!emloyeeID.equals("")) {
            for (Shift shift : list) {
                if (!shift.getAccount().getEmployee().getEmployeeID().equals(emloyeeID)) {
                    listRemove.add(shift);
                }
                list.removeAll(listRemove);
                listRemove.clear();
            }
        }


        if (!role.equals("Tất Cả")) {
            for (Shift shift : list) {
                if (!shift.getAccount().getEmployee().getRole().equals(role)) {
                    listRemove.add(shift);
                }
            }
            list.removeAll(listRemove);
        }

        return list;
    }
}

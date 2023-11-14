/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Shift_DAO;
import entity.Shift;
import java.util.ArrayList;

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
    
    public String renderID(){
        return shift_DAO.generateID();
    }
}

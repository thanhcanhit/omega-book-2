/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.CashCountSheet_DAO;
import entity.CashCountSheet;
import java.util.ArrayList;

/**
 *
 * @author Hoàng Khang
 */
public class ViewCashCountSheetList_BUS {

    private CashCountSheet_DAO cashDAO = new CashCountSheet_DAO();

    public ArrayList<CashCountSheet> getAll() {
        return cashDAO.getAll();
    }

}

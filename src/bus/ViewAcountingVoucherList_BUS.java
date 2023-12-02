/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.AcountingVoucher_DAO;
import dao.CashCountSheet_DAO;
import entity.AcountingVoucher;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Hoàng Khang
 */
public class ViewAcountingVoucherList_BUS {

    private AcountingVoucher_DAO acountingVoucher_DAO = new AcountingVoucher_DAO();
    private CashCountSheet_DAO cashCountSheet_DAO = new CashCountSheet_DAO();

    public AcountingVoucher getOne(String acountingVoucherID) {
        return acountingVoucher_DAO.getOne(acountingVoucherID);
    }

    public ArrayList<AcountingVoucher> getAll() {
        ArrayList<AcountingVoucher> list = new ArrayList<>();
        for (AcountingVoucher acountingVoucher : acountingVoucher_DAO.getAll()) {
            acountingVoucher.setCashCountSheet(cashCountSheet_DAO.getOne(acountingVoucher.getCashCountSheet().getCashCountSheetID()));
            list.add(acountingVoucher);
        }
        return acountingVoucher_DAO.getAll();
    }

    public ArrayList<AcountingVoucher> getByDate(Date start, Date end) {
        ArrayList<AcountingVoucher> list = getAll();
        return list;
    }
}

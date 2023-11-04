/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import entity.PurchaseOrder;
import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;
import database.ConnectDB;
import entity.Employee;
import entity.PurchaseOrderDetail;
import enums.PurchaseOrderStatus;
import entity.Supplier;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author KienTran
 */
public class PurchaseOrder_DAO implements DAOBase<PurchaseOrder> {

    @Override
    public PurchaseOrder getOne(String id) {
        PurchaseOrder result = null;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("SELECT * FROM PurchaseOrder where PurchaseOrderID = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {

                Date orderDate = rs.getDate("orderDate");
                Date receiveDate = rs.getDate("receiveDate");
                int status = rs.getInt("status");
                String note = rs.getString("note");
                String supplierID = rs.getString("supplierID");
                String employeeID = rs.getString("employeeID");

                ArrayList<PurchaseOrderDetail> purchaseOrderDetail = new PurchaseOrderDetail_DAO().getAll(id);

                result = new PurchaseOrder(id, orderDate, receiveDate, note, PurchaseOrderStatus.values()[status], new Supplier(supplierID), new Employee(employeeID), purchaseOrderDetail);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<PurchaseOrder> getAll() {
        ArrayList<PurchaseOrder> result = new ArrayList<>();

        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PurchaseOrder;");

            while (rs.next()) {
                String purchaseOrderID = rs.getString("purchaseOrderID");

                Date orderDate = rs.getDate("orderDate");
                Date receiveDate = rs.getDate("receiveDate");
                int status = rs.getInt("status");
                String note = rs.getString("note");
                String supplierID = rs.getString("supplierID");
                String employeeID = rs.getString("employeeID");

                ArrayList<PurchaseOrderDetail> purchaseOrderDetail = new PurchaseOrderDetail_DAO().getAll(purchaseOrderID);

                result.add(new PurchaseOrder(purchaseOrderID, orderDate, receiveDate, note, PurchaseOrderStatus.values()[status], new Supplier(supplierID), new Employee(employeeID), purchaseOrderDetail));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public String generateID() {
        String result = "DN";
        LocalDate time = LocalDate.now();
        DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("MMyyyy");

        result += dateFormater.format(time);

        String query = """
                       select top 1 * from PurchaseOrder
                       where purchaseOrderID like ?
                       order by purchaseOrderID desc
                       """;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            st.setString(1, result + "%");
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String lastID = rs.getString("purchaseOrderID");
                String sNumber = lastID.substring(lastID.length() - 2);
                int num = Integer.parseInt(sNumber) + 1;
                result += String.format("%02d", num);
            } else {
                result += String.format("%02d", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Boolean create(PurchaseOrder object) {
        int n = 0;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("insert into PurchaseOrder (purchaseOrderID, orderDate, receiveDate, status, note, supplierID, employeeID) values (?, ?, ?, ?, ?, ?, ? )");
            st.setString(1, object.getPurchaseOrderID());
            st.setDate(2, new java.sql.Date(object.getOrderDate().getTime()));
            st.setDate(3, new java.sql.Date(object.getReceiveDate().getTime()));
            st.setInt(4, object.getStatus().getValue());
            st.setString(5, object.getNote());
            st.setString(7, object.getEmployee().getEmployeeID());
            st.setString(6, object.getSupplier().getSupplierID());

            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return n > 0;
    }

    @Override
    public Boolean update(String id, PurchaseOrder newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

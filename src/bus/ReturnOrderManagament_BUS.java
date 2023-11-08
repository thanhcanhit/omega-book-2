/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.OrderDetail_DAO;
import dao.Order_DAO;
import dao.Product_DAO;
import dao.ReturnOrderDetail_DAO;
import dao.ReturnOrder_DAO;
import entity.Order;
import entity.OrderDetail;
import entity.ReturnOrder;
import entity.ReturnOrderDetail;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Như Tâm
 */
public class ReturnOrderManagament_BUS {
    private ReturnOrder_DAO dao = new ReturnOrder_DAO();

    public Order getOrder(String orderID) {
        return new Order_DAO().getOne(orderID);
    }

    public ReturnOrder getReturnOrder(String returnOrderID) {
        return dao.getOne(returnOrderID);
    }
    public ArrayList<ReturnOrder> getAllReturnOrder() {
        return dao.getAll();
    }

    public ArrayList<ReturnOrderDetail> getAllReturnOrderDetail(String returnOrderID) {
        return new ReturnOrderDetail_DAO().getAllForOrderReturnID(returnOrderID);
    }

    public boolean updateReturnOder(ReturnOrder newReturnOrder) {
        return dao.update(newReturnOrder.getReturnOrderID(), newReturnOrder);
    }

    public ArrayList<ReturnOrder> searchById(String returnOrderID) {
        return dao.findById(returnOrderID);
    }

    public ArrayList<ReturnOrder> filter(int type, int status) {
        return dao.filter(type, status);
    }

    public ArrayList<OrderDetail> getAllOrderDetail(String orderID) {
        return new OrderDetail_DAO().getAll(orderID);
    }
    public ArrayList<Order> getAllOrder() {
        return new Order_DAO().getAll();
    }

    public String getNameProduct(String productID) {
        return new Product_DAO().getOne(productID).getName();
    }
    
    public String generateID(Date returnDate) {
        //Khởi tạo mã đơn đổi trả HDT
        String prefix = "HDT";
        //8 kí tự tiếp theo là ngày tháng năm lập đơn đổi trả
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("ddmmyyyy");
        String formatDate = simpleDateFormat.format(returnDate);        
        prefix += formatDate;
        //Tìm mã có tiền tố là code và xxxx lớn nhất
        String maxID = ReturnOrder_DAO.getMaxSequence(prefix);
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

    public boolean createNew(ReturnOrder newReturnOrder) {
        return dao.create(newReturnOrder);
    }

    public ArrayList<Order> searchByOrderId(String orderID) {
        return new Order_DAO().findById(orderID);
    }
}
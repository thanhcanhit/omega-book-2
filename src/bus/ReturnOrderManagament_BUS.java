package bus;

import dao.OrderDetail_DAO;
import dao.Order_DAO;
import dao.Product_DAO;
import dao.ReturnOrderDetail_DAO;
import dao.ReturnOrder_DAO;
import entity.Order;
import entity.OrderDetail;
import entity.Product;
import entity.Promotion;
import entity.ReturnOrder;
import entity.ReturnOrderDetail;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Như Tâm
 */
public class ReturnOrderManagament_BUS {
    private ReturnOrder_DAO dao = new ReturnOrder_DAO();
    private ReturnOrderDetail_DAO detail_dao = new ReturnOrderDetail_DAO();
    private Order_DAO order_dao = new Order_DAO();

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
        return detail_dao.getAllForOrderReturnID(returnOrderID);
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
        simpleDateFormat.applyPattern("ddMMyyyy");
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

    public Order searchByOrderId(String orderID) {
        return new Order_DAO().getOne(orderID);
    }

    public Product getProduct(String productID) {
        return new Product_DAO().getOne(productID);
    }

    public void createReturnOrderDetail(ReturnOrder newReturnOrder, ArrayList<ReturnOrderDetail> cart) {
        for (ReturnOrderDetail returnOrderDetail : cart) {
            try {
                returnOrderDetail.setReturnOrder(newReturnOrder);
                detail_dao.create(returnOrderDetail);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void updateReturnOrderDetail(ReturnOrder newReturnOrder, ArrayList<ReturnOrderDetail> listDetail) {
        if(newReturnOrder.getStatus().getValue() == 1) {
            if(!newReturnOrder.isType()) {
                for (ReturnOrderDetail returnOrderDetail : listDetail) {
                    detail_dao.updateProduct(returnOrderDetail.getProduct().getProductID(), returnOrderDetail.getQuantity());
                }
            }
            else {
                newReturnOrder.setRefund();
            }
            
        }
        
    }

    public boolean isExist(Order order) {
        return dao.getOneForOrderID(order.getOrderID()) != null;
    }

    public Promotion getDiscount(String orderID) {
        return order_dao.getDiscount(orderID);
    }
}
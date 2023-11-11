/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Customer_DAO;
import dao.OrderDetail_DAO;
import dao.Order_DAO;
import dao.Product_DAO;
import entity.Customer;
import entity.Order;
import entity.OrderDetail;
import entity.Product;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thanhcanhit
 */
public class Sales_BUS {

    private final Order_DAO orderDAO = new Order_DAO();
    private final OrderDetail_DAO orderDetailDAO = new OrderDetail_DAO();
    private final Product_DAO productDAO = new Product_DAO();
    private final Customer_DAO customerDAO = new Customer_DAO();

    public Product getProduct(String id) {
        return productDAO.getOne(id);
    }

    public Customer getCustomerByPhone(String phone) {
        return customerDAO.getByPhone(phone);
    }

    public Order CreateNewOrder() throws Exception {
        Order order = new Order(orderDAO.generateID());
        order.setStatus(false);
//        Chỉ hiển thị ngày lập, khi lưu sẽ lấy thời gian tại lúc bấm thanh toán
        LocalDate now = LocalDate.now();
        order.setOrderAt(Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return order;
    }

    public boolean saveToDatabase(Order order) {
        if (!orderDAO.create(order)) {
            return false;
        }

        for (OrderDetail detail : order.getOrderDetail()) {
            if (!orderDetailDAO.create(detail)) {
                return false;
            } else {
//                Giảm số lượng tồn kho nếu trạng thái đã thành công
                if (order.isStatus()) {
                    decreaseProductInventory(detail.getProduct(), detail.getQuantity());
                }
            }
        }

        return true;
    }

    public boolean updateInDatabase(Order order) {
        if (!orderDAO.update(order.getOrderID(), order)) {
            return false;
        }

//        Xóa hết detail cũ vì nếu cập nhật từng thành phần sẽ rất mất thời gian (cập nhật, thêm vào detail mới, xóa detail cũ,...)
        if (!orderDetailDAO.delete(order.getOrderID())) {
            return false;
        }

        for (OrderDetail detail : order.getOrderDetail()) {
            if (!orderDetailDAO.create(detail)) {
                return false;
            } else {
//                Giảm số lượng tồn kho nếu trạng thái đã thành công
                if (order.isStatus()) {
                    decreaseProductInventory(detail.getProduct(), detail.getQuantity());
                }
            }
        }
        return true;
    }

    public boolean decreaseProductInventory(Product product, int quantity) {
        int newInventory = product.getInventory() - quantity;
        return productDAO.updateInventory(product.getProductID(), newInventory);
    }

    public boolean increaseProductInventory(Product product, int quantity) {
        int newInventory = product.getInventory() + quantity;
        return productDAO.updateInventory(product.getProductID(), newInventory);
    }

//    Handle saved Order
    public ArrayList<Order> getSavedOrders() {
        ArrayList<Order> result = orderDAO.getNotCompleteOrder();

//        Lấy thông tin khách hàng của hóa đơn
        for (Order item : result) {
            try {
                Customer fullInfoCustomer = customerDAO.getOne(item.getCustomer().getCustomerID());
                item.setCustomer(fullInfoCustomer);
            } catch (Exception ex) {
                Logger.getLogger(Sales_BUS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;
    }

    public Order getOrder(String id) {
        Order result = orderDAO.getOne(id);
        try {
//            Lấy thông tin khách hàng
            Customer fullInfoCustomer = customerDAO.getOne(result.getCustomer().getCustomerID());
            result.setCustomer(fullInfoCustomer);
        } catch (Exception ex) {
            Logger.getLogger(Sales_BUS.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            System.out.println("Here");
        }
        return result;
    }

    public boolean deleteOrder(String id) {
        return orderDAO.delete(id);
    }
}

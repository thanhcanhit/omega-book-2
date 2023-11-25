/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Customer_DAO;
import dao.Employee_DAO;
import dao.OrderDetail_DAO;
import dao.Order_DAO;
import dao.Product_DAO;
import dao.Promotion_DAO;
import entity.Customer;
import entity.Employee;
import entity.Order;
import entity.OrderDetail;
import entity.Product;
import entity.Promotion;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author KienTran
 */
public class OrderManagement_BUS {

    private final Order_DAO orderDAO = new Order_DAO();
    private final OrderDetail_DAO orderDetailDAO = new OrderDetail_DAO();
    private final Customer_DAO customerDAO = new Customer_DAO();
    private final Product_DAO productDAO = new Product_DAO();
    private final Employee_DAO employeeDAO = new Employee_DAO();
    private final Promotion_DAO promotionDAO = new Promotion_DAO();

    public Promotion getPromotion(String promotionID) {
        return promotionDAO.getOne(promotionID);
    }

    public ArrayList<Order> getDataOfPage(int page) {
        ArrayList<Order> list = orderDAO.getPage(page);
        
        return list;
    }
    public ArrayList<Order> getAll(){
        return orderDAO.getAll();
    }
    public Employee getEmployee(String emplpyeeID) {
        return employeeDAO.getOne(emplpyeeID);
    }

    public Customer getCustomer(String customerID) {
        return customerDAO.getOne(customerID);

    }

    public Product getProduct(String productID) {
        return productDAO.getOne(productID);
    }

    public Order getOrder(String ID) throws Exception {
        Order order = orderDAO.getOne(ID);
        Customer customer = getCustomer(order.getCustomer().getCustomerID());
        order.setCustomer(customer);
        Employee employee = getEmployee(order.getEmployee().getEmployeeID());
        order.setEmployee(employee);
        if(order.getPromotion()!=null){
             Promotion promotion = getPromotion(order.getPromotion().getPromotionID());
             order.setPromotion(promotion);
        }
       
        return order;
    }

    public ArrayList<OrderDetail> getOrderDetailList(String orderID) {
        return orderDetailDAO.getAll(orderID);
    }

    public int getLastPage() {

        int result = (int) Math.ceil(Double.valueOf(orderDAO.getLength()) / 50);
        return result;
    }

    public ArrayList<Order> orderListWithFilter(String orderID, String customerID, String phoneNumber, String priceFrom, String priceTo, Date orderFrom, Date orderTo) {
        ArrayList<Order> list = orderDAO.getAll();
        for(Order order:list){
            if(order.isStatus()==false)
            {
                list.remove(order);
            }
        }
        ArrayList<Order> xoa = new ArrayList<>();
       
        if (phoneNumber.trim().length() > 0) {
            for (Order order : list) {
                Order od = orderDAO.getOne(order.getOrderID());
                Customer ct = customerDAO.getOne(od.getCustomer().getCustomerID());
                if (!ct.getPhoneNumber().equals(phoneNumber)) {
                    xoa.add(order);
                }
            }
            list.removeAll(xoa);
        }
        xoa.clear();
        if (orderID.trim().length() > 0) {
            for (Order order : list) {
                if (!order.getOrderID().equals(orderID)) {
                    xoa.add(order);
                }
            }
            list.removeAll(xoa);
        }
        xoa.clear();
        if (customerID.trim().length() > 0) {

            for (Order order : list) {
                if (!order.getCustomer().getCustomerID().equals(customerID)) {
                    xoa.add(order);
                }
            }
            list.removeAll(xoa);
        }
        xoa.clear();
        if (priceFrom.trim().length() > 0) {
            for (Order order : list) {
                if (order.getSubTotal() < Double.parseDouble(priceFrom)) {
                    xoa.add(order);
                }
            }
            list.removeAll(xoa);
        }
        xoa.clear();
        if (priceTo.trim().length() > 0) {
            for (Order order : list) {
                if (order.getSubTotal() > Double.parseDouble(priceTo)) {
                    xoa.add(order);
                }
            }
            list.removeAll(xoa);
        }
        xoa.clear();

        for (Order order : list) {
            if (order.getOrderAt().before(orderFrom)) {
                xoa.add(order);
            }
        }
        list.removeAll(xoa);

        xoa.clear();

        for (Order order : list) {
            if (order.getOrderAt().after(orderTo) ) {
                xoa.add(order);
            }
        }
        xoa.clear();
        list.removeAll(xoa);

        return list;
    }
}

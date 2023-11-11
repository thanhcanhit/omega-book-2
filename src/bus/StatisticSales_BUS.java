/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Order_DAO;
import dao.Product_DAO;
import dao.PurchaseOrder_DAO;
import dao.ReturnOrder_DAO;
import entity.Order;
import entity.PurchaseOrder;
import entity.ReturnOrder;
import java.util.ArrayList;

/**
 *
 * @author KienTran
 */
public class StatisticSales_BUS {
    private final Order_DAO orderDAO = new Order_DAO();
    private final PurchaseOrder_DAO purchaseOrderDAO = new PurchaseOrder_DAO();
    private final ReturnOrder_DAO returnOrderDAO = new ReturnOrder_DAO();
    private final Product_DAO productDAO = new Product_DAO();
    
    public int getTotalNumberOrder(int month, int year){
        ArrayList<Order> listOrder =  orderDAO.getAll();
        int result=0;
        for(Order order: listOrder){
            if(order.getOrderAt().getMonth()==month){
                result+=1;
            }
        }
        return result;
    }
    public int getTotalNumberPurchaseOrder(int month, int year){
        ArrayList<PurchaseOrder> listPurchaseOrder =  purchaseOrderDAO.getAll();
        int result=0;
        for(PurchaseOrder order: listPurchaseOrder){
            if(order.getReceiveDate().getMonth()==month){
                result+=1;
            }
        }
        return result;
    }
    public int getTotalNumberReturnOrder(int month, int year){
        ArrayList<ReturnOrder> listReturnOrder = returnOrderDAO.getAll();
        int result=0;
        for(ReturnOrder returnOrder: listReturnOrder){
            if(returnOrder.getOrderDate().getMonth()==month){
                result+=1;
            }
        }
        return result;
    }
    public double getTotalInMonth(int month, int year){
        ArrayList<Order> listOrder =  orderDAO.getAll();
        double result=0;
        for(Order order: listOrder){
            if(order.getOrderAt().getMonth()==month){
                result+=order.getTotalDue();
            }
        }
        return result;
    }
    public double getTargetInMonth(int month, int year){
        ArrayList<Order> listOrder =  orderDAO.getAll();
        double result=0;
        for(Order order: listOrder){
            if(order.getOrderAt().getMonth()==month){
                result+=order.getTotalDue();
            }
        }
        return (result * 100 / 50000000 );
    }

    public double[] getTotalPerDay(int month, int year){
        return orderDAO.getToTalInMonth(month, year);
        
    }
    public int getQuantityProductType(int type, int month, int year){
        return productDAO.getQuantityProductType(type, month, year);
    }
    
}

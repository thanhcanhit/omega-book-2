/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Order_DAO;
import dao.Product_DAO;
import dao.PurchaseOrder_DAO;
import dao.ReturnOrder_DAO;

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
        return orderDAO.getNumberOfOrderInMonth(month, year);
    }
    public int getTotalNumberPurchaseOrder(int month, int year){
       
        return purchaseOrderDAO.getNumberOfPurchaseOrderInMonth(month, year);
    }
    public int getTotalNumberReturnOrder(int month, int year){
        return returnOrderDAO.getNumberOfReturnOrderInMonth(month, year);
    }
    public double getTotalInMonth(int month, int year){
       return orderDAO.getTotalInMonth(month, year);
    }
    public double getTargetInMonth(int month, int year){
        double result = orderDAO.getTotalInMonth(month, year);
        return (result * 100 / 50000000 );
    }

    public double[] getTotalPerDay(int month, int year){
        return orderDAO.getToTalInMonth(month, year);
        
    }
    public int getQuantityProductType(int type, int month, int year){
        return productDAO.getQuantityProductType(type, month, year);
    }
    
}

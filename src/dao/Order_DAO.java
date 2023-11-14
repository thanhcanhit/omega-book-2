/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.Customer;
import entity.Employee;
import entity.Order;
import entity.OrderDetail;
import entity.Promotion;
import interfaces.DAOBase;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 *
 * @author KienTran
 */
public class Order_DAO implements DAOBase<Order> {

    @Override
    public Order getOne(String id) {
        Order order = null;
        try {
            String sql = "SELECT * FROM [Order] WHERE orderID = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                boolean status = resultSet.getBoolean("status");
                Date orderAt = resultSet.getDate("orderAt");
                boolean payment = resultSet.getBoolean("payment");
                String employeeID = resultSet.getString("employeeID");
                String customerID = resultSet.getString("customerID");
                String promotionID = resultSet.getString("promotionID");
                Double totalDue = resultSet.getDouble("totalDue");
                Double subTotal = resultSet.getDouble("subTotal");

                Double moneyGiven = resultSet.getDouble("moneyGiven");
               if(promotionID!=null)
                    order = new Order(id, orderAt, payment, status, new Promotion(promotionID), new Employee(employeeID), new Customer(customerID), new OrderDetail_DAO().getAll(id), subTotal, totalDue, moneyGiven);
               else
                   order = new Order(id, orderAt, payment, status,  new Employee(employeeID), new Customer(customerID), new OrderDetail_DAO().getAll(id), subTotal, totalDue, moneyGiven);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public ArrayList<Order> getAll() {
        ArrayList<Order> result = new ArrayList<>();
        try {
            Statement statement = ConnectDB.conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM [Order]");

            while (resultSet.next()) {
                String orderID = resultSet.getString("orderID");
                boolean status = resultSet.getBoolean("status");
                Timestamp orderAt = resultSet.getTimestamp("orderAt");
                boolean payment = resultSet.getBoolean("payment");
                String employeeID = resultSet.getString("employeeID");
                String customerID = resultSet.getString("customerID");
                String promotionID = resultSet.getString("promotionID");
                Double totalDue = resultSet.getDouble("totalDue");
                Double subTotal = resultSet.getDouble("subTotal");

                Double moneyGiven = resultSet.getDouble("moneyGiven");
                Order order = null;
                if(promotionID!=null)
                     order = new Order(orderID, orderAt, payment, status, new Promotion_DAO().getOne(promotionID), new Employee_DAO().getOne(employeeID), new Customer_DAO().getOne(customerID), new OrderDetail_DAO().getAll(orderID), subTotal, totalDue, moneyGiven);
                else
                    order = new Order(orderID, orderAt, payment, status, new Promotion_DAO().getOne(promotionID), new Employee_DAO().getOne(employeeID), new Customer_DAO().getOne(customerID), new OrderDetail_DAO().getAll(orderID), subTotal, totalDue, moneyGiven);
                
                result.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String generateID() {
        String result = "HD";
        LocalDate time = LocalDate.now();
        DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("ddMMyyyy");

        result += dateFormater.format(time);
        String query = """
                       select top 1 * from [Order]
                       where orderID like ?
                       order by orderID desc
                       """;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            st.setString(1, result + "%");
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                String lastID = rs.getString("orderID");
                String sNumber = lastID.substring(lastID.length() - 2);
                int num = Integer.parseInt(sNumber) + 1;
                result += String.format("%04d", num);
            } else {
                result += String.format("%04d", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Boolean create(Order object) {
        try {
            String sql = "INSERT INTO [Order] (orderID, payment, status, orderAt, employeeID, customerID, promotionID, totalDue, subTotal, moneyGiven) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

            preparedStatement.setString(1, object.getOrderID());
            preparedStatement.setBoolean(2, object.isPayment());
            preparedStatement.setBoolean(3, object.isStatus());
            preparedStatement.setTimestamp(4, new Timestamp(object.getOrderAt().getTime()));
            preparedStatement.setString(5, object.getEmployee().getEmployeeID());
            preparedStatement.setString(6, object.getCustomer().getCustomerID());
            preparedStatement.setString(7, object.getPromotion() == null ? null : object.getPromotion().getPromotionID());
            preparedStatement.setDouble(8, object.getTotalDue());
            preparedStatement.setDouble(9, object.getSubTotal());
            preparedStatement.setDouble(10, object.getMoneyGiven());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean update(String id, Order newObject) {
        try {
            String sql = "UPDATE [Order] SET payment=?, status=?, orderAt=?, employeeID=?, customerID=?, promotionID=?, totalDue=?, subTotal=?, moneyGiven=?  "
                    + "WHERE orderID=?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setBoolean(1, newObject.isPayment());
            preparedStatement.setBoolean(2, newObject.isStatus());
            preparedStatement.setDate(3, new java.sql.Date(newObject.getOrderAt().getTime()));
            preparedStatement.setString(4, newObject.getEmployee().getEmployeeID());
            preparedStatement.setString(5, newObject.getCustomer().getCustomerID());
            preparedStatement.setString(6, newObject.getPromotion() != null ? newObject.getPromotion().getPromotionID() : null);
            preparedStatement.setDouble(7, newObject.getTotalDue());
            preparedStatement.setDouble(8, newObject.getSubTotal());
            preparedStatement.setDouble(9, newObject.getMoneyGiven());
            preparedStatement.setString(10, id);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean delete(String id) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("delete from [Order] where orderID = ?");
            st.setString(1, id);
            new OrderDetail_DAO().delete(id);
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public int getLength() {
        int length = 0;

        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("select length = count(*) from [Order]");

            if (rs.next()) {
                length = rs.getInt("length");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return length;
    }

    public ArrayList<Order> getPage(int page) {
        ArrayList<Order> result = new ArrayList<>();
        String query = """
                       select * from [Order]
                       order by orderID
                       offset ? rows
                       FETCH NEXT 50 ROWS ONLY
                       """;
        int offsetQuantity = (page - 1) * 50;
        try {

            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            st.setInt(1, offsetQuantity);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String orderID = rs.getString("orderID");
                boolean status = rs.getBoolean("status");
                Date orderAt = rs.getDate("orderAt");
                boolean payment = rs.getBoolean("payment");
                String employeeID = rs.getString("employeeID");
                String customerID = rs.getString("customerID");
                String promotionID = rs.getString("promotionID");
                Double totalDue = rs.getDouble("totalDue");
                Double subTotal = rs.getDouble("subTotal");
                
                if(status==false){
                    continue;
                }
                Double moneyGiven = rs.getDouble("moneyGiven");

                Order order = new Order();
                if(promotionID!=null)
                     order = new Order(orderID, orderAt, payment, status, new Promotion_DAO().getOne(promotionID), new Employee_DAO().getOne(employeeID), new Customer_DAO().getOne(customerID), new OrderDetail_DAO().getAll(orderID), subTotal, totalDue, moneyGiven);
                else
                    order = new Order(orderID, orderAt, payment, status, new Employee_DAO().getOne(employeeID), new Customer_DAO().getOne(customerID), new OrderDetail_DAO().getAll(orderID), subTotal, totalDue, moneyGiven);
                
                result.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * @param accoutingVoucherID Mã phiếu kết toán
     * @return ArrayList<Order> Danh sách hóa đơn đã được kết toán trong
     * phiếu kết toán
     * @author Hoàng Khang
     */
    public ArrayList<Order> getAllOrderInAcountingVoucher(String acountingVoucherID) {
        ArrayList<Order> result = new ArrayList<>();
        try {
            String sql = "SELECT * FROM [Order] WHERE acountingVoucherID = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, acountingVoucherID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String orderID = resultSet.getString("orderID");
                boolean status = resultSet.getBoolean("status");
                Date orderAt = resultSet.getDate("orderAt");
                boolean payment = resultSet.getBoolean("payment");
                String employeeID = resultSet.getString("employeeID");
                String customerID = resultSet.getString("customerID");
                String promotionID = resultSet.getString("promotionID");
                Double totalDue = resultSet.getDouble("totalDue");
                Double subTotal = resultSet.getDouble("subTotal");
                Double moneyGiven = resultSet.getDouble("moneyGiven");

                Order order = null;
                if(promotionID!=null)
                     order = new Order(orderID, orderAt, payment, status, new Promotion_DAO().getOne(promotionID), new Employee_DAO().getOne(employeeID), new Customer_DAO().getOne(customerID), new OrderDetail_DAO().getAll(orderID), subTotal, totalDue, moneyGiven);
                else
                    order = new Order(orderID, orderAt, payment, status, new Employee_DAO().getOne(employeeID), new Customer_DAO().getOne(customerID), new OrderDetail_DAO().getAll(orderID), subTotal, totalDue, moneyGiven);
                
                result.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Phương thức thực hiện việc cập nhật mã phiếu kết toán cho một
     * hóa đơn
     *
     * @param orderID Mã hóa đơn
     * @param acountingVoucherID Mã phiếu kết toán
     * @author Hoàng Khang
     */
    public boolean updateOrderAcountingVoucher(String orderID, String acountingVoucherID) {
        try {
            String sql = "UPDATE [Order] SET acountingVoucherID = ? WHERE orderID = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, acountingVoucherID);
            preparedStatement.setString(2, orderID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Phương thức thực hiện việc lấy ra các hoá đơn theo mã hoá đơn
     *
     * @param orderID Mã hóa đơn
     * @author Như Tâm
     */
    public ArrayList<Order> findById(String orderID) {
        ArrayList<Order> result = new ArrayList<>();
        String query = """
                       SELECT * FROM [dbo].[Order]
                       where orderID LIKE ?
                       """;
        try {

            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            st.setString(1, orderID + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (rs != null) {
                    result.add(getOrderData(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private Order getOrderData(ResultSet rs) throws Exception {
        Order result = null;

        //Lấy thông tin tổng quát của lớp order
        String orderID = rs.getString("orderID");
        String employeeID = rs.getString("employeeID");
        String customerID = rs.getString("customerID");
        Date orderAt = rs.getDate("orderAt");
        boolean status = rs.getBoolean("status");
        boolean payment = rs.getBoolean("payment");
        double subTotal = rs.getDouble("subTotal");
        double totalDue = rs.getDouble("totalDue");

        Double moneyGiven = rs.getDouble("moneyGiven");
        ArrayList<OrderDetail> orderDetailList = new OrderDetail_DAO().getAll(orderID);
        Employee employee = new Employee(employeeID);
        Customer customer = new Customer(customerID);
        result = new Order(orderID, orderAt, status, subTotal, totalDue, payment, employee, customer, orderDetailList, moneyGiven);
        return result;
    }

    public double[] getToTalInMonth(int month, int year) {
        double[] result = new double[31];

        for (int i = 0; i < result.length; i++) {
            result[i] = 0;
        }

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("select Day(orderAt) as day, sum(totalDue) as total from [Order] where YEAR(orderAt) = ? and Month(orderAt) = ? and status = 1 group by Day(orderAt)");
            st.setInt(1, year);
            st.setInt(2, month);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int day = rs.getInt("day");
                double total = rs.getDouble("total");

                result[day - 1] = total;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public int getNumberOfOrderInMonth(int month, int year){
        int result=0;


        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("select count(orderID) as sl from [Order] where YEAR(orderAt) = ? and Month(orderAt) = ? and status=1 ");
            st.setInt(1, year);
            st.setInt(2, month);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int sl = rs.getInt("sl");
                result=sl;

                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public double getTotalInMonth(int month, int year){
        double result=0;


        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("select sum(totalDue) as total from [Order] where YEAR(orderAt) = ? and Month(orderAt) = ? and status=1");
            st.setInt(1, year);
            st.setInt(2, month);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int total = rs.getInt("total");
                result=total;

                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    


    public ArrayList<Order> getNotCompleteOrder() {
        ArrayList<Order> result = new ArrayList<>();
        String query = """
                       SELECT * FROM [dbo].[Order]
                       where status = 0
                       """;
        try {

            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (rs != null) {
                    result.add(getOrderData(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

}

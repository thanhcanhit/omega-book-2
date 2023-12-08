/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConnectDB;
import entity.Account;
import entity.Customer;
import entity.Employee;
import entity.Shift;
import interfaces.DAOBase;
import java.security.CodeSigner;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import dao.Shift_DAO;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 *
 * @author Hoàng Khang
 */
public class Shift_DAO implements DAOBase<Shift> {

    private Employee_DAO employee_DAO = new Employee_DAO();

    @Override
    public Shift getOne(String id) {
        Shift shift = null;
        try {
            String sql = "SELECT * FROM Shift WHERE shiftID = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String employeeID = rs.getString("employeeID");
                String shiftID = rs.getString("shiftID");
                Timestamp startTimestamp = rs.getTimestamp("startedAt");
                Timestamp endTimestamp = rs.getTimestamp("endedAt");

                Date started = new java.sql.Date(startTimestamp.getTime());
                Date ended = new java.sql.Date(endTimestamp.getTime());

                shift = new Shift(shiftID, started, ended, new Account(employee_DAO.getOne(employeeID)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shift;
    }

   public ArrayList<Shift> getShiftsByDate(Date date) {
        ArrayList<Shift> shifts = new ArrayList<>();

        try {
            // Chuyển đổi ngày thành LocalDate để sử dụng trong truy vấn SQL
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Timestamp startOfDay = Timestamp.valueOf(localDate.atStartOfDay());
            Timestamp endOfDay = Timestamp.valueOf(localDate.plusDays(1).atStartOfDay().minusSeconds(1));

            String sql = "SELECT * FROM Shift WHERE startedAt >= ? AND startedAt <= ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setTimestamp(1, startOfDay);
            preparedStatement.setTimestamp(2, endOfDay);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String shiftID = rs.getString("shiftID");
                String employeeID = rs.getString("employeeID");
                Timestamp startTimestamp = rs.getTimestamp("startedAt");
                Timestamp endTimestamp = rs.getTimestamp("endedAt");

                Date started = new java.sql.Date(startTimestamp.getTime());
                Date ended = new java.sql.Date(endTimestamp.getTime());

                Shift shift = new Shift(shiftID, started, ended, new Account(employee_DAO.getOne(employeeID)));
                shifts.add(shift);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shifts;
    }

    @Override
    public ArrayList<Shift> getAll() {
        ArrayList<Shift> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("Select * from Shift");
            while (rs.next()) {
                String shiftID = rs.getString("shiftID");
                result.add(getOne(shiftID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String generateID() {
        String code = "PH";
        String maxID = "";
        String newID = "";
        Date date = new Date();
        // Tạo một đối tượng SimpleDateFormat với định dạng chỉ chứa giờ
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        // Sử dụng đối tượng SimpleDateFormat để chuyển đổi Date thành chuỗi giờ
        String hour = sdf.format(date);
        if (Integer.parseInt(hour) < 14) {
            code += "01";
        } else {
            code += "02";
        }
        SimpleDateFormat datef = new SimpleDateFormat("ddMMyyyy");
        code += datef.format(date);
        newID = code;
        try {
            code += "%";
            String sql = "  SELECT TOP 1  * FROM [Shift] WHERE shiftID LIKE '" + code + "' ORDER BY shiftID DESC;";
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                maxID = rs.getString("shiftID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (maxID.equals("")) {
            newID += "0000";
        } else {
            String lastFourChars = maxID.substring(maxID.length() - 4);
            int num = Integer.parseInt(lastFourChars);
            num++;
            newID += String.format("%04d", num);
        }
        return newID;

    }

    @Override
    public Boolean create(Shift shift) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("insert into Shift(shiftID, startedAt, endedAt, employeeID) "
                    + " values(?,?,?,?)");
            st.setString(1, shift.getShiftID());

            Timestamp end = new Timestamp(shift.getStartedAt().getTime());
            st.setTimestamp(2, end);

            Timestamp start = new Timestamp(shift.getEndedAt().getTime());
            st.setTimestamp(3, start);

            st.setString(4, shift.getAccount().getEmployee().getEmployeeID());

            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, Shift newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

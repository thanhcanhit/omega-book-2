/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bus;

import dao.Account_DAO;
import dao.Employee_DAO;
import entity.Employee;

/**
 *
 * @author thanhcanhit
 */
public class Login_BUS {

    private final Account_DAO accountDAO = new Account_DAO();
    private final Employee_DAO employeeDAO = new Employee_DAO();

    public Employee login(String id, String password) {
        boolean isValidAccount = accountDAO.validateAccount(id, password);

        if (!isValidAccount) {
            return null;
        } else {
            Employee employee = employeeDAO.getOne(id);
            return employee;
        }

    }
}

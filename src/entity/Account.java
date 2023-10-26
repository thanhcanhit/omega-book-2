/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.regex.Pattern;

/**
 *
 * @author macbookk
 */
public class Account {
    
    private final String PASSWORD_ERROR = "Password không hợp lệ !";
    private final String EMPLOYEE_ERROR = "Employee không được rỗng !";
  
    private String passWord;
    private Employee employee;

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) throws Exception{
        String regex = "^[A-Z][a-zA-Z0-9.,@&*^]{7,}.*\\d.*";
        if(Pattern.matches(regex, passWord))
            this.passWord = passWord;
        else
            throw new Exception(PASSWORD_ERROR);
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) throws Exception{
        if(employee!=null)
            this.employee = employee;
        else
            throw new Exception(EMPLOYEE_ERROR);
    }

    public Account(String passWord, Employee employee) throws Exception {
        setPassWord(passWord);
        setEmployee(employee);
    }
    
    public Account(Employee employee) throws Exception {
        setEmployee(employee);
    }

    public Account() {
    }

    @Override
    public String toString() {
        return "Account{" + "PASSWORD_ERROR=" + PASSWORD_ERROR + ", EMPLOYEE_ERROR=" + EMPLOYEE_ERROR + ", passWord=" + passWord + ", employee=" + employee + '}';
    }
    
    
    
}

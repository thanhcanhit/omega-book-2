/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import bus.EmployeeManagament_BUS;
import com.formdev.flatlaf.FlatClientProperties;
import entity.Employee;
import entity.Store;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import main.Application;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.*;
import raven.toast.Notifications;
import utilities.SVGIcon;

/**
 *
 * @author Như Tâm
 */
public class EmployeeManagement_GUI extends javax.swing.JPanel {

    private EmployeeManagament_BUS bus;
    private DefaultTableModel tblModel_employee;
    private Employee currentEmployee;
    private DefaultComboBoxModel cmbModel_role;
    private DefaultComboBoxModel cmbModel_status;
    private DefaultComboBoxModel cmbModel_roleInfo;
    private DefaultButtonModel btnModel_gender;
    private DefaultButtonModel btnModel_status;
    private static CellStyle cellStyleFormatNumber = null;

    /**
     * Creates new form EmployeeManagement_GUI
     */
    public EmployeeManagement_GUI() {
        initComponents();
        init();
    }
    
    private void init() {
        bus = new EmployeeManagament_BUS();
        //model
        tblModel_employee = new DefaultTableModel(new String[] {"Mã nhân viên", "Tên nhân viên", "Ngày sinh", "Trạng thái"}, 0);
        tbl_employeeInfor.setModel(tblModel_employee);
        tbl_employeeInfor.getSelectionModel().addListSelectionListener((e) -> {
            int rowIndex = tbl_employeeInfor.getSelectedRow();
            if(rowIndex == -1)
                return;
            
            String employeeID = tblModel_employee.getValueAt(rowIndex, 0).toString();
            this.currentEmployee = bus.getEmployee(employeeID);
            renderCurrentEmployee();
        });
        //combobox
        cmbModel_role = new DefaultComboBoxModel(new String[]{"Chức vụ", "Nhân viên Bán Hàng", "Cửa hàng trưởng"});
        cmb_roleEmp.setModel(cmbModel_role);
        cmbModel_roleInfo = new DefaultComboBoxModel(new String[]{"Nhân viên Bán Hàng", "Cửa hàng trưởng"});
        cmb_roleInfoEmp.setModel(cmbModel_roleInfo);
        cmbModel_status = new DefaultComboBoxModel(new String[]{"Trạng thái", "Đang làm việc", "Đã nghỉ"});
        cmb_statusEmp.setModel(cmbModel_status);
        //radio button
        group_gender.add(rdb_male);
        group_gender.add(rdb_female);
        group_statusEmp.add(rdb_woking);
        group_statusEmp.add(rdb_stopWorking);
        renderEmployeeTable(bus.getAllEmployee());
        
    }
    private void renderCurrentEmployee() {
        txt_empID.setText(currentEmployee.getEmployeeID());
        txt_name.setText(currentEmployee.getName());
        txt_addressEmp.setText(currentEmployee.getAddress());
        if(currentEmployee.isGender())
            rdb_male.setSelected(true);
        else
            rdb_female.setSelected(true);
        txt_phoneNumberEmp.setText(currentEmployee.getPhoneNumber());
        txt_citizenIDEmp.setText(currentEmployee.getCitizenIdentification());
        if(currentEmployee.isStatus())
            rdb_woking.setSelected(true);
        else
            rdb_stopWorking.setSelected(true);
        if(currentEmployee.getRole().equals("Nhân Viên Bán Hàng"))
            cmbModel_roleInfo.setSelectedItem("Nhân Viên Bán Hàng");
        else
            cmbModel_roleInfo.setSelectedItem("Cửa Hàng Trưởng");
        txt_storeID.setText(currentEmployee.getStore().getStoreID());
        chooseDateOfBirth.setDate(currentEmployee.getDateOfBirth());
        //chooseDateStart.setDate();
    }
    
    private void renderEmployeeTable(ArrayList<Employee> employeeList) {
        tblModel_employee.setRowCount(0);
        String status;
        for (Employee employee : employeeList) {
            if(employee.isStatus())
                status = "Đang làm việc";
            else
                status = "Đã nghỉ";
            String[] newRow = {employee.getEmployeeID(), employee.getName(), employee.getDateOfBirth().toString(), status};
            tblModel_employee.addRow(newRow);
        }        
    }
    private void renderEmployeeInfor() {
        txt_empID.setText("");
        txt_name.setText("");
        txt_addressEmp.setText("");
        group_gender.clearSelection();
        txt_phoneNumberEmp.setText("");
        txt_citizenIDEmp.setText("");
        rdb_female.setSelected(true);
        cmbModel_roleInfo.setSelectedItem("Nhân Viên Bán Hàng");
        chooseDateOfBirth.setDate(java.sql.Date.valueOf(LocalDate.now()));
        chooseDateStart.setDate(java.sql.Date.valueOf(LocalDate.now()));
        txt_storeID.setText("");
    }
    private boolean validEmployee() {
        if(txt_name.getText().equals("")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập điền tên nhân viên");
            txt_name.requestFocus();
            return false;
        }
        if(txt_addressEmp.getText().equals("")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập địa chỉ nhân viên");
            txt_addressEmp.requestFocus();
            return false;
        }
        if(txt_phoneNumberEmp.getText().equals("")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập SDT tên nhân viên");
            txt_phoneNumberEmp.requestFocus();
            return false;
        }
        if(txt_citizenIDEmp.getText().equals("")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập mã CCCD của nhân viên");
            txt_citizenIDEmp.requestFocus();
            return false;
        }
        if(!Pattern.matches("[0-9]{12}", txt_citizenIDEmp.getText())) {
            Notifications.getInstance().show(Notifications.Type.INFO, "CCCD gồm 12 chữ số");
            txt_citizenIDEmp.requestFocus();
            return false;
        }
        if(group_gender.isSelected(btnModel_gender)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng chọn giới tính nhân viên");
            return false;
        }
        if(group_statusEmp.isSelected(btnModel_status)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng chọn trạng thái làm việc của nhân viên");
            return false;
        }
        return true;
    }
    private Employee getCurrentValue() {
        String name = txt_name.getText();
        String phoneNumber = txt_phoneNumberEmp.getText();
        String address = txt_addressEmp.getText();
        String citizenID = txt_citizenIDEmp.getText();
        String role = (String) cmb_roleInfoEmp.getSelectedItem();
        boolean gender = false;
        if(rdb_male.isSelected())
            gender = true;
        boolean status = false;
        if(rdb_woking.isSelected())
            status = true;
        Date dateOfBirth = chooseDateOfBirth.getDate();
        Date dateStart = chooseDateStart.getDate();
        String employeeID = txt_empID.getText();
        String storeID = txt_storeID.getText();
        
        Store store = bus.getStore(storeID);
        Employee employee = new Employee(employeeID, citizenID, role, status, name, phoneNumber, gender, dateOfBirth, address, store);
        return employee;
    }
    private Employee getNewValue() throws Exception {
        String name = txt_name.getText();
        String phoneNumber = txt_phoneNumberEmp.getText();
        String address = txt_addressEmp.getText();
        String citizenID = txt_citizenIDEmp.getText();
        String role = (String) cmb_roleInfoEmp.getSelectedItem();
        boolean gender = false;
        if(rdb_male.isSelected())
            gender = true;
        boolean status = false;
        if(rdb_woking.isSelected())
            status = true;
        Date dateOfBirth = chooseDateOfBirth.getDate();
        Date dateStart = chooseDateStart.getDate();
        String employeeID = bus.generateID(gender, dateOfBirth, dateStart);
        String storeID = txt_storeID.getText();
        
        Store store = new Store(storeID);
        Employee employee = new Employee(employeeID, citizenID, role, status, name, phoneNumber, gender, dateOfBirth, address, store);
        //Account account = new Account(employee);
        return employee;
    }
    private void updateEmployee() throws Exception {
        Employee newEmployee = getCurrentValue();
        try {
            if(bus.updateEmployee(newEmployee)) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật thông tin thành công");
                renderEmployeeTable(bus.getAllEmployee());
                renderEmployeeInfor();
            }
            else
                Notifications.getInstance().show(Notifications.Type.ERROR, "Cập nhật không thành công");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createExcel(ArrayList<Employee> listEmp, String filePath) {
        try {
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            SXSSFSheet sheet = workbook.createSheet("Danh Sách Nhân Viên");
            
            sheet.trackAllColumnsForAutoSizing();
            int rowIndex = 0;
            writeHeader(sheet, rowIndex);
            rowIndex = 4;
            for (Employee employee : listEmp) {
                SXSSFRow row = sheet.createRow(rowIndex);
                writeEmployee(employee, row);
                rowIndex++;
            }
            writeFooter(sheet, rowIndex);
            
            createOutputFile(workbook, filePath);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xuất file thành công!");
         
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private static void writeHeader(SXSSFSheet sheet, int rowIndex) {
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

        String[] title = {"MÃ NHÂN VIÊN", "TÊN NHÂN VIÊN", "GIỚI TÍNH", "NGÀY SINH", "CHỨC VỤ", "TRẠNG THÁI", "SỐ ĐIỆN THOẠI", "CĂN CƯỚC CÔNG DÂN", "ĐỊA CHỈ"};

        CellStyle cellStyle = createStyleForHeader(sheet);
        CellStyle headerCellStyle = createStyleForTitle(sheet);
        
        SXSSFRow headerRow = sheet.createRow(rowIndex++);
        SXSSFCell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("DANH SÁCH NHÂN VIÊN");
        headerCell.setCellStyle(headerCellStyle);
        
        SXSSFRow dateRow = sheet.createRow(rowIndex++);
        SXSSFCell dateCell = dateRow.createCell(0);
        dateCell.setCellValue("Ngày in: " + LocalDate.now());
        
        SXSSFRow empRow = sheet.createRow(rowIndex++);
        SXSSFCell empCell = empRow.createCell(0);
        empCell.setCellValue("Nhân viên in: " + Application.employee.getName());
        
        SXSSFRow row = sheet.createRow(rowIndex++);
        
        // Create cells
        SXSSFCell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("MÃ NHÂN VIÊN");
        for (int i = 0; i < title.length - 1; i++) {
            cell = row.createCell(i+1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(title[i+1]);
        }        
    }
    private void writeEmployee(Employee emp, SXSSFRow row) {
        if (cellStyleFormatNumber == null) {
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            SXSSFWorkbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
 
        SXSSFCell cell = row.createCell(0);
        cell.setCellValue(emp.getEmployeeID());
 
        cell = row.createCell(1);
        cell.setCellValue(emp.getName());
 
        cell = row.createCell(2);
        String gender = "Nam";
        if(!emp.isGender())
            gender = "Nữ";
        cell.setCellValue(gender);
        
 
        cell = row.createCell(3);
        cell.setCellValue(emp.getDateOfBirth().toString());
        
        cell = row.createCell(4);
        cell.setCellValue(emp.getRole());
        
        cell = row.createCell(5);
        String status = "Đã nghỉ";
        if(emp.isStatus())
            status = "Đang làm việc";
        cell.setCellValue(status);
        
        cell = row.createCell(6);
        cell.setCellValue(emp.getPhoneNumber());
        
        cell = row.createCell(7);
        cell.setCellValue(emp.getCitizenIdentification());
        
        cell = row.createCell(8);
        cell.setCellValue(emp.getAddress());
    }
    private void writeFooter(SXSSFSheet sheet, int rowIndex) {
        SXSSFRow row = sheet.createRow(rowIndex);
        SXSSFCell cell = row.createCell(9, CellType.FORMULA);
        cell.setCellFormula("COUNT(A2:A11)");
    }
    private static CellStyle createStyleForTitle(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 20); // font size
        font.setColor(IndexedColors.GREEN.getIndex()); // text color
        
 
        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }
    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.VIOLET.getIndex()); // text color
 
        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        return cellStyle;
    }
    private static void createOutputFile(SXSSFWorkbook workbook, String excelFilePath) throws FileNotFoundException, IOException {
        try (OutputStream os = new FileOutputStream(excelFilePath)) {
            workbook.write(os);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        group_gender = new javax.swing.ButtonGroup();
        group_statusEmp = new javax.swing.ButtonGroup();
        group_roleEmp = new javax.swing.ButtonGroup();
        pnl_topEmp = new javax.swing.JPanel();
        pnl_searchEmp = new javax.swing.JPanel();
        txt_searchEmp = new javax.swing.JTextField();
        pnl_btnSearchEmp = new javax.swing.JPanel();
        btn_searchEmp = new javax.swing.JButton();
        pnl_cmb = new javax.swing.JPanel();
        cmb_roleEmp = new javax.swing.JComboBox<>();
        cmb_statusEmp = new javax.swing.JComboBox<>();
        btn_searchFilterEmp = new javax.swing.JButton();
        btn_reloadEmp = new javax.swing.JButton();
        pnl_centerEmp = new javax.swing.JPanel();
        spl_inforEmp = new javax.swing.JSplitPane();
        scr_tableInforEmp = new javax.swing.JScrollPane();
        tbl_employeeInfor = new javax.swing.JTable();
        pnl_inforDetailEmp = new javax.swing.JPanel();
        pnl_txtInforEmp = new javax.swing.JPanel();
        pnl_empID = new javax.swing.JPanel();
        lbl_empID = new javax.swing.JLabel();
        txt_empID = new javax.swing.JTextField();
        pnl_nameEmp = new javax.swing.JPanel();
        lbl_name = new javax.swing.JLabel();
        txt_name = new javax.swing.JTextField();
        pnl_addressEmp = new javax.swing.JPanel();
        lbl_addressEmp = new javax.swing.JLabel();
        txt_addressEmp = new javax.swing.JTextField();
        pnl_genderEmp = new javax.swing.JPanel();
        lbl_genderEmp = new javax.swing.JLabel();
        pnl_genderRadioEmp = new javax.swing.JPanel();
        rdb_female = new javax.swing.JRadioButton();
        rdb_male = new javax.swing.JRadioButton();
        pnl_dateOfBirth = new javax.swing.JPanel();
        lbl_dateOfBirth = new javax.swing.JLabel();
        pnl_chooseDateOfBirth = new javax.swing.JPanel();
        chooseDateOfBirth = new com.toedter.calendar.JDateChooser();
        pnl_roleEmp = new javax.swing.JPanel();
        lbl_roleEmp = new javax.swing.JLabel();
        cmb_roleInfoEmp = new javax.swing.JComboBox<>();
        pnl_phoneNumberEmp = new javax.swing.JPanel();
        lbl_phoneNumberEmp = new javax.swing.JLabel();
        txt_phoneNumberEmp = new javax.swing.JTextField();
        pnl_citizenIDEmp = new javax.swing.JPanel();
        lbl_citizenID = new javax.swing.JLabel();
        txt_citizenIDEmp = new javax.swing.JTextField();
        pnl_dateStart = new javax.swing.JPanel();
        lbl_dateStart = new javax.swing.JLabel();
        pnl_chooseDateStart = new javax.swing.JPanel();
        chooseDateStart = new com.toedter.calendar.JDateChooser();
        pnl_statusEmp = new javax.swing.JPanel();
        lbl_statusEmp = new javax.swing.JLabel();
        pnl_statusRadioEmp = new javax.swing.JPanel();
        rdb_woking = new javax.swing.JRadioButton();
        rdb_stopWorking = new javax.swing.JRadioButton();
        pnl_storeID = new javax.swing.JPanel();
        lbl_storeID = new javax.swing.JLabel();
        txt_storeID = new javax.swing.JTextField();
        pnl_btnEmp = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btn_clearValue = new javax.swing.JButton();
        btn_updateEmp = new javax.swing.JButton();
        btn_changePass = new javax.swing.JButton();
        btn_printFile = new javax.swing.JButton();
        btn_addEmp = new javax.swing.JButton();

        setToolTipText("");
        setPreferredSize(new java.awt.Dimension(1366, 768));
        setLayout(new java.awt.BorderLayout());

        pnl_topEmp.setMinimumSize(new java.awt.Dimension(20, 20));
        pnl_topEmp.setPreferredSize(new java.awt.Dimension(1368, 70));
        pnl_topEmp.setLayout(new javax.swing.BoxLayout(pnl_topEmp, javax.swing.BoxLayout.X_AXIS));

        pnl_searchEmp.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_searchEmp.setLayout(new javax.swing.BoxLayout(pnl_searchEmp, javax.swing.BoxLayout.X_AXIS));

        txt_searchEmp.setPreferredSize(new java.awt.Dimension(500, 30));
        txt_searchEmp.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,"Nhập mã nhân viên");
        txt_searchEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searchEmpActionPerformed(evt);
            }
        });
        txt_searchEmp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_searchEmpKeyPressed(evt);
            }
        });
        pnl_searchEmp.add(txt_searchEmp);

        pnl_btnSearchEmp.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_btnSearchEmp.setLayout(new java.awt.BorderLayout());

        btn_searchEmp.setText("Tìm kiếm");
        btn_searchEmp.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_searchEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchEmpActionPerformed(evt);
            }
        });
        pnl_btnSearchEmp.add(btn_searchEmp, java.awt.BorderLayout.CENTER);

        pnl_searchEmp.add(pnl_btnSearchEmp);

        pnl_topEmp.add(pnl_searchEmp);

        pnl_cmb.setMaximumSize(new java.awt.Dimension(500, 32767));
        pnl_cmb.setPreferredSize(new java.awt.Dimension(500, 100));
        pnl_cmb.setLayout(new java.awt.GridLayout(1, 0));

        cmb_roleEmp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chức vụ", "NV Bán Hàng", "Cửa Hàng Trưởng", "Kiểm Sát Viên" }));
        cmb_roleEmp.setPreferredSize(new java.awt.Dimension(128, 32));
        pnl_cmb.add(cmb_roleEmp);

        cmb_statusEmp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Trạng thái", "Đang làm việc", "Đã nghỉ" }));
        cmb_statusEmp.setPreferredSize(new java.awt.Dimension(128, 32));
        pnl_cmb.add(cmb_statusEmp);

        btn_searchFilterEmp.setText("Lọc");
        btn_searchFilterEmp.setMaximumSize(new java.awt.Dimension(72, 40));
        btn_searchFilterEmp.setPreferredSize(new java.awt.Dimension(72, 40));
        btn_searchFilterEmp.setIcon(SVGIcon.getSVGIcon("imgs/public/filter.svg"));
        btn_searchFilterEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchFilterEmpActionPerformed(evt);
            }
        });
        pnl_cmb.add(btn_searchFilterEmp);

        btn_reloadEmp.setIcon(SVGIcon.getSVGIcon("imgs/public/refresh.svg"));
        btn_reloadEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reloadEmpActionPerformed(evt);
            }
        });
        pnl_cmb.add(btn_reloadEmp);

        pnl_topEmp.add(pnl_cmb);

        add(pnl_topEmp, java.awt.BorderLayout.PAGE_START);

        pnl_centerEmp.setLayout(new javax.swing.BoxLayout(pnl_centerEmp, javax.swing.BoxLayout.LINE_AXIS));

        spl_inforEmp.setResizeWeight(0.6);

        scr_tableInforEmp.setMinimumSize(new java.awt.Dimension(400, 20));
        scr_tableInforEmp.setPreferredSize(new java.awt.Dimension(800, 402));

        tbl_employeeInfor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã NV", "Họ tên", "Ngày sinh", "Chức vụ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_employeeInfor.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbl_employeeInfor.setShowGrid(false);
        scr_tableInforEmp.setViewportView(tbl_employeeInfor);
        if (tbl_employeeInfor.getColumnModel().getColumnCount() > 0) {
            tbl_employeeInfor.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        spl_inforEmp.setLeftComponent(scr_tableInforEmp);

        pnl_inforDetailEmp.setMinimumSize(new java.awt.Dimension(400, 379));
        pnl_inforDetailEmp.setPreferredSize(new java.awt.Dimension(250, 100));
        pnl_inforDetailEmp.setLayout(new java.awt.BorderLayout());

        pnl_txtInforEmp.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin nhân viên", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        pnl_txtInforEmp.setLayout(new javax.swing.BoxLayout(pnl_txtInforEmp, javax.swing.BoxLayout.Y_AXIS));

        pnl_empID.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_empID.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_empID.setPreferredSize(new java.awt.Dimension(210, 40));
        pnl_empID.setLayout(new javax.swing.BoxLayout(pnl_empID, javax.swing.BoxLayout.LINE_AXIS));

        lbl_empID.setText("Mã NV:");
        lbl_empID.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_empID.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_empID.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_empID.add(lbl_empID);

        txt_empID.setEditable(false);
        txt_empID.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_empID.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_empID.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_empID.setPreferredSize(new java.awt.Dimension(150, 40));
        pnl_empID.add(txt_empID);

        pnl_txtInforEmp.add(pnl_empID);

        pnl_nameEmp.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_nameEmp.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_nameEmp.setLayout(new javax.swing.BoxLayout(pnl_nameEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_name.setText("Họ tên:");
        lbl_name.setPreferredSize(lbl_empID.getPreferredSize());
        pnl_nameEmp.add(lbl_name);

        txt_name.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txt_name.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_name.setPreferredSize(new java.awt.Dimension(64, 30));
        pnl_nameEmp.add(txt_name);

        pnl_txtInforEmp.add(pnl_nameEmp);

        pnl_addressEmp.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_addressEmp.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_addressEmp.setLayout(new javax.swing.BoxLayout(pnl_addressEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_addressEmp.setText("Địa chỉ:");
        lbl_addressEmp.setPreferredSize(lbl_empID.getPreferredSize());
        pnl_addressEmp.add(lbl_addressEmp);

        txt_addressEmp.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txt_addressEmp.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_addressEmp.setPreferredSize(new java.awt.Dimension(64, 30));
        pnl_addressEmp.add(txt_addressEmp);

        pnl_txtInforEmp.add(pnl_addressEmp);

        pnl_genderEmp.setMaximumSize(new java.awt.Dimension(32815, 40));
        pnl_genderEmp.setPreferredSize(new java.awt.Dimension(183, 40));
        pnl_genderEmp.setLayout(new javax.swing.BoxLayout(pnl_genderEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_genderEmp.setText("Giới tính:");
        lbl_genderEmp.setPreferredSize(lbl_empID.getPreferredSize());
        pnl_genderEmp.add(lbl_genderEmp);

        pnl_genderRadioEmp.setMaximumSize(new java.awt.Dimension(32767, 30));
        pnl_genderRadioEmp.setMinimumSize(new java.awt.Dimension(103, 30));
        pnl_genderRadioEmp.setPreferredSize(new java.awt.Dimension(103, 30));
        pnl_genderRadioEmp.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        group_gender.add(rdb_female);
        rdb_female.setText("Nữ");
        rdb_female.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdb_femaleActionPerformed(evt);
            }
        });
        pnl_genderRadioEmp.add(rdb_female);

        group_gender.add(rdb_male);
        rdb_male.setText("Nam");
        pnl_genderRadioEmp.add(rdb_male);

        pnl_genderEmp.add(pnl_genderRadioEmp);

        pnl_txtInforEmp.add(pnl_genderEmp);

        pnl_dateOfBirth.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_dateOfBirth.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_dateOfBirth.setLayout(new javax.swing.BoxLayout(pnl_dateOfBirth, javax.swing.BoxLayout.LINE_AXIS));

        lbl_dateOfBirth.setText("Ngày sinh:");
        lbl_dateOfBirth.setPreferredSize(lbl_empID.getPreferredSize());
        pnl_dateOfBirth.add(lbl_dateOfBirth);

        pnl_chooseDateOfBirth.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        pnl_chooseDateOfBirth.setMaximumSize(new java.awt.Dimension(32767, 40));
        pnl_chooseDateOfBirth.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_chooseDateOfBirth.setLayout(new java.awt.GridLayout(1, 0));

        chooseDateOfBirth.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        chooseDateOfBirth.setPreferredSize(new java.awt.Dimension(148, 30));
        pnl_chooseDateOfBirth.add(chooseDateOfBirth);

        pnl_dateOfBirth.add(pnl_chooseDateOfBirth);

        pnl_txtInforEmp.add(pnl_dateOfBirth);

        pnl_roleEmp.setMaximumSize(new java.awt.Dimension(32815, 40));
        pnl_roleEmp.setPreferredSize(new java.awt.Dimension(183, 40));
        pnl_roleEmp.setLayout(new javax.swing.BoxLayout(pnl_roleEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_roleEmp.setText("Chức vụ:");
        lbl_roleEmp.setPreferredSize(lbl_empID.getPreferredSize());
        pnl_roleEmp.add(lbl_roleEmp);

        cmb_roleInfoEmp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhân Viên Bán Hàng", "Cửa Hàng Trưởng", "Giám Sát Viên" }));
        cmb_roleInfoEmp.setMaximumSize(new java.awt.Dimension(32767, 40));
        cmb_roleInfoEmp.setMinimumSize(new java.awt.Dimension(144, 30));
        cmb_roleInfoEmp.setPreferredSize(new java.awt.Dimension(144, 30));
        pnl_roleEmp.add(cmb_roleInfoEmp);

        pnl_txtInforEmp.add(pnl_roleEmp);

        pnl_phoneNumberEmp.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_phoneNumberEmp.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_phoneNumberEmp.setLayout(new javax.swing.BoxLayout(pnl_phoneNumberEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_phoneNumberEmp.setText("SDT:");
        lbl_phoneNumberEmp.setPreferredSize(lbl_empID.getPreferredSize());
        pnl_phoneNumberEmp.add(lbl_phoneNumberEmp);

        txt_phoneNumberEmp.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txt_phoneNumberEmp.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_phoneNumberEmp.setPreferredSize(new java.awt.Dimension(64, 30));
        pnl_phoneNumberEmp.add(txt_phoneNumberEmp);

        pnl_txtInforEmp.add(pnl_phoneNumberEmp);

        pnl_citizenIDEmp.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_citizenIDEmp.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_citizenIDEmp.setLayout(new javax.swing.BoxLayout(pnl_citizenIDEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_citizenID.setText("CCCD:");
        lbl_citizenID.setPreferredSize(lbl_empID.getPreferredSize());
        pnl_citizenIDEmp.add(lbl_citizenID);

        txt_citizenIDEmp.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txt_citizenIDEmp.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_citizenIDEmp.setPreferredSize(new java.awt.Dimension(64, 30));
        pnl_citizenIDEmp.add(txt_citizenIDEmp);

        pnl_txtInforEmp.add(pnl_citizenIDEmp);

        pnl_dateStart.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_dateStart.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_dateStart.setLayout(new javax.swing.BoxLayout(pnl_dateStart, javax.swing.BoxLayout.LINE_AXIS));

        lbl_dateStart.setText("Ngày vào làm:");
        lbl_dateStart.setPreferredSize(lbl_empID.getPreferredSize());
        pnl_dateStart.add(lbl_dateStart);

        pnl_chooseDateStart.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        pnl_chooseDateStart.setMaximumSize(new java.awt.Dimension(32767, 40));
        pnl_chooseDateStart.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_chooseDateStart.setLayout(new java.awt.GridLayout(1, 0, 3, 3));
        pnl_chooseDateStart.add(chooseDateStart);

        pnl_dateStart.add(pnl_chooseDateStart);

        pnl_txtInforEmp.add(pnl_dateStart);

        pnl_statusEmp.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_statusEmp.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_statusEmp.setLayout(new javax.swing.BoxLayout(pnl_statusEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_statusEmp.setText("Trạng thái:");
        lbl_statusEmp.setPreferredSize(lbl_empID.getPreferredSize());
        pnl_statusEmp.add(lbl_statusEmp);

        pnl_statusRadioEmp.setMaximumSize(new java.awt.Dimension(32767, 30));
        pnl_statusRadioEmp.setMinimumSize(new java.awt.Dimension(103, 30));
        pnl_statusRadioEmp.setPreferredSize(new java.awt.Dimension(103, 30));
        pnl_statusRadioEmp.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        group_statusEmp.add(rdb_woking);
        rdb_woking.setText("Đang làm việc");
        rdb_woking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdb_wokingActionPerformed(evt);
            }
        });
        pnl_statusRadioEmp.add(rdb_woking);

        group_statusEmp.add(rdb_stopWorking);
        rdb_stopWorking.setText("Đã nghỉ");
        pnl_statusRadioEmp.add(rdb_stopWorking);

        pnl_statusEmp.add(pnl_statusRadioEmp);

        pnl_txtInforEmp.add(pnl_statusEmp);

        pnl_storeID.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_storeID.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_storeID.setLayout(new javax.swing.BoxLayout(pnl_storeID, javax.swing.BoxLayout.LINE_AXIS));

        lbl_storeID.setText("Chi nhánh CH:");
        lbl_storeID.setPreferredSize(lbl_empID.getPreferredSize());
        pnl_storeID.add(lbl_storeID);

        txt_storeID.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txt_storeID.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_storeID.setPreferredSize(new java.awt.Dimension(64, 30));
        pnl_storeID.add(txt_storeID);

        pnl_txtInforEmp.add(pnl_storeID);

        pnl_inforDetailEmp.add(pnl_txtInforEmp, java.awt.BorderLayout.CENTER);

        pnl_btnEmp.setMaximumSize(new java.awt.Dimension(2147483647, 100));
        pnl_btnEmp.setMinimumSize(new java.awt.Dimension(176, 100));
        pnl_btnEmp.setPreferredSize(new java.awt.Dimension(100, 150));
        pnl_btnEmp.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(2, 2, 5, 5));

        btn_clearValue.setText("Xóa trắng");
        btn_clearValue.setIcon(SVGIcon.getSVGIcon("imgs/public/clear.svg"));
        btn_clearValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearValueActionPerformed(evt);
            }
        });
        jPanel1.add(btn_clearValue);

        btn_updateEmp.setText("Cập nhật");
        btn_updateEmp.setIcon(SVGIcon.getSVGIcon("imgs/public/update.svg"));
        btn_updateEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateEmpActionPerformed(evt);
            }
        });
        jPanel1.add(btn_updateEmp);

        btn_changePass.setText("Đặt lại mật khẩu");
        btn_changePass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_changePassActionPerformed(evt);
            }
        });
        jPanel1.add(btn_changePass);

        btn_printFile.setText("Xuất Excel");
        btn_printFile.setIcon(SVGIcon.getSVGIcon("imgs/public/excel.svg"));
        btn_printFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_printFileActionPerformed(evt);
            }
        });
        jPanel1.add(btn_printFile);

        pnl_btnEmp.add(jPanel1, java.awt.BorderLayout.CENTER);

        btn_addEmp.setText("Thêm mới nhân viên");
        btn_addEmp.setPreferredSize(new java.awt.Dimension(72, 50));
        btn_addEmp.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_addEmp.setIcon(SVGIcon.getPrimarySVGIcon("imgs/public/add.svg"));
        btn_addEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addEmpActionPerformed(evt);
            }
        });
        pnl_btnEmp.add(btn_addEmp, java.awt.BorderLayout.SOUTH);

        pnl_inforDetailEmp.add(pnl_btnEmp, java.awt.BorderLayout.SOUTH);

        spl_inforEmp.setRightComponent(pnl_inforDetailEmp);

        pnl_centerEmp.add(spl_inforEmp);

        add(pnl_centerEmp, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searchEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchEmpActionPerformed

    private void rdb_femaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdb_femaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdb_femaleActionPerformed

    private void rdb_wokingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdb_wokingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdb_wokingActionPerformed

    private void btn_searchEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchEmpActionPerformed
        String searchQuery = txt_searchEmp.getText();
        if (searchQuery.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng điền mã nhân viên");
            return;
        }
        renderEmployeeTable(bus.searchById(searchQuery));
        //disablePage();
    }//GEN-LAST:event_btn_searchEmpActionPerformed

    private void btn_searchFilterEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchFilterEmpActionPerformed
        int role = cmb_roleEmp.getSelectedIndex();
        int status = cmb_statusEmp.getSelectedIndex();
        renderEmployeeTable(bus.filter(role, status));
    }//GEN-LAST:event_btn_searchFilterEmpActionPerformed

    private void btn_reloadEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reloadEmpActionPerformed
        renderEmployeeTable(bus.getAllEmployee());
        renderEmployeeInfor();
    }//GEN-LAST:event_btn_reloadEmpActionPerformed

    private void btn_updateEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateEmpActionPerformed
       
        if(tbl_employeeInfor.getSelectedRow() == -1) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Vui lòng chọn nhân viên để cập nhật");
            return;
        }
        try {
            Employee newEmployee = getCurrentValue();
            if(bus.updateEmployee(newEmployee)) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật thông tin thành công");
                renderEmployeeTable(bus.getAllEmployee());
                renderEmployeeInfor();
            }
            else
                Notifications.getInstance().show(Notifications.Type.ERROR, "Cập nhật không thành công");
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, ex.getMessage());
        }
    }//GEN-LAST:event_btn_updateEmpActionPerformed

    private void btn_addEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addEmpActionPerformed
        try {
            Employee newEmployee = getNewValue();
            if(newEmployee == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Vui lòng nhập đầy đủ thông tin");
                return;
            }
            bus.addNewEmployee(newEmployee);
            renderEmployeeInfor();
            renderEmployeeTable(bus.getAllEmployee());
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm nhân viên thành công");
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, ex.getMessage());
        }
    }//GEN-LAST:event_btn_addEmpActionPerformed

    private void txt_searchEmpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchEmpKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String searchQuery = txt_searchEmp.getText();
            if (searchQuery.isBlank()) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng điền mã nhân viên");
                return;
            }
            renderEmployeeTable(bus.searchById(searchQuery));
        }
    }//GEN-LAST:event_txt_searchEmpKeyPressed

    private void btn_changePassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_changePassActionPerformed
        if(currentEmployee == null) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn nhân viên cần đặt lại mật khẩu");
            return;
        }
        String id = currentEmployee.getEmployeeID();
        if(bus.updatePassword(id))
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đặt lại mật khẩu thành công");
        else
            Notifications.getInstance().show(Notifications.Type.ERROR, "Đặt lại mật khẩu không thành công");
    }//GEN-LAST:event_btn_changePassActionPerformed

    private void btn_clearValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearValueActionPerformed
        renderEmployeeTable(bus.getAllEmployee());
        renderEmployeeInfor();
    }//GEN-LAST:event_btn_clearValueActionPerformed

    private void btn_printFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_printFileActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn đường dẫn và tên file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Hiển thị hộp thoại và kiểm tra nếu người dùng chọn OK
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                // Lấy đường dẫn và tên file được chọn
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                // Gọi phương thức để tạo file Excel với đường dẫn và tên file đã chọn
                createExcel(bus.getAllEmployee(), filePath+".xlsx");
                Desktop.getDesktop().open(new File(filePath+".xlsx"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
    }//GEN-LAST:event_btn_printFileActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_addEmp;
    private javax.swing.JButton btn_changePass;
    private javax.swing.JButton btn_clearValue;
    private javax.swing.JButton btn_printFile;
    private javax.swing.JButton btn_reloadEmp;
    private javax.swing.JButton btn_searchEmp;
    private javax.swing.JButton btn_searchFilterEmp;
    private javax.swing.JButton btn_updateEmp;
    private com.toedter.calendar.JDateChooser chooseDateOfBirth;
    private com.toedter.calendar.JDateChooser chooseDateStart;
    private javax.swing.JComboBox<String> cmb_roleEmp;
    private javax.swing.JComboBox<String> cmb_roleInfoEmp;
    private javax.swing.JComboBox<String> cmb_statusEmp;
    private javax.swing.ButtonGroup group_gender;
    private javax.swing.ButtonGroup group_roleEmp;
    private javax.swing.ButtonGroup group_statusEmp;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_addressEmp;
    private javax.swing.JLabel lbl_citizenID;
    private javax.swing.JLabel lbl_dateOfBirth;
    private javax.swing.JLabel lbl_dateStart;
    private javax.swing.JLabel lbl_empID;
    private javax.swing.JLabel lbl_genderEmp;
    private javax.swing.JLabel lbl_name;
    private javax.swing.JLabel lbl_phoneNumberEmp;
    private javax.swing.JLabel lbl_roleEmp;
    private javax.swing.JLabel lbl_statusEmp;
    private javax.swing.JLabel lbl_storeID;
    private javax.swing.JPanel pnl_addressEmp;
    private javax.swing.JPanel pnl_btnEmp;
    private javax.swing.JPanel pnl_btnSearchEmp;
    private javax.swing.JPanel pnl_centerEmp;
    private javax.swing.JPanel pnl_chooseDateOfBirth;
    private javax.swing.JPanel pnl_chooseDateStart;
    private javax.swing.JPanel pnl_citizenIDEmp;
    private javax.swing.JPanel pnl_cmb;
    private javax.swing.JPanel pnl_dateOfBirth;
    private javax.swing.JPanel pnl_dateStart;
    private javax.swing.JPanel pnl_empID;
    private javax.swing.JPanel pnl_genderEmp;
    private javax.swing.JPanel pnl_genderRadioEmp;
    private javax.swing.JPanel pnl_inforDetailEmp;
    private javax.swing.JPanel pnl_nameEmp;
    private javax.swing.JPanel pnl_phoneNumberEmp;
    private javax.swing.JPanel pnl_roleEmp;
    private javax.swing.JPanel pnl_searchEmp;
    private javax.swing.JPanel pnl_statusEmp;
    private javax.swing.JPanel pnl_statusRadioEmp;
    private javax.swing.JPanel pnl_storeID;
    private javax.swing.JPanel pnl_topEmp;
    private javax.swing.JPanel pnl_txtInforEmp;
    private javax.swing.JRadioButton rdb_female;
    private javax.swing.JRadioButton rdb_male;
    private javax.swing.JRadioButton rdb_stopWorking;
    private javax.swing.JRadioButton rdb_woking;
    private javax.swing.JScrollPane scr_tableInforEmp;
    private javax.swing.JSplitPane spl_inforEmp;
    private javax.swing.JTable tbl_employeeInfor;
    private javax.swing.JTextField txt_addressEmp;
    private javax.swing.JTextField txt_citizenIDEmp;
    private javax.swing.JTextField txt_empID;
    private javax.swing.JTextField txt_name;
    private javax.swing.JTextField txt_phoneNumberEmp;
    private javax.swing.JTextField txt_searchEmp;
    private javax.swing.JTextField txt_storeID;
    // End of variables declaration//GEN-END:variables

    

    

    
}

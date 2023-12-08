/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import bus.CustomerManagement_BUS;
import com.formdev.flatlaf.FlatClientProperties;
import entity.Customer;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import raven.toast.Notifications;
import utilities.SVGIcon;

/**
 *
 * @author Hoàng Khang
 */
public class CustomerManagement_GUI extends javax.swing.JPanel {

    private DefaultTableModel tblModel_customer;
    private CustomerManagement_BUS customer_BUS = new CustomerManagement_BUS();

    /**
     * Creates new form CustomerManagement_GUI
     */
    public CustomerManagement_GUI() {
        initTableModel();
        initComponents();
        alterTable();
        renderCustomerTable(customer_BUS.getAllCustomer());
        tbl_customer.getSelectionModel().addListSelectionListener((e) -> {
            int row = tbl_customer.getSelectedRow();
            if (row != -1) {
                String customerID = tblModel_customer.getValueAt(row, 0).toString();
                Customer customer = customer_BUS.getOne(customerID);
                txt_customerID.setText(customerID);
                txt_name.setText(customer.getName());
                txt_phoneNumber.setText(customer.getPhoneNumber());
                txt_address.setText(customer.getAddress());
                txt_score.setText(Integer.toString(customer.getScore()));
                txt_rank.setText(customer.getRank());
                if (customer.isGender()) {
                    rad_men.setSelected(true);
                } else {
                    rad_women.setSelected(true);
                }
                ;
                date_dateOfBirth.setDate(customer.getDateOfBirth());
            }
        });
    }

    public void renderCustomerTable(ArrayList<Customer> list) {
        tblModel_customer.setRowCount(0);
        for (Customer customer : list) {
            Object[] row = new Object[]{customer.getCustomerID(), customer.getName(), customer.getDateOfBirth(),
                customer.isGender() ? "Nam" : "Nữ", customer.getRank()};
            tblModel_customer.addRow(row);
        }
    }

    public void alterTable() {
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);

        //// Align
        tbl_customer.getColumnModel().getColumn(2).setCellRenderer(rightAlign);
    }

    public void initTableModel() {
        // Products
        tblModel_customer = new DefaultTableModel(
                new String[]{"Mã", "Tên khách hàng", "Ngày sinh", "Giới tính", "Hạng"
                }, 0);
    }

    public void reloadForm() {
        txt_customerID.setText("");
        txt_name.setText("");
        txt_phoneNumber.setText("");
        txt_rank.setText("");
        txt_score.setText("");
        date_dateOfBirth.setDate(Calendar.getInstance().getTime());
        txt_address.setText("");
        rad_men.setSelected(true);
    }

    public static void createExcel(ArrayList<Customer> list, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Customer Data");

        // Gộp ô cho tiêu đề
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

        // Thêm dòng thông tin đầu tiên
        Row infoRow = sheet.createRow(0);
        Cell infoCell = infoRow.createCell(0);
        infoCell.setCellValue("Danh sách khách hàng");

        // Thiết lập style cho phần tiêu đề
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        infoCell.setCellStyle(titleStyle);

        Row row_date = sheet.createRow(1);
        Cell cell_date = row_date.createCell(0);
        cell_date.setCellValue("Ngày in: " + new Date());

        // Tạo header row
        Row headerRow = sheet.createRow(2);
        String[] columns = {"Mã khách hàng", "Tên", "Điểm tích lũy", "Gới tính", "Ngày sinh", "Số điện thoại", "Hạng", "Địa chỉ"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Đổ dữ liệu từ ArrayList vào Excel
        int rowNum = 3;
        for (Customer customer : list) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(customer.getCustomerID());
            row.createCell(1).setCellValue(customer.getName());
            row.createCell(2).setCellValue(customer.getScore());
            row.createCell(3).setCellValue(customer.isGender() ? "Nam" : "Nữ");
            row.createCell(4).setCellValue(customer.getDateOfBirth().toString()); // Cần xử lý định dạng ngày tháng
            row.createCell(5).setCellValue(customer.getPhoneNumber());
            row.createCell(6).setCellValue(customer.getRank());
            row.createCell(7).setCellValue(customer.getAddress());
        }

        // Ghi vào file
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grp_gender = new javax.swing.ButtonGroup();
        pnl_searchCustomer = new javax.swing.JPanel();
        pnl_filterCustomer = new javax.swing.JPanel();
        pnl_searchForPhone = new javax.swing.JPanel();
        lbl_filterNumberPhone = new javax.swing.JLabel();
        txt_searchForPhone = new javax.swing.JTextField();
        pnl_filterGender = new javax.swing.JPanel();
        lbl_filterGender = new javax.swing.JLabel();
        cbo_filterGender = new javax.swing.JComboBox<>();
        pnl_filterRank = new javax.swing.JPanel();
        lbl_filterRank = new javax.swing.JLabel();
        cbo_filterRank = new javax.swing.JComboBox<>();
        pnl_filterAge = new javax.swing.JPanel();
        lbl_filterGender1 = new javax.swing.JLabel();
        cbo_filterAge = new javax.swing.JComboBox<>();
        btn_filter = new javax.swing.JButton();
        btn_reloadList = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        pnl_info = new javax.swing.JPanel();
        pnl_infoBody = new javax.swing.JPanel();
        pnl_customerID = new javax.swing.JPanel();
        lbl_customerID = new javax.swing.JLabel();
        txt_customerID = new javax.swing.JTextField();
        pnl_name = new javax.swing.JPanel();
        lbl_name = new javax.swing.JLabel();
        txt_name = new javax.swing.JTextField();
        pnl_dateOfBirth = new javax.swing.JPanel();
        lbl_dateOfBirth = new javax.swing.JLabel();
        date_dateOfBirth = new com.toedter.calendar.JDateChooser();
        pnl_gender = new javax.swing.JPanel();
        lbl_gender = new javax.swing.JLabel();
        pnl_genderGr = new javax.swing.JPanel();
        rad_men = new javax.swing.JRadioButton();
        rad_women = new javax.swing.JRadioButton();
        pnl_phoneNumber = new javax.swing.JPanel();
        lbl_phoneNumber = new javax.swing.JLabel();
        txt_phoneNumber = new javax.swing.JTextField();
        pnl_address = new javax.swing.JPanel();
        lbl_address = new javax.swing.JLabel();
        txt_address = new javax.swing.JTextField();
        pnl_score = new javax.swing.JPanel();
        lbl_score = new javax.swing.JLabel();
        txt_score = new javax.swing.JTextField();
        pnl_rank = new javax.swing.JPanel();
        lbl_rank = new javax.swing.JLabel();
        txt_rank = new javax.swing.JTextField();
        pnl_infoFooter = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        btn_reloadForm = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btn_create = new javax.swing.JButton();
        btn_exportExcel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_customer = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(1366, 768));
        setLayout(new java.awt.BorderLayout());

        pnl_searchCustomer.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 50, 20, 50));
        pnl_searchCustomer.setLayout(new javax.swing.BoxLayout(pnl_searchCustomer, javax.swing.BoxLayout.LINE_AXIS));

        pnl_filterCustomer.setLayout(new javax.swing.BoxLayout(pnl_filterCustomer, javax.swing.BoxLayout.X_AXIS));

        pnl_searchForPhone.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 50));
        pnl_searchForPhone.setMinimumSize(new java.awt.Dimension(195, 30));
        pnl_searchForPhone.setPreferredSize(new java.awt.Dimension(181, 30));
        pnl_searchForPhone.setLayout(new javax.swing.BoxLayout(pnl_searchForPhone, javax.swing.BoxLayout.X_AXIS));

        lbl_filterNumberPhone.setFont(lbl_filterNumberPhone.getFont().deriveFont((float)14));
        lbl_filterNumberPhone.setText("Số điện thoại:");
        lbl_filterNumberPhone.setMinimumSize(new java.awt.Dimension(100, 20));
        lbl_filterNumberPhone.setPreferredSize(new java.awt.Dimension(100, 20));
        pnl_searchForPhone.add(lbl_filterNumberPhone);

        txt_searchForPhone.setPreferredSize(new java.awt.Dimension(50, 22));
        txt_searchForPhone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Số điện thoại người dùng");
        pnl_searchForPhone.add(txt_searchForPhone);

        pnl_filterCustomer.add(pnl_searchForPhone);

        pnl_filterGender.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        pnl_filterGender.setLayout(new javax.swing.BoxLayout(pnl_filterGender, javax.swing.BoxLayout.LINE_AXIS));

        lbl_filterGender.setFont(lbl_filterGender.getFont().deriveFont((float)14));
        lbl_filterGender.setText("Giới tính: ");
        pnl_filterGender.add(lbl_filterGender);

        cbo_filterGender.setFont(cbo_filterGender.getFont().deriveFont((float)14));
        cbo_filterGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Nam", "Nữ" }));
        pnl_filterGender.add(cbo_filterGender);

        pnl_filterCustomer.add(pnl_filterGender);

        pnl_filterRank.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20));
        pnl_filterRank.setLayout(new javax.swing.BoxLayout(pnl_filterRank, javax.swing.BoxLayout.LINE_AXIS));

        lbl_filterRank.setFont(lbl_filterRank.getFont().deriveFont((float)14));
        lbl_filterRank.setText("Hạng: ");
        lbl_filterRank.setMaximumSize(new java.awt.Dimension(400, 20));
        pnl_filterRank.add(lbl_filterRank);

        cbo_filterRank.setFont(cbo_filterRank.getFont().deriveFont((float)14));
        cbo_filterRank.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Chưa có", "Bạc", "Vàng", "Kim cương" }));
        cbo_filterRank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_filterRankActionPerformed(evt);
            }
        });
        pnl_filterRank.add(cbo_filterRank);

        pnl_filterCustomer.add(pnl_filterRank);

        pnl_filterAge.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        pnl_filterAge.setLayout(new javax.swing.BoxLayout(pnl_filterAge, javax.swing.BoxLayout.LINE_AXIS));

        lbl_filterGender1.setFont(lbl_filterGender1.getFont().deriveFont((float)14));
        lbl_filterGender1.setText("Độ tuổi: ");
        lbl_filterGender1.setMaximumSize(new java.awt.Dimension(40, 20));
        pnl_filterAge.add(lbl_filterGender1);

        cbo_filterAge.setFont(cbo_filterAge.getFont().deriveFont((float)14));
        cbo_filterAge.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Dưới 18 tuổi", "Từ 18 đến 40 tuổi", "Trên 40 tuổi" }));
        cbo_filterAge.setToolTipText("");
        pnl_filterAge.add(cbo_filterAge);

        pnl_filterCustomer.add(pnl_filterAge);

        pnl_searchCustomer.add(pnl_filterCustomer);

        btn_filter.setText("Lọc");
        btn_filter.setIcon(SVGIcon.getSVGIcon("imgs/public/filter.svg"));
        btn_filter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_filterActionPerformed(evt);
            }
        });
        pnl_searchCustomer.add(btn_filter);

        btn_reloadList.setIcon(SVGIcon.getSVGIcon("imgs/public/refresh.svg"));
        pnl_searchCustomer.add(btn_reloadList);

        add(pnl_searchCustomer, java.awt.BorderLayout.NORTH);

        jSplitPane1.setResizeWeight(0.95);

        pnl_info.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        pnl_info.setMinimumSize(new java.awt.Dimension(400, 337));
        pnl_info.setPreferredSize(new java.awt.Dimension(400, 464));
        pnl_info.setLayout(new java.awt.BorderLayout());

        pnl_infoBody.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_infoBody.setLayout(new javax.swing.BoxLayout(pnl_infoBody, javax.swing.BoxLayout.Y_AXIS));

        pnl_customerID.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_customerID.setMaximumSize(new java.awt.Dimension(1000, 40));
        pnl_customerID.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_customerID.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_customerID.setLayout(new javax.swing.BoxLayout(pnl_customerID, javax.swing.BoxLayout.LINE_AXIS));

        lbl_customerID.setFont(lbl_customerID.getFont().deriveFont((float)16));
        lbl_customerID.setText("Mã:");
        lbl_customerID.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_customerID.add(lbl_customerID);

        txt_customerID.setEditable(false);
        txt_customerID.setFont(txt_customerID.getFont().deriveFont((float)16));
        txt_customerID.setToolTipText("");
        txt_customerID.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0)));
        txt_customerID.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txt_customerID.setFocusable(false);
        txt_customerID.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_customerID.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_customerID.setPreferredSize(new java.awt.Dimension(100, 30));
        txt_customerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_customerIDActionPerformed(evt);
            }
        });
        pnl_customerID.add(txt_customerID);

        pnl_infoBody.add(pnl_customerID);

        pnl_name.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_name.setMaximumSize(new java.awt.Dimension(1000, 40));
        pnl_name.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_name.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_name.setLayout(new javax.swing.BoxLayout(pnl_name, javax.swing.BoxLayout.X_AXIS));

        lbl_name.setFont(lbl_name.getFont().deriveFont((float)16));
        lbl_name.setText("Họ và tên:");
        lbl_name.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_name.add(lbl_name);

        txt_name.setFont(txt_name.getFont().deriveFont((float)16));
        txt_name.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_name.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_name.add(txt_name);

        pnl_infoBody.add(pnl_name);

        pnl_dateOfBirth.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_dateOfBirth.setMaximumSize(new java.awt.Dimension(1000, 70));
        pnl_dateOfBirth.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_dateOfBirth.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_dateOfBirth.setLayout(new javax.swing.BoxLayout(pnl_dateOfBirth, javax.swing.BoxLayout.LINE_AXIS));

        lbl_dateOfBirth.setFont(lbl_dateOfBirth.getFont().deriveFont((float)16));
        lbl_dateOfBirth.setText("Ngày sinh:");
        lbl_dateOfBirth.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_dateOfBirth.add(lbl_dateOfBirth);

        date_dateOfBirth.setDateFormatString("MMMM d, yyyy");
        date_dateOfBirth.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        date_dateOfBirth.setPreferredSize(new java.awt.Dimension(100, 40));
        pnl_dateOfBirth.add(date_dateOfBirth);
        date_dateOfBirth.setDateFormatString("dd/MM/yyyy");

        pnl_infoBody.add(pnl_dateOfBirth);

        pnl_gender.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_gender.setMaximumSize(new java.awt.Dimension(1000, 40));
        pnl_gender.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_gender.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_gender.setLayout(new javax.swing.BoxLayout(pnl_gender, javax.swing.BoxLayout.LINE_AXIS));

        lbl_gender.setFont(lbl_gender.getFont().deriveFont((float)16));
        lbl_gender.setText("Giới tính:");
        lbl_gender.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_gender.add(lbl_gender);

        pnl_genderGr.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 90, 1, 1));
        pnl_genderGr.setLayout(new javax.swing.BoxLayout(pnl_genderGr, javax.swing.BoxLayout.X_AXIS));

        grp_gender.add(rad_men);
        rad_men.setFont(rad_men.getFont().deriveFont((float)16));
        rad_men.setSelected(true);
        rad_men.setText("Nam");
        pnl_genderGr.add(rad_men);

        grp_gender.add(rad_women);
        rad_women.setFont(rad_women.getFont().deriveFont((float)16));
        rad_women.setText("Nữ");
        rad_women.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rad_womenActionPerformed(evt);
            }
        });
        pnl_genderGr.add(rad_women);

        pnl_gender.add(pnl_genderGr);

        pnl_infoBody.add(pnl_gender);

        pnl_phoneNumber.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_phoneNumber.setMaximumSize(new java.awt.Dimension(1000, 40));
        pnl_phoneNumber.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_phoneNumber.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_phoneNumber.setLayout(new javax.swing.BoxLayout(pnl_phoneNumber, javax.swing.BoxLayout.LINE_AXIS));

        lbl_phoneNumber.setFont(lbl_phoneNumber.getFont().deriveFont((float)16));
        lbl_phoneNumber.setText("Số điện thoại:");
        lbl_phoneNumber.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_phoneNumber.add(lbl_phoneNumber);

        txt_phoneNumber.setFont(txt_phoneNumber.getFont().deriveFont((float)16));
        txt_phoneNumber.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_phoneNumber.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_phoneNumber.add(txt_phoneNumber);

        pnl_infoBody.add(pnl_phoneNumber);

        pnl_address.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_address.setMaximumSize(new java.awt.Dimension(1000, 40));
        pnl_address.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_address.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_address.setLayout(new javax.swing.BoxLayout(pnl_address, javax.swing.BoxLayout.LINE_AXIS));

        lbl_address.setFont(lbl_address.getFont().deriveFont((float)16));
        lbl_address.setText("Địa chỉ:");
        lbl_address.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_address.add(lbl_address);

        txt_address.setFont(txt_address.getFont().deriveFont((float)16));
        txt_address.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_address.setPreferredSize(new java.awt.Dimension(100, 30));
        txt_address.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_addressActionPerformed(evt);
            }
        });
        pnl_address.add(txt_address);

        pnl_infoBody.add(pnl_address);

        pnl_score.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_score.setMaximumSize(new java.awt.Dimension(1000, 40));
        pnl_score.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_score.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_score.setLayout(new javax.swing.BoxLayout(pnl_score, javax.swing.BoxLayout.LINE_AXIS));

        lbl_score.setFont(lbl_score.getFont().deriveFont((float)16));
        lbl_score.setText("Điểm thành viên:");
        lbl_score.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_score.add(lbl_score);

        txt_score.setEditable(false);
        txt_score.setFont(txt_score.getFont().deriveFont((float)16));
        txt_score.setFocusable(false);
        txt_score.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_score.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_score.add(txt_score);

        pnl_infoBody.add(pnl_score);

        pnl_rank.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_rank.setMaximumSize(new java.awt.Dimension(1000, 40));
        pnl_rank.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_rank.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_rank.setLayout(new javax.swing.BoxLayout(pnl_rank, javax.swing.BoxLayout.LINE_AXIS));

        lbl_rank.setFont(lbl_rank.getFont().deriveFont((float)16));
        lbl_rank.setText("Hạng thành viên:");
        lbl_rank.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_rank.add(lbl_rank);

        txt_rank.setEditable(false);
        txt_rank.setFont(txt_rank.getFont().deriveFont((float)16));
        txt_rank.setFocusable(false);
        txt_rank.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_rank.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_rank.add(txt_rank);

        pnl_infoBody.add(pnl_rank);

        pnl_info.add(pnl_infoBody, java.awt.BorderLayout.CENTER);

        pnl_infoFooter.setLayout(new javax.swing.BoxLayout(pnl_infoFooter, javax.swing.BoxLayout.Y_AXIS));

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jPanel9.setMaximumSize(new java.awt.Dimension(32767, 80));
        jPanel9.setPreferredSize(new java.awt.Dimension(200, 50));
        jPanel9.setLayout(new java.awt.GridLayout(1, 2));

        btn_reloadForm.setFont(btn_reloadForm.getFont().deriveFont((float)14));
        btn_reloadForm.setText("Xóa trắng");
        btn_reloadForm.setIcon(SVGIcon.getSVGIcon("imgs/public/clear.svg"));
        btn_reloadForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reloadFormActionPerformed(evt);
            }
        });
        jPanel9.add(btn_reloadForm);

        btn_update.setFont(btn_update.getFont().deriveFont((float)14));
        btn_update.setText("Cập nhật");
        btn_update.setIcon(SVGIcon.getSVGIcon("imgs/public/update.svg"));
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        jPanel9.add(btn_update);

        pnl_infoFooter.add(jPanel9);

        jPanel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jPanel10.setMaximumSize(new java.awt.Dimension(32767, 80));
        jPanel10.setPreferredSize(new java.awt.Dimension(200, 50));
        jPanel10.setLayout(new java.awt.GridLayout(1, 1));

        btn_create.setFont(btn_create.getFont().deriveFont((float)14));
        btn_create.setText("Thêm mới");
        btn_create.putClientProperty(FlatClientProperties.STYLE, "background: $Menu.background;"+"foreground: $Menu.foreground");
        btn_create.setIcon(SVGIcon.getPrimarySVGIcon("imgs/public/add.svg"));
        btn_create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_createActionPerformed(evt);
            }
        });
        jPanel10.add(btn_create);

        btn_exportExcel.setFont(btn_exportExcel.getFont().deriveFont((float)14));
        btn_exportExcel.setText("Xuất file");
        btn_exportExcel.setIcon(SVGIcon.getSVGIcon("imgs/public/excel.svg"));
        btn_exportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exportExcelActionPerformed(evt);
            }
        });
        jPanel10.add(btn_exportExcel);

        pnl_infoFooter.add(jPanel10);

        pnl_info.add(pnl_infoFooter, java.awt.BorderLayout.SOUTH);

        jSplitPane1.setRightComponent(pnl_info);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185))); // NOI18N
        jScrollPane1.setMinimumSize(new java.awt.Dimension(900, 41));

        tbl_customer.setFont(tbl_customer.getFont().deriveFont((float)14));
        tbl_customer.setModel(tblModel_customer);
        jScrollPane1.setViewportView(tbl_customer);

        jSplitPane1.setLeftComponent(jScrollPane1);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_addressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_addressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_addressActionPerformed

    private void btn_exportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exportExcelActionPerformed
        // Hiển thị hộp thoại chọn file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn đường dẫn và tên file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Hiển thị hộp thoại và kiểm tra nếu người dùng chọn OK
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Lấy đường dẫn và tên file được chọn
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Gọi phương thức để tạo file Excel với đường dẫn và tên file đã chọn
            createExcel(customer_BUS.getAllCustomer(), filePath + ".xlsx");
        }
    }//GEN-LAST:event_btn_exportExcelActionPerformed

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_searchActionPerformed
        // TODO add your handling code here:
        Customer customer = customer_BUS.searchByPhoneNumber(txt_searchForPhone.getText().trim());
        if (customer == null) {
            JOptionPane.showConfirmDialog(null, "Khách hàng chưa phải là thành viên");
            txt_searchForPhone.setFocusable(true);
        } else {
            ArrayList<Customer> list = new ArrayList<>();
            list.add(customer);
            renderCustomerTable(list);
            txt_searchForPhone.setText("");
        }
    }// GEN-LAST:event_btn_searchActionPerformed

    private void cbo_filterRankActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbo_filterRankActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_cbo_filterRankActionPerformed

    private void txt_customerIDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txt_customerIDActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txt_customerIDActionPerformed

    private void btn_reloadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_reloadActionPerformed
        // TODO add your handling code here:
        renderCustomerTable(customer_BUS.getAllCustomer());
    }// GEN-LAST:event_btn_reloadActionPerformed

    private void rad_womenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_rad_womenActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_rad_womenActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_updateActionPerformed
        try {
            // TODO add your handling code here:
            customer_BUS.updateCustomer(getValueForm(), txt_customerID.getText());
            renderCustomerTable(customer_BUS.getAllCustomer());

        } catch (Exception ex) {
            Logger.getLogger(CustomerManagement_GUI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }// GEN-LAST:event_btn_updateActionPerformed

    private void btn_createActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_createActionPerformed
        try {
            // TODO add your handling code here:
            customer_BUS.createCustomer(txt_name.getText(), date_dateOfBirth.getDate(), txt_phoneNumber.getText(),
                    txt_address.getText(), rad_men.isSelected() ? true : false);
            renderCustomerTable(customer_BUS.getAllCustomer());
            reloadForm();
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, ex.getMessage());
        }

    }// GEN-LAST:event_btn_createActionPerformed

    // ...
    private void btn_reloadFormActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void btn_filterActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_filterActionPerformed
        // TODO add your handling code here:
        String gender = cbo_filterGender.getSelectedItem().toString();
        String rank = cbo_filterRank.getSelectedItem().toString();
        String age = cbo_filterAge.getSelectedItem().toString();
        ArrayList<Customer> listFilter = customer_BUS.filterCustomer(gender, rank, age);
        renderCustomerTable(listFilter);
    }// GEN-LAST:event_btn_filterActionPerformed

    private Customer getValueForm() throws Exception {
        String customerID = txt_customerID.getText();
        String name = txt_name.getText();
        Date dateOfBirthf = date_dateOfBirth.getDate();
        String address = txt_address.getText();
        String numberPhone = txt_phoneNumber.getText().trim();
        boolean gender;
        if (rad_men.isSelected()) {
            gender = true;
        } else {
            gender = false;
        }
        int score = Integer.parseInt(txt_score.getText());
        return new Customer(customerID, name, gender, dateOfBirthf, score, numberPhone, address);
    }

    public boolean checkValueForm() {
        if (txt_name == null || txt_address == null || txt_phoneNumber == null) {
            return false;
        }
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_create;
    private javax.swing.JButton btn_exportExcel;
    private javax.swing.JButton btn_filter;
    private javax.swing.JButton btn_reloadForm;
    private javax.swing.JButton btn_reloadList;
    private javax.swing.JButton btn_update;
    private javax.swing.JComboBox<String> cbo_filterAge;
    private javax.swing.JComboBox<String> cbo_filterGender;
    private javax.swing.JComboBox<String> cbo_filterRank;
    private com.toedter.calendar.JDateChooser date_dateOfBirth;
    private javax.swing.ButtonGroup grp_gender;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lbl_address;
    private javax.swing.JLabel lbl_customerID;
    private javax.swing.JLabel lbl_dateOfBirth;
    private javax.swing.JLabel lbl_filterGender;
    private javax.swing.JLabel lbl_filterGender1;
    private javax.swing.JLabel lbl_filterNumberPhone;
    private javax.swing.JLabel lbl_filterRank;
    private javax.swing.JLabel lbl_gender;
    private javax.swing.JLabel lbl_name;
    private javax.swing.JLabel lbl_phoneNumber;
    private javax.swing.JLabel lbl_rank;
    private javax.swing.JLabel lbl_score;
    private javax.swing.JPanel pnl_address;
    private javax.swing.JPanel pnl_customerID;
    private javax.swing.JPanel pnl_dateOfBirth;
    private javax.swing.JPanel pnl_filterAge;
    private javax.swing.JPanel pnl_filterCustomer;
    private javax.swing.JPanel pnl_filterGender;
    private javax.swing.JPanel pnl_filterRank;
    private javax.swing.JPanel pnl_gender;
    private javax.swing.JPanel pnl_genderGr;
    private javax.swing.JPanel pnl_info;
    private javax.swing.JPanel pnl_infoBody;
    private javax.swing.JPanel pnl_infoFooter;
    private javax.swing.JPanel pnl_name;
    private javax.swing.JPanel pnl_phoneNumber;
    private javax.swing.JPanel pnl_rank;
    private javax.swing.JPanel pnl_score;
    private javax.swing.JPanel pnl_searchCustomer;
    private javax.swing.JPanel pnl_searchForPhone;
    private javax.swing.JRadioButton rad_men;
    private javax.swing.JRadioButton rad_women;
    private javax.swing.JTable tbl_customer;
    private javax.swing.JTextField txt_address;
    private javax.swing.JTextField txt_customerID;
    private javax.swing.JTextField txt_name;
    private javax.swing.JTextField txt_phoneNumber;
    private javax.swing.JTextField txt_rank;
    private javax.swing.JTextField txt_score;
    private javax.swing.JTextField txt_searchForPhone;
    // End of variables declaration//GEN-END:variables
}

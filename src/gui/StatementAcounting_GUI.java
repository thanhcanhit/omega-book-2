/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import bus.StatementAcounting_BUS;
import bus.StatementCashCount_BUS;
import com.formdev.flatlaf.FlatClientProperties;
import entity.AcountingVoucher;
import entity.CashCount;
import entity.CashCountSheet;
import entity.CashCountSheetDetail;
import entity.Employee;
import entity.Order;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import main.Application;
import raven.toast.Notifications;
import utilities.AcountingVoucherPrinter;
import utilities.CashCountSheetPrinter;
import utilities.FormatNumber;

/**
 *
 * @author Hoàng Khang
 */
public class StatementAcounting_GUI extends javax.swing.JPanel {

    private DefaultTableModel tbl_modalCashCounts = new DefaultTableModel();
    private double sum = 0;
    private Employee employee1 = Application.employee;
    private Employee employee2;
    private StatementAcounting_BUS acountingVoucher_BUS = new StatementAcounting_BUS();
    private Date endDate = new Date();
    private ArrayList<Order> listOrder;
    private AcountingVoucher acountingVoucher;
    private StatementCashCount_BUS statementCashCount_BUS = new StatementCashCount_BUS();
    double sale = 0;
    double payViaATM = 0;
    double withdraw = 0;
    double difference = 0;

    /**
     * Creates new form Statement_GUI
     */
    public StatementAcounting_GUI() {
        initTableModel();
        initComponents();
        initForm();
//        generatePDF(acountingVoucher_BUS.getAcountingByID("KTO151120230000"));
        alterTable();

        tbl_cashCounts.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column == 2) { // Số lượng
                    TableModel model = (TableModel) e.getSource();
                    try {
                        int quantity = Integer.parseInt(model.getValueAt(row, column).toString());
                        double denomination = Integer.parseInt(model.getValueAt(row, 1).toString().replace(".", ""));
                        double total = quantity * denomination;

                        sum = acountingVoucher_BUS.getTotal(getValueInTable());
                        txt_difference.setText(FormatNumber.toVND(sum - withdraw - 1765000));
                        txt_total.setText(FormatNumber.toVND(sum));
                        txt_totalMoney.setText(FormatNumber.toVND(sum));
                        model.setValueAt(FormatNumber.toVND(total), row, 3); // Tổng
                    } catch (NumberFormatException ex) {
                        model.setValueAt(0, row, column);
                        JOptionPane.showMessageDialog(null, "Số lượng không hợp lệ");
                    }
                }
            }

        });
    }

    public void generatePDF(AcountingVoucher acounting) {
        AcountingVoucherPrinter printer = new AcountingVoucherPrinter(acounting);
        printer.generatePDF();
        AcountingVoucherPrinter.PrintStatus status = printer.printFile();
        if (status == AcountingVoucherPrinter.PrintStatus.NOT_FOUND_FILE) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi không thể in hóa đơn: Không tìm thấy file");
        } else if (status == AcountingVoucherPrinter.PrintStatus.NOT_FOUND_PRINTER) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi không thể in hóa đơn: Không tìm thấy máy in");
        }
    }

    public void initForm() {
        //Lấy thời gian kết thúc phiếu kết toán trước đó (Thời gian bắt đầu lần kết toán này)
        Date start = acountingVoucher_BUS.getLastAcounting().getEndedDate();
        //Hiển thị thời gian bắt đầu -> kết thúc kết toán
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String endStr = formatter.format(endDate);
        String startStr = formatter.format(start);
        txt_timeAccounting.setText(startStr + " - " + endStr);
        //Hiển thị mã phiếu kết toán
        txt_acountingVoucherID.setText(acountingVoucher_BUS.generateID(endDate));
        listOrder = acountingVoucher_BUS.getAllOrderInAcounting(acountingVoucher_BUS.getLastAcounting().getEndedDate(), endDate);
        sale = acountingVoucher_BUS.getSale(listOrder);
        payViaATM = acountingVoucher_BUS.getPayViaATM(listOrder);
        withdraw = sale - payViaATM;
        difference = 0 - withdraw - 1765000;
        txt_saleAccounting.setText(FormatNumber.toVND(sale));
        txt_payViaATM.setText(FormatNumber.toVND(payViaATM));
        txt_withdraw.setText(FormatNumber.toVND(withdraw));
        txt_difference.setText(FormatNumber.toVND(difference));
    }

    public void alterTable() {
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);
        //Align
        tbl_cashCounts.getColumnModel().getColumn(1).setCellRenderer(rightAlign);
        tbl_cashCounts.getColumnModel().getColumn(2).setCellRenderer(rightAlign);
        tbl_cashCounts.getColumnModel().getColumn(3).setCellRenderer(rightAlign);
        tbl_cashCounts.getColumnModel().getColumn(0).setCellRenderer(rightAlign);
    }

    public void initTableModel() {
        // Products
        tbl_modalCashCounts = new DefaultTableModel(new Object[]{"STT", "Mệnh giá", "Số lượng", "Tổng"
        }, 0);
    }

    /**
     * Lấy giá trị phiếu kiểm tiền trong bảng
     *
     * @return
     */
    public ArrayList<CashCount> getValueInTable() {
        DefaultTableModel tableModel = (DefaultTableModel) tbl_cashCounts.getModel();
        int rowCount = tableModel.getRowCount();
        ArrayList<CashCount> cashCounts = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            String valueStr = (String) tableModel.getValueAt(i, 1);
            String quantityStr = (String) tableModel.getValueAt(i, 2);
            double value = Double.parseDouble(valueStr.replace(".", "").replace(",", "."));
            int quantity;
            if (quantityStr == null || quantityStr.equals("0")) {
                quantity = 0;
            } else {
                quantity = Integer.parseInt(quantityStr.trim());
            }
            if (quantity > 0) {
                cashCounts.add(new CashCount(quantity, value));
            }
        }
        return cashCounts;
    }

    /**
     * Tạo phiếu kiểm tiền dựa trên thông tin có sẵn
     *
     * @return
     */
    public CashCountSheet getCashCountSheet() {
        String cashCountSheetID = statementCashCount_BUS.generateID(endDate);
        ArrayList<CashCount> cashCounts = getValueInTable();
        ArrayList<CashCountSheetDetail> listEmployee = new ArrayList<>();
        listEmployee.add(new CashCountSheetDetail(true, employee1, new CashCountSheet(cashCountSheetID)));
        listEmployee.add(new CashCountSheetDetail(false, employee2, new CashCountSheet(cashCountSheetID)));
        return new CashCountSheet(cashCountSheetID, cashCounts, listEmployee, endDate, new Date());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_title = new javax.swing.JPanel();
        lbl_titleCashCount = new javax.swing.JLabel();
        pnl_center = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        pnl_infomation = new javax.swing.JPanel();
        pnl_accountingInfoHeader = new javax.swing.JPanel();
        txt_timeAccounting = new javax.swing.JTextField();
        pnl_accountingInfoBody = new javax.swing.JPanel();
        pnl_acountingVoucherID = new javax.swing.JPanel();
        lbl_acountingVoucherID = new javax.swing.JLabel();
        txt_acountingVoucherID = new javax.swing.JTextField();
        pnl_employeeAcounting1 = new javax.swing.JPanel();
        lbl_employeeAccounting1 = new javax.swing.JLabel();
        txt_employeeAccounting1 = new javax.swing.JTextField();
        pnl_employeeAcounting2 = new javax.swing.JPanel();
        lbl_employeeAccounting2 = new javax.swing.JLabel();
        txt_employeeAccounting2 = new javax.swing.JTextField();
        btn_addEmployee = new javax.swing.JButton();
        pnl_totalMoney = new javax.swing.JPanel();
        lbl_totalMoney = new javax.swing.JLabel();
        txt_totalMoney = new javax.swing.JTextField();
        pnl_saleAccounting = new javax.swing.JPanel();
        lbl_saleAccounting = new javax.swing.JLabel();
        txt_saleAccounting = new javax.swing.JTextField();
        pnl_payViaATM = new javax.swing.JPanel();
        lbl_payViaATM = new javax.swing.JLabel();
        txt_payViaATM = new javax.swing.JTextField();
        pnl_withdraw = new javax.swing.JPanel();
        lbl_withdraw = new javax.swing.JLabel();
        txt_withdraw = new javax.swing.JTextField();
        pnl_difference = new javax.swing.JPanel();
        lbl_difference = new javax.swing.JLabel();
        txt_difference = new javax.swing.JTextField();
        pnl_AccountingInfoFooter = new javax.swing.JPanel();
        pnl_accountingConfirm = new javax.swing.JPanel();
        btn_accountingConform = new javax.swing.JButton();
        pnl_cashCount = new javax.swing.JPanel();
        scr_cashCounts = new javax.swing.JScrollPane();
        tbl_cashCounts = new javax.swing.JTable();
        pnl_total = new javax.swing.JPanel();
        lbl_total = new javax.swing.JLabel();
        txt_total = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(1366, 768));
        setLayout(new java.awt.BorderLayout());

        pnl_title.setPreferredSize(new java.awt.Dimension(108, 62));
        pnl_title.setLayout(new java.awt.BorderLayout());

        lbl_titleCashCount.setFont(lbl_titleCashCount.getFont().deriveFont(lbl_titleCashCount.getFont().getStyle() | java.awt.Font.BOLD, 24));
        lbl_titleCashCount.setText("Kết toán");
        lbl_titleCashCount.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 1));
        lbl_titleCashCount.setMinimumSize(new java.awt.Dimension(108, 62));
        pnl_title.add(lbl_titleCashCount, java.awt.BorderLayout.CENTER);

        add(pnl_title, java.awt.BorderLayout.NORTH);

        pnl_center.setLayout(new javax.swing.BoxLayout(pnl_center, javax.swing.BoxLayout.LINE_AXIS));

        jSplitPane1.setResizeWeight(0.9);

        pnl_infomation.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        pnl_infomation.setMinimumSize(new java.awt.Dimension(420, 0));
        pnl_infomation.setPreferredSize(new java.awt.Dimension(300, 690));
        pnl_infomation.setLayout(new java.awt.BorderLayout());

        pnl_accountingInfoHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 15, 1));
        pnl_accountingInfoHeader.setPreferredSize(new java.awt.Dimension(204, 60));
        pnl_accountingInfoHeader.setRequestFocusEnabled(false);
        pnl_accountingInfoHeader.setLayout(new javax.swing.BoxLayout(pnl_accountingInfoHeader, javax.swing.BoxLayout.Y_AXIS));

        txt_timeAccounting.setEditable(false);
        txt_timeAccounting.setFont(txt_timeAccounting.getFont().deriveFont(txt_timeAccounting.getFont().getStyle() | java.awt.Font.BOLD, 16));
        txt_timeAccounting.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_timeAccounting.setText("8:10 18/10/2023 - 14:00 18/10/2023 ");
        txt_timeAccounting.setBorder(null);
        txt_timeAccounting.setFocusable(false);
        txt_timeAccounting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_timeAccountingActionPerformed(evt);
            }
        });
        pnl_accountingInfoHeader.add(txt_timeAccounting);

        pnl_infomation.add(pnl_accountingInfoHeader, java.awt.BorderLayout.NORTH);

        pnl_accountingInfoBody.setLayout(new javax.swing.BoxLayout(pnl_accountingInfoBody, javax.swing.BoxLayout.Y_AXIS));

        pnl_acountingVoucherID.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_acountingVoucherID.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_acountingVoucherID.setLayout(new javax.swing.BoxLayout(pnl_acountingVoucherID, javax.swing.BoxLayout.LINE_AXIS));

        lbl_acountingVoucherID.setFont(lbl_acountingVoucherID.getFont().deriveFont((float)16));
        lbl_acountingVoucherID.setText("Mã phiếu:");
        lbl_acountingVoucherID.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_acountingVoucherID.add(lbl_acountingVoucherID);

        txt_acountingVoucherID.setEditable(false);
        txt_acountingVoucherID.setFont(txt_acountingVoucherID.getFont().deriveFont((float)16));
        txt_acountingVoucherID.setText("KTI0018102023");
        txt_acountingVoucherID.setBorder(null);
        txt_acountingVoucherID.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txt_acountingVoucherID.setFocusAccelerator('n');
        txt_acountingVoucherID.setFocusable(false);
        txt_acountingVoucherID.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_acountingVoucherID.setPreferredSize(new java.awt.Dimension(100, 22));
        txt_acountingVoucherID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_acountingVoucherIDActionPerformed(evt);
            }
        });
        pnl_acountingVoucherID.add(txt_acountingVoucherID);

        pnl_accountingInfoBody.add(pnl_acountingVoucherID);

        pnl_employeeAcounting1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_employeeAcounting1.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_employeeAcounting1.setLayout(new javax.swing.BoxLayout(pnl_employeeAcounting1, javax.swing.BoxLayout.LINE_AXIS));

        lbl_employeeAccounting1.setFont(lbl_employeeAccounting1.getFont().deriveFont((float)16));
        lbl_employeeAccounting1.setText("Nhân viên 1:");
        lbl_employeeAccounting1.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_employeeAcounting1.add(lbl_employeeAccounting1);

        txt_employeeAccounting1.setEditable(false);
        txt_employeeAccounting1.setFont(txt_employeeAccounting1.getFont().deriveFont((float)16));
        txt_employeeAccounting1.setText("NV010020232300 - Lê Hoàng Khang");
        txt_employeeAccounting1.setBorder(null);
        txt_employeeAccounting1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txt_employeeAccounting1.setFocusable(false);
        txt_employeeAccounting1.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_employeeAccounting1.setPreferredSize(new java.awt.Dimension(100, 22));
        txt_employeeAccounting1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_employeeAccounting1ActionPerformed(evt);
            }
        });
        pnl_employeeAcounting1.add(txt_employeeAccounting1);

        pnl_accountingInfoBody.add(pnl_employeeAcounting1);

        pnl_employeeAcounting2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_employeeAcounting2.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_employeeAcounting2.setPreferredSize(new java.awt.Dimension(200, 32));
        pnl_employeeAcounting2.setLayout(new javax.swing.BoxLayout(pnl_employeeAcounting2, javax.swing.BoxLayout.LINE_AXIS));

        lbl_employeeAccounting2.setFont(lbl_employeeAccounting2.getFont().deriveFont((float)16));
        lbl_employeeAccounting2.setText("Nhân viên 2:");
        lbl_employeeAccounting2.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_employeeAcounting2.add(lbl_employeeAccounting2);

        txt_employeeAccounting2.setEditable(false);
        txt_employeeAccounting2.setFont(txt_employeeAccounting2.getFont().deriveFont((float)16));
        txt_employeeAccounting2.setText("Chưa có");
        txt_employeeAccounting2.setBorder(null);
        txt_employeeAccounting2.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txt_employeeAccounting2.setFocusable(false);
        txt_employeeAccounting2.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_employeeAccounting2.setMinimumSize(new java.awt.Dimension(100, 22));
        txt_employeeAccounting2.setPreferredSize(new java.awt.Dimension(100, 22));
        txt_employeeAccounting2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_employeeAccounting2ActionPerformed(evt);
            }
        });
        pnl_employeeAcounting2.add(txt_employeeAccounting2);

        btn_addEmployee.setText("+");
        btn_addEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addEmployeeActionPerformed(evt);
            }
        });
        pnl_employeeAcounting2.add(btn_addEmployee);

        pnl_accountingInfoBody.add(pnl_employeeAcounting2);

        pnl_totalMoney.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_totalMoney.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_totalMoney.setLayout(new javax.swing.BoxLayout(pnl_totalMoney, javax.swing.BoxLayout.LINE_AXIS));

        lbl_totalMoney.setFont(lbl_totalMoney.getFont().deriveFont((float)16));
        lbl_totalMoney.setText("Tiền mặt:");
        lbl_totalMoney.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_totalMoney.add(lbl_totalMoney);

        txt_totalMoney.setEditable(false);
        txt_totalMoney.setFont(txt_totalMoney.getFont().deriveFont((float)16));
        txt_totalMoney.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_totalMoney.setText("0");
        txt_totalMoney.setBorder(null);
        txt_totalMoney.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txt_totalMoney.setFocusable(false);
        txt_totalMoney.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_totalMoney.setPreferredSize(new java.awt.Dimension(100, 22));
        txt_totalMoney.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalMoneyActionPerformed(evt);
            }
        });
        pnl_totalMoney.add(txt_totalMoney);

        pnl_accountingInfoBody.add(pnl_totalMoney);

        pnl_saleAccounting.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_saleAccounting.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_saleAccounting.setLayout(new javax.swing.BoxLayout(pnl_saleAccounting, javax.swing.BoxLayout.LINE_AXIS));

        lbl_saleAccounting.setFont(lbl_saleAccounting.getFont().deriveFont((float)16));
        lbl_saleAccounting.setText("Doanh thu:");
        lbl_saleAccounting.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_saleAccounting.add(lbl_saleAccounting);

        txt_saleAccounting.setEditable(false);
        txt_saleAccounting.setFont(txt_saleAccounting.getFont().deriveFont((float)16));
        txt_saleAccounting.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_saleAccounting.setText("0");
        txt_saleAccounting.setBorder(null);
        txt_saleAccounting.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txt_saleAccounting.setFocusable(false);
        txt_saleAccounting.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_saleAccounting.setPreferredSize(new java.awt.Dimension(100, 22));
        txt_saleAccounting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_saleAccountingActionPerformed(evt);
            }
        });
        pnl_saleAccounting.add(txt_saleAccounting);

        pnl_accountingInfoBody.add(pnl_saleAccounting);

        pnl_payViaATM.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_payViaATM.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_payViaATM.setLayout(new javax.swing.BoxLayout(pnl_payViaATM, javax.swing.BoxLayout.LINE_AXIS));

        lbl_payViaATM.setFont(lbl_payViaATM.getFont().deriveFont((float)16));
        lbl_payViaATM.setText("ATM:");
        lbl_payViaATM.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_payViaATM.add(lbl_payViaATM);

        txt_payViaATM.setEditable(false);
        txt_payViaATM.setFont(txt_payViaATM.getFont().deriveFont((float)16));
        txt_payViaATM.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_payViaATM.setText("0");
        txt_payViaATM.setBorder(null);
        txt_payViaATM.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txt_payViaATM.setFocusable(false);
        txt_payViaATM.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_payViaATM.setPreferredSize(new java.awt.Dimension(100, 22));
        txt_payViaATM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_payViaATMActionPerformed(evt);
            }
        });
        pnl_payViaATM.add(txt_payViaATM);

        pnl_accountingInfoBody.add(pnl_payViaATM);

        pnl_withdraw.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_withdraw.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_withdraw.setLayout(new javax.swing.BoxLayout(pnl_withdraw, javax.swing.BoxLayout.LINE_AXIS));

        lbl_withdraw.setFont(lbl_withdraw.getFont().deriveFont((float)16));
        lbl_withdraw.setText("Tiền lấy ra:");
        lbl_withdraw.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_withdraw.add(lbl_withdraw);

        txt_withdraw.setEditable(false);
        txt_withdraw.setFont(txt_withdraw.getFont().deriveFont((float)16));
        txt_withdraw.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_withdraw.setText("0");
        txt_withdraw.setBorder(null);
        txt_withdraw.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txt_withdraw.setFocusable(false);
        txt_withdraw.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_withdraw.setPreferredSize(new java.awt.Dimension(100, 22));
        txt_withdraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_withdrawActionPerformed(evt);
            }
        });
        pnl_withdraw.add(txt_withdraw);

        pnl_accountingInfoBody.add(pnl_withdraw);

        pnl_difference.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_difference.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_difference.setLayout(new javax.swing.BoxLayout(pnl_difference, javax.swing.BoxLayout.LINE_AXIS));

        lbl_difference.setFont(lbl_difference.getFont().deriveFont((float)16));
        lbl_difference.setText("Chênh lệch:");
        lbl_difference.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_difference.add(lbl_difference);

        txt_difference.setEditable(false);
        txt_difference.setFont(txt_difference.getFont().deriveFont((float)16));
        txt_difference.setForeground(new java.awt.Color(255, 0, 51));
        txt_difference.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_difference.setText("0");
        txt_difference.setBorder(null);
        txt_difference.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txt_difference.setFocusable(false);
        txt_difference.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_difference.setPreferredSize(new java.awt.Dimension(100, 22));
        txt_difference.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_differenceActionPerformed(evt);
            }
        });
        pnl_difference.add(txt_difference);

        pnl_accountingInfoBody.add(pnl_difference);

        pnl_infomation.add(pnl_accountingInfoBody, java.awt.BorderLayout.CENTER);

        pnl_AccountingInfoFooter.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 0, 1));
        pnl_AccountingInfoFooter.setLayout(new javax.swing.BoxLayout(pnl_AccountingInfoFooter, javax.swing.BoxLayout.Y_AXIS));

        pnl_accountingConfirm.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 5, 0, 5));
        pnl_accountingConfirm.setLayout(new java.awt.BorderLayout());

        btn_accountingConform.setFont(btn_accountingConform.getFont().deriveFont(btn_accountingConform.getFont().getStyle() | java.awt.Font.BOLD, 20));
        btn_accountingConform.setText("Kết toán");
        btn_accountingConform.putClientProperty(FlatClientProperties.STYLE, "background: $Menu.background;"+"foreground: $Menu.foreground;");
        btn_accountingConform.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_accountingConformActionPerformed(evt);
            }
        });
        pnl_accountingConfirm.add(btn_accountingConform, java.awt.BorderLayout.PAGE_END);

        pnl_AccountingInfoFooter.add(pnl_accountingConfirm);

        pnl_infomation.add(pnl_AccountingInfoFooter, java.awt.BorderLayout.SOUTH);

        jSplitPane1.setRightComponent(pnl_infomation);

        pnl_cashCount.setMinimumSize(new java.awt.Dimension(700, 0));
        pnl_cashCount.setPreferredSize(new java.awt.Dimension(600, 447));
        pnl_cashCount.setLayout(new java.awt.BorderLayout());

        scr_cashCounts.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết kiểm tiền", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185)), javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 5))); // NOI18N
        scr_cashCounts.setMinimumSize(new java.awt.Dimension(0, 0));

        tbl_cashCounts.setFont(tbl_cashCounts.getFont().deriveFont(tbl_cashCounts.getFont().getStyle() & ~java.awt.Font.BOLD, 22));
        tbl_cashCounts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "500.000", null, null},
                {"2", "200.000", null, null},
                {"3", "100.000", null, null},
                {"4", "50.000", null, null},
                {"5", "20.000", null, null},
                {"6", "10.000", null, null},
                {"7", "5.000", null, null},
                {"8", "2.000", null, null},
                {"9", "1.000", null, null},
                {"10", "500", null, null}
            },
            new String [] {
                "STT", "Mệnh giá", "Số lượng", "Tổng"
            }
        ));
        tbl_cashCounts.setMaximumSize(new java.awt.Dimension(2147483647, 254623));
        tbl_cashCounts.setMinimumSize(new java.awt.Dimension(800, 0));
        tbl_cashCounts.setRowHeight(45);
        tbl_cashCounts.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr_cashCounts.setViewportView(tbl_cashCounts);

        pnl_cashCount.add(scr_cashCounts, java.awt.BorderLayout.CENTER);

        pnl_total.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 1, 5));
        pnl_total.setMaximumSize(new java.awt.Dimension(2147483647, 70));
        pnl_total.setMinimumSize(new java.awt.Dimension(149, 40));
        pnl_total.setPreferredSize(new java.awt.Dimension(160, 45));
        pnl_total.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        lbl_total.setFont(lbl_total.getFont().deriveFont(lbl_total.getFont().getStyle() | java.awt.Font.BOLD, 20));
        lbl_total.setText("Tổng:");
        pnl_total.add(lbl_total);

        txt_total.setEditable(false);
        txt_total.setFont(txt_total.getFont().deriveFont(txt_total.getFont().getStyle() | java.awt.Font.BOLD, 26));
        txt_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_total.setText("0");
        txt_total.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0)));
        txt_total.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txt_total.setEnabled(false);
        txt_total.setPreferredSize(new java.awt.Dimension(200, 22));
        txt_total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalActionPerformed(evt);
            }
        });
        pnl_total.add(txt_total);

        pnl_cashCount.add(pnl_total, java.awt.BorderLayout.SOUTH);

        jSplitPane1.setLeftComponent(pnl_cashCount);

        pnl_center.add(jSplitPane1);

        add(pnl_center, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_employeeAccounting1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_employeeAccounting1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_employeeAccounting1ActionPerformed

    private void txt_employeeAccounting2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_employeeAccounting2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_employeeAccounting2ActionPerformed

    private void btn_accountingConformActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_accountingConformActionPerformed
        // TODO add your handling code here:
        if (employee2 != null) {
            acountingVoucher_BUS.createAcountingVoucher(getCashCountSheet(), endDate);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Tạo phiếu kết toán thành công");
            Application.showForm(new StatementAcounting_GUI());
        } else {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Chưa có người đồng kiểm!");
            Notifications.getInstance().show(Notifications.Type.ERROR, "Kết toán thất bại!");
        }
    }//GEN-LAST:event_btn_accountingConformActionPerformed

    private void txt_saleAccountingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_saleAccountingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_saleAccountingActionPerformed

    private void txt_payViaATMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_payViaATMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_payViaATMActionPerformed

    private void txt_withdrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_withdrawActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_withdrawActionPerformed

    private void txt_differenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_differenceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_differenceActionPerformed

    private void txt_totalMoneyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalMoneyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalMoneyActionPerformed

    private void txt_timeAccountingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_timeAccountingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_timeAccountingActionPerformed

    private void txt_acountingVoucherIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_acountingVoucherIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_acountingVoucherIDActionPerformed

    private void txt_totalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalActionPerformed

    private void btn_addEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addEmployeeActionPerformed
        String id = JOptionPane.showInputDialog("Nhập mã nhân viên");
        Employee e = acountingVoucher_BUS.getEmployeeByID(id);
        if (e == null) {
            JOptionPane.showConfirmDialog(jSplitPane1, "Nhân viên không tồn tại.");
        } else {
            if (e.equals(employee1)) {
                JOptionPane.showConfirmDialog(jSplitPane1, "Nhân viên chứng kiến không được là nhân viên kiểm!");
            } else {
                employee2 = e;
                txt_employeeAccounting2.setText(employee2.getEmployeeID() + " - " + employee2.getName());
            }

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_addEmployeeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_accountingConform;
    private javax.swing.JButton btn_addEmployee;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lbl_acountingVoucherID;
    private javax.swing.JLabel lbl_difference;
    private javax.swing.JLabel lbl_employeeAccounting1;
    private javax.swing.JLabel lbl_employeeAccounting2;
    private javax.swing.JLabel lbl_payViaATM;
    private javax.swing.JLabel lbl_saleAccounting;
    private javax.swing.JLabel lbl_titleCashCount;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JLabel lbl_totalMoney;
    private javax.swing.JLabel lbl_withdraw;
    private javax.swing.JPanel pnl_AccountingInfoFooter;
    private javax.swing.JPanel pnl_accountingConfirm;
    private javax.swing.JPanel pnl_accountingInfoBody;
    private javax.swing.JPanel pnl_accountingInfoHeader;
    private javax.swing.JPanel pnl_acountingVoucherID;
    private javax.swing.JPanel pnl_cashCount;
    private javax.swing.JPanel pnl_center;
    private javax.swing.JPanel pnl_difference;
    private javax.swing.JPanel pnl_employeeAcounting1;
    private javax.swing.JPanel pnl_employeeAcounting2;
    private javax.swing.JPanel pnl_infomation;
    private javax.swing.JPanel pnl_payViaATM;
    private javax.swing.JPanel pnl_saleAccounting;
    private javax.swing.JPanel pnl_title;
    private javax.swing.JPanel pnl_total;
    private javax.swing.JPanel pnl_totalMoney;
    private javax.swing.JPanel pnl_withdraw;
    private javax.swing.JScrollPane scr_cashCounts;
    private javax.swing.JTable tbl_cashCounts;
    private javax.swing.JTextField txt_acountingVoucherID;
    private javax.swing.JTextField txt_difference;
    private javax.swing.JTextField txt_employeeAccounting1;
    private javax.swing.JTextField txt_employeeAccounting2;
    private javax.swing.JTextField txt_payViaATM;
    private javax.swing.JTextField txt_saleAccounting;
    private javax.swing.JTextField txt_timeAccounting;
    private javax.swing.JTextField txt_total;
    private javax.swing.JTextField txt_totalMoney;
    private javax.swing.JTextField txt_withdraw;
    // End of variables declaration//GEN-END:variables
}

/*
 * Click nbfs:    @Override
    protected void computeTime() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void computeFields() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void add(int field, int amount) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void roll(int field, boolean up) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getMinimum(int field) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getMaximum(int field) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getGreatestMinimum(int field) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getLeastMaximum(int field) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    @Override
    protected void computeTime() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    protected void computeFields() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void add(int field, int amount) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void roll(int field, boolean up) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getMinimum(int field) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getMaximum(int field) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getGreatestMinimum(int field) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getLeastMaximum(int field) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
//nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import bus.OrderManagement_BUS;
import bus.ProductManagement_BUS;
import entity.Customer;
import entity.Order;
import entity.OrderDetail;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import utilities.FormatNumber;
import com.formdev.flatlaf.FlatClientProperties;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.swing.JFileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilities.OrderPrinter;
import utilities.SVGIcon;
import org.apache.poi.ss.usermodel.Font;
import raven.toast.Notifications;

/**
 *
 * @author KienTran
 */
public final class OrderManagement_GUI extends javax.swing.JPanel {

    private OrderManagement_BUS bus;

    private DefaultTableModel tblModel_order;
    private DefaultTableModel tblModel_orderDetail;

    private int currentPage;
    private int lastPage;

    /**
     * Creates new form OrderManagement_GUI
     */
    public OrderManagement_GUI() {
        initComponents();
        init();
        alterTable();

    }

    public void init() {
        bus = new OrderManagement_BUS();

        tblModel_order = new DefaultTableModel(new String[]{"Mã hoá đơn", "Nhân viên", "Khách hàng", "Ngày mua", "Thành tiền"}, 0);
        tbl_order.setModel(tblModel_order);
        tbl_order.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            int rowIndex = tbl_order.getSelectedRow();
            if (rowIndex != -1) {
                String id = tblModel_order.getValueAt(rowIndex, 0).toString();
                Order order;
                try {
                    order = bus.getOrder(id);
                    renderOrderDetailTable(bus.getOrderDetailList(id));
                    renderInfomationOrder(order);
                } catch (Exception ex) {
                    Logger.getLogger(OrderManagement_GUI.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            return;

        });

        tblModel_orderDetail = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá", "Tổng tiền"}, 0);
        tbl_orderDetail.setModel(tblModel_orderDetail);
        tbl_orderDetail.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            int rowIndex = tbl_orderDetail.getSelectedRow();
            if (rowIndex == -1) {
                return;
            }
        });
        this.currentPage = 1;
        this.lastPage = bus.getLastPage();
        renderCurrentPage();

//        Gắn sự kiện xem lại hóa đơn pdf
        tbl_order.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
                    viewPDFFile();
                }
            }
        });
    }

    private void viewPDFFile() {
        int rowIndex = tbl_order.getSelectedRow();
        if (rowIndex == -1) {
            return;
        }
        String orderID = tblModel_order.getValueAt(rowIndex, 0).toString();
        Order order;
        try {
            order = bus.getOrder(orderID);
            new OrderPrinter(order).generatePDF();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void renderCurrentPage() {
        lbl_pageNumber.setText(currentPage + "/" + lastPage);
        renderOrdersTable(bus.getDataOfPage(currentPage));

//      Toggle button
        btn_previous.setEnabled(currentPage != 1);
        btn_next.setEnabled(currentPage != lastPage);
    }

    private void renderOrdersTable(ArrayList<Order> orderList) {
        tblModel_order.setRowCount(0);
        for (Order order : orderList) {
            Object[] newRow = new Object[]{order.getOrderID(), order.getEmployee().getName(), order.getCustomer().getCustomerID(), order.getOrderAt(), FormatNumber.toVND(order.getSubTotal())};
            tblModel_order.addRow(newRow);
        }
    }

    private void renderOrderDetailTable(ArrayList<OrderDetail> list) {
        tblModel_orderDetail.setRowCount(0);
        for (OrderDetail orderDetail : list) {
            ProductManagement_BUS productBUS = new ProductManagement_BUS();
            Object[] newRow = new Object[]{orderDetail.getProduct().getProductID(), productBUS.getProduct(orderDetail.getProduct().getProductID()).getName(), orderDetail.getQuantity(), orderDetail.getPrice(), FormatNumber.toVND(orderDetail.getLineTotal())};

            tblModel_orderDetail.addRow(newRow);
        }
    }

    public void renderInfomationOrder(Order order) {
        Customer customer = bus.getCustomer(order.getCustomer().getCustomerID());
        txt_customerName.setText(customer.getName());
        txt_phone.setText(customer.getPhoneNumber());
        txt_total.setText(FormatNumber.toVND(order.getSubTotal()));

    }

    public void alterTable() {
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);

        tbl_order.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tbl_order.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbl_order.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbl_order.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbl_order.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbl_order.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbl_order.getColumnModel().getColumn(4).setCellRenderer(rightAlign);
        tbl_order
                .setDefaultEditor(Object.class,
                         null);

        tbl_orderDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_orderDetail.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbl_orderDetail.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbl_orderDetail.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbl_orderDetail.getColumnModel().getColumn(2).setCellRenderer(rightAlign);
        tbl_orderDetail.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbl_orderDetail.getColumnModel().getColumn(3).setCellRenderer(rightAlign);
        tbl_orderDetail.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbl_orderDetail.getColumnModel().getColumn(4).setCellRenderer(rightAlign);
        tbl_orderDetail
                .setDefaultEditor(Object.class,
                         null);

    }

    public boolean validateFields() {
        return true;
    }

    public static void createExcel(ArrayList<Order> list, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order Data");

        // Gộp ô cho tiêu đề
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

        // Thêm dòng thông tin đầu tiên
        Row infoRow = sheet.createRow(0);
        Cell infoCell = infoRow.createCell(0);
        infoCell.setCellValue("Danh sách hoá đơn");

        // Thiết lập style cho phần tiêu đề
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = (Font) workbook.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleStyle.setFont((org.apache.poi.ss.usermodel.Font) titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        infoCell.setCellStyle(titleStyle);

        Row row_date = sheet.createRow(1);
        Cell cell_date = row_date.createCell(0);
        cell_date.setCellValue("Ngày in: " + new Date());

        // Tạo header row
        Row headerRow = sheet.createRow(2);
        String[] columns = {"Mã hoá đơn", " Mã nhân viên", "Tên khách hàng", " Ngày mua hàng", "Số điện thoại khách hàng", "Tổng tiền"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Đổ dữ liệu từ ArrayList vào Excel
        int rowNum = 3;
        for (Order order : list) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(order.getOrderID());
            row.createCell(1).setCellValue(order.getEmployee().getEmployeeID());
            row.createCell(2).setCellValue(order.getCustomer().getName());
            row.createCell(3).setCellValue(order.getOrderAt().toString());
            row.createCell(4).setCellValue(order.getCustomer().getPhoneNumber()); // Cần xử lý định dạng ngày tháng
            row.createCell(5).setCellValue(FormatNumber.toVND(order.getTotalDue()));
        }

        // Ghi vào file
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã tạo file thành công !");

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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_header = new javax.swing.JPanel();
        pnl_orderFilter = new javax.swing.JPanel();
        lbl_orderID = new javax.swing.JLabel();
        txt_orderID = new javax.swing.JTextField();
        pnl_orderStatusFilter = new javax.swing.JPanel();
        lbl_orderStatusFilter = new javax.swing.JLabel();
        cmb_orderPriceFilter = new javax.swing.JComboBox<>();
        pnl_orderDate = new javax.swing.JPanel();
        lbl_orderDate = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        pnl_searchButton = new javax.swing.JPanel();
        btn_search = new javax.swing.JButton();
        pnl_customerFilter = new javax.swing.JPanel();
        lbl_customerID = new javax.swing.JLabel();
        txt_customerID = new javax.swing.JTextField();
        pnl_customerPhone = new javax.swing.JPanel();
        lbl_customerPhone = new javax.swing.JLabel();
        txt_customerPhone = new javax.swing.JTextField();
        pnl_orderDateTo = new javax.swing.JPanel();
        lbl_orderDateTo = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        pnl_refreshButton = new javax.swing.JPanel();
        btn_refresh = new javax.swing.JButton();
        btn_wfile = new javax.swing.JButton();
        splitPane = new javax.swing.JSplitPane();
        pnl_center = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_order = new javax.swing.JTable();
        pnl_cartFooter = new javax.swing.JPanel();
        btn_previous = new javax.swing.JButton();
        lbl_pageNumber = new javax.swing.JLabel();
        btn_next = new javax.swing.JButton();
        btn_viewPDF = new javax.swing.JButton();
        pnl_infomation = new javax.swing.JPanel();
        pnl_orderDetail = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_orderDetail = new javax.swing.JTable();
        pnl_info = new javax.swing.JPanel();
        pnl_customerName = new javax.swing.JPanel();
        lbl_customerName = new javax.swing.JLabel();
        txt_customerName = new javax.swing.JTextField();
        pnl_phone = new javax.swing.JPanel();
        lbl_phone = new javax.swing.JLabel();
        txt_phone = new javax.swing.JTextField();
        pnl_total = new javax.swing.JPanel();
        lbl_customerName1 = new javax.swing.JLabel();
        txt_total = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        pnl_header.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm kiếm: "));
        pnl_header.setMinimumSize(new java.awt.Dimension(10, 100));
        pnl_header.setPreferredSize(new java.awt.Dimension(1366, 130));
        pnl_header.setLayout(new java.awt.GridLayout(2, 4));

        pnl_orderFilter.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_orderFilter.setLayout(new javax.swing.BoxLayout(pnl_orderFilter, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderID.setText("Mã đơn hàng: ");
        lbl_orderID.setPreferredSize(new java.awt.Dimension(130, 130));
        pnl_orderFilter.add(lbl_orderID);

        txt_orderID.setMinimumSize(null);
        txt_orderID.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_orderFilter.add(txt_orderID);

        pnl_header.add(pnl_orderFilter);

        pnl_orderStatusFilter.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_orderStatusFilter.setLayout(new javax.swing.BoxLayout(pnl_orderStatusFilter, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderStatusFilter.setText("Tổng doanh thu:");
        lbl_orderStatusFilter.setMaximumSize(new java.awt.Dimension(160, 150));
        lbl_orderStatusFilter.setMinimumSize(new java.awt.Dimension(130, 130));
        lbl_orderStatusFilter.setPreferredSize(new java.awt.Dimension(140, 150));
        pnl_orderStatusFilter.add(lbl_orderStatusFilter);

        cmb_orderPriceFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả","Dưới 100.000VNĐ", "Trên 100.000VNĐ & Dưới 500.000VNĐ", "Trên 500.000VNĐ & Dưới 1.000.000VNĐ", "Trên 1.000.000VNĐ" }));
        cmb_orderPriceFilter.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmb_orderPriceFilter.setMaximumSize(new java.awt.Dimension(10000, 32767));
        cmb_orderPriceFilter.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_orderStatusFilter.add(cmb_orderPriceFilter);

        pnl_header.add(pnl_orderStatusFilter);

        pnl_orderDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_orderDate.setMaximumSize(new java.awt.Dimension(20000, 2147483647));
        pnl_orderDate.setMinimumSize(new java.awt.Dimension(200, 200));
        pnl_orderDate.setPreferredSize(new java.awt.Dimension(300, 43));
        pnl_orderDate.setLayout(new javax.swing.BoxLayout(pnl_orderDate, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderDate.setText("Từ ngày: ");
        lbl_orderDate.setPreferredSize(new java.awt.Dimension(80, 0));
        pnl_orderDate.add(lbl_orderDate);

        jDateChooser1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20));
        jDateChooser1.setDateFormatString("dd/MM/yyyy");
        jDateChooser1.setDate(Calendar.getInstance().getTime());
        jDateChooser1.setMinimumSize(new java.awt.Dimension(150, 30));
        jDateChooser1.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_orderDate.add(jDateChooser1);

        pnl_header.add(pnl_orderDate);

        pnl_searchButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 50, 8, 10));
        pnl_searchButton.setMaximumSize(new java.awt.Dimension(800, 2147483647));
        pnl_searchButton.setPreferredSize(new java.awt.Dimension(80, 100));
        pnl_searchButton.setLayout(new java.awt.BorderLayout());

        btn_search.setText("Tìm kiếm");
        btn_search.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_search.putClientProperty(FlatClientProperties.STYLE, "background: $Menu.background;"
            + "foreground:$Menu.foreground;"
        );
        btn_search.setIcon(SVGIcon.getPrimarySVGIcon("imgs/orderManagement/searchButtonOM.svg"));
        btn_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchActionPerformed(evt);
            }
        });
        pnl_searchButton.add(btn_search, java.awt.BorderLayout.CENTER);

        pnl_header.add(pnl_searchButton);

        pnl_customerFilter.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_customerFilter.setLayout(new javax.swing.BoxLayout(pnl_customerFilter, javax.swing.BoxLayout.LINE_AXIS));

        lbl_customerID.setText("Mã khách hàng: ");
        lbl_customerID.setPreferredSize(new java.awt.Dimension(130, 130));
        pnl_customerFilter.add(lbl_customerID);

        txt_customerID.setMinimumSize(null);
        txt_customerID.setPreferredSize(new java.awt.Dimension(30, 30));
        txt_customerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_customerIDActionPerformed(evt);
            }
        });
        pnl_customerFilter.add(txt_customerID);

        pnl_header.add(pnl_customerFilter);

        pnl_customerPhone.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_customerPhone.setLayout(new javax.swing.BoxLayout(pnl_customerPhone, javax.swing.BoxLayout.LINE_AXIS));

        lbl_customerPhone.setText("Số điện thoại:");
        lbl_customerPhone.setMaximumSize(new java.awt.Dimension(160, 150));
        lbl_customerPhone.setMinimumSize(new java.awt.Dimension(130, 130));
        lbl_customerPhone.setPreferredSize(new java.awt.Dimension(140, 150));
        pnl_customerPhone.add(lbl_customerPhone);

        txt_customerPhone.setMinimumSize(null);
        txt_customerPhone.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_customerPhone.add(txt_customerPhone);

        pnl_header.add(pnl_customerPhone);

        pnl_orderDateTo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_orderDateTo.setMaximumSize(new java.awt.Dimension(200000, 2147483647));
        pnl_orderDateTo.setMinimumSize(new java.awt.Dimension(200, 200));
        pnl_orderDateTo.setPreferredSize(new java.awt.Dimension(300, 43));
        pnl_orderDateTo.setLayout(new javax.swing.BoxLayout(pnl_orderDateTo, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderDateTo.setText("Đến ngày:");
        lbl_orderDateTo.setPreferredSize(new java.awt.Dimension(80, 0));
        pnl_orderDateTo.add(lbl_orderDateTo);

        jDateChooser2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20));
        jDateChooser2.setDateFormatString("dd/MM/yyyy");
        jDateChooser2.setDate(Calendar.getInstance().getTime());
        jDateChooser2.setMinimumSize(new java.awt.Dimension(100, 30));
        jDateChooser2.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_orderDateTo.add(jDateChooser2);

        pnl_header.add(pnl_orderDateTo);

        pnl_refreshButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 50, 8, 10));
        pnl_refreshButton.setMaximumSize(new java.awt.Dimension(800, 2147483647));
        pnl_refreshButton.setPreferredSize(new java.awt.Dimension(80, 100));
        pnl_refreshButton.setLayout(new java.awt.BorderLayout());

        btn_refresh.setText("Làm mới");
        btn_refresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_refresh.setIcon(SVGIcon.getSVGIcon("imgs/orderManagement/refreshButtonOM.svg"));
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });
        pnl_refreshButton.add(btn_refresh, java.awt.BorderLayout.CENTER);

        btn_wfile.setText("Xuất file");
        btn_wfile.setPreferredSize(new java.awt.Dimension(80, 23));
        btn_wfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_wfileActionPerformed(evt);
            }
        });
        pnl_refreshButton.add(btn_wfile, java.awt.BorderLayout.EAST);

        pnl_header.add(pnl_refreshButton);

        add(pnl_header, java.awt.BorderLayout.NORTH);

        pnl_center.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách hoá đơn: "));
        pnl_center.setDoubleBuffered(false);
        pnl_center.setMinimumSize(new java.awt.Dimension(600, 40));
        pnl_center.setLayout(new java.awt.BorderLayout());

        tbl_order.setMinimumSize(new java.awt.Dimension(400, 80));
        jScrollPane1.setViewportView(tbl_order);

        pnl_center.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pnl_cartFooter.setPreferredSize(new java.awt.Dimension(800, 40));

        btn_previous.setText("Trang trước");
        btn_previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_previousActionPerformed(evt);
            }
        });
        pnl_cartFooter.add(btn_previous);

        lbl_pageNumber.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_pageNumber.setText("1/10");
        lbl_pageNumber.setToolTipText("");
        lbl_pageNumber.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        pnl_cartFooter.add(lbl_pageNumber);

        btn_next.setText("Trang tiếp");
        btn_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nextActionPerformed(evt);
            }
        });
        pnl_cartFooter.add(btn_next);

        btn_viewPDF.setText("Xem bản in");
        btn_viewPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_viewPDFActionPerformed(evt);
            }
        });
        pnl_cartFooter.add(btn_viewPDF);

        pnl_center.add(pnl_cartFooter, java.awt.BorderLayout.PAGE_END);

        splitPane.setLeftComponent(pnl_center);

        pnl_infomation.setMinimumSize(new java.awt.Dimension(400, 0));
        pnl_infomation.setPreferredSize(new java.awt.Dimension(500, 768));
        pnl_infomation.setLayout(new java.awt.BorderLayout());

        pnl_orderDetail.setBorder(javax.swing.BorderFactory.createTitledBorder("Chi tiết hoá đơn:"));
        pnl_orderDetail.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setViewportView(tbl_orderDetail);

        pnl_orderDetail.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        pnl_infomation.add(pnl_orderDetail, java.awt.BorderLayout.CENTER);

        pnl_info.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin:"));
        pnl_info.setPreferredSize(new java.awt.Dimension(400, 200));
        pnl_info.setLayout(new javax.swing.BoxLayout(pnl_info, javax.swing.BoxLayout.Y_AXIS));

        pnl_customerName.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 20, 10));
        pnl_customerName.setLayout(new javax.swing.BoxLayout(pnl_customerName, javax.swing.BoxLayout.LINE_AXIS));

        lbl_customerName.setText("Tên khách hàng:");
        lbl_customerName.setPreferredSize(new java.awt.Dimension(120, 30));
        pnl_customerName.add(lbl_customerName);

        txt_customerName.setEditable(false);
        txt_customerName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txt_customerName.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_customerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_customerNameActionPerformed(evt);
            }
        });
        pnl_customerName.add(txt_customerName);

        pnl_info.add(pnl_customerName);

        pnl_phone.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 20, 10));
        pnl_phone.setLayout(new javax.swing.BoxLayout(pnl_phone, javax.swing.BoxLayout.LINE_AXIS));

        lbl_phone.setText("Số điện thoại:");
        lbl_phone.setPreferredSize(new java.awt.Dimension(120, 30));
        pnl_phone.add(lbl_phone);

        txt_phone.setEditable(false);
        txt_phone.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txt_phone.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_phone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_phoneActionPerformed(evt);
            }
        });
        pnl_phone.add(txt_phone);

        pnl_info.add(pnl_phone);

        pnl_total.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 10));
        pnl_total.setLayout(new javax.swing.BoxLayout(pnl_total, javax.swing.BoxLayout.LINE_AXIS));

        lbl_customerName1.setText("Tổng tiền: ");
        lbl_customerName1.setPreferredSize(new java.awt.Dimension(120, 30));
        pnl_total.add(lbl_customerName1);

        txt_total.setEditable(false);
        txt_total.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txt_total.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalActionPerformed(evt);
            }
        });
        pnl_total.add(txt_total);

        pnl_info.add(pnl_total);

        pnl_infomation.add(pnl_info, java.awt.BorderLayout.SOUTH);

        splitPane.setRightComponent(pnl_infomation);

        add(splitPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_customerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_customerNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_customerNameActionPerformed

    private void txt_phoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_phoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_phoneActionPerformed

    private void txt_totalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalActionPerformed

    private void btn_previousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_previousActionPerformed
        this.currentPage--;
        renderCurrentPage();
    }//GEN-LAST:event_btn_previousActionPerformed

    private void btn_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nextActionPerformed
        this.currentPage++;
        renderCurrentPage();
    }//GEN-LAST:event_btn_nextActionPerformed

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refreshActionPerformed

        txt_customerID.setText("");
        txt_customerPhone.setText("");
        txt_orderID.setText("");
        txt_phone.setText("");
        txt_customerName.setText("");
        txt_total.setText("");
        cmb_orderPriceFilter.setSelectedIndex(0);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        jDateChooser1.setDate(cal.getTime());

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        jDateChooser2.setDate(cal.getTime());

        init();
        alterTable();
    }//GEN-LAST:event_btn_refreshActionPerformed

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed
        // TODO add your handling code here:
        txt_phone.setText("");
        txt_customerName.setText("");
        txt_total.setText("");
        if (validateFields()) {
            String priceFrom, priceTo;
            String oderID = txt_orderID.getText();
            String customerID = txt_customerID.getText();
            String phone = txt_customerPhone.getText();
            if (cmb_orderPriceFilter.getSelectedIndex() == 0) {
                priceFrom = "";
                priceTo = "";
            } else if (cmb_orderPriceFilter.getSelectedIndex() == 1) {
                priceFrom = "";
                priceTo = "100000";
            } else if (cmb_orderPriceFilter.getSelectedIndex() == 2) {
                priceFrom = "100000";
                priceTo = "500000";
            } else if (cmb_orderPriceFilter.getSelectedIndex() == 3) {
                priceFrom = "500000";
                priceTo = "1000000";
            } else {
                priceFrom = "1000000";
                priceTo = "";
            }

            Date begin = jDateChooser1.getDate();
            begin.setHours(0);
            begin.setMinutes(0);
            Date end = jDateChooser2.getDate();
            end.setHours(23);
            end.setMinutes(59);
            ArrayList<Order> list = bus.orderListWithFilter(oderID, customerID, phone, priceFrom, priceTo, begin, end);

            renderOrdersTable(list);
        }
    }//GEN-LAST:event_btn_searchActionPerformed

    private void txt_customerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_customerIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_customerIDActionPerformed

    private void btn_wfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_wfileActionPerformed
        // TODO add your handling code here:
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
            createExcel(bus.getAll(), filePath + ".xlsx");
        }
    }//GEN-LAST:event_btn_wfileActionPerformed

    private void btn_viewPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_viewPDFActionPerformed
        viewPDFFile();
    }//GEN-LAST:event_btn_viewPDFActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_previous;
    private javax.swing.JButton btn_refresh;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_viewPDF;
    private javax.swing.JButton btn_wfile;
    private javax.swing.JComboBox<String> cmb_orderPriceFilter;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_customerID;
    private javax.swing.JLabel lbl_customerName;
    private javax.swing.JLabel lbl_customerName1;
    private javax.swing.JLabel lbl_customerPhone;
    private javax.swing.JLabel lbl_orderDate;
    private javax.swing.JLabel lbl_orderDateTo;
    private javax.swing.JLabel lbl_orderID;
    private javax.swing.JLabel lbl_orderStatusFilter;
    private javax.swing.JLabel lbl_pageNumber;
    private javax.swing.JLabel lbl_phone;
    private javax.swing.JPanel pnl_cartFooter;
    private javax.swing.JPanel pnl_center;
    private javax.swing.JPanel pnl_customerFilter;
    private javax.swing.JPanel pnl_customerName;
    private javax.swing.JPanel pnl_customerPhone;
    private javax.swing.JPanel pnl_header;
    private javax.swing.JPanel pnl_info;
    private javax.swing.JPanel pnl_infomation;
    private javax.swing.JPanel pnl_orderDate;
    private javax.swing.JPanel pnl_orderDateTo;
    private javax.swing.JPanel pnl_orderDetail;
    private javax.swing.JPanel pnl_orderFilter;
    private javax.swing.JPanel pnl_orderStatusFilter;
    private javax.swing.JPanel pnl_phone;
    private javax.swing.JPanel pnl_refreshButton;
    private javax.swing.JPanel pnl_searchButton;
    private javax.swing.JPanel pnl_total;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTable tbl_order;
    private javax.swing.JTable tbl_orderDetail;
    private javax.swing.JTextField txt_customerID;
    private javax.swing.JTextField txt_customerName;
    private javax.swing.JTextField txt_customerPhone;
    private javax.swing.JTextField txt_orderID;
    private javax.swing.JTextField txt_phone;
    private javax.swing.JTextField txt_total;
    // End of variables declaration//GEN-END:variables
}

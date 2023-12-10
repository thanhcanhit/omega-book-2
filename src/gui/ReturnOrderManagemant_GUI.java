package gui;

import bus.ReturnOrderManagament_BUS;
import com.formdev.flatlaf.FlatClientProperties;
import entity.ReturnOrder;
import entity.ReturnOrderDetail;
import enums.ReturnOrderStatus;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import raven.toast.Notifications;
import utilities.FormatNumber;
import utilities.ReturnOrderPrinter;
import utilities.SVGIcon;

/**
 *
 * @author Như Tâm
 */
public class ReturnOrderManagemant_GUI extends javax.swing.JPanel {
    
    private ReturnOrderManagament_BUS bus;
    private DefaultTableModel tblModel_returnOrder;
    private DefaultTableModel tblModel_product;
    private ReturnOrder currentReturnOrder;
    private DefaultComboBoxModel cmbModel_statusReturnOrder;
    private DefaultComboBoxModel cmbModel_typeReturnOrder;
    private ArrayList<ReturnOrderDetail> listDetail;
    
    /**
     * Creates new form ReturnOrderManagemant_GUI
     */
    public ReturnOrderManagemant_GUI() {
        initComponents();
        init();
    }
    private void init(){
        bus = new ReturnOrderManagament_BUS();
        //model
        tblModel_returnOrder = new DefaultTableModel(new String[]{"Mã hoá đơn đổi trả", "Mã hoá đơn", "Ngày đổi trả", "Trạng thái"}, 0);
        tbl_inforReturnOrder.setModel(tblModel_returnOrder);
        tblModel_product = new DefaultTableModel(new String[]{"Mã sản phẩm", "Số lượng"}, 0);
        tbl_productInfor.setModel(tblModel_product);
        //combobox
        cmbModel_statusReturnOrder = new DefaultComboBoxModel(new String[]{"Trạng thái", "Đang chờ", "Đã xác nhận", "Đã từ chối"});
        cmb_statusReturnOrder.setModel(cmbModel_statusReturnOrder);
        cmbModel_typeReturnOrder = new DefaultComboBoxModel(new String[]{"Loại", "Đơn đổi", "Đơn trả"});
        cmb_typeReturnOrder.setModel(cmbModel_typeReturnOrder);
        
        tbl_inforReturnOrder.getSelectionModel().addListSelectionListener((e) -> {
            int rowIndex = tbl_inforReturnOrder.getSelectedRow();
            if(rowIndex == -1)
                return;
            
            String returnOrderID = tblModel_returnOrder.getValueAt(rowIndex, 0).toString();
            this.currentReturnOrder = bus.getReturnOrder(returnOrderID);
            this.listDetail = bus.getAllReturnOrderDetail(currentReturnOrder.getReturnOrderID());
            renderCurrentReturnOrder();
        });
        rdb_admit.setSelected(true);
        rdb_exchange.setSelected(true);
        renderReturnOrderTables(bus.getAllReturnOrder());
    }
    
    private void renderCurrentReturnOrder() {
        txt_returnOrderID.setText(currentReturnOrder.getReturnOrderID());
        txt_employeeID.setText(currentReturnOrder.getEmployee().getEmployeeID());
        txt_orderID.setText(currentReturnOrder.getOrder().getOrderID());
        chooseDateReturnOrder.setDate(currentReturnOrder.getOrderDate());
        if(currentReturnOrder.getStatus().getValue() == 1)
            rdb_admit.setSelected(true);
        else if(currentReturnOrder.getStatus().getValue() == 2)
            rdb_deny.setSelected(true);
        else
            group_statusReturnOrder.clearSelection();
        //0-exchange; 1-return
        if(currentReturnOrder.isType())
            rdb_return.setSelected(true);
        else
            rdb_exchange.setSelected(true);
        txt_refund.setText(FormatNumber.toVND(currentReturnOrder.getRefund()));
        txt_reason.setText(currentReturnOrder.getReason());
        renderProductTable(currentReturnOrder.getReturnOrderID());
    }
    private void renderReturnOrderDetail() {
        txt_returnOrderID.setText("");
        txt_employeeID.setText("");
        txt_orderID.setText("");
        txt_searchReturnOrder.setText("");
        rdb_admit.setSelected(true);
        rdb_exchange.setSelected(true);
        tblModel_product.setRowCount(0);
    }
    private void renderProductTable(String returnOrderID) {
        tblModel_product.setRowCount(0);
        ArrayList<ReturnOrderDetail> detailList = bus.getAllReturnOrderDetail(returnOrderID);
        for (ReturnOrderDetail returnOrderDetail : detailList) {
            String[] newRow = {returnOrderDetail.getProduct().getProductID(), returnOrderDetail.getQuantity() + ""};
            tblModel_product.addRow(newRow);
        }
    }
    private void renderReturnOrderTables(ArrayList<ReturnOrder> allReturnOrder) {
        tblModel_returnOrder.setRowCount(0);
        String status;
        for (ReturnOrder returnOrder : allReturnOrder) {
            if(returnOrder.getStatus().getValue() == 0)
                status = "Đang chờ";
            else if(returnOrder.getStatus().getValue() == 1)
                status = "Đã xác nhận";
            else
                status = "Đã từ chối";
            String[] newRow = {returnOrder.getReturnOrderID(), returnOrder.getOrder().getOrderID(), returnOrder.getOrderDate().toString(), status};
            tblModel_returnOrder.addRow(newRow);
        }
    }
    private ReturnOrder getNewValue() {
        int status;
        if(rdb_admit.isSelected())
            status = 1;
        else if(rdb_deny.isSelected())
            status = 2;
        else 
            status = 0;
        currentReturnOrder.setStatus(ReturnOrderStatus.fromInt(status));
        return currentReturnOrder;
    }
    private void updateReturnOrder() {
        ReturnOrder newReturnOrder = getNewValue();
        if(newReturnOrder == null) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Không có đơn đổi trả để xác nhận");
            return;
        }
        if(!rdb_admit.isSelected() & !rdb_deny.isSelected()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng chọn trạng thái xác nhận đơn đổi trả");
            return;
        }
        if(bus.updateReturnOder(newReturnOrder)) {
            if(rdb_admit.isSelected()) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xác nhận đơn đổi trả thành công");
                bus.updateReturnOrderDetail(newReturnOrder, listDetail);
                renderReturnOrderTables(bus.getAllReturnOrder());
                renderReturnOrderDetail();
            }
            else {
                Notifications.getInstance().show(Notifications.Type.INFO, "Đã từ chối đơn đổi trả thành công");
                bus.updateReturnOrderDetail(newReturnOrder, listDetail);
                renderReturnOrderTables(bus.getAllReturnOrder());
                renderReturnOrderDetail();
            }
        }
        else
            Notifications.getInstance().show(Notifications.Type.ERROR, "Cập nhật không thành công");
    }
    private void Print() {
        ReturnOrderPrinter printer = new ReturnOrderPrinter(getNewValue());
        printer.generatePDF();
//        ReturnOrderPrinter.PrintStatus status = printer.printFile();
//        if (status == ReturnOrderPrinter.PrintStatus.NOT_FOUND_FILE) {
//            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi không thể in hóa đơn: Không tìm thấy file");
//        } else if (status == ReturnOrderPrinter.PrintStatus.NOT_FOUND_PRINTER) {
//            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi không thể in hóa đơn: Không tìm thấy máy in");
//        }
    }
 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        group_statusReturnOrder = new javax.swing.ButtonGroup();
        group_typeReturnOrder = new javax.swing.ButtonGroup();
        pnl_topReturnOrder = new javax.swing.JPanel();
        pnl_searchRerturnOrder = new javax.swing.JPanel();
        txt_searchReturnOrder = new javax.swing.JTextField();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        btn_searchReturnOrder = new javax.swing.JButton();
        pnl_filterReturnOrder = new javax.swing.JPanel();
        cmb_statusReturnOrder = new javax.swing.JComboBox<>();
        cmb_typeReturnOrder = new javax.swing.JComboBox<>();
        btn_searchFilterReturnOrder = new javax.swing.JButton();
        btn_refeshReturnOrder = new javax.swing.JButton();
        pnl_centerReturnOrder = new javax.swing.JPanel();
        scr_inforReturnOrder = new javax.swing.JScrollPane();
        tbl_inforReturnOrder = new javax.swing.JTable();
        pnl_eastReturnOrder = new javax.swing.JPanel();
        pnl_employeeID = new javax.swing.JPanel();
        lbl_employeeID = new javax.swing.JLabel();
        txt_employeeID = new javax.swing.JTextField();
        pnl_orderID = new javax.swing.JPanel();
        lbl_orderID = new javax.swing.JLabel();
        txt_orderID = new javax.swing.JTextField();
        pnl_returnOrderID = new javax.swing.JPanel();
        lbl_returnOrderID = new javax.swing.JLabel();
        txt_returnOrderID = new javax.swing.JTextField();
        pnl_dateReturnOrder = new javax.swing.JPanel();
        lbl_dateReturnOrder = new javax.swing.JLabel();
        pnl_chooseDateReturnOrder = new javax.swing.JPanel();
        chooseDateReturnOrder = new com.toedter.calendar.JDateChooser();
        pnl_statusReturnOrder = new javax.swing.JPanel();
        lbl_status = new javax.swing.JLabel();
        pnl_radioStatusReturnOrder = new javax.swing.JPanel();
        rdb_admit = new javax.swing.JRadioButton();
        rdb_deny = new javax.swing.JRadioButton();
        pnl_typeReturnOrder = new javax.swing.JPanel();
        lbl_typeReturnOrder = new javax.swing.JLabel();
        pnl_radioTypeReturnOrder = new javax.swing.JPanel();
        rdb_exchange = new javax.swing.JRadioButton();
        rdb_return = new javax.swing.JRadioButton();
        pnl_productID = new javax.swing.JPanel();
        lbl_product = new javax.swing.JLabel();
        scr_productInfor = new javax.swing.JScrollPane();
        tbl_productInfor = new javax.swing.JTable();
        pnl_refund = new javax.swing.JPanel();
        lbl_refund = new javax.swing.JLabel();
        txt_refund = new javax.swing.JTextField();
        pnl_reason = new javax.swing.JPanel();
        lbl_reason = new javax.swing.JLabel();
        txt_reason = new javax.swing.JTextField();
        pnl_buttonSave = new javax.swing.JPanel();
        btn_saveReturnOrder = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1366, 768));
        setLayout(new java.awt.BorderLayout());

        pnl_topReturnOrder.setPreferredSize(new java.awt.Dimension(1368, 60));
        pnl_topReturnOrder.setLayout(new javax.swing.BoxLayout(pnl_topReturnOrder, javax.swing.BoxLayout.LINE_AXIS));

        pnl_searchRerturnOrder.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_searchRerturnOrder.setLayout(new javax.swing.BoxLayout(pnl_searchRerturnOrder, javax.swing.BoxLayout.X_AXIS));

        txt_searchReturnOrder.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mã đơn đổi trả");
        txt_searchReturnOrder.setPreferredSize(new java.awt.Dimension(500, 22));
        txt_searchReturnOrder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_searchReturnOrderKeyPressed(evt);
            }
        });
        pnl_searchRerturnOrder.add(txt_searchReturnOrder);
        pnl_searchRerturnOrder.add(filler6);

        btn_searchReturnOrder.setText("Tìm kiếm");
        btn_searchReturnOrder.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_searchReturnOrder.setMaximumSize(new java.awt.Dimension(79, 43));
        btn_searchReturnOrder.setPreferredSize(new java.awt.Dimension(90, 33));
        btn_searchReturnOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchReturnOrderActionPerformed(evt);
            }
        });
        pnl_searchRerturnOrder.add(btn_searchReturnOrder);

        pnl_topReturnOrder.add(pnl_searchRerturnOrder);

        pnl_filterReturnOrder.setLayout(new java.awt.GridLayout(1, 0));

        cmb_statusReturnOrder.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Trạng thái", "Đang chờ", "Đã xác nhận", "Đã từ chối" }));
        pnl_filterReturnOrder.add(cmb_statusReturnOrder);

        cmb_typeReturnOrder.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Loại đơn", "Đổi hàng", "Trả hàng" }));
        pnl_filterReturnOrder.add(cmb_typeReturnOrder);

        btn_searchFilterReturnOrder.setText("Lọc");
        btn_searchFilterReturnOrder.setMaximumSize(new java.awt.Dimension(72, 33));
        btn_searchFilterReturnOrder.setPreferredSize(new java.awt.Dimension(72, 33));
        btn_searchFilterReturnOrder.setIcon(SVGIcon.getSVGIcon("imgs/public/filter.svg"));
        btn_searchFilterReturnOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchFilterReturnOrderActionPerformed(evt);
            }
        });
        pnl_filterReturnOrder.add(btn_searchFilterReturnOrder);

        btn_refeshReturnOrder.setIcon(SVGIcon.getSVGIcon("imgs/public/refresh.svg"));
        btn_refeshReturnOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refeshReturnOrderActionPerformed(evt);
            }
        });
        pnl_filterReturnOrder.add(btn_refeshReturnOrder);

        pnl_topReturnOrder.add(pnl_filterReturnOrder);

        add(pnl_topReturnOrder, java.awt.BorderLayout.NORTH);

        pnl_centerReturnOrder.setLayout(new java.awt.BorderLayout());

        tbl_inforReturnOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã HĐĐT", "Mã HĐ", "Ngày đổi", "Trạng thái"
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
        tbl_inforReturnOrder.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr_inforReturnOrder.setViewportView(tbl_inforReturnOrder);

        pnl_centerReturnOrder.add(scr_inforReturnOrder, java.awt.BorderLayout.CENTER);

        add(pnl_centerReturnOrder, java.awt.BorderLayout.CENTER);

        pnl_eastReturnOrder.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết đơn đổi trả", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        pnl_eastReturnOrder.setPreferredSize(new java.awt.Dimension(500, 437));
        pnl_eastReturnOrder.setLayout(new javax.swing.BoxLayout(pnl_eastReturnOrder, javax.swing.BoxLayout.Y_AXIS));

        pnl_employeeID.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_employeeID.setLayout(new javax.swing.BoxLayout(pnl_employeeID, javax.swing.BoxLayout.LINE_AXIS));

        lbl_employeeID.setText("Mã nhân viên:");
        lbl_employeeID.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl_employeeID.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_employeeID.add(lbl_employeeID);

        txt_employeeID.setEditable(false);
        txt_employeeID.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_employeeID.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_employeeID.setPreferredSize(new java.awt.Dimension(64, 40));
        pnl_employeeID.add(txt_employeeID);

        pnl_eastReturnOrder.add(pnl_employeeID);

        pnl_orderID.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_orderID.setLayout(new javax.swing.BoxLayout(pnl_orderID, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderID.setText("Mã hoá đơn:");
        lbl_orderID.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl_orderID.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_orderID.add(lbl_orderID);

        txt_orderID.setEditable(false);
        txt_orderID.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_orderID.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_orderID.setPreferredSize(new java.awt.Dimension(64, 40));
        pnl_orderID.add(txt_orderID);

        pnl_eastReturnOrder.add(pnl_orderID);

        pnl_returnOrderID.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_returnOrderID.setLayout(new javax.swing.BoxLayout(pnl_returnOrderID, javax.swing.BoxLayout.LINE_AXIS));

        lbl_returnOrderID.setText("Mã đơn ĐT:");
        lbl_returnOrderID.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl_returnOrderID.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_returnOrderID.add(lbl_returnOrderID);

        txt_returnOrderID.setEditable(false);
        txt_returnOrderID.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_returnOrderID.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_returnOrderID.setPreferredSize(new java.awt.Dimension(64, 40));
        pnl_returnOrderID.add(txt_returnOrderID);

        pnl_eastReturnOrder.add(pnl_returnOrderID);

        pnl_dateReturnOrder.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_dateReturnOrder.setLayout(new javax.swing.BoxLayout(pnl_dateReturnOrder, javax.swing.BoxLayout.LINE_AXIS));

        lbl_dateReturnOrder.setText("Ngày đổi trả:");
        lbl_dateReturnOrder.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl_dateReturnOrder.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_dateReturnOrder.add(lbl_dateReturnOrder);

        pnl_chooseDateReturnOrder.setMaximumSize(new java.awt.Dimension(32767, 30));
        pnl_chooseDateReturnOrder.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_chooseDateReturnOrder.setLayout(new java.awt.GridLayout(1, 0));
        pnl_chooseDateReturnOrder.add(chooseDateReturnOrder);

        pnl_dateReturnOrder.add(pnl_chooseDateReturnOrder);

        pnl_eastReturnOrder.add(pnl_dateReturnOrder);

        pnl_statusReturnOrder.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_statusReturnOrder.setMaximumSize(new java.awt.Dimension(32833, 40));
        pnl_statusReturnOrder.setMinimumSize(new java.awt.Dimension(135, 30));
        pnl_statusReturnOrder.setPreferredSize(new java.awt.Dimension(290, 40));
        pnl_statusReturnOrder.setLayout(new javax.swing.BoxLayout(pnl_statusReturnOrder, javax.swing.BoxLayout.LINE_AXIS));

        lbl_status.setText("Trạng thái:");
        lbl_status.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl_status.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_statusReturnOrder.add(lbl_status);
        lbl_status.getAccessibleContext().setAccessibleName("Trạng thái");

        pnl_radioStatusReturnOrder.setMaximumSize(new java.awt.Dimension(32767, 30));
        pnl_radioStatusReturnOrder.setPreferredSize(new java.awt.Dimension(219, 30));
        pnl_radioStatusReturnOrder.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        group_statusReturnOrder.add(rdb_admit);
        rdb_admit.setText("Xác nhận");
        pnl_radioStatusReturnOrder.add(rdb_admit);

        group_statusReturnOrder.add(rdb_deny);
        rdb_deny.setText("Từ chối");
        pnl_radioStatusReturnOrder.add(rdb_deny);

        pnl_statusReturnOrder.add(pnl_radioStatusReturnOrder);

        pnl_eastReturnOrder.add(pnl_statusReturnOrder);

        pnl_typeReturnOrder.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_typeReturnOrder.setMaximumSize(new java.awt.Dimension(32833, 40));
        pnl_typeReturnOrder.setMinimumSize(new java.awt.Dimension(135, 30));
        pnl_typeReturnOrder.setPreferredSize(new java.awt.Dimension(290, 40));
        pnl_typeReturnOrder.setLayout(new javax.swing.BoxLayout(pnl_typeReturnOrder, javax.swing.BoxLayout.LINE_AXIS));

        lbl_typeReturnOrder.setText("Loại:");
        lbl_typeReturnOrder.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl_typeReturnOrder.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_typeReturnOrder.add(lbl_typeReturnOrder);

        pnl_radioTypeReturnOrder.setMaximumSize(new java.awt.Dimension(32767, 30));
        pnl_radioTypeReturnOrder.setPreferredSize(new java.awt.Dimension(219, 30));
        pnl_radioTypeReturnOrder.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        group_typeReturnOrder.add(rdb_exchange);
        rdb_exchange.setText("Đơn đổi");
        pnl_radioTypeReturnOrder.add(rdb_exchange);

        group_typeReturnOrder.add(rdb_return);
        rdb_return.setText("Đơn trả");
        pnl_radioTypeReturnOrder.add(rdb_return);

        pnl_typeReturnOrder.add(pnl_radioTypeReturnOrder);

        pnl_eastReturnOrder.add(pnl_typeReturnOrder);

        pnl_productID.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_productID.setPreferredSize(new java.awt.Dimension(462, 260));
        pnl_productID.setLayout(new java.awt.BorderLayout());

        lbl_product.setText("Sản phẩm:");
        lbl_product.setPreferredSize(new java.awt.Dimension(56, 26));
        pnl_productID.add(lbl_product, java.awt.BorderLayout.PAGE_START);

        scr_productInfor.setPreferredSize(new java.awt.Dimension(452, 250));

        tbl_productInfor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Mã SP", "Tên SP"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_productInfor.setMinimumSize(new java.awt.Dimension(30, 40));
        tbl_productInfor.setPreferredSize(new java.awt.Dimension(150, 40));
        tbl_productInfor.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbl_productInfor.setShowGrid(false);
        scr_productInfor.setViewportView(tbl_productInfor);

        pnl_productID.add(scr_productInfor, java.awt.BorderLayout.CENTER);

        pnl_eastReturnOrder.add(pnl_productID);

        pnl_refund.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_refund.setLayout(new javax.swing.BoxLayout(pnl_refund, javax.swing.BoxLayout.LINE_AXIS));

        lbl_refund.setText("Tiền hoàn:");
        lbl_refund.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl_refund.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_refund.add(lbl_refund);

        txt_refund.setEditable(false);
        txt_refund.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_refund.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_refund.setPreferredSize(new java.awt.Dimension(64, 40));
        pnl_refund.add(txt_refund);

        pnl_eastReturnOrder.add(pnl_refund);

        pnl_reason.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_reason.setLayout(new javax.swing.BoxLayout(pnl_reason, javax.swing.BoxLayout.LINE_AXIS));

        lbl_reason.setText("Lý do:");
        lbl_reason.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl_reason.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_reason.add(lbl_reason);

        txt_reason.setEditable(false);
        txt_reason.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_reason.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_reason.setPreferredSize(new java.awt.Dimension(64, 40));
        txt_reason.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_reasonActionPerformed(evt);
            }
        });
        pnl_reason.add(txt_reason);

        pnl_eastReturnOrder.add(pnl_reason);

        pnl_buttonSave.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_buttonSave.setMinimumSize(new java.awt.Dimension(72, 40));
        pnl_buttonSave.setPreferredSize(new java.awt.Dimension(300, 45));
        pnl_buttonSave.setLayout(new java.awt.BorderLayout());

        btn_saveReturnOrder.setText("Lưu");
        btn_saveReturnOrder.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_saveReturnOrder.setPreferredSize(new java.awt.Dimension(300, 40));
        btn_saveReturnOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveReturnOrderActionPerformed(evt);
            }
        });
        pnl_buttonSave.add(btn_saveReturnOrder, java.awt.BorderLayout.CENTER);

        pnl_eastReturnOrder.add(pnl_buttonSave);

        add(pnl_eastReturnOrder, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_saveReturnOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveReturnOrderActionPerformed
        if(currentReturnOrder.getStatus().getValue() != 0) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Đơn đổi trả này đã được xác nhận");
            renderCurrentReturnOrder();
            return;
        }
        updateReturnOrder();
        Print();
    }//GEN-LAST:event_btn_saveReturnOrderActionPerformed

    private void btn_searchReturnOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchReturnOrderActionPerformed
        String returnOrderID = txt_searchReturnOrder.getText();
        if(txt_searchReturnOrder.equals("")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập mã hoá đơn cần tìm");
            return;
        }
        renderReturnOrderTables(bus.searchById(returnOrderID));
        
    }//GEN-LAST:event_btn_searchReturnOrderActionPerformed

    private void btn_searchFilterReturnOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchFilterReturnOrderActionPerformed
        int type = cmb_typeReturnOrder.getSelectedIndex();
        int status = cmb_statusReturnOrder.getSelectedIndex();
        renderReturnOrderTables(bus.filter(type, status));
    }//GEN-LAST:event_btn_searchFilterReturnOrderActionPerformed

    private void btn_refeshReturnOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_refeshReturnOrderActionPerformed
        renderReturnOrderTables(bus.getAllReturnOrder());
        renderReturnOrderDetail();
    }//GEN-LAST:event_btn_refeshReturnOrderActionPerformed

    private void txt_searchReturnOrderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchReturnOrderKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String returnOrderID = txt_searchReturnOrder.getText();
            if(txt_searchReturnOrder.equals("")) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập mã hoá đơn cần tìm");
                return;
            }
            renderReturnOrderTables(bus.searchById(returnOrderID));
        }
    }//GEN-LAST:event_txt_searchReturnOrderKeyPressed

    private void txt_reasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_reasonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_reasonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_refeshReturnOrder;
    private javax.swing.JButton btn_saveReturnOrder;
    private javax.swing.JButton btn_searchFilterReturnOrder;
    private javax.swing.JButton btn_searchReturnOrder;
    private com.toedter.calendar.JDateChooser chooseDateReturnOrder;
    private javax.swing.JComboBox<String> cmb_statusReturnOrder;
    private javax.swing.JComboBox<String> cmb_typeReturnOrder;
    private javax.swing.Box.Filler filler6;
    private javax.swing.ButtonGroup group_statusReturnOrder;
    private javax.swing.ButtonGroup group_typeReturnOrder;
    private javax.swing.JLabel lbl_dateReturnOrder;
    private javax.swing.JLabel lbl_employeeID;
    private javax.swing.JLabel lbl_orderID;
    private javax.swing.JLabel lbl_product;
    private javax.swing.JLabel lbl_reason;
    private javax.swing.JLabel lbl_refund;
    private javax.swing.JLabel lbl_returnOrderID;
    private javax.swing.JLabel lbl_status;
    private javax.swing.JLabel lbl_typeReturnOrder;
    private javax.swing.JPanel pnl_buttonSave;
    private javax.swing.JPanel pnl_centerReturnOrder;
    private javax.swing.JPanel pnl_chooseDateReturnOrder;
    private javax.swing.JPanel pnl_dateReturnOrder;
    private javax.swing.JPanel pnl_eastReturnOrder;
    private javax.swing.JPanel pnl_employeeID;
    private javax.swing.JPanel pnl_filterReturnOrder;
    private javax.swing.JPanel pnl_orderID;
    private javax.swing.JPanel pnl_productID;
    private javax.swing.JPanel pnl_radioStatusReturnOrder;
    private javax.swing.JPanel pnl_radioTypeReturnOrder;
    private javax.swing.JPanel pnl_reason;
    private javax.swing.JPanel pnl_refund;
    private javax.swing.JPanel pnl_returnOrderID;
    private javax.swing.JPanel pnl_searchRerturnOrder;
    private javax.swing.JPanel pnl_statusReturnOrder;
    private javax.swing.JPanel pnl_topReturnOrder;
    private javax.swing.JPanel pnl_typeReturnOrder;
    private javax.swing.JRadioButton rdb_admit;
    private javax.swing.JRadioButton rdb_deny;
    private javax.swing.JRadioButton rdb_exchange;
    private javax.swing.JRadioButton rdb_return;
    private javax.swing.JScrollPane scr_inforReturnOrder;
    private javax.swing.JScrollPane scr_productInfor;
    private javax.swing.JTable tbl_inforReturnOrder;
    private javax.swing.JTable tbl_productInfor;
    private javax.swing.JTextField txt_employeeID;
    private javax.swing.JTextField txt_orderID;
    private javax.swing.JTextField txt_reason;
    private javax.swing.JTextField txt_refund;
    private javax.swing.JTextField txt_returnOrderID;
    private javax.swing.JTextField txt_searchReturnOrder;
    // End of variables declaration//GEN-END:variables

}

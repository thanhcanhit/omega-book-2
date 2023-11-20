/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import bus.PurchaseOrderManagement_BUS;
import com.formdev.flatlaf.FlatClientProperties;
import entity.PurchaseOrder;
import entity.PurchaseOrderDetail;
import enums.PurchaseOrderStatus;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import raven.toast.Notifications;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import main.Application;
import utilities.FormatNumber;

import utilities.SVGIcon;

/**
 *
 * @author KienTran
 */
public final class PurchaseOrderManagement_GUI extends javax.swing.JPanel {

    private PurchaseOrderManagement_BUS bus;

    private DefaultTableModel tblModel_purchaseOrder;
    private DefaultTableModel tblModel_purchaseOrderDetail;

    /**
     * Creates new form PurchaseOrderManagement_GUI
     */
    public PurchaseOrderManagement_GUI() {
        initComponents();
        init();
        alterTable();
    }

    public final void init() {
        bus = new PurchaseOrderManagement_BUS();
        
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rad_decline);
        buttonGroup.add(rad_notReceiver);
        buttonGroup.add(rad_receiver);

        tblModel_purchaseOrder = new DefaultTableModel(new String[]{"Mã đơn nhập", "Nhân viên", "Nhà cung cấp", "Ngày đặt", "Ngày nhận"}, 0);
        tbl_purchaseOrder.setModel(tblModel_purchaseOrder);
        tbl_purchaseOrder.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            int rowIndex = tbl_purchaseOrder.getSelectedRow();
            if (rowIndex != -1) {
                String id = tblModel_purchaseOrder.getValueAt(rowIndex, 0).toString();
                PurchaseOrder purchaseOrder;
                try {
                    purchaseOrder = bus.getPurchaseOrder(id);
                    renderPurchaseOrderDetailTable(bus.getPurchaseOrderDetailList(id));
                    renderInfomationPurchaseOrder(purchaseOrder);

                } catch (Exception ex) {

                }

            }
            return;

        });

        tblModel_purchaseOrderDetail = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá", "Tổng tiền"}, 0);
        tbl_purchaseOrderDetail.setModel(tblModel_purchaseOrderDetail);
        tbl_purchaseOrderDetail.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            int rowIndex = tbl_purchaseOrderDetail.getSelectedRow();
            if (rowIndex == -1) {
                return;
            }
        });
        renderPruchaseOrdersTable(bus.getAll());

    }

    private void renderPruchaseOrdersTable(ArrayList<PurchaseOrder> purchaseOrderList) {
        tblModel_purchaseOrder.setRowCount(0);
        for (PurchaseOrder po : purchaseOrderList) {
            Object[] newRow = new Object[]{po.getPurchaseOrderID(), po.getEmployee().getName(), po.getSupplier().getSupplierID(), po.getOrderDate(), po.getReceiveDate()};
            tblModel_purchaseOrder.addRow(newRow);
        }
    }

    private void renderPurchaseOrderDetailTable(ArrayList<PurchaseOrderDetail> list) {
        tblModel_purchaseOrderDetail.setRowCount(0);
        for (PurchaseOrderDetail pod : list) {

            Object[] newRow = new Object[]{pod.getProduct().getProductID(), pod.getProduct().getName(), pod.getQuantity(), FormatNumber.toVND(pod.getCostPrice()), FormatNumber.toVND(pod.getLineTotal())};

            tblModel_purchaseOrderDetail.addRow(newRow);
        }
    }

    public void renderInfomationPurchaseOrder(PurchaseOrder purchaseOrder) {
        txt_orderDate.setText(purchaseOrder.getOrderDate().toString());
        txt_supplierName.setText(purchaseOrder.getSupplier().getName());
        txa_note.setText(purchaseOrder.getNote());

        txt_subTotal.setText(FormatNumber.toVND(purchaseOrder.getTotal()));

        rad_notReceiver.setSelected(purchaseOrder.getStatus() == PurchaseOrderStatus.PENDING);
        rad_receiver.setSelected(purchaseOrder.getStatus() == PurchaseOrderStatus.SUCCESS);
        rad_decline.setSelected(purchaseOrder.getStatus() == PurchaseOrderStatus.CANCEL);
        if(rad_receiver.isSelected()){
            rad_notReceiver.setEnabled(false);
            rad_decline.setEnabled(false);
            rad_receiver.setEnabled(false);
                    
        }
        if(rad_decline.isSelected()){
            rad_notReceiver.setEnabled(false);
            rad_receiver.setEnabled(false);
            rad_decline.setEnabled(false);
        }
        if(rad_notReceiver.isSelected()){
            rad_notReceiver.setEnabled(false);
            rad_receiver.setEnabled(true);
            rad_decline.setEnabled(true);
        }


    }

    public void alterTable() {
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);

        tbl_purchaseOrder.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tbl_purchaseOrder.getColumnModel().getColumn(0).setPreferredWidth(150);
        tbl_purchaseOrder.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbl_purchaseOrder.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbl_purchaseOrder.getColumnModel().getColumn(3).setPreferredWidth(150);
        tbl_purchaseOrder.getColumnModel().getColumn(4).setPreferredWidth(150);
        tbl_purchaseOrder.getColumnModel().getColumn(4).setCellRenderer(rightAlign);
        tbl_purchaseOrder.setDefaultEditor(Object.class, null);

        tbl_purchaseOrderDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_purchaseOrderDetail.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbl_purchaseOrderDetail.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbl_purchaseOrderDetail.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbl_purchaseOrderDetail.getColumnModel().getColumn(2).setCellRenderer(rightAlign);
        tbl_purchaseOrderDetail.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbl_purchaseOrderDetail.getColumnModel().getColumn(3).setCellRenderer(rightAlign);
        tbl_purchaseOrderDetail.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbl_purchaseOrderDetail.getColumnModel().getColumn(4).setCellRenderer(rightAlign);
        tbl_purchaseOrderDetail.setDefaultEditor(Object.class, null);

    }

    public boolean validateFields() {
        return true;
    }
    private void rerender() {
        Application.showForm(new PurchaseOrderManagement_GUI());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new javax.swing.JSplitPane();
        pnl_right = new javax.swing.JPanel();
        pnl_purchaseOrderDetail = new javax.swing.JPanel();
        scr_orderDetail = new javax.swing.JScrollPane();
        tbl_purchaseOrderDetail = new javax.swing.JTable();
        pnl_purchaseOrderInfo = new javax.swing.JPanel();
        pnl_status = new javax.swing.JPanel();
        lbl_status = new javax.swing.JLabel();
        rad_notReceiver = new javax.swing.JRadioButton();
        rad_receiver = new javax.swing.JRadioButton();
        rad_decline = new javax.swing.JRadioButton();
        pnl_orderDate = new javax.swing.JPanel();
        lbl_status1 = new javax.swing.JLabel();
        txt_orderDate = new javax.swing.JTextField();
        pnl_supplierName = new javax.swing.JPanel();
        lbl_supplierName = new javax.swing.JLabel();
        txt_supplierName = new javax.swing.JTextField();
        pnl_noteLabel = new javax.swing.JPanel();
        lbl_note = new javax.swing.JLabel();
        pnl_note = new javax.swing.JPanel();
        scr_note = new javax.swing.JScrollPane();
        txa_note = new javax.swing.JTextArea();
        pnl_total = new javax.swing.JPanel();
        lbl_subTotal = new javax.swing.JLabel();
        txt_subTotal = new javax.swing.JTextField();
        pnl_control = new javax.swing.JPanel();
        btn_submit = new javax.swing.JButton();
        pnl_center = new javax.swing.JPanel();
        scr_purchaseOrder = new javax.swing.JScrollPane();
        tbl_purchaseOrder = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        pnl_right.setMaximumSize(new java.awt.Dimension(600, 2147483647));
        pnl_right.setMinimumSize(new java.awt.Dimension(500, 399));
        pnl_right.setPreferredSize(new java.awt.Dimension(500, 0));
        pnl_right.setLayout(new java.awt.BorderLayout());

        pnl_purchaseOrderDetail.setBorder(javax.swing.BorderFactory.createTitledBorder("Chi tiết đơn nhập hàng:"));
        pnl_purchaseOrderDetail.setLayout(new java.awt.BorderLayout());

        scr_orderDetail.setViewportView(tbl_purchaseOrderDetail);

        pnl_purchaseOrderDetail.add(scr_orderDetail, java.awt.BorderLayout.CENTER);

        pnl_right.add(pnl_purchaseOrderDetail, java.awt.BorderLayout.CENTER);

        pnl_purchaseOrderInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin:"));
        pnl_purchaseOrderInfo.setPreferredSize(new java.awt.Dimension(500, 400));
        pnl_purchaseOrderInfo.setLayout(new javax.swing.BoxLayout(pnl_purchaseOrderInfo, javax.swing.BoxLayout.Y_AXIS));

        pnl_status.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 5, 10));
        pnl_status.setMaximumSize(new java.awt.Dimension(1000, 50));
        pnl_status.setLayout(new javax.swing.BoxLayout(pnl_status, javax.swing.BoxLayout.LINE_AXIS));

        lbl_status.setText("Trạng thái:");
        lbl_status.setMaximumSize(new java.awt.Dimension(110, 100));
        lbl_status.setPreferredSize(new java.awt.Dimension(110, 0));
        pnl_status.add(lbl_status);

        rad_notReceiver.setText("Chưa nhận");
        rad_notReceiver.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        rad_notReceiver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rad_notReceiver.setIconTextGap(8);
        rad_notReceiver.setSize(new java.awt.Dimension(50, 0));
        pnl_status.add(rad_notReceiver);

        rad_receiver.setText("Đã nhận");
        rad_receiver.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        rad_receiver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rad_receiver.setIconTextGap(8);
        rad_receiver.setSize(new java.awt.Dimension(50, 0));
        rad_receiver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rad_receiverActionPerformed(evt);
            }
        });
        pnl_status.add(rad_receiver);

        rad_decline.setText("Đã huỷ");
        rad_decline.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        rad_decline.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rad_decline.setIconTextGap(8);
        rad_decline.setSize(new java.awt.Dimension(50, 0));
        pnl_status.add(rad_decline);

        pnl_purchaseOrderInfo.add(pnl_status);

        pnl_orderDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 5, 10));
        pnl_orderDate.setMaximumSize(new java.awt.Dimension(1000, 50));
        pnl_orderDate.setLayout(new javax.swing.BoxLayout(pnl_orderDate, javax.swing.BoxLayout.LINE_AXIS));

        lbl_status1.setText("Ngày nhập hàng:");
        lbl_status1.setMaximumSize(new java.awt.Dimension(110, 100));
        lbl_status1.setPreferredSize(new java.awt.Dimension(125, 0));
        pnl_orderDate.add(lbl_status1);

        txt_orderDate.setEditable(false);
        txt_orderDate.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txt_orderDate.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_orderDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_orderDateActionPerformed(evt);
            }
        });
        pnl_orderDate.add(txt_orderDate);

        pnl_purchaseOrderInfo.add(pnl_orderDate);

        pnl_supplierName.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 5, 10));
        pnl_supplierName.setMaximumSize(new java.awt.Dimension(1000, 50));
        pnl_supplierName.setLayout(new javax.swing.BoxLayout(pnl_supplierName, javax.swing.BoxLayout.LINE_AXIS));

        lbl_supplierName.setText("Nhà cung cấp:");
        lbl_supplierName.setMaximumSize(new java.awt.Dimension(110, 100));
        lbl_supplierName.setPreferredSize(new java.awt.Dimension(125, 0));
        pnl_supplierName.add(lbl_supplierName);

        txt_supplierName.setEditable(false);
        txt_supplierName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        txt_supplierName.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_supplierName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_supplierNameActionPerformed(evt);
            }
        });
        pnl_supplierName.add(txt_supplierName);

        pnl_purchaseOrderInfo.add(pnl_supplierName);

        pnl_noteLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_noteLabel.setMaximumSize(new java.awt.Dimension(1000, 30));
        pnl_noteLabel.setMinimumSize(new java.awt.Dimension(49, 5));
        pnl_noteLabel.setPreferredSize(new java.awt.Dimension(49, 40));
        pnl_noteLabel.setLayout(new javax.swing.BoxLayout(pnl_noteLabel, javax.swing.BoxLayout.LINE_AXIS));

        lbl_note.setText("Ghi chú:");
        pnl_noteLabel.add(lbl_note);

        pnl_purchaseOrderInfo.add(pnl_noteLabel);

        pnl_note.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_note.setMaximumSize(new java.awt.Dimension(1000, 100));
        pnl_note.setLayout(new javax.swing.BoxLayout(pnl_note, javax.swing.BoxLayout.LINE_AXIS));

        scr_note.setMinimumSize(new java.awt.Dimension(16, 120));
        scr_note.setPreferredSize(new java.awt.Dimension(244, 120));

        txa_note.setColumns(20);
        txa_note.setRows(5);
        txa_note.setMinimumSize(new java.awt.Dimension(13, 100));
        txa_note.setPreferredSize(new java.awt.Dimension(232, 120));
        scr_note.setViewportView(txa_note);

        pnl_note.add(scr_note);

        pnl_purchaseOrderInfo.add(pnl_note);

        pnl_total.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_total.setMaximumSize(new java.awt.Dimension(1000, 100));
        pnl_total.setPreferredSize(new java.awt.Dimension(199, 50));
        pnl_total.setLayout(new javax.swing.BoxLayout(pnl_total, javax.swing.BoxLayout.LINE_AXIS));

        lbl_subTotal.setText("Tổng tiền:");
        lbl_subTotal.setToolTipText("");
        lbl_subTotal.setMaximumSize(new java.awt.Dimension(110, 100));
        lbl_subTotal.setPreferredSize(new java.awt.Dimension(125, 0));
        pnl_total.add(lbl_subTotal);

        txt_subTotal.setEditable(false);
        txt_subTotal.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_subTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_subTotalActionPerformed(evt);
            }
        });
        pnl_total.add(txt_subTotal);

        pnl_purchaseOrderInfo.add(pnl_total);

        pnl_control.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_control.setMaximumSize(new java.awt.Dimension(1000, 100));
        pnl_control.setLayout(new java.awt.BorderLayout());

        btn_submit.setText("Xác nhận");
        btn_submit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_submit.putClientProperty(FlatClientProperties.STYLE, "background:$Menu.background;"+"foreground:$Menu.foreground;");
        btn_submit.setIcon(SVGIcon.getPrimarySVGIcon("imgs/orderManagement/confirmButtonPOM.svg"));
        btn_submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_submitActionPerformed(evt);
            }
        });
        pnl_control.add(btn_submit, java.awt.BorderLayout.CENTER);

        pnl_purchaseOrderInfo.add(pnl_control);

        pnl_right.add(pnl_purchaseOrderInfo, java.awt.BorderLayout.SOUTH);

        splitPane.setRightComponent(pnl_right);

        pnl_center.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách đơn nhập hàng:"));
        pnl_center.setMinimumSize(new java.awt.Dimension(600, 44));
        pnl_center.setPreferredSize(new java.awt.Dimension(950, 768));
        pnl_center.setLayout(new java.awt.BorderLayout());

        scr_purchaseOrder.setPreferredSize(new java.awt.Dimension(800, 402));

        tbl_purchaseOrder.setSize(new java.awt.Dimension(800, 80));
        scr_purchaseOrder.setViewportView(tbl_purchaseOrder);

        pnl_center.add(scr_purchaseOrder, java.awt.BorderLayout.CENTER);

        splitPane.setLeftComponent(pnl_center);

        add(splitPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_orderDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_orderDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_orderDateActionPerformed

    private void txt_supplierNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_supplierNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_supplierNameActionPerformed

    private void txt_subTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_subTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_subTotalActionPerformed

    private void rad_receiverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rad_receiverActionPerformed

    }//GEN-LAST:event_rad_receiverActionPerformed

    private void btn_submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_submitActionPerformed
        // TODO add your handling code here:
        if (rad_receiver.isSelected() && rad_receiver.isEnabled()) {
            if (JOptionPane.showConfirmDialog(this, "Đơn hàng này đã được nhận?", "Xác nhận đã nhận hàng", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                int row = tbl_purchaseOrder.getSelectedRow();
                if (row != -1) {
                    String ID = tbl_purchaseOrder.getValueAt(row, 0).toString();
                    bus.updateStatus(ID, 1);
                    rad_notReceiver.setSelected(false);
                    rad_decline.setEnabled(true);
                    rad_receiver.setEnabled(false);
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã cập nhật trạng thái thành công !");
                    this.invalidate();
                    this.repaint();
                    rerender();

                }
            } else {
                rad_notReceiver.setSelected(true);
            }
        }
        if (rad_decline.isSelected() && rad_decline.isEnabled()) {
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc sẽ huỷ đơn hàng?", "Xác nhận huỷ đơn hàng", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                int row = tbl_purchaseOrder.getSelectedRow();
                if (row != -1) {
                    String ID = tbl_purchaseOrder.getValueAt(row, 0).toString();
                    bus.updateStatus(ID, 2);
                    rad_notReceiver.setEnabled(false);
                    rad_receiver.setSelected(false);
                    rad_notReceiver.setSelected(false);
                    rad_receiver.setEnabled(false);
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã cập nhật trạng thái thành công !");
                     this.invalidate();
                    this.repaint();
                    rerender();
                }
            }
        }
    }//GEN-LAST:event_btn_submitActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_submit;
    private javax.swing.JLabel lbl_note;
    private javax.swing.JLabel lbl_status;
    private javax.swing.JLabel lbl_status1;
    private javax.swing.JLabel lbl_subTotal;
    private javax.swing.JLabel lbl_supplierName;
    private javax.swing.JPanel pnl_center;
    private javax.swing.JPanel pnl_control;
    private javax.swing.JPanel pnl_note;
    private javax.swing.JPanel pnl_noteLabel;
    private javax.swing.JPanel pnl_orderDate;
    private javax.swing.JPanel pnl_purchaseOrderDetail;
    private javax.swing.JPanel pnl_purchaseOrderInfo;
    private javax.swing.JPanel pnl_right;
    private javax.swing.JPanel pnl_status;
    private javax.swing.JPanel pnl_supplierName;
    private javax.swing.JPanel pnl_total;
    private javax.swing.JRadioButton rad_decline;
    private javax.swing.JRadioButton rad_notReceiver;
    private javax.swing.JRadioButton rad_receiver;
    private javax.swing.JScrollPane scr_note;
    private javax.swing.JScrollPane scr_orderDetail;
    private javax.swing.JScrollPane scr_purchaseOrder;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTable tbl_purchaseOrder;
    private javax.swing.JTable tbl_purchaseOrderDetail;
    private javax.swing.JTextArea txa_note;
    private javax.swing.JTextField txt_orderDate;
    private javax.swing.JTextField txt_subTotal;
    private javax.swing.JTextField txt_supplierName;
    // End of variables declaration//GEN-END:variables
}

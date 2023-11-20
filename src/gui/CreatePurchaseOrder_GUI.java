/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import bus.CreatePurchaseOrder_BUS;
import com.formdev.flatlaf.FlatClientProperties;
import entity.Employee;
import entity.Product;
import entity.PurchaseOrder;
import entity.PurchaseOrderDetail;
import entity.Supplier;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import main.Application;
import raven.toast.Notifications;

/**
 *
 * @author thanhcanhit
 */
public class CreatePurchaseOrder_GUI extends javax.swing.JPanel {

    /**
     * Creates new form Sales_GUI
     */
    private CreatePurchaseOrder_BUS bus;

    //
    private PurchaseOrder purchaseOrder;
    private ArrayList<PurchaseOrderDetail> cart;
    private DefaultTableModel tblModel_cart;
    private DefaultComboBoxModel cmbModel_suplier;

    public CreatePurchaseOrder_GUI() {
        initComponents();
        init();
    }

    private void init() {
        bus = new CreatePurchaseOrder_BUS();
        try {
            purchaseOrder = bus.createNewPurchaseOrder();
            txt_orderID.setText(purchaseOrder.getPurchaseOrderID());
            txt_orderID.setEditable(false);
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 5000, "Không thể tạo hóa đơn mới, vui lòng thử lại lúc khác");
        }

//        table
        cart = new ArrayList<>();
        tblModel_cart = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá nhập", "Thành tiền"}, 50) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 2) {
                    return true;
                }

                return false;
            }

        };;
        tbl_cart.setModel(tblModel_cart);

//        Sự thay đổi số lượng sản phẩm
        tbl_cart.getModel().addTableModelListener((TableModelEvent evt) -> {
            int row = evt.getFirstRow();
            int col = evt.getColumn();
//              Không xử lí nếu row hoặc col = -1 và col không phải là ô nhập số lượng
            if (row == -1 || col == -1 && col != 2) {
                return;
            }

            int newValue = Integer.parseInt(tblModel_cart.getValueAt(row, col).toString());
            PurchaseOrderDetail current = cart.get(row);

//            Nếu số lượng mới bằng 0 thì xóa khỏi giỏ hàng
            if (newValue == 0 && JOptionPane.showConfirmDialog(this, "Xóa sản phẩm " + current.getProduct().getProductID() + " ra khỏi giỏ hàng", "Xóa sản phẩm khỏi giỏ", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                cart.remove(current);
                renderCartTable();
                return;
            }

            try {
                current.setQuantity(newValue);
                renderCartTable();
//                if (current.getProduct().getInventory() >= newValue) {
//                } else {
////                    Trả về giá trị cũ
//                    tbl_cart.setValueAt(current.getQuantity(), row, col);
//                    Notifications.getInstance().show(Notifications.Type.ERROR, "Số lượng sản phẩm không đủ!");
//                }

//                Focus lại ô search
                toogleChangeToSearch();
            } catch (Exception ex) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể cập nhật số lượng mới!");
            }
        });
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);

//        Align
        TableColumnModel columnModel = tbl_cart.getColumnModel();
        for (int index : new Integer[]{2, 3, 4}) {
            columnModel.getColumn(index).setCellRenderer(rightAlign);
        }
        renderCartTable();

//        form
        txt_orderDate.setEditable(false);
        txt_orderDate.setText(LocalDate.now().toString());
        renderSuplier();
    }

    private void renderCartTable() {
        tblModel_cart.setRowCount(0);

        double total = 0.0;
        for (PurchaseOrderDetail item : cart) {
            Object[] newRow = new Object[]{item.getProduct().getProductID(), item.getProduct().getName(), item.getQuantity(), item.getCostPrice(), item.getLineTotal()};
            total += item.getLineTotal();
            tblModel_cart.addRow(newRow);
        }

        lbl_total.setText("Tổng tiền: " + utilities.FormatNumber.toVND(total));
    }

    private void renderSuplier() {
        Object[] items = bus.getAllSuplier().stream().map((suplier) -> String.format("(%s) %s", suplier.getSupplierID(), suplier.getName())).toArray();
        cmbModel_suplier = new DefaultComboBoxModel(items);
        cmb_suplier.setModel(cmbModel_suplier);
    }

    private String getSuplierID() {
        Pattern pattern = Pattern.compile("\\(([^\\)]+)\\)");
        Matcher matcher = pattern.matcher(cmbModel_suplier.getSelectedItem().toString());

        if (matcher.find()) {
            String id = matcher.group(0);
            id = id.replaceAll("\\(", "");
            id = id.replaceAll("\\)", "");
            return id;
        }
        return null;
    }

    private void toggleChangeQuantity() {
        txt_search.setText("");
        int row = cart.size() - 1;
        tbl_cart.requestFocus();
        tbl_cart.changeSelection(row, 2, false, false);
        tbl_cart.setColumnSelectionInterval(2, 2);
        tbl_cart.setRowSelectionInterval(row, row);
        tbl_cart.editCellAt(row, 2);
    }

    private void toogleChangeToSearch() {
        txt_search.requestFocus();
    }

    private void rerender() {
        Application.showForm(new CreatePurchaseOrder_GUI());
        toogleChangeToSearch();
    }

    private void addItemToCart(String productID) {
//        Nếu chưa có trong giỏ hàng
        Product item = bus.getProduct(productID);
        if (item == null) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không tìm thấy sản phẩm có mã " + productID);
        } else {
            try {
//                Thêm vào giỏ hàng
                PurchaseOrderDetail newLine = new PurchaseOrderDetail(this.purchaseOrder, item, 1, item.getCostPrice());
                cart.add(newLine);
                renderCartTable();
                toggleChangeQuantity();
            } catch (Exception ex) {
                ex.printStackTrace();
                Notifications.getInstance().show(Notifications.Type.ERROR, "Có lỗi xảy ra khi thêm sản phẩm " + productID);
            }
        }
    }

    private void increateItemInCart(PurchaseOrderDetail detail) {
        try {
            detail.setQuantity(detail.getQuantity() + 1);
            renderCartTable();
//            if (detail.getProduct().getInventory() > detail.getQuantity()) {
//            } else {
//                Notifications.getInstance().show(Notifications.Type.ERROR, "Số lượng sản phẩm không đủ!");
//            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể tăng số lượng: " + e.getMessage());
        }

    }

    private void handleAddItem() {
        String productID = txt_search.getText();

        //  Nếu chưa điền mã sẽ cảnh báo
        if (productID.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng điền mã sản phẩm");
            return;
        }

//        Kiểm tra xem trong giỏ hàng đã có sản phẩm đó hay chưa
        for (PurchaseOrderDetail detail : cart) {
//                Nếu tìm thấy thì tăng số lượng lên 1 và thoát
            if (detail.getProduct().getProductID().equals(productID)) {
                increateItemInCart(detail);
                return;
            }
        }

//       Nếu chưa có thì thêm mới vào cart 
        addItemToCart(productID);
    }

    private void handleCreateOrder() {
        //         Nếu chưa có sản phẩm sẽ cảnh báo
        if (cart.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Bạn chưa thêm sản phẩm vào danh sách!");
            return;
        }

        try {
            Notifications.getInstance().show(Notifications.Type.INFO, "Đang lưu trữ hóa đơn...");
            purchaseOrder.setSupplier(new Supplier(getSuplierID()));
//            Temp
            purchaseOrder.setEmployee(new Employee("NV019982020001"));
            purchaseOrder.setPurchaseOrderDetailList(cart);
            purchaseOrder.setNote(txa_description.getText());
//            Để tạm để xử lí sau
            purchaseOrder.setReceiveDate(java.sql.Date.valueOf(LocalDate.now()));

            boolean isSaved = bus.saveToDatabase(purchaseOrder);

            if (isSaved) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã tạo thành công đơn nhập " + purchaseOrder.getPurchaseOrderID());
//                Rerender panel
                rerender();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Có lỗi xảy ra khi lưu đơn nhập vào cơ sở dữ liệu" + purchaseOrder.getPurchaseOrderID());
            }
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể tạo đơn nhập " + purchaseOrder.getPurchaseOrderID() + ": " + ex.getMessage());
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

        splitPane_main = new javax.swing.JSplitPane();
        pnl_left = new javax.swing.JPanel();
        pnl_header = new javax.swing.JPanel();
        txt_search = new javax.swing.JTextField();
        btn_search = new javax.swing.JButton();
        pnl_cart = new javax.swing.JPanel();
        scr_cart = new javax.swing.JScrollPane();
        tbl_cart = new javax.swing.JTable();
        pnl_cartFooter = new javax.swing.JPanel();
        lbl_total = new javax.swing.JLabel();
        pnl_right = new javax.swing.JPanel();
        pnl_info = new javax.swing.JPanel();
        pnl_orderInfo = new javax.swing.JPanel();
        pnl_orderID = new javax.swing.JPanel();
        lbl_orderID = new javax.swing.JLabel();
        txt_orderID = new javax.swing.JTextField();
        pnl_orderPaymentMethod = new javax.swing.JPanel();
        lbl_suplier = new javax.swing.JLabel();
        cmb_suplier = new javax.swing.JComboBox<>();
        pnl_orderDate = new javax.swing.JPanel();
        lbl_orderDate = new javax.swing.JLabel();
        txt_orderDate = new javax.swing.JTextField();
        pnl_orderCustomerGive = new javax.swing.JPanel();
        pnl_container = new javax.swing.JPanel();
        lbl_description = new javax.swing.JLabel();
        scr_description = new javax.swing.JScrollPane();
        txa_description = new javax.swing.JTextArea();
        pnl_btnGroup = new javax.swing.JPanel();
        btn_cancle = new javax.swing.JButton();
        btn_create = new javax.swing.JButton();

        setLayout(new java.awt.GridLayout(1, 0));

        splitPane_main.setResizeWeight(0.7);
        splitPane_main.setMinimumSize(new java.awt.Dimension(1305, 768));

        pnl_left.setMinimumSize(new java.awt.Dimension(700, 59));
        pnl_left.setPreferredSize(new java.awt.Dimension(900, 768));
        pnl_left.setLayout(new java.awt.BorderLayout());

        pnl_header.setPreferredSize(new java.awt.Dimension(1366, 50));
        pnl_header.setLayout(new javax.swing.BoxLayout(pnl_header, javax.swing.BoxLayout.LINE_AXIS));

        txt_search.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mã sản phẩm");
        txt_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_searchKeyPressed(evt);
            }
        });
        pnl_header.add(txt_search);

        btn_search.setText("Thêm");
        btn_search.setMaximumSize(new java.awt.Dimension(100, 50));
        btn_search.setMinimumSize(new java.awt.Dimension(100, 50));
        btn_search.setPreferredSize(new java.awt.Dimension(100, 50));
        btn_search.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchActionPerformed(evt);
            }
        });
        pnl_header.add(btn_search);

        pnl_left.add(pnl_header, java.awt.BorderLayout.NORTH);

        pnl_cart.setLayout(new java.awt.BorderLayout());

        scr_cart.setViewportView(tbl_cart);

        pnl_cart.add(scr_cart, java.awt.BorderLayout.CENTER);

        pnl_cartFooter.setPreferredSize(new java.awt.Dimension(800, 60));
        pnl_cartFooter.setLayout(new java.awt.BorderLayout());

        lbl_total.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_total.setText("Tổng tiền");
        lbl_total.setToolTipText("");
        lbl_total.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        pnl_cartFooter.add(lbl_total, java.awt.BorderLayout.CENTER);

        pnl_cart.add(pnl_cartFooter, java.awt.BorderLayout.PAGE_END);

        pnl_left.add(pnl_cart, java.awt.BorderLayout.CENTER);

        splitPane_main.setLeftComponent(pnl_left);

        pnl_right.setPreferredSize(new java.awt.Dimension(400, 768));
        pnl_right.setLayout(new java.awt.BorderLayout());

        pnl_info.setLayout(new javax.swing.BoxLayout(pnl_info, javax.swing.BoxLayout.Y_AXIS));

        pnl_orderInfo.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin đơn nhập"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnl_orderInfo.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        pnl_orderInfo.setPreferredSize(new java.awt.Dimension(500, 400));
        pnl_orderInfo.setLayout(new javax.swing.BoxLayout(pnl_orderInfo, javax.swing.BoxLayout.Y_AXIS));

        pnl_orderID.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_orderID.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_orderID.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_orderID.setLayout(new javax.swing.BoxLayout(pnl_orderID, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderID.setText("Mã hóa đơn");
        lbl_orderID.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_orderID.add(lbl_orderID);

        txt_orderID.setPreferredSize(new java.awt.Dimension(64, 40));
        pnl_orderID.add(txt_orderID);

        pnl_orderInfo.add(pnl_orderID);

        pnl_orderPaymentMethod.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_orderPaymentMethod.setMaximumSize(new java.awt.Dimension(32836, 40));
        pnl_orderPaymentMethod.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_orderPaymentMethod.setLayout(new javax.swing.BoxLayout(pnl_orderPaymentMethod, javax.swing.BoxLayout.LINE_AXIS));

        lbl_suplier.setText("Nhà cung cấp");
        lbl_suplier.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_orderPaymentMethod.add(lbl_suplier);

        cmb_suplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt", "ATM" }));
        pnl_orderPaymentMethod.add(cmb_suplier);

        pnl_orderInfo.add(pnl_orderPaymentMethod);

        pnl_orderDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_orderDate.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_orderDate.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_orderDate.setLayout(new javax.swing.BoxLayout(pnl_orderDate, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderDate.setText("Ngày tạo");
        lbl_orderDate.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_orderDate.add(lbl_orderDate);

        txt_orderDate.setPreferredSize(new java.awt.Dimension(64, 40));
        txt_orderDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_orderDateActionPerformed(evt);
            }
        });
        pnl_orderDate.add(txt_orderDate);

        pnl_orderInfo.add(pnl_orderDate);

        pnl_orderCustomerGive.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_orderCustomerGive.setMaximumSize(new java.awt.Dimension(32767, 120));
        pnl_orderCustomerGive.setPreferredSize(new java.awt.Dimension(561, 100));
        pnl_orderCustomerGive.setLayout(new javax.swing.BoxLayout(pnl_orderCustomerGive, javax.swing.BoxLayout.Y_AXIS));

        pnl_container.setLayout(new java.awt.GridLayout(1, 0));

        lbl_description.setText("Lưu ý");
        lbl_description.setToolTipText("");
        lbl_description.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_container.add(lbl_description);

        pnl_orderCustomerGive.add(pnl_container);

        txa_description.setColumns(20);
        txa_description.setRows(10);
        txa_description.setTabSize(4);
        txa_description.setWrapStyleWord(true);
        txa_description.setMinimumSize(new java.awt.Dimension(13, 200));
        txa_description.setPreferredSize(new java.awt.Dimension(232, 200));
        scr_description.setViewportView(txa_description);

        pnl_orderCustomerGive.add(scr_description);

        pnl_orderInfo.add(pnl_orderCustomerGive);

        pnl_info.add(pnl_orderInfo);

        pnl_right.add(pnl_info, java.awt.BorderLayout.CENTER);

        pnl_btnGroup.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_btnGroup.setPreferredSize(new java.awt.Dimension(281, 60));
        pnl_btnGroup.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        btn_cancle.setText("HỦY");
        btn_cancle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancleActionPerformed(evt);
            }
        });
        pnl_btnGroup.add(btn_cancle);

        btn_create.setText("TẠO ĐƠN NHẬP");
        btn_create.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_createActionPerformed(evt);
            }
        });
        pnl_btnGroup.add(btn_create);

        pnl_right.add(pnl_btnGroup, java.awt.BorderLayout.SOUTH);

        splitPane_main.setRightComponent(pnl_right);

        add(splitPane_main);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_orderDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_orderDateActionPerformed

    }//GEN-LAST:event_txt_orderDateActionPerformed

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed
        handleAddItem();
    }//GEN-LAST:event_btn_searchActionPerformed

    private void btn_createActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_createActionPerformed
        handleCreateOrder();
    }//GEN-LAST:event_btn_createActionPerformed

    private void txt_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchKeyPressed
//        Bắt sự kiện bấm enter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            handleAddItem();
        }
    }//GEN-LAST:event_txt_searchKeyPressed

    private void btn_cancleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancleActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Bạn có muốn hủy hóa đơn " + purchaseOrder.getPurchaseOrderID(), "Xác nhận hủy hóa đơn", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // Tạo lại trang mới
            rerender();
            Notifications.getInstance().show(Notifications.Type.INFO, "Đã hủy thành công hóa đơn");
        }

    }//GEN-LAST:event_btn_cancleActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancle;
    private javax.swing.JButton btn_create;
    private javax.swing.JButton btn_search;
    private javax.swing.JComboBox<String> cmb_suplier;
    private javax.swing.JLabel lbl_description;
    private javax.swing.JLabel lbl_orderDate;
    private javax.swing.JLabel lbl_orderID;
    private javax.swing.JLabel lbl_suplier;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JPanel pnl_btnGroup;
    private javax.swing.JPanel pnl_cart;
    private javax.swing.JPanel pnl_cartFooter;
    private javax.swing.JPanel pnl_container;
    private javax.swing.JPanel pnl_header;
    private javax.swing.JPanel pnl_info;
    private javax.swing.JPanel pnl_left;
    private javax.swing.JPanel pnl_orderCustomerGive;
    private javax.swing.JPanel pnl_orderDate;
    private javax.swing.JPanel pnl_orderID;
    private javax.swing.JPanel pnl_orderInfo;
    private javax.swing.JPanel pnl_orderPaymentMethod;
    private javax.swing.JPanel pnl_right;
    private javax.swing.JScrollPane scr_cart;
    private javax.swing.JScrollPane scr_description;
    private javax.swing.JSplitPane splitPane_main;
    private javax.swing.JTable tbl_cart;
    private javax.swing.JTextArea txa_description;
    private javax.swing.JTextField txt_orderDate;
    private javax.swing.JTextField txt_orderID;
    private javax.swing.JTextField txt_search;
    // End of variables declaration//GEN-END:variables
}

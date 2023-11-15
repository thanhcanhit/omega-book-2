/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import database.ConnectDB;
import entity.Order;
import entity.OrderDetail;

/**
 *
 * @author thanhcanhit
 */
public final class PreviewOrder_GUI extends javax.swing.JFrame {

    private final Order order;
    private String content = "";
    private int width = 80;
    private String separateLine = "";
    private final String lineDetailFormat;
    private final String orderDetailFormat;

    /**
     * Creates new form PreviewOrder_GUI
     *
     * @param order
     * @param width
     */
    public PreviewOrder_GUI(Order order, int width) {
        initComponents();
        this.order = order;
        this.width = width;
        setTitle("Hóa đơn " + order.getOrderID());
        setLocationRelativeTo(null);

//        Sinh đoạn chia phù hợp với chiều rộng
        for (int i = 0; i < width; i++) {
            separateLine += "-";
        }

//        Sinh đoạn chia kích thước phù hợp  (4 cột) tỉ lệ 1 3 3 3
        int part = Math.round((width - 4) / 10);
        lineDetailFormat = String.format("  %%-%ss%%-%ss%%-%ss%%-%ss", part, part * 3, part * 3, part * 3);
        orderDetailFormat = String.format("%%-%ss%%-%ss", part * 6, part * 4);

        renderOrderContent();
    }

    private void addLine(String line) {
        content += String.format("  %s  \n", line);
    }

    @Deprecated
    private void addParagraph(String line) {
        while (line.length() > 0) {
            int endIndex = line.length() - 1 < width - 4 ? line.length() - 1 : width - 4;
            String subLine = line.substring(0, endIndex);
            line = line.substring(endIndex, line.length() - 1);

            addLine(subLine);
        }
    }

    private void addCenterLine(String line) {
        int index = (width + line.length()) / 2;
        String format = "%" + index + "s\n";
        System.out.println(format);
        content += String.format(format, line);
    }

    private void separateLine() {
        content += separateLine + "\n";
    }

    private void newLine() {
        content += "\n";
    }

    private String getVND(double value) {
        return utilities.FormatNumber.toVND(value);
    }

    private void addOrderLine(OrderDetail detail, int index) {
        String productName = detail.getProduct().getName();
        String displayName = productName.length() > width - 4 ? productName.substring(0, width - 7) + "..." : productName;
        addLine(index + ". " + displayName);
        boolean isHasDiscout = detail.getSeasonalDiscount() != 0;
        addLine(String.format(lineDetailFormat, String.format("%.0f%%", detail.getVAT()), getVND(detail.getPrice()), detail.getQuantity(), getVND(detail.getLineTotal())));
        if (isHasDiscout) {
            double pricePerItemAfterDiscount = detail.getPrice() - detail.getSeasonalDiscount() / detail.getQuantity();
            addLine(String.format("  (Giá giảm: %s, tổng giảm %s)", getVND(pricePerItemAfterDiscount), getVND(detail.getSeasonalDiscount())));
        }
    }

    public void renderOrderContent() {
        addCenterLine("OMEGA BOOK");
        addCenterLine("Hãy nghĩ sách là một loại vitamin");
        separateLine();
        addCenterLine("HÓA ĐƠN THANH TOÁN");
        newLine();
        addLine(String.format("Số hóa đơn:  %s", order.getOrderID()));
        addLine(String.format("Ngày tạo:  %s", order.getOrderAt()));
        addLine(String.format("Nhân viên:  %s", order.getEmployee().getName()));
        addLine(String.format("Khách hàng:  %s", order.getCustomer().getName()));
        separateLine();
        addLine(String.format(lineDetailFormat, "VAT", "Giá", "Số lượng", "Tổng tiền"));
        separateLine();
        int index = 0;
        for (OrderDetail item : order.getOrderDetail()) {
            addOrderLine(item, ++index);
        }
        separateLine();
        addLine(String.format(orderDetailFormat, "Tổng tiền:", getVND(order.getSubTotal())));
        String orderDiscount = getVND(order.getSubTotal() - order.getTotalDue());
        addLine(String.format(orderDetailFormat, "Chiết khấu đơn:", orderDiscount));
        addLine(String.format(orderDetailFormat, "Thanh toán:", getVND(order.getTotalDue())));
        separateLine();
        boolean isATMPayment = order.getMoneyGiven() == order.getTotalDue();
        if (isATMPayment) {
            addLine(String.format(orderDetailFormat, "Thanh toán:", "Thanh toán bằng thẻ"));
        } else {
            addLine(String.format(orderDetailFormat, "Tiền mặt:", getVND(order.getMoneyGiven())));
            addLine(String.format(orderDetailFormat, "Tiền trả lại:", getVND(order.getMoneyGiven() - order.getTotalDue())));
        }

        txa_orderContent.setText(content);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scr_display = new javax.swing.JScrollPane();
        txa_orderContent = new javax.swing.JTextArea();
        pnl_control = new javax.swing.JPanel();
        btn_close = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setMaximumSize(new java.awt.Dimension(435, 800));
        setMinimumSize(new java.awt.Dimension(435, 800));
        setPreferredSize(new java.awt.Dimension(435, 800));

        scr_display.setToolTipText("");
        scr_display.setMaximumSize(new java.awt.Dimension(450, 600));

        txa_orderContent.setColumns(20);
        txa_orderContent.setFont(txa_orderContent.getFont());
        txa_orderContent.setRows(5);
        txa_orderContent.setTabSize(4);
        scr_display.setViewportView(txa_orderContent);

        getContentPane().add(scr_display, java.awt.BorderLayout.CENTER);

        pnl_control.setMinimumSize(new java.awt.Dimension(78, 100));
        pnl_control.setLayout(new java.awt.GridLayout(1, 0));

        btn_close.setText("Xác nhận");
        btn_close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_closeActionPerformed(evt);
            }
        });
        pnl_control.add(btn_close);

        getContentPane().add(pnl_control, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_closeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_closeActionPerformed
        this.dispose();
    }//GEN-LAST:event_btn_closeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_close;
    private javax.swing.JPanel pnl_control;
    private javax.swing.JScrollPane scr_display;
    private javax.swing.JTextArea txa_orderContent;
    // End of variables declaration//GEN-END:variables
}

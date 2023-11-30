/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import bus.StatisticProduct_BUS;
import com.itextpdf.text.log.Logger;
import gui.customchart.ModelChart;
import entity.Product;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.lang.System.Logger.Level;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;

import javax.swing.JTable;

import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.knowm.xchart.*;
import utilities.FormatNumber;

/**
 *
 * @author KienTran
 */
public final class StatisticProduct_GUI extends javax.swing.JPanel {

    private DefaultTableModel tblModel_product;
    private StatisticProduct_BUS bus;

    private XChartPanel<CategoryChart> chartPanel;

    /**
     * Creates new form StatisticProduct_GUI
     */
    public StatisticProduct_GUI() {
        initComponents();
        init();
        alterTable();

    }

    public void init() {
        bus = new StatisticProduct_BUS();
        tblModel_product = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Giá tiền", "Tổng doanh thu"
        }, 0);
        tbl_topProduct.setModel(tblModel_product);
        tbl_topProduct.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            int rowIndex = tbl_topProduct.getSelectedRow();
            if (rowIndex != -1) {
                String id = tblModel_product.getValueAt(rowIndex, 0).toString();
                Product product;
                try {

                } catch (Exception ex) {

                }

            }
            return;

        });

        Date date = date_statisticProduct.getDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = format.format(date);
        chart.setBackground(getBackground());
        chart.addLegend("Sản phẩm", new Color(71, 118, 185));
        getChart(formatDate);
        renderProductTable(bus.getTopProductInDay(formatDate), formatDate);

        date_statisticProduct.addPropertyChangeListener((PropertyChangeEvent e) -> {
            if ("date".equals(e.getPropertyName())) {
                Date selectedDate = (Date) e.getNewValue();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(selectedDate);
                renderProductTable(bus.getTopProductInDay(formattedDate), formattedDate);
                tbl_topProduct.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
                    int rowIndex = tbl_topProduct.getSelectedRow();
                    if (rowIndex != -1) {
                        String id = tblModel_product.getValueAt(rowIndex, 0).toString();
                        Product p;
                        try {
                            p = bus.getProduct(id);
                            renderInfoProduct(p, formattedDate);
                        } catch (Exception ex) {
                        }

                    }
                    return;

                });
                getChart(formattedDate);

            }
        });

    }

    private void renderProductTable(ArrayList<Product> productList, String date) {
        tblModel_product.setRowCount(0);
        for (Product p : productList) {
            if (p != null) {
                Object[] newRow = new Object[]{p.getProductID(), p.getName(), bus.getQuantitySale(p.getProductID(), date), FormatNumber.toVND(p.getPrice()), FormatNumber.toVND(bus.getTotalProduct(p.getProductID(), date))};
                tblModel_product.addRow(newRow);
            }

        }
    }

    public void alterTable() {
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);

        tbl_topProduct.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tbl_topProduct.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbl_topProduct.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbl_topProduct.getColumnModel().getColumn(2).setPreferredWidth(80);

        tbl_topProduct.getColumnModel().getColumn(2).setCellRenderer(rightAlign);
        tbl_topProduct.getColumnModel().getColumn(3).setPreferredWidth(100);

        tbl_topProduct.getColumnModel().getColumn(3).setCellRenderer(rightAlign);
        tbl_topProduct.getColumnModel().getColumn(4).setPreferredWidth(150);
        tbl_topProduct.getColumnModel().getColumn(4).setCellRenderer(rightAlign);
        tbl_topProduct.setDefaultEditor(Object.class, null);
    }

    public void getChart(String date) {
        chart.clear();
        ArrayList<Product> list = bus.getTop10Product(date);
        for (Product p : list) {
            if (p != null) {
                chart.addData(new ModelChart(p.getProductID(), new double[]{bus.getTotalProduct(p.getProductID(), date)}));
            }
        }

        chart.start();
    }

    public void renderInfoProduct(Product p, String date) {
        txt_productName.setText(p.getName());
        txt_price.setText(FormatNumber.toVND(p.getPrice()));
        txt_productID.setText(p.getProductID());
        txt_productType.setText(p.getType().toString());
        txt_quantity.setText(bus.getQuantitySale(p.getProductID(), date) + "");
        txt_total.setText(FormatNumber.toVND(bus.getTotalProduct(p.getProductID(), date)));
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
        pnl_control = new javax.swing.JPanel();
        filler26 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        date_statisticProduct = new com.toedter.calendar.JDateChooser();
        pnl_tableTopProduct = new javax.swing.JPanel();
        scr_tableProduct = new javax.swing.JScrollPane();
        tbl_topProduct = new javax.swing.JTable();
        pnl_infomation = new javax.swing.JPanel();
        filler21 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 6), new java.awt.Dimension(32767, 10));
        pnl_productID = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        lbl_productID = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        txt_productID = new javax.swing.JTextField();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler20 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 6), new java.awt.Dimension(32767, 10));
        pnl_productName = new javax.swing.JPanel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        lbl_productIName = new javax.swing.JLabel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        txt_productName = new javax.swing.JTextField();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler19 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 6), new java.awt.Dimension(32767, 10));
        pnl_productType = new javax.swing.JPanel();
        filler22 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        lbl_productIType = new javax.swing.JLabel();
        filler23 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        txt_productType = new javax.swing.JTextField();
        filler24 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler25 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 6), new java.awt.Dimension(32767, 10));
        pnl_price = new javax.swing.JPanel();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        lbl_price = new javax.swing.JLabel();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        txt_price = new javax.swing.JTextField();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler18 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 6), new java.awt.Dimension(32767, 10));
        pnl_quantity = new javax.swing.JPanel();
        filler10 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        lbl_quantity = new javax.swing.JLabel();
        filler11 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        txt_quantity = new javax.swing.JTextField();
        filler12 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler17 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 6), new java.awt.Dimension(32767, 10));
        pnl_total = new javax.swing.JPanel();
        filler13 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        lbl_total = new javax.swing.JLabel();
        filler14 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        txt_total = new javax.swing.JTextField();
        filler15 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler16 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 6), new java.awt.Dimension(32767, 10));
        pnl_center = new javax.swing.JPanel();
        chart = new gui.customchart.Chart();

        setPreferredSize(new java.awt.Dimension(1366, 768));
        setLayout(new java.awt.BorderLayout());

        pnl_header.setPreferredSize(new java.awt.Dimension(661, 368));
        pnl_header.setLayout(new java.awt.BorderLayout());

        pnl_control.setBorder(javax.swing.BorderFactory.createTitledBorder("Lọc: "));
        pnl_control.setPreferredSize(new java.awt.Dimension(1366, 80));
        pnl_control.setLayout(new javax.swing.BoxLayout(pnl_control, javax.swing.BoxLayout.LINE_AXIS));
        pnl_control.add(filler26);

        date_statisticProduct.setDateFormatString("dd/MM/yyyy");
        date_statisticProduct.setDate(Calendar.getInstance().getTime());
        date_statisticProduct.setMaximumSize(new java.awt.Dimension(180, 40));
        date_statisticProduct.setPreferredSize(new java.awt.Dimension(155, 30));
        pnl_control.add(date_statisticProduct);

        pnl_header.add(pnl_control, java.awt.BorderLayout.NORTH);

        pnl_tableTopProduct.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách sản phẩm bán trong ngày"));
        pnl_tableTopProduct.setLayout(new java.awt.BorderLayout());

        scr_tableProduct.setViewportView(tbl_topProduct);

        pnl_tableTopProduct.add(scr_tableProduct, java.awt.BorderLayout.CENTER);

        pnl_header.add(pnl_tableTopProduct, java.awt.BorderLayout.CENTER);

        pnl_infomation.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin sản phẩm"));
        pnl_infomation.setPreferredSize(new java.awt.Dimension(450, 400));
        pnl_infomation.setLayout(new javax.swing.BoxLayout(pnl_infomation, javax.swing.BoxLayout.Y_AXIS));
        pnl_infomation.add(filler21);

        pnl_productID.setLayout(new javax.swing.BoxLayout(pnl_productID, javax.swing.BoxLayout.LINE_AXIS));
        pnl_productID.add(filler1);

        lbl_productID.setText("Mã sản phẩm: ");
        lbl_productID.setPreferredSize(new java.awt.Dimension(120, 0));
        lbl_productID.setSize(new java.awt.Dimension(100, 0));
        pnl_productID.add(lbl_productID);
        pnl_productID.add(filler2);

        txt_productID.setEditable(false);
        txt_productID.setMinimumSize(new java.awt.Dimension(50, 20));
        txt_productID.setPreferredSize(new java.awt.Dimension(50, 30));
        pnl_productID.add(txt_productID);
        pnl_productID.add(filler3);

        pnl_infomation.add(pnl_productID);
        pnl_infomation.add(filler20);

        pnl_productName.setLayout(new javax.swing.BoxLayout(pnl_productName, javax.swing.BoxLayout.LINE_AXIS));
        pnl_productName.add(filler4);

        lbl_productIName.setText("Tên sản phẩm: ");
        lbl_productIName.setPreferredSize(new java.awt.Dimension(120, 0));
        lbl_productIName.setSize(new java.awt.Dimension(100, 0));
        pnl_productName.add(lbl_productIName);
        pnl_productName.add(filler5);

        txt_productName.setEditable(false);
        txt_productName.setMinimumSize(new java.awt.Dimension(50, 20));
        txt_productName.setPreferredSize(new java.awt.Dimension(50, 30));
        txt_productName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_productNameActionPerformed(evt);
            }
        });
        pnl_productName.add(txt_productName);
        pnl_productName.add(filler6);

        pnl_infomation.add(pnl_productName);
        pnl_infomation.add(filler19);

        pnl_productType.setLayout(new javax.swing.BoxLayout(pnl_productType, javax.swing.BoxLayout.LINE_AXIS));
        pnl_productType.add(filler22);

        lbl_productIType.setText("Loại sản phẩm: ");
        lbl_productIType.setPreferredSize(new java.awt.Dimension(120, 0));
        pnl_productType.add(lbl_productIType);
        pnl_productType.add(filler23);

        txt_productType.setEditable(false);
        txt_productType.setMinimumSize(new java.awt.Dimension(50, 20));
        txt_productType.setPreferredSize(new java.awt.Dimension(50, 30));
        pnl_productType.add(txt_productType);
        pnl_productType.add(filler24);

        pnl_infomation.add(pnl_productType);
        pnl_infomation.add(filler25);

        pnl_price.setLayout(new javax.swing.BoxLayout(pnl_price, javax.swing.BoxLayout.LINE_AXIS));
        pnl_price.add(filler7);

        lbl_price.setText("Giá sản phẩm:");
        lbl_price.setPreferredSize(new java.awt.Dimension(120, 0));
        lbl_price.setSize(new java.awt.Dimension(100, 0));
        pnl_price.add(lbl_price);
        pnl_price.add(filler8);

        txt_price.setEditable(false);
        txt_price.setMinimumSize(new java.awt.Dimension(50, 20));
        txt_price.setPreferredSize(new java.awt.Dimension(50, 30));
        pnl_price.add(txt_price);
        pnl_price.add(filler9);

        pnl_infomation.add(pnl_price);
        pnl_infomation.add(filler18);

        pnl_quantity.setLayout(new javax.swing.BoxLayout(pnl_quantity, javax.swing.BoxLayout.LINE_AXIS));
        pnl_quantity.add(filler10);

        lbl_quantity.setText("Số lượng đã bán: ");
        lbl_quantity.setPreferredSize(new java.awt.Dimension(120, 0));
        lbl_quantity.setSize(new java.awt.Dimension(100, 0));
        pnl_quantity.add(lbl_quantity);
        pnl_quantity.add(filler11);

        txt_quantity.setEditable(false);
        txt_quantity.setMinimumSize(new java.awt.Dimension(50, 20));
        txt_quantity.setPreferredSize(new java.awt.Dimension(50, 30));
        pnl_quantity.add(txt_quantity);
        pnl_quantity.add(filler12);

        pnl_infomation.add(pnl_quantity);
        pnl_infomation.add(filler17);

        pnl_total.setLayout(new javax.swing.BoxLayout(pnl_total, javax.swing.BoxLayout.LINE_AXIS));
        pnl_total.add(filler13);

        lbl_total.setText("Tổng doanh thu: ");
        lbl_total.setPreferredSize(new java.awt.Dimension(120, 0));
        lbl_total.setSize(new java.awt.Dimension(100, 0));
        pnl_total.add(lbl_total);
        pnl_total.add(filler14);

        txt_total.setEditable(false);
        txt_total.setMinimumSize(new java.awt.Dimension(50, 20));
        txt_total.setPreferredSize(new java.awt.Dimension(50, 30));
        pnl_total.add(txt_total);
        pnl_total.add(filler15);

        pnl_infomation.add(pnl_total);
        pnl_infomation.add(filler16);

        pnl_header.add(pnl_infomation, java.awt.BorderLayout.EAST);

        add(pnl_header, java.awt.BorderLayout.NORTH);

        pnl_center.setBorder(javax.swing.BorderFactory.createTitledBorder("Biểu đồ thống kê top 10 sản phẩm bán chạy trong tháng"));
        pnl_center.setPreferredSize(new java.awt.Dimension(661, 400));
        pnl_center.setLayout(new java.awt.BorderLayout());
        pnl_center.add(chart, java.awt.BorderLayout.CENTER);

        add(pnl_center, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_productNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_productNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_productNameActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.customchart.Chart chart;
    private com.toedter.calendar.JDateChooser date_statisticProduct;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler10;
    private javax.swing.Box.Filler filler11;
    private javax.swing.Box.Filler filler12;
    private javax.swing.Box.Filler filler13;
    private javax.swing.Box.Filler filler14;
    private javax.swing.Box.Filler filler15;
    private javax.swing.Box.Filler filler16;
    private javax.swing.Box.Filler filler17;
    private javax.swing.Box.Filler filler18;
    private javax.swing.Box.Filler filler19;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler20;
    private javax.swing.Box.Filler filler21;
    private javax.swing.Box.Filler filler22;
    private javax.swing.Box.Filler filler23;
    private javax.swing.Box.Filler filler24;
    private javax.swing.Box.Filler filler25;
    private javax.swing.Box.Filler filler26;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JLabel lbl_price;
    private javax.swing.JLabel lbl_productID;
    private javax.swing.JLabel lbl_productIName;
    private javax.swing.JLabel lbl_productIType;
    private javax.swing.JLabel lbl_quantity;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JPanel pnl_center;
    private javax.swing.JPanel pnl_control;
    private javax.swing.JPanel pnl_header;
    private javax.swing.JPanel pnl_infomation;
    private javax.swing.JPanel pnl_price;
    private javax.swing.JPanel pnl_productID;
    private javax.swing.JPanel pnl_productName;
    private javax.swing.JPanel pnl_productType;
    private javax.swing.JPanel pnl_quantity;
    private javax.swing.JPanel pnl_tableTopProduct;
    private javax.swing.JPanel pnl_total;
    private javax.swing.JScrollPane scr_tableProduct;
    private javax.swing.JTable tbl_topProduct;
    private javax.swing.JTextField txt_price;
    private javax.swing.JTextField txt_productID;
    private javax.swing.JTextField txt_productName;
    private javax.swing.JTextField txt_productType;
    private javax.swing.JTextField txt_quantity;
    private javax.swing.JTextField txt_total;
    // End of variables declaration//GEN-END:variables
}

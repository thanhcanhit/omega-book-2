/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
*/
package gui;

import bus.PromotionManagament_BUS;
import com.formdev.flatlaf.FlatClientProperties;
import entity.Promotion;
import enums.DiscountType;
import enums.CustomerRank;
import enums.PromotionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import raven.toast.Notifications;

/**
 *
 * @author Như Tâm
 */
public class OrderPromotionManagement_GUI extends javax.swing.JPanel implements ActionListener{
    
    /**
     * Creates new form OrderPromotionManagement_GUI
     */
    private PromotionManagament_BUS bus;
    private Promotion currentPromotion = null;
    //Model
    private DefaultTableModel tblModel_promotion;
    private DefaultComboBoxModel cmbModel_type;
    private DefaultComboBoxModel cmbModel_status;
    private ButtonModel btnModel_type;
    private DefaultComboBoxModel cmbModel_rankCus;
    DecimalFormat price = new DecimalFormat("###,### VND");
    
    public OrderPromotionManagement_GUI() {
        initComponents();
        init();
    }
    
    private void init() {
        bus = new PromotionManagament_BUS();
        //model
        tblModel_promotion = new DefaultTableModel(new String[]{"Mã khuyến mãi", "Loại", "Giảm giá", "Hạng khách hàng", "Trạng thái"}, 0);
        tbl_inforPromo.setModel(tblModel_promotion);
        tbl_inforPromo.getSelectionModel().addListSelectionListener((e) -> {
            int rowIndex = tbl_inforPromo.getSelectedRow();
            if(rowIndex == -1)
                return;
            
            String promotionID = tblModel_promotion.getValueAt(rowIndex, 0).toString();
            this.currentPromotion = bus.getPromotion(promotionID);
            renderCurrentPromotion();
        });
        //combobox
        cmbModel_type = new DefaultComboBoxModel(new String[] {"Loại", "Số tiền", "Phần trăm"});
        cmb_typePromo.setModel(cmbModel_type);
        cmbModel_status = new DefaultComboBoxModel(new String[] {"Trạng thái", "Đang diễn ra", "Đã kết thúc"});
        cmb_statusPromo.setModel(cmbModel_status);
        cmbModel_rankCus = new DefaultComboBoxModel(new String[] {"Chưa có", "Bạc", "Vàng", "Kim cương"});
        cmb_rankCus.setModel(cmbModel_rankCus);
        //button group
        group_typePromo.add(rdb_price);
        group_typePromo.add(rdb_percent);
        
        renderPromotionTables(bus.getAllPromotionForOrder());
    }
    private void renderCurrentPromotion() {
        txt_promotionID.setText(currentPromotion.getPromotionID());
        if(currentPromotion.getTypeDiscount().getValue() == 1) {
            rdb_price.setSelected(true);
            txt_discountPromo.setText(price.format(currentPromotion.getDiscount()));
        }
        else {
            rdb_percent.setSelected(true);
            txt_discountPromo.setText(currentPromotion.getDiscount()+"");
        }
        cmb_rankCus.setSelectedIndex(currentPromotion.getCondition().getValue());
        
        chooseStartDate.setDate(currentPromotion.getStartedDate());
        chooseEndDate.setDate(currentPromotion.getEndedDate());
        
    }
    private void renderPromotionInfor() {
        txt_searchPromo.setText("");
        txt_promotionID.setText("");
        rdb_percent.setSelected(true);
        cmb_rankCus.setSelectedIndex(0);
        txt_discountPromo.setText("");
        chooseStartDate.setDate(java.sql.Date.valueOf(LocalDate.now()));
        chooseEndDate.setDate(java.sql.Date.valueOf(LocalDate.now()));
    }
    
    private void renderPromotionTables(ArrayList<Promotion> promotionList) {
        tblModel_promotion.setRowCount(0);
        String status, type, rank, discount;
        for (Promotion promotion : promotionList) {
            if(promotion.getEndedDate().after(java.sql.Date.valueOf(LocalDate.now())))
                status = "Còn hạn";
            else
                status = "Hết hạn";
            if(promotion.getTypeDiscount().getValue() == 1) {
                type = "Tiền";
                discount = price.format(promotion.getDiscount());
            }
            else {
                type = "Phần trăm";
                discount = promotion.getDiscount()+"";
            }
            if(promotion.getCondition().getValue() == 1)
                rank = "Bạc";
            else if(promotion.getCondition().getValue() == 2)
                rank = "Vàng";
            else if(promotion.getCondition().getValue() == 3)
                rank = "Kim cương";
            else
                rank = "Chưa có";
            String[] newRow = {promotion.getPromotionID(), type, discount, rank, status};
            tblModel_promotion.addRow(newRow);
        }
    }
    
    private boolean validPromotion() {
        if(txt_discountPromo.getText().equals("")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập giá trị khuyến mãi");
            txt_discountPromo.requestFocus();
            return false;
        }
        if(!Pattern.matches("[0-9]*", txt_discountPromo.getText()) || Double.parseDouble(txt_discountPromo.getText()) < 0) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Giá trị khuyến mãi chỉ gồm số dương");
            txt_discountPromo.requestFocus();
            return false;
        }
        if(chooseEndDate.getDate().before(chooseStartDate.getDate())) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng chọn ngày kết thúc sau ngày bắt đầu");
            return false;
        }
        if(group_typePromo.isSelected(btnModel_type)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng chọn loại khuyến mãi");
            return false;
        }
        return true;
    }
    private Promotion getCurrentValue() throws Exception{
        double discount = Double.parseDouble(txt_discountPromo.getText());
        int type;
        if(rdb_price.isSelected() == true)
            type = 1;
        else
            type = 0;
        Date startedDate = chooseStartDate.getDate();
        Date endedDate = chooseEndDate.getDate();
        String promotionID = txt_promotionID.getText();
        int rankCus = cmb_rankCus.getSelectedIndex();
             
        Promotion promotion = new Promotion(promotionID, startedDate, endedDate, PromotionType.ORDER, DiscountType.fromInt(type), discount, CustomerRank.fromInt(rankCus));
        return promotion;
    }
    private Promotion getNewValue() throws Exception {
        double discount = Double.parseDouble(txt_discountPromo.getText());
        int type;
        if(rdb_price.isSelected() == true)
            type = 1;
        else
            type = 0;
        Date startedDate = chooseStartDate.getDate();
        Date endedDate = chooseEndDate.getDate();
        String promotionID = bus.generateID(PromotionType.ORDER, DiscountType.fromInt(type), endedDate);
        int rankCus = cmb_rankCus.getSelectedIndex();             
        Promotion promotion = new Promotion(promotionID, startedDate, endedDate, PromotionType.ORDER, DiscountType.fromInt(type), discount, CustomerRank.fromInt(rankCus));
        return promotion;
    }
    
    private void addPromotion() throws Exception {
        Promotion newPromotion = getNewValue();
        try {
            if(bus.addNewOrderPromotion(newPromotion)) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm thành công");
                renderPromotionTables(bus.getAllPromotionForOrder());
                renderPromotionInfor();
            }
            else
                Notifications.getInstance().show(Notifications.Type.ERROR, "Thêm không thành công");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void removePromotion(String promotionID) throws Exception {
        if(bus.removePromotion(promotionID))
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã gỡ khuyến mãi " + promotionID);
        else
            Notifications.getInstance().show(Notifications.Type.ERROR, "Gỡ không thành công");
    }
    private void removeOrderPromotionOther(Promotion pm) {
        if(bus.removeOrderPromotionOther(pm)) {
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã gỡ khuyến mãi " + pm.getPromotionID());
            renderPromotionTables(bus.getAllPromotionForProduct());
        }
        else
            Notifications.getInstance().show(Notifications.Type.ERROR, "Gỡ không thành công");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        group_typePromo = new javax.swing.ButtonGroup();
        pnl_searchPromotion = new javax.swing.JPanel();
        pnl_txtSearchPromo = new javax.swing.JPanel();
        txt_searchPromo = new javax.swing.JTextField();
        pnl_buttonSearchPromo = new javax.swing.JPanel();
        btn_searchPromo = new javax.swing.JButton();
        pnl_filterPromo = new javax.swing.JPanel();
        cmb_typePromo = new javax.swing.JComboBox<>();
        cmb_statusPromo = new javax.swing.JComboBox<>();
        btn_searchFilterPromo = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        slp_promotion = new javax.swing.JSplitPane();
        pnl_listPromotion = new javax.swing.JPanel();
        pnl_promotionInfor = new javax.swing.JPanel();
        src_inforPromo = new javax.swing.JScrollPane();
        tbl_inforPromo = new javax.swing.JTable();
        pnl_promotionNew = new javax.swing.JPanel();
        pnl_inforPromo = new javax.swing.JPanel();
        pnl_txtInforPromo = new javax.swing.JPanel();
        pnl_promoID = new javax.swing.JPanel();
        lbl_promotionID = new javax.swing.JLabel();
        txt_promotionID = new javax.swing.JTextField();
        pnl_typePromo = new javax.swing.JPanel();
        lbl_typePromo = new javax.swing.JLabel();
        pnl_rdbTypePromo = new javax.swing.JPanel();
        rdb_percent = new javax.swing.JRadioButton();
        rdb_price = new javax.swing.JRadioButton();
        pnl_rankCus = new javax.swing.JPanel();
        lbl_rankCus = new javax.swing.JLabel();
        pnl_cmbRankCus = new javax.swing.JPanel();
        cmb_rankCus = new javax.swing.JComboBox<>();
        pnl_discountPromo = new javax.swing.JPanel();
        lbl_discountPromo = new javax.swing.JLabel();
        txt_discountPromo = new javax.swing.JTextField();
        pnl_startDatePromo = new javax.swing.JPanel();
        lbl_startDatePromo = new javax.swing.JLabel();
        pnl_chooseStartDate = new javax.swing.JPanel();
        chooseStartDate = new com.toedter.calendar.JDateChooser();
        pnl_endDatePromo = new javax.swing.JPanel();
        lbl_endDatePromo = new javax.swing.JLabel();
        pnl_chooseDateEnd = new javax.swing.JPanel();
        chooseEndDate = new com.toedter.calendar.JDateChooser();
        pnl_buttonPromo = new javax.swing.JPanel();
        btn_createPromo = new javax.swing.JButton();
        btn_removePromo = new javax.swing.JButton();
        btn_clearValue = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1366, 768));
        setLayout(new java.awt.BorderLayout());

        pnl_searchPromotion.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pnl_searchPromotion.setMaximumSize(new java.awt.Dimension(900, 50));
        pnl_searchPromotion.setMinimumSize(new java.awt.Dimension(500, 27));
        pnl_searchPromotion.setPreferredSize(new java.awt.Dimension(700, 50));
        pnl_searchPromotion.setLayout(new javax.swing.BoxLayout(pnl_searchPromotion, javax.swing.BoxLayout.X_AXIS));

        pnl_txtSearchPromo.setLayout(new javax.swing.BoxLayout(pnl_txtSearchPromo, javax.swing.BoxLayout.LINE_AXIS));

        txt_searchPromo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập mã khuyến mãi");
        txt_searchPromo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_searchPromoKeyPressed(evt);
            }
        });
        pnl_txtSearchPromo.add(txt_searchPromo);

        pnl_searchPromotion.add(pnl_txtSearchPromo);

        pnl_buttonSearchPromo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pnl_buttonSearchPromo.setMaximumSize(new java.awt.Dimension(100, 2147483647));
        pnl_buttonSearchPromo.setPreferredSize(new java.awt.Dimension(100, 23));
        pnl_buttonSearchPromo.setLayout(new java.awt.BorderLayout());

        btn_searchPromo.setText("Tìm kiếm");
        btn_searchPromo.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_searchPromo.setMaximumSize(new java.awt.Dimension(39, 23));
        btn_searchPromo.setMinimumSize(new java.awt.Dimension(39, 23));
        btn_searchPromo.setPreferredSize(new java.awt.Dimension(39, 23));
        btn_searchPromo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchPromoActionPerformed(evt);
            }
        });
        pnl_buttonSearchPromo.add(btn_searchPromo, java.awt.BorderLayout.CENTER);

        pnl_searchPromotion.add(pnl_buttonSearchPromo);

        pnl_filterPromo.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        pnl_filterPromo.setMaximumSize(new java.awt.Dimension(500, 50));
        pnl_filterPromo.setMinimumSize(new java.awt.Dimension(300, 32));
        pnl_filterPromo.setPreferredSize(new java.awt.Dimension(400, 50));
        pnl_filterPromo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        cmb_typePromo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Loại" }));
        cmb_typePromo.setMaximumSize(new java.awt.Dimension(32767, 30));
        cmb_typePromo.setPreferredSize(new java.awt.Dimension(85, 30));
        pnl_filterPromo.add(cmb_typePromo);

        cmb_statusPromo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Trạng thái", "Đang diễn ra", "Đã kết thúc" }));
        cmb_statusPromo.setMaximumSize(new java.awt.Dimension(32767, 30));
        cmb_statusPromo.setPreferredSize(new java.awt.Dimension(110, 30));
        pnl_filterPromo.add(cmb_statusPromo);

        btn_searchFilterPromo.setText("Lọc");
        btn_searchFilterPromo.setActionCommand("");
        btn_searchFilterPromo.setMaximumSize(new java.awt.Dimension(72, 35));
        btn_searchFilterPromo.setPreferredSize(new java.awt.Dimension(72, 35));
        btn_searchFilterPromo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchFilterPromoActionPerformed(evt);
            }
        });
        pnl_filterPromo.add(btn_searchFilterPromo);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/employee/reload_employee.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        pnl_filterPromo.add(jButton1);

        pnl_searchPromotion.add(pnl_filterPromo);

        add(pnl_searchPromotion, java.awt.BorderLayout.NORTH);

        pnl_listPromotion.setMaximumSize(new java.awt.Dimension(1000, 2147483647));
        pnl_listPromotion.setPreferredSize(new java.awt.Dimension(800, 432));
        pnl_listPromotion.setLayout(new java.awt.BorderLayout());

        pnl_promotionInfor.setMaximumSize(new java.awt.Dimension(900, 2147483647));
        pnl_promotionInfor.setMinimumSize(new java.awt.Dimension(700, 16));
        pnl_promotionInfor.setPreferredSize(new java.awt.Dimension(700, 402));
        pnl_promotionInfor.setLayout(new java.awt.BorderLayout());

        src_inforPromo.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));

        tbl_inforPromo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã KM", "Loại KM", "Giảm giá", "Trạng thái", "Hạng khách hàng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        src_inforPromo.setViewportView(tbl_inforPromo);

        pnl_promotionInfor.add(src_inforPromo, java.awt.BorderLayout.CENTER);

        pnl_listPromotion.add(pnl_promotionInfor, java.awt.BorderLayout.CENTER);

        slp_promotion.setLeftComponent(pnl_listPromotion);

        pnl_promotionNew.setAlignmentX(0.0F);
        pnl_promotionNew.setAlignmentY(0.0F);
        pnl_promotionNew.setMaximumSize(new java.awt.Dimension(366, 32767));
        pnl_promotionNew.setMinimumSize(new java.awt.Dimension(300, 192));
        pnl_promotionNew.setPreferredSize(new java.awt.Dimension(300, 190));
        pnl_promotionNew.setLayout(new javax.swing.BoxLayout(pnl_promotionNew, javax.swing.BoxLayout.Y_AXIS));

        pnl_inforPromo.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnl_inforPromo.setMinimumSize(new java.awt.Dimension(300, 263));
        pnl_inforPromo.setLayout(new java.awt.BorderLayout());

        pnl_txtInforPromo.setLayout(new javax.swing.BoxLayout(pnl_txtInforPromo, javax.swing.BoxLayout.Y_AXIS));

        pnl_promoID.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_promoID.setMaximumSize(new java.awt.Dimension(2147483647, 50));
        pnl_promoID.setMinimumSize(new java.awt.Dimension(151, 20));
        pnl_promoID.setPreferredSize(new java.awt.Dimension(176, 40));
        pnl_promoID.setLayout(new javax.swing.BoxLayout(pnl_promoID, javax.swing.BoxLayout.X_AXIS));

        lbl_promotionID.setText("Mã KM:");
        lbl_promotionID.setMaximumSize(new java.awt.Dimension(105, 16));
        lbl_promotionID.setMinimumSize(new java.awt.Dimension(77, 16));
        lbl_promotionID.setPreferredSize(new java.awt.Dimension(105, 16));
        pnl_promoID.add(lbl_promotionID);

        txt_promotionID.setEditable(false);
        txt_promotionID.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_promotionID.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_promotionID.setPreferredSize(new java.awt.Dimension(71, 40));
        txt_promotionID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_promotionIDActionPerformed(evt);
            }
        });
        pnl_promoID.add(txt_promotionID);

        pnl_txtInforPromo.add(pnl_promoID);

        pnl_typePromo.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_typePromo.setLayout(new javax.swing.BoxLayout(pnl_typePromo, javax.swing.BoxLayout.X_AXIS));

        lbl_typePromo.setText("Loại KM:");
        lbl_typePromo.setMaximumSize(new java.awt.Dimension(105, 16));
        lbl_typePromo.setMinimumSize(new java.awt.Dimension(77, 16));
        lbl_typePromo.setPreferredSize(new java.awt.Dimension(105, 16));
        pnl_typePromo.add(lbl_typePromo);

        pnl_rdbTypePromo.setMaximumSize(new java.awt.Dimension(32767, 40));
        pnl_rdbTypePromo.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_rdbTypePromo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        group_typePromo.add(rdb_percent);
        rdb_percent.setText("Phần trăm");
        pnl_rdbTypePromo.add(rdb_percent);

        group_typePromo.add(rdb_price);
        rdb_price.setText("Tiền");
        rdb_price.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdb_priceActionPerformed(evt);
            }
        });
        pnl_rdbTypePromo.add(rdb_price);

        pnl_typePromo.add(pnl_rdbTypePromo);

        pnl_txtInforPromo.add(pnl_typePromo);

        pnl_rankCus.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_rankCus.setLayout(new javax.swing.BoxLayout(pnl_rankCus, javax.swing.BoxLayout.X_AXIS));

        lbl_rankCus.setText("Hạng khách hàng:");
        lbl_rankCus.setMaximumSize(new java.awt.Dimension(105, 16));
        lbl_rankCus.setMinimumSize(new java.awt.Dimension(77, 16));
        lbl_rankCus.setPreferredSize(new java.awt.Dimension(105, 16));
        pnl_rankCus.add(lbl_rankCus);

        pnl_cmbRankCus.setMaximumSize(new java.awt.Dimension(32767, 40));
        pnl_cmbRankCus.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_cmbRankCus.setLayout(new java.awt.BorderLayout());

        cmb_rankCus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chưa có", "Bạc", "Vàng", "Kim cương" }));
        pnl_cmbRankCus.add(cmb_rankCus, java.awt.BorderLayout.CENTER);

        pnl_rankCus.add(pnl_cmbRankCus);

        pnl_txtInforPromo.add(pnl_rankCus);

        pnl_discountPromo.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_discountPromo.setLayout(new javax.swing.BoxLayout(pnl_discountPromo, javax.swing.BoxLayout.X_AXIS));

        lbl_discountPromo.setText("Giảm giá:");
        lbl_discountPromo.setMaximumSize(new java.awt.Dimension(105, 16));
        lbl_discountPromo.setMinimumSize(new java.awt.Dimension(77, 16));
        lbl_discountPromo.setPreferredSize(new java.awt.Dimension(105, 16));
        pnl_discountPromo.add(lbl_discountPromo);

        txt_discountPromo.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txt_discountPromo.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_discountPromo.setPreferredSize(new java.awt.Dimension(64, 30));
        pnl_discountPromo.add(txt_discountPromo);

        pnl_txtInforPromo.add(pnl_discountPromo);

        pnl_startDatePromo.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_startDatePromo.setLayout(new javax.swing.BoxLayout(pnl_startDatePromo, javax.swing.BoxLayout.X_AXIS));

        lbl_startDatePromo.setText("Ngày bắt đầu:");
        lbl_startDatePromo.setMaximumSize(new java.awt.Dimension(105, 16));
        lbl_startDatePromo.setMinimumSize(new java.awt.Dimension(77, 16));
        lbl_startDatePromo.setPreferredSize(new java.awt.Dimension(105, 16));
        pnl_startDatePromo.add(lbl_startDatePromo);

        pnl_chooseStartDate.setMaximumSize(new java.awt.Dimension(32767, 30));
        pnl_chooseStartDate.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_chooseStartDate.setLayout(new java.awt.GridLayout(1, 0));
        pnl_chooseStartDate.add(chooseStartDate);

        pnl_startDatePromo.add(pnl_chooseStartDate);

        pnl_txtInforPromo.add(pnl_startDatePromo);

        pnl_endDatePromo.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_endDatePromo.setLayout(new javax.swing.BoxLayout(pnl_endDatePromo, javax.swing.BoxLayout.X_AXIS));

        lbl_endDatePromo.setText("Ngày kết thúc:");
        lbl_endDatePromo.setMaximumSize(new java.awt.Dimension(105, 16));
        lbl_endDatePromo.setPreferredSize(new java.awt.Dimension(105, 16));
        pnl_endDatePromo.add(lbl_endDatePromo);

        pnl_chooseDateEnd.setMaximumSize(new java.awt.Dimension(32767, 30));
        pnl_chooseDateEnd.setPreferredSize(new java.awt.Dimension(10, 30));
        pnl_chooseDateEnd.setLayout(new java.awt.GridLayout(1, 0));
        pnl_chooseDateEnd.add(chooseEndDate);

        pnl_endDatePromo.add(pnl_chooseDateEnd);

        pnl_txtInforPromo.add(pnl_endDatePromo);

        pnl_inforPromo.add(pnl_txtInforPromo, java.awt.BorderLayout.CENTER);

        pnl_buttonPromo.setMinimumSize(new java.awt.Dimension(100, 50));
        pnl_buttonPromo.setPreferredSize(new java.awt.Dimension(1261, 50));
        pnl_buttonPromo.setLayout(new java.awt.GridLayout(1, 0));

        btn_createPromo.setText("TẠO MỚI");
        btn_createPromo.setPreferredSize(new java.awt.Dimension(79, 50));
        btn_createPromo.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_createPromo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_createPromoActionPerformed(evt);
            }
        });
        pnl_buttonPromo.add(btn_createPromo);

        btn_removePromo.setText("GỠ ");
        btn_removePromo.setActionCommand("");
        btn_removePromo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_removePromoActionPerformed(evt);
            }
        });
        pnl_buttonPromo.add(btn_removePromo);

        btn_clearValue.setText("XOÁ TRẮNG");
        btn_clearValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearValueActionPerformed(evt);
            }
        });
        pnl_buttonPromo.add(btn_clearValue);

        pnl_inforPromo.add(pnl_buttonPromo, java.awt.BorderLayout.SOUTH);

        pnl_promotionNew.add(pnl_inforPromo);

        slp_promotion.setRightComponent(pnl_promotionNew);

        add(slp_promotion, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    private void btn_removePromoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_removePromoActionPerformed
        String promotionID = txt_promotionID.getText();
        Promotion pm = bus.getPromotion(promotionID);
        if(pm.getStartedDate().after(java.sql.Date.valueOf(LocalDate.now()))) {
            if (JOptionPane.showConfirmDialog(null,
            "Bạn thật sự muốn gỡ khuyến mãi " + promotionID + " không? Vẫn chưa đến hạn diễn ra khuyến mãi!", "Gỡ khuyến mãi?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    // Xoá
                    removeOrderPromotionOther(pm);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                renderPromotionTables(bus.getAllPromotionForOrder());
                renderPromotionInfor();
            }
            else
                Notifications.getInstance().show(Notifications.Type.INFO, "Đã huỷ thao tác!");
        }
        else {
            if (JOptionPane.showConfirmDialog(null,
            "Bạn thật sự muốn gỡ khuyến mãi " + promotionID + " không?", "Gỡ khuyến mãi?",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    // Xoá
                    removePromotion(promotionID);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                renderPromotionTables(bus.getAllPromotionForOrder());
                renderPromotionInfor();
            }
            else
                Notifications.getInstance().show(Notifications.Type.INFO, "Đã huỷ thao tác!");
        }
    }//GEN-LAST:event_btn_removePromoActionPerformed
    
    private void txt_promotionIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_promotionIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_promotionIDActionPerformed
    
    private void rdb_priceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdb_priceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdb_priceActionPerformed
    
    private void btn_searchPromoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchPromoActionPerformed
        String searchQuery = txt_searchPromo.getText();
        if (searchQuery.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng điền mã khuyến mãi");
            return;
        }
        renderPromotionTables(bus.searchForOrderById(searchQuery));
    }//GEN-LAST:event_btn_searchPromoActionPerformed
    
    private void btn_searchFilterPromoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchFilterPromoActionPerformed
        int type = cmb_typePromo.getSelectedIndex();
        int status = cmb_statusPromo.getSelectedIndex();
        renderPromotionTables(bus.filterForOrder(type, status));
    }//GEN-LAST:event_btn_searchFilterPromoActionPerformed
    
    private void btn_createPromoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_createPromoActionPerformed
        if(validPromotion() == false) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập thông tin khuyến mãi để thêm");
            return;
        }
        else
            try {
                addPromotion();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }//GEN-LAST:event_btn_createPromoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        renderPromotionTables(bus.getAllPromotionForOrder());
        renderPromotionInfor();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txt_searchPromoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchPromoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String searchQuery = txt_searchPromo.getText();
            if (searchQuery.isBlank()) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng điền mã khuyến mãi");
                return;
            }
            renderPromotionTables(bus.searchById(searchQuery));
        }
    }//GEN-LAST:event_txt_searchPromoKeyPressed

    private void btn_clearValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearValueActionPerformed
        renderPromotionTables(bus.getAllPromotionForOrder());
        renderPromotionInfor();
    }//GEN-LAST:event_btn_clearValueActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_clearValue;
    private javax.swing.JButton btn_createPromo;
    private javax.swing.JButton btn_removePromo;
    private javax.swing.JButton btn_searchFilterPromo;
    private javax.swing.JButton btn_searchPromo;
    private com.toedter.calendar.JDateChooser chooseEndDate;
    private com.toedter.calendar.JDateChooser chooseStartDate;
    private javax.swing.JComboBox<String> cmb_rankCus;
    private javax.swing.JComboBox<String> cmb_statusPromo;
    private javax.swing.JComboBox<String> cmb_typePromo;
    private javax.swing.ButtonGroup group_typePromo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel lbl_discountPromo;
    private javax.swing.JLabel lbl_endDatePromo;
    private javax.swing.JLabel lbl_promotionID;
    private javax.swing.JLabel lbl_rankCus;
    private javax.swing.JLabel lbl_startDatePromo;
    private javax.swing.JLabel lbl_typePromo;
    private javax.swing.JPanel pnl_buttonPromo;
    private javax.swing.JPanel pnl_buttonSearchPromo;
    private javax.swing.JPanel pnl_chooseDateEnd;
    private javax.swing.JPanel pnl_chooseStartDate;
    private javax.swing.JPanel pnl_cmbRankCus;
    private javax.swing.JPanel pnl_discountPromo;
    private javax.swing.JPanel pnl_endDatePromo;
    private javax.swing.JPanel pnl_filterPromo;
    private javax.swing.JPanel pnl_inforPromo;
    private javax.swing.JPanel pnl_listPromotion;
    private javax.swing.JPanel pnl_promoID;
    private javax.swing.JPanel pnl_promotionInfor;
    private javax.swing.JPanel pnl_promotionNew;
    private javax.swing.JPanel pnl_rankCus;
    private javax.swing.JPanel pnl_rdbTypePromo;
    private javax.swing.JPanel pnl_searchPromotion;
    private javax.swing.JPanel pnl_startDatePromo;
    private javax.swing.JPanel pnl_txtInforPromo;
    private javax.swing.JPanel pnl_txtSearchPromo;
    private javax.swing.JPanel pnl_typePromo;
    private javax.swing.JRadioButton rdb_percent;
    private javax.swing.JRadioButton rdb_price;
    private javax.swing.JSplitPane slp_promotion;
    private javax.swing.JScrollPane src_inforPromo;
    private javax.swing.JTable tbl_inforPromo;
    private javax.swing.JTextField txt_discountPromo;
    private javax.swing.JTextField txt_promotionID;
    private javax.swing.JTextField txt_searchPromo;
    // End of variables declaration//GEN-END:variables
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}

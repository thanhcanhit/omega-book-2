/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import bus.ProductManagement_BUS;
import com.formdev.flatlaf.FlatClientProperties;
import entity.Book;
import entity.Product;
import enums.Type;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import utilities.FormatNumber;
import utilities.SVGIcon;

/**
 *
 * @author thanhcanhit
 */
public class ProductManagement_GUI extends javax.swing.JPanel {

    /**
     * Creates new form Sales_GUI
     */
    private ProductManagement_BUS bus;

//    Model
    private DefaultTableModel tblModel_products;
    private DefaultComboBoxModel cmbModel_type;
    private DefaultComboBoxModel cmbModel_subType;
    private DefaultComboBoxModel cmbModel_bookCategory;
    private DefaultComboBoxModel cmbModel_stationeryType;

//    Internal frame
    JFileChooser fileChooser_productImg;

//    state
    private Product currentProduct = null;
    private int currentPage;
    private int lastPage;

    public ProductManagement_GUI() {
        initComponents();
        init();
    }

    private void init() {
        bus = new ProductManagement_BUS();

//        Frame
        fileChooser_productImg = new javax.swing.JFileChooser();
        fileChooser_productImg.setCurrentDirectory(new File(System.getProperty("user.home")));

//        Table model
        tblModel_products = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Giá nhập", "Giá bán", "Số lượng tồn"}, 50);
        tbl_products.setModel(tblModel_products);
        tbl_products.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            int rowIndex = tbl_products.getSelectedRow();
            if (rowIndex == -1) {
                return;
            }

            String productID = tbl_products.getValueAt(rowIndex, 0).toString();
            this.currentProduct = bus.getProduct(productID);
            renderCurrentProduct();

        });

//        Page
        this.currentPage = 1;
        this.lastPage = bus.getLastPage();
        renderCurrentPage();

//        Combobox model
        cmbModel_type = new DefaultComboBoxModel(new String[]{"Sách", "Văn phòng phẩm"});
        cmbModel_bookCategory = new DefaultComboBoxModel(new String[]{"Văn học", "Kinh tế", "Tâm lý - kỹ năng sống", "Thiếu nhi", "Nuôi dạy con", "Tiểu sử - hồi ký", "Sách giáo khoa - tham khảo", "Sách học ngoại ngữ"});
        cmbModel_stationeryType = new DefaultComboBoxModel(new String[]{"Bút - viết", "Dụng cụ học sinh", "Dụng cụ văn phòng", "Dụng cụ vẽ", "Sản phẩm về giấy", "Sản phẩm khác"});
        cmb_type.setModel(cmbModel_type);
        cmb_stationeryType.setModel(cmbModel_stationeryType);
        cmb_bookCategory.setModel(cmbModel_bookCategory);
        renderComboboxType();

//      Product detail
        pnl_bookDetail.setVisible(true);
        pnl_stationeryDetail.setVisible(false);
    }

    private void renderCurrentProduct() {
//        update form
        txt_productId.setText(currentProduct.getProductID());
        txa_productName.setText(currentProduct.getName());
        txt_productCostPrice.setText(currentProduct.getCostPrice().toString());
        txt_productPrice.setText(currentProduct.getPrice().toString());
        txt_productVAT.setText(currentProduct.getVAT().toString());
        txt_productInventory.setText(currentProduct.getInventory().toString());
        cmb_productType.setSelectedItem(currentProduct.getType() == Type.BOOK ? "Sách" : "Văn phòng phẩm");
        lbl_productImg.setIcon(new ImageIcon(currentProduct.getImage()));
        lbl_productImg.setText("");

//        Update detail form
        boolean isBook = currentProduct.getType() == Type.BOOK;
        pnl_bookDetail.setVisible(isBook);
        pnl_stationeryDetail.setVisible(!isBook);

        if (isBook) {
            Book book = (Book) currentProduct;
            txt_bookAuthor.setText(book.getAuthor());
            txt_bookLanguage.setText(book.getLanguage() == null ? "Tiếng Việt" : "Tiếng Anh");
            txt_bookPublisher.setText(book.getPublisher());
            txt_bookPublishDate.setText(book.getPublishYear().toString());
            txt_bookTranslator.setText(book.getTranslator());
            txt_bookQuantityPage.setText(book.getPageQuantity().toString());
            cmb_bookCategory.setSelectedIndex(book.getBookCategory().getValue() - 1);
            cmb_bookHardCover.setSelectedIndex(book.getIsHardCover() ? 0 : 1);
        }
    }

    private void renderCurrentPage() {
        lbl_pageNumber.setText(currentPage + "/" + lastPage);
        renderProductsTable(bus.getDataOfPage(currentPage));

//      Toggle button
        btn_previous.setEnabled(currentPage != 1);
        btn_next.setEnabled(currentPage != lastPage);
    }

    private void renderProductsTable(ArrayList<Product> productList) {
        tblModel_products.setRowCount(0);
        for (Product product : productList) {
            Object[] newRow = new Object[]{product.getProductID(), product.getName(), FormatNumber.toVND(product.getCostPrice()), FormatNumber.toVND(product.getPrice()), product.getInventory()};
            tblModel_products.addRow(newRow);
        }
    }

    private void renderComboboxType() {
        String currentType = cmbModel_type.getSelectedItem().toString();

        if (currentType.equals(cmbModel_type.getElementAt(0))) {
            cmbModel_subType = new DefaultComboBoxModel(new String[]{"Văn học", "Kinh tế", "Tâm lý - kỹ năng sống", "Thiếu nhi", "Nuôi dạy con", "Tiểu sử - hồi ký", "Sách giáo khoa - tham khảo", "Sách học ngoại ngữ"});
        } else if (currentType.equals(cmbModel_type.getElementAt(1))) {
            cmbModel_subType = new DefaultComboBoxModel(new String[]{"Bút - viết", "Dụng cụ học sinh", "Dụng cụ văn phòng", "Dụng cụ vẽ", "Sản phẩm về giấy", "Sản phẩm khác"});
        }

        cmb_typeDetail.setModel(cmbModel_subType);
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
        pnl_search = new javax.swing.JPanel();
        txt_search = new javax.swing.JTextField();
        btn_search = new javax.swing.JButton();
        pnl_filter = new javax.swing.JPanel();
        pnl_container20 = new javax.swing.JPanel();
        pnl_name = new javax.swing.JPanel();
        lbl_name = new javax.swing.JLabel();
        txt_name = new javax.swing.JTextField();
        pnl_empty = new javax.swing.JPanel();
        chk_empty = new javax.swing.JCheckBox();
        pnl_type = new javax.swing.JPanel();
        lbl_type = new javax.swing.JLabel();
        cmb_type = new javax.swing.JComboBox<>();
        pnl_typeDetail = new javax.swing.JPanel();
        lbl_typeDetail = new javax.swing.JLabel();
        cmb_typeDetail = new javax.swing.JComboBox<>();
        pnl_container21 = new javax.swing.JPanel();
        btn_filter = new javax.swing.JButton();
        pnl_cart = new javax.swing.JPanel();
        scr_cart = new javax.swing.JScrollPane();
        tbl_products = new javax.swing.JTable();
        pnl_cartFooter = new javax.swing.JPanel();
        btn_previous = new javax.swing.JButton();
        lbl_pageNumber = new javax.swing.JLabel();
        btn_next = new javax.swing.JButton();
        pnl_right = new javax.swing.JPanel();
        scr_detail = new javax.swing.JScrollPane();
        pnl_rightCenter = new javax.swing.JPanel();
        pnl_productInfo = new javax.swing.JPanel();
        pnl_productTop = new javax.swing.JPanel();
        pnl_productTopLeft = new javax.swing.JPanel();
        lbl_productImg = new javax.swing.JLabel();
        btn_selectImg = new javax.swing.JButton();
        pnl_productTopRight = new javax.swing.JPanel();
        pnl_orderId = new javax.swing.JPanel();
        pnl_container = new javax.swing.JPanel();
        lbl_productId = new javax.swing.JLabel();
        txt_productId = new javax.swing.JTextField();
        pnl_container1 = new javax.swing.JPanel();
        lbl_productName = new javax.swing.JLabel();
        scr_productName = new javax.swing.JScrollPane();
        txa_productName = new javax.swing.JTextArea();
        pnl_productCenter = new javax.swing.JPanel();
        pnl_container3 = new javax.swing.JPanel();
        lbl_productCostPrice = new javax.swing.JLabel();
        txt_productCostPrice = new javax.swing.JTextField();
        pnl_container4 = new javax.swing.JPanel();
        lbl_productPrice = new javax.swing.JLabel();
        txt_productPrice = new javax.swing.JTextField();
        pnl_container5 = new javax.swing.JPanel();
        lbl_productInventory = new javax.swing.JLabel();
        txt_productInventory = new javax.swing.JTextField();
        pnl_container6 = new javax.swing.JPanel();
        lbl_productVAT = new javax.swing.JLabel();
        txt_productVAT = new javax.swing.JTextField();
        pnl_container2 = new javax.swing.JPanel();
        lbl_productType = new javax.swing.JLabel();
        cmb_productType = new javax.swing.JComboBox<>();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        pnl_bookDetail = new javax.swing.JPanel();
        pnl_bookAuthor = new javax.swing.JPanel();
        lbl_bookAuthor = new javax.swing.JLabel();
        txt_bookAuthor = new javax.swing.JTextField();
        pnl_bookPublisher = new javax.swing.JPanel();
        lbl_bookPublisher = new javax.swing.JLabel();
        txt_bookPublisher = new javax.swing.JTextField();
        pnl_container15 = new javax.swing.JPanel();
        lbl_bookTranslator = new javax.swing.JLabel();
        txt_bookTranslator = new javax.swing.JTextField();
        pnl_container18 = new javax.swing.JPanel();
        lbl_bookCategory1 = new javax.swing.JLabel();
        cmb_bookCategory = new javax.swing.JComboBox<>();
        pnl_bookCenter = new javax.swing.JPanel();
        pnl_container11 = new javax.swing.JPanel();
        lbl_bookPublishDate = new javax.swing.JLabel();
        txt_bookPublishDate = new javax.swing.JTextField();
        pnl_container12 = new javax.swing.JPanel();
        lbl_bookHardCover = new javax.swing.JLabel();
        cmb_bookHardCover = new javax.swing.JComboBox<>();
        pnl_container13 = new javax.swing.JPanel();
        lbl_bookQuantityPage = new javax.swing.JLabel();
        txt_bookQuantityPage = new javax.swing.JTextField();
        pnl_container14 = new javax.swing.JPanel();
        lbl_bookLanguage = new javax.swing.JLabel();
        txt_bookLanguage = new javax.swing.JTextField();
        pnl_container17 = new javax.swing.JPanel();
        lbl_bookType = new javax.swing.JLabel();
        cmb_bookType = new javax.swing.JComboBox<>();
        pnl_container7 = new javax.swing.JPanel();
        pnl_container16 = new javax.swing.JPanel();
        lbl_bookDescription = new javax.swing.JLabel();
        scr_productName1 = new javax.swing.JScrollPane();
        txa_bookDescription = new javax.swing.JTextArea();
        pnl_stationeryDetail = new javax.swing.JPanel();
        pnl_stationery = new javax.swing.JPanel();
        pnl_container23 = new javax.swing.JPanel();
        lbl_stationeryColor = new javax.swing.JLabel();
        txt_stationeryColor = new javax.swing.JTextField();
        pnl_container24 = new javax.swing.JPanel();
        lbl_stationeryType = new javax.swing.JLabel();
        cmb_stationeryType = new javax.swing.JComboBox<>();
        pnl_container25 = new javax.swing.JPanel();
        lbl_stationeryOrigin = new javax.swing.JLabel();
        txt_stationeryOrigin = new javax.swing.JTextField();
        pnl_container26 = new javax.swing.JPanel();
        lbl_stationeryWeight = new javax.swing.JLabel();
        txt_stationeryWeight = new javax.swing.JTextField();
        pnl_container28 = new javax.swing.JPanel();
        lbl_stationeryBrand = new javax.swing.JLabel();
        cmb_stationeryBrand = new javax.swing.JComboBox<>();
        pnl_control = new javax.swing.JPanel();
        btn_clear = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        btn_add = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        splitPane_main.setResizeWeight(0.7);
        splitPane_main.setMinimumSize(new java.awt.Dimension(1305, 768));

        pnl_left.setMinimumSize(new java.awt.Dimension(400, 59));
        pnl_left.setPreferredSize(new java.awt.Dimension(800, 768));
        pnl_left.setLayout(new java.awt.BorderLayout());

        pnl_header.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Tìm kiếm & Lọc"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnl_header.setMinimumSize(new java.awt.Dimension(516, 150));
        pnl_header.setPreferredSize(new java.awt.Dimension(1366, 150));
        pnl_header.setLayout(new javax.swing.BoxLayout(pnl_header, javax.swing.BoxLayout.Y_AXIS));

        pnl_search.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_search.setMinimumSize(new java.awt.Dimension(164, 40));
        pnl_search.setPreferredSize(new java.awt.Dimension(100, 40));
        pnl_search.setLayout(new javax.swing.BoxLayout(pnl_search, javax.swing.BoxLayout.LINE_AXIS));

        txt_search.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mã sản phẩm");
        pnl_search.add(txt_search);

        btn_search.setText("Tìm kiếm");
        btn_search.setMaximumSize(new java.awt.Dimension(100, 50));
        btn_search.setMinimumSize(new java.awt.Dimension(100, 50));
        btn_search.setPreferredSize(new java.awt.Dimension(100, 50));
        btn_search.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        pnl_search.add(btn_search);

        pnl_header.add(pnl_search);

        pnl_filter.setMaximumSize(new java.awt.Dimension(32867, 80));
        pnl_filter.setMinimumSize(new java.awt.Dimension(506, 80));
        pnl_filter.setOpaque(false);
        pnl_filter.setPreferredSize(new java.awt.Dimension(100, 80));
        pnl_filter.setLayout(new javax.swing.BoxLayout(pnl_filter, javax.swing.BoxLayout.LINE_AXIS));

        pnl_container20.setMinimumSize(new java.awt.Dimension(406, 80));
        pnl_container20.setPreferredSize(new java.awt.Dimension(100, 80));
        pnl_container20.setLayout(new java.awt.GridLayout(2, 2, 5, 5));

        pnl_name.setLayout(new javax.swing.BoxLayout(pnl_name, javax.swing.BoxLayout.LINE_AXIS));

        lbl_name.setText("Tên sản phẩm");
        lbl_name.setPreferredSize(new java.awt.Dimension(110, 35));
        pnl_name.add(lbl_name);

        txt_name.setMinimumSize(new java.awt.Dimension(100, 22));
        pnl_name.add(txt_name);

        pnl_container20.add(pnl_name);

        pnl_empty.setMaximumSize(new java.awt.Dimension(100, 20));
        pnl_empty.setLayout(new javax.swing.BoxLayout(pnl_empty, javax.swing.BoxLayout.LINE_AXIS));

        chk_empty.setText("Sản phẩm hết hàng");
        chk_empty.setPreferredSize(new java.awt.Dimension(140, 35));
        pnl_empty.add(chk_empty);

        pnl_container20.add(pnl_empty);

        pnl_type.setLayout(new javax.swing.BoxLayout(pnl_type, javax.swing.BoxLayout.LINE_AXIS));

        lbl_type.setText("Loại sản phẩm");
        lbl_type.setPreferredSize(new java.awt.Dimension(110, 35));
        pnl_type.add(lbl_type);

        cmb_type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sách", "Văn phòng phẩm" }));
        cmb_type.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_typeActionPerformed(evt);
            }
        });
        pnl_type.add(cmb_type);

        pnl_container20.add(pnl_type);

        pnl_typeDetail.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        pnl_typeDetail.setPreferredSize(new java.awt.Dimension(140, 35));
        pnl_typeDetail.setLayout(new javax.swing.BoxLayout(pnl_typeDetail, javax.swing.BoxLayout.LINE_AXIS));

        lbl_typeDetail.setText("Loại chi tiết");
        lbl_typeDetail.setPreferredSize(new java.awt.Dimension(110, 35));
        pnl_typeDetail.add(lbl_typeDetail);

        cmb_typeDetail.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Wibu", "Anime" }));
        cmb_typeDetail.setMinimumSize(new java.awt.Dimension(100, 22));
        pnl_typeDetail.add(cmb_typeDetail);

        pnl_container20.add(pnl_typeDetail);

        pnl_filter.add(pnl_container20);

        pnl_container21.setMaximumSize(new java.awt.Dimension(100, 32767));
        pnl_container21.setLayout(new java.awt.GridLayout(1, 0));

        btn_filter.setText("Lọc");
        btn_filter.setMaximumSize(new java.awt.Dimension(100, 50));
        btn_filter.setMinimumSize(new java.awt.Dimension(100, 50));
        btn_filter.setPreferredSize(new java.awt.Dimension(100, 50));
        btn_filter.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        pnl_container21.add(btn_filter);

        pnl_filter.add(pnl_container21);

        pnl_header.add(pnl_filter);

        pnl_left.add(pnl_header, java.awt.BorderLayout.NORTH);

        pnl_cart.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách sản phẩm"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnl_cart.setLayout(new java.awt.BorderLayout());

        tbl_products.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"SP0001", "Thiên tài bên trái, kẻ điên bên phải", "1", "100000", "100000"},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Giá nhập", "Giá bán", "Số lượng tồn"
            }
        ));
        scr_cart.setViewportView(tbl_products);

        pnl_cart.add(scr_cart, java.awt.BorderLayout.CENTER);

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

        pnl_cart.add(pnl_cartFooter, java.awt.BorderLayout.PAGE_END);

        pnl_left.add(pnl_cart, java.awt.BorderLayout.CENTER);

        splitPane_main.setLeftComponent(pnl_left);

        pnl_right.setMaximumSize(new java.awt.Dimension(500, 2147483647));
        pnl_right.setMinimumSize(new java.awt.Dimension(350, 39));
        pnl_right.setPreferredSize(new java.awt.Dimension(400, 768));
        pnl_right.setLayout(new java.awt.BorderLayout());

        scr_detail.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scr_detail.setAutoscrolls(true);
        scr_detail.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        scr_detail.setViewportView(pnl_rightCenter);

        pnl_rightCenter.setMinimumSize(new java.awt.Dimension(420, 800));
        pnl_rightCenter.setName(""); // NOI18N
        pnl_rightCenter.setPreferredSize(new java.awt.Dimension(400, 600));
        pnl_rightCenter.setLayout(new javax.swing.BoxLayout(pnl_rightCenter, javax.swing.BoxLayout.Y_AXIS));

        pnl_productInfo.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin sản phẩm"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnl_productInfo.setPreferredSize(new java.awt.Dimension(400, 400));
        pnl_productInfo.setLayout(new javax.swing.BoxLayout(pnl_productInfo, javax.swing.BoxLayout.Y_AXIS));

        pnl_productTop.setMinimumSize(new java.awt.Dimension(400, 170));
        pnl_productTop.setPreferredSize(new java.awt.Dimension(400, 170));
        pnl_productTop.setLayout(new java.awt.GridLayout(1, 0));

        pnl_productTopLeft.setLayout(new java.awt.BorderLayout());

        lbl_productImg.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;");
        lbl_productImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_productImg.setText("Hình ảnh sản phẩm");
        pnl_productTopLeft.add(lbl_productImg, java.awt.BorderLayout.CENTER);

        btn_selectImg.setText("Chọn hình ảnh");
        btn_selectImg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_selectImgActionPerformed(evt);
            }
        });
        pnl_productTopLeft.add(btn_selectImg, java.awt.BorderLayout.SOUTH);

        pnl_productTop.add(pnl_productTopLeft);

        pnl_productTopRight.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        pnl_productTopRight.setMinimumSize(new java.awt.Dimension(300, 132));
        pnl_productTopRight.setPreferredSize(new java.awt.Dimension(200, 100));
        pnl_productTopRight.setLayout(new java.awt.BorderLayout());

        pnl_orderId.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_orderId.setLayout(new javax.swing.BoxLayout(pnl_orderId, javax.swing.BoxLayout.Y_AXIS));

        pnl_container.setPreferredSize(new java.awt.Dimension(100, 40));
        pnl_container.setLayout(new javax.swing.BoxLayout(pnl_container, javax.swing.BoxLayout.LINE_AXIS));

        lbl_productId.setText("Mã sản phẩm");
        lbl_productId.setPreferredSize(new java.awt.Dimension(100, 25));
        pnl_container.add(lbl_productId);

        txt_productId.setText(" SP01221232");
        pnl_container.add(txt_productId);

        pnl_orderId.add(pnl_container);

        pnl_container1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 5, 0));
        pnl_container1.setMinimumSize(new java.awt.Dimension(200, 110));
        pnl_container1.setPreferredSize(new java.awt.Dimension(279, 140));
        pnl_container1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        lbl_productName.setText("Tên sản phẩm");
        lbl_productName.setPreferredSize(new java.awt.Dimension(100, 25));
        pnl_container1.add(lbl_productName);

        scr_productName.setMinimumSize(new java.awt.Dimension(100, 100));

        txa_productName.setColumns(15);
        txa_productName.setLineWrap(true);
        txa_productName.setRows(4);
        txa_productName.setTabSize(4);
        txa_productName.setText("Cuộc đời của thanhcanhit");
        scr_productName.setViewportView(txa_productName);

        pnl_container1.add(scr_productName);

        pnl_orderId.add(pnl_container1);

        pnl_productTopRight.add(pnl_orderId, java.awt.BorderLayout.CENTER);

        pnl_productTop.add(pnl_productTopRight);

        pnl_productInfo.add(pnl_productTop);

        pnl_productCenter.setLayout(new javax.swing.BoxLayout(pnl_productCenter, javax.swing.BoxLayout.Y_AXIS));

        pnl_container3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_container3.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_container3.setLayout(new javax.swing.BoxLayout(pnl_container3, javax.swing.BoxLayout.LINE_AXIS));

        lbl_productCostPrice.setText("Giá nhập");
        lbl_productCostPrice.setMaximumSize(null);
        lbl_productCostPrice.setMinimumSize(new java.awt.Dimension(110, 30));
        lbl_productCostPrice.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_container3.add(lbl_productCostPrice);

        txt_productCostPrice.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_productCostPrice.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_productCostPrice.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container3.add(txt_productCostPrice);

        pnl_productCenter.add(pnl_container3);

        pnl_container4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_container4.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_container4.setLayout(new javax.swing.BoxLayout(pnl_container4, javax.swing.BoxLayout.LINE_AXIS));

        lbl_productPrice.setText("Giá bán");
        lbl_productPrice.setMaximumSize(null);
        lbl_productPrice.setMinimumSize(new java.awt.Dimension(110, 30));
        lbl_productPrice.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_container4.add(lbl_productPrice);

        txt_productPrice.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_productPrice.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_productPrice.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container4.add(txt_productPrice);

        pnl_productCenter.add(pnl_container4);

        pnl_container5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_container5.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_container5.setLayout(new javax.swing.BoxLayout(pnl_container5, javax.swing.BoxLayout.LINE_AXIS));

        lbl_productInventory.setText("Tồn kho");
        lbl_productInventory.setMaximumSize(null);
        lbl_productInventory.setMinimumSize(new java.awt.Dimension(110, 30));
        lbl_productInventory.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_container5.add(lbl_productInventory);

        txt_productInventory.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_productInventory.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_productInventory.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container5.add(txt_productInventory);

        pnl_productCenter.add(pnl_container5);

        pnl_container6.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_container6.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_container6.setLayout(new javax.swing.BoxLayout(pnl_container6, javax.swing.BoxLayout.LINE_AXIS));

        lbl_productVAT.setText("VAT");
        lbl_productVAT.setMaximumSize(null);
        lbl_productVAT.setMinimumSize(new java.awt.Dimension(110, 30));
        lbl_productVAT.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_container6.add(lbl_productVAT);

        txt_productVAT.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_productVAT.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_productVAT.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container6.add(txt_productVAT);

        pnl_productCenter.add(pnl_container6);

        pnl_container2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_container2.setLayout(new javax.swing.BoxLayout(pnl_container2, javax.swing.BoxLayout.LINE_AXIS));

        lbl_productType.setText("Loại sản phẩm");
        lbl_productType.setMaximumSize(null);
        lbl_productType.setMinimumSize(new java.awt.Dimension(110, 30));
        lbl_productType.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_container2.add(lbl_productType);

        cmb_productType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sách", "Văn phòng phẩm" }));
        cmb_productType.setMaximumSize(new java.awt.Dimension(9999, 40));
        cmb_productType.setMinimumSize(new java.awt.Dimension(100, 35));
        cmb_productType.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container2.add(cmb_productType);

        pnl_productCenter.add(pnl_container2);

        pnl_productInfo.add(pnl_productCenter);

        pnl_rightCenter.add(pnl_productInfo);
        pnl_rightCenter.add(filler1);

        pnl_bookDetail.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin chi tiết sách"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnl_bookDetail.setPreferredSize(new java.awt.Dimension(400, 400));
        pnl_bookDetail.setLayout(new javax.swing.BoxLayout(pnl_bookDetail, javax.swing.BoxLayout.Y_AXIS));

        pnl_bookAuthor.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnl_bookAuthor.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_bookAuthor.setLayout(new javax.swing.BoxLayout(pnl_bookAuthor, javax.swing.BoxLayout.LINE_AXIS));

        lbl_bookAuthor.setText("Tác giả");
        lbl_bookAuthor.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_bookAuthor.add(lbl_bookAuthor);

        txt_bookAuthor.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_bookAuthor.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_bookAuthor.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_bookAuthor.add(txt_bookAuthor);

        pnl_bookDetail.add(pnl_bookAuthor);

        pnl_bookPublisher.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnl_bookPublisher.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_bookPublisher.setLayout(new javax.swing.BoxLayout(pnl_bookPublisher, javax.swing.BoxLayout.LINE_AXIS));

        lbl_bookPublisher.setText("NXB");
        lbl_bookPublisher.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_bookPublisher.add(lbl_bookPublisher);

        txt_bookPublisher.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_bookPublisher.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_bookPublisher.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_bookPublisher.add(txt_bookPublisher);

        pnl_bookDetail.add(pnl_bookPublisher);

        pnl_container15.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnl_container15.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container15.setLayout(new javax.swing.BoxLayout(pnl_container15, javax.swing.BoxLayout.LINE_AXIS));

        lbl_bookTranslator.setText("Dịch giả");
        lbl_bookTranslator.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_container15.add(lbl_bookTranslator);

        txt_bookTranslator.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_bookTranslator.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_bookTranslator.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container15.add(txt_bookTranslator);

        pnl_bookDetail.add(pnl_container15);

        pnl_container18.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnl_container18.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container18.setLayout(new javax.swing.BoxLayout(pnl_container18, javax.swing.BoxLayout.LINE_AXIS));

        lbl_bookCategory1.setText("Danh mục");
        lbl_bookCategory1.setMaximumSize(new java.awt.Dimension(40, 16));
        lbl_bookCategory1.setMinimumSize(new java.awt.Dimension(40, 30));
        lbl_bookCategory1.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_container18.add(lbl_bookCategory1);

        cmb_bookCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Trinh thám" }));
        cmb_bookCategory.setMaximumSize(new java.awt.Dimension(9999, 40));
        cmb_bookCategory.setMinimumSize(new java.awt.Dimension(100, 35));
        cmb_bookCategory.setPreferredSize(new java.awt.Dimension(120, 30));
        pnl_container18.add(cmb_bookCategory);

        pnl_bookDetail.add(pnl_container18);

        pnl_bookCenter.setMinimumSize(new java.awt.Dimension(300, 135));
        pnl_bookCenter.setLayout(new java.awt.GridLayout(4, 2, 5, 5));

        pnl_container11.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container11.setLayout(new javax.swing.BoxLayout(pnl_container11, javax.swing.BoxLayout.LINE_AXIS));

        lbl_bookPublishDate.setText("Năm xuất bản");
        lbl_bookPublishDate.setToolTipText("");
        lbl_bookPublishDate.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_container11.add(lbl_bookPublishDate);

        txt_bookPublishDate.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_bookPublishDate.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_bookPublishDate.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container11.add(txt_bookPublishDate);

        pnl_bookCenter.add(pnl_container11);

        pnl_container12.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container12.setLayout(new javax.swing.BoxLayout(pnl_container12, javax.swing.BoxLayout.LINE_AXIS));

        lbl_bookHardCover.setText("Bìa cứng");
        lbl_bookHardCover.setPreferredSize(new java.awt.Dimension(100, 40));
        pnl_container12.add(lbl_bookHardCover);

        cmb_bookHardCover.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Có", "Không" }));
        cmb_bookHardCover.setMaximumSize(new java.awt.Dimension(9999, 40));
        cmb_bookHardCover.setMinimumSize(new java.awt.Dimension(100, 35));
        cmb_bookHardCover.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container12.add(cmb_bookHardCover);

        pnl_bookCenter.add(pnl_container12);

        pnl_container13.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container13.setLayout(new javax.swing.BoxLayout(pnl_container13, javax.swing.BoxLayout.LINE_AXIS));

        lbl_bookQuantityPage.setText("Số trang");
        lbl_bookQuantityPage.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_container13.add(lbl_bookQuantityPage);

        txt_bookQuantityPage.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_bookQuantityPage.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_bookQuantityPage.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container13.add(txt_bookQuantityPage);

        pnl_bookCenter.add(pnl_container13);

        pnl_container14.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container14.setLayout(new javax.swing.BoxLayout(pnl_container14, javax.swing.BoxLayout.LINE_AXIS));

        lbl_bookLanguage.setText("Ngôn ngữ");
        lbl_bookLanguage.setPreferredSize(new java.awt.Dimension(100, 40));
        pnl_container14.add(lbl_bookLanguage);

        txt_bookLanguage.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_bookLanguage.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_bookLanguage.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container14.add(txt_bookLanguage);

        pnl_bookCenter.add(pnl_container14);

        pnl_container17.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container17.setLayout(new javax.swing.BoxLayout(pnl_container17, javax.swing.BoxLayout.LINE_AXIS));

        lbl_bookType.setText("Danh mục");
        lbl_bookType.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_container17.add(lbl_bookType);

        cmb_bookType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nội", "Ngoại" }));
        cmb_bookType.setMaximumSize(new java.awt.Dimension(9999, 40));
        cmb_bookType.setMinimumSize(new java.awt.Dimension(100, 35));
        cmb_bookType.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container17.add(cmb_bookType);

        pnl_bookCenter.add(pnl_container17);

        pnl_bookDetail.add(pnl_bookCenter);

        pnl_container7.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pnl_container7.setMinimumSize(new java.awt.Dimension(200, 110));
        pnl_container7.setPreferredSize(new java.awt.Dimension(279, 140));
        pnl_container7.setLayout(new javax.swing.BoxLayout(pnl_container7, javax.swing.BoxLayout.Y_AXIS));

        pnl_container16.setLayout(new java.awt.GridLayout(1, 0));

        lbl_bookDescription.setText("Mô tả");
        lbl_bookDescription.setPreferredSize(new java.awt.Dimension(100, 20));
        pnl_container16.add(lbl_bookDescription);

        pnl_container7.add(pnl_container16);

        scr_productName1.setMinimumSize(new java.awt.Dimension(100, 100));

        txa_bookDescription.setColumns(15);
        txa_bookDescription.setLineWrap(true);
        txa_bookDescription.setRows(4);
        txa_bookDescription.setTabSize(4);
        scr_productName1.setViewportView(txa_bookDescription);

        pnl_container7.add(scr_productName1);

        pnl_bookDetail.add(pnl_container7);

        pnl_rightCenter.add(pnl_bookDetail);

        pnl_stationeryDetail.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin chi tiết văn phòng phẩm"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnl_stationeryDetail.setMinimumSize(new java.awt.Dimension(425, 200));
        pnl_stationeryDetail.setPreferredSize(new java.awt.Dimension(400, 300));
        pnl_stationeryDetail.setLayout(new javax.swing.BoxLayout(pnl_stationeryDetail, javax.swing.BoxLayout.Y_AXIS));

        pnl_stationery.setLayout(new java.awt.GridLayout(5, 1, 5, 5));

        pnl_container23.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container23.setLayout(new javax.swing.BoxLayout(pnl_container23, javax.swing.BoxLayout.LINE_AXIS));

        lbl_stationeryColor.setText("Màu sắc");
        lbl_stationeryColor.setToolTipText("");
        lbl_stationeryColor.setPreferredSize(new java.awt.Dimension(110, 30));
        lbl_stationeryColor.setRequestFocusEnabled(false);
        pnl_container23.add(lbl_stationeryColor);

        txt_stationeryColor.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_stationeryColor.setMinimumSize(new java.awt.Dimension(100, 30));
        txt_stationeryColor.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container23.add(txt_stationeryColor);

        pnl_stationery.add(pnl_container23);

        pnl_container24.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container24.setLayout(new javax.swing.BoxLayout(pnl_container24, javax.swing.BoxLayout.LINE_AXIS));

        lbl_stationeryType.setText("Loại VPP");
        lbl_stationeryType.setMinimumSize(new java.awt.Dimension(120, 30));
        lbl_stationeryType.setPreferredSize(new java.awt.Dimension(110, 30));
        lbl_stationeryType.setRequestFocusEnabled(false);
        pnl_container24.add(lbl_stationeryType);

        cmb_stationeryType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bút-viết", "Dụng cụ học sinh" }));
        cmb_stationeryType.setMaximumSize(new java.awt.Dimension(9999, 40));
        cmb_stationeryType.setMinimumSize(new java.awt.Dimension(100, 30));
        cmb_stationeryType.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container24.add(cmb_stationeryType);

        pnl_stationery.add(pnl_container24);

        pnl_container25.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container25.setLayout(new javax.swing.BoxLayout(pnl_container25, javax.swing.BoxLayout.LINE_AXIS));

        lbl_stationeryOrigin.setText("Xuất xứ");
        lbl_stationeryOrigin.setPreferredSize(new java.awt.Dimension(110, 30));
        lbl_stationeryOrigin.setRequestFocusEnabled(false);
        pnl_container25.add(lbl_stationeryOrigin);

        txt_stationeryOrigin.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_stationeryOrigin.setMinimumSize(new java.awt.Dimension(100, 30));
        txt_stationeryOrigin.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container25.add(txt_stationeryOrigin);

        pnl_stationery.add(pnl_container25);

        pnl_container26.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container26.setLayout(new javax.swing.BoxLayout(pnl_container26, javax.swing.BoxLayout.LINE_AXIS));

        lbl_stationeryWeight.setText("Trọng lượng");
        lbl_stationeryWeight.setPreferredSize(new java.awt.Dimension(110, 30));
        lbl_stationeryWeight.setRequestFocusEnabled(false);
        pnl_container26.add(lbl_stationeryWeight);

        txt_stationeryWeight.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_stationeryWeight.setMinimumSize(new java.awt.Dimension(100, 30));
        txt_stationeryWeight.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container26.add(txt_stationeryWeight);

        pnl_stationery.add(pnl_container26);

        pnl_container28.setMinimumSize(new java.awt.Dimension(200, 30));
        pnl_container28.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container28.setLayout(new javax.swing.BoxLayout(pnl_container28, javax.swing.BoxLayout.LINE_AXIS));

        lbl_stationeryBrand.setText("Nhãn hàng");
        lbl_stationeryBrand.setMinimumSize(new java.awt.Dimension(120, 30));
        lbl_stationeryBrand.setPreferredSize(new java.awt.Dimension(110, 30));
        lbl_stationeryBrand.setRequestFocusEnabled(false);
        pnl_container28.add(lbl_stationeryBrand);

        cmb_stationeryBrand.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhãn hàng 1", "Nhãn hàng 2", " " }));
        cmb_stationeryBrand.setMaximumSize(new java.awt.Dimension(9999, 40));
        cmb_stationeryBrand.setMinimumSize(new java.awt.Dimension(100, 30));
        cmb_stationeryBrand.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container28.add(cmb_stationeryBrand);

        pnl_stationery.add(pnl_container28);

        pnl_stationeryDetail.add(pnl_stationery);

        pnl_rightCenter.add(pnl_stationeryDetail);

        scr_detail.setViewportView(pnl_rightCenter);

        pnl_right.add(scr_detail, java.awt.BorderLayout.CENTER);

        pnl_control.setPreferredSize(new java.awt.Dimension(281, 50));
        pnl_control.setLayout(new java.awt.GridLayout(1, 3));

        btn_clear.setText("Xóa trắng");
        btn_clear.setToolTipText("");
        pnl_control.add(btn_clear);

        btn_update.setText("Cập nhật");
        pnl_control.add(btn_update);

        btn_add.setText("Thêm sản phẩm mới");
        btn_add.setActionCommand("");
        btn_add.setIcon(SVGIcon.getSVGIcon("imgs/menu/1.svg"));
        pnl_control.add(btn_add);

        pnl_right.add(pnl_control, java.awt.BorderLayout.SOUTH);

        splitPane_main.setRightComponent(pnl_right);

        add(splitPane_main, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void cmb_typeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_typeActionPerformed

        renderComboboxType();
    }//GEN-LAST:event_cmb_typeActionPerformed

    private void btn_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nextActionPerformed
        this.currentPage++;
        renderCurrentPage();
    }//GEN-LAST:event_btn_nextActionPerformed

    private void btn_previousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_previousActionPerformed
        this.currentPage--;
        renderCurrentPage();
    }//GEN-LAST:event_btn_previousActionPerformed

    private void btn_selectImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_selectImgActionPerformed
        int isSelected = fileChooser_productImg.showOpenDialog(this);
//        Nếu người dùng có chọn file
        if (isSelected == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser_productImg.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }
    }//GEN-LAST:event_btn_selectImgActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_filter;
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_previous;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_selectImg;
    private javax.swing.JButton btn_update;
    private javax.swing.JCheckBox chk_empty;
    private javax.swing.JComboBox<String> cmb_bookCategory;
    private javax.swing.JComboBox<String> cmb_bookHardCover;
    private javax.swing.JComboBox<String> cmb_bookType;
    private javax.swing.JComboBox<String> cmb_productType;
    private javax.swing.JComboBox<String> cmb_stationeryBrand;
    private javax.swing.JComboBox<String> cmb_stationeryType;
    private javax.swing.JComboBox<String> cmb_type;
    private javax.swing.JComboBox<String> cmb_typeDetail;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel lbl_bookAuthor;
    private javax.swing.JLabel lbl_bookCategory1;
    private javax.swing.JLabel lbl_bookDescription;
    private javax.swing.JLabel lbl_bookHardCover;
    private javax.swing.JLabel lbl_bookLanguage;
    private javax.swing.JLabel lbl_bookPublishDate;
    private javax.swing.JLabel lbl_bookPublisher;
    private javax.swing.JLabel lbl_bookQuantityPage;
    private javax.swing.JLabel lbl_bookTranslator;
    private javax.swing.JLabel lbl_bookType;
    private javax.swing.JLabel lbl_name;
    private javax.swing.JLabel lbl_pageNumber;
    private javax.swing.JLabel lbl_productCostPrice;
    private javax.swing.JLabel lbl_productId;
    private javax.swing.JLabel lbl_productImg;
    private javax.swing.JLabel lbl_productInventory;
    private javax.swing.JLabel lbl_productName;
    private javax.swing.JLabel lbl_productPrice;
    private javax.swing.JLabel lbl_productType;
    private javax.swing.JLabel lbl_productVAT;
    private javax.swing.JLabel lbl_stationeryBrand;
    private javax.swing.JLabel lbl_stationeryColor;
    private javax.swing.JLabel lbl_stationeryOrigin;
    private javax.swing.JLabel lbl_stationeryType;
    private javax.swing.JLabel lbl_stationeryWeight;
    private javax.swing.JLabel lbl_type;
    private javax.swing.JLabel lbl_typeDetail;
    private javax.swing.JPanel pnl_bookAuthor;
    private javax.swing.JPanel pnl_bookCenter;
    private javax.swing.JPanel pnl_bookDetail;
    private javax.swing.JPanel pnl_bookPublisher;
    private javax.swing.JPanel pnl_cart;
    private javax.swing.JPanel pnl_cartFooter;
    private javax.swing.JPanel pnl_container;
    private javax.swing.JPanel pnl_container1;
    private javax.swing.JPanel pnl_container11;
    private javax.swing.JPanel pnl_container12;
    private javax.swing.JPanel pnl_container13;
    private javax.swing.JPanel pnl_container14;
    private javax.swing.JPanel pnl_container15;
    private javax.swing.JPanel pnl_container16;
    private javax.swing.JPanel pnl_container17;
    private javax.swing.JPanel pnl_container18;
    private javax.swing.JPanel pnl_container2;
    private javax.swing.JPanel pnl_container20;
    private javax.swing.JPanel pnl_container21;
    private javax.swing.JPanel pnl_container23;
    private javax.swing.JPanel pnl_container24;
    private javax.swing.JPanel pnl_container25;
    private javax.swing.JPanel pnl_container26;
    private javax.swing.JPanel pnl_container28;
    private javax.swing.JPanel pnl_container3;
    private javax.swing.JPanel pnl_container4;
    private javax.swing.JPanel pnl_container5;
    private javax.swing.JPanel pnl_container6;
    private javax.swing.JPanel pnl_container7;
    private javax.swing.JPanel pnl_control;
    private javax.swing.JPanel pnl_empty;
    private javax.swing.JPanel pnl_filter;
    private javax.swing.JPanel pnl_header;
    private javax.swing.JPanel pnl_left;
    private javax.swing.JPanel pnl_name;
    private javax.swing.JPanel pnl_orderId;
    private javax.swing.JPanel pnl_productCenter;
    private javax.swing.JPanel pnl_productInfo;
    private javax.swing.JPanel pnl_productTop;
    private javax.swing.JPanel pnl_productTopLeft;
    private javax.swing.JPanel pnl_productTopRight;
    private javax.swing.JPanel pnl_right;
    private javax.swing.JPanel pnl_rightCenter;
    private javax.swing.JPanel pnl_search;
    private javax.swing.JPanel pnl_stationery;
    private javax.swing.JPanel pnl_stationeryDetail;
    private javax.swing.JPanel pnl_type;
    private javax.swing.JPanel pnl_typeDetail;
    private javax.swing.JScrollPane scr_cart;
    private javax.swing.JScrollPane scr_detail;
    private javax.swing.JScrollPane scr_productName;
    private javax.swing.JScrollPane scr_productName1;
    private javax.swing.JSplitPane splitPane_main;
    private javax.swing.JTable tbl_products;
    private javax.swing.JTextArea txa_bookDescription;
    private javax.swing.JTextArea txa_productName;
    private javax.swing.JTextField txt_bookAuthor;
    private javax.swing.JTextField txt_bookLanguage;
    private javax.swing.JTextField txt_bookPublishDate;
    private javax.swing.JTextField txt_bookPublisher;
    private javax.swing.JTextField txt_bookQuantityPage;
    private javax.swing.JTextField txt_bookTranslator;
    private javax.swing.JTextField txt_name;
    private javax.swing.JTextField txt_productCostPrice;
    private javax.swing.JTextField txt_productId;
    private javax.swing.JTextField txt_productInventory;
    private javax.swing.JTextField txt_productPrice;
    private javax.swing.JTextField txt_productVAT;
    private javax.swing.JTextField txt_search;
    private javax.swing.JTextField txt_stationeryColor;
    private javax.swing.JTextField txt_stationeryOrigin;
    private javax.swing.JTextField txt_stationeryWeight;
    // End of variables declaration//GEN-END:variables
}

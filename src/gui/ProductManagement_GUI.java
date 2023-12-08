/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import bus.ProductManagement_BUS;
import com.formdev.flatlaf.FlatClientProperties;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.Book;
import entity.Brand;
import entity.Product;
import entity.Stationery;
import enums.BookCategory;
import enums.BookType;
import enums.StationeryType;
import enums.Type;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import raven.toast.Notifications;
import utilities.FormatNumber;
import utilities.SVGIcon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.io.FileOutputStream;
import java.util.Date;
import main.Application;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilities.BarcodeGenerator;
import static utilities.OrderPrinter.FONT;

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
    private DefaultComboBoxModel cmbModel_stationeryBrand;

//    Internal frame
    JFileChooser fileChooser_productImg;

//    state
    private Product currentProduct = null;
    private int currentPage;
    private int lastPage;
    private ArrayList<Product> listExport = new ArrayList<>();

    public ProductManagement_GUI() {
        initComponents();
        init();
    }

    private void init() {
        bus = new ProductManagement_BUS();

//        Frame
        fileChooser_productImg = new javax.swing.JFileChooser();
        fileChooser_productImg.setCurrentDirectory(new File(System.getProperty("user.home")));

//        Table
        initTable();

//        Page
        this.lastPage = bus.getLastPage();
        this.currentPage = 1;
        renderCurrentPage();

//        Combobox model
        initCombobox();

//      Product detail
        scr_bookDetail.setVisible(false);
        scr_stationeryDetail.setVisible(false);

        cmb_productType.addActionListener((ActionEvent e) -> {
            toggleProductDetail();
        });
    }

    private void formatTable() {
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);

//        Align
        TableColumnModel columnModel = tbl_products.getColumnModel();
        for (int index : new Integer[]{2, 3, 4}) {
            columnModel.getColumn(index).setCellRenderer(rightAlign);
        }
    }

    private void initTable() {
        tblModel_products = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Giá nhập", "Giá bán", "Số lượng tồn"}, 50) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbl_products.setModel(tblModel_products);
        tbl_products.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            int rowIndex = tbl_products.getSelectedRow();
            if (rowIndex == -1) {
                return;
            }

            String productID = tbl_products.getValueAt(rowIndex, 0).toString();
            this.currentProduct = bus.getProduct(productID);
            fileChooser_productImg.setSelectedFile(null);
            renderCurrentProduct();

        });

        formatTable();
    }

    private void initCombobox() {
        cmbModel_type = new DefaultComboBoxModel(new String[]{"Tất cả", "Sách", "Văn phòng phẩm"});
        cmbModel_bookCategory = new DefaultComboBoxModel(new String[]{"Văn học", "Kinh tế", "Tâm lý - kỹ năng sống", "Thiếu nhi", "Nuôi dạy con", "Tiểu sử - hồi ký", "Sách giáo khoa - tham khảo", "Sách học ngoại ngữ"});
        cmbModel_stationeryType = new DefaultComboBoxModel(new String[]{"Bút - viết", "Dụng cụ học sinh", "Dụng cụ văn phòng", "Dụng cụ vẽ", "Sản phẩm về giấy", "Sản phẩm khác"});
        cmb_type.setModel(cmbModel_type);
        cmb_stationeryType.setModel(cmbModel_stationeryType);
        cmb_bookCategory.setModel(cmbModel_bookCategory);

        renderComboboxType();
        renderBrand();
    }

    private void renderBrand() {
        Object[] items = bus.getAllBrand().toArray();
        cmbModel_stationeryBrand = new DefaultComboBoxModel(items);
        cmb_stationeryBrand.setModel(cmbModel_stationeryBrand);
    }

    private String getBrandIDSelected() {
        Pattern pattern = Pattern.compile("\\(([^\\)]+)\\)");
        Matcher matcher = pattern.matcher(cmbModel_stationeryBrand.getSelectedItem().toString());

        if (matcher.find()) {
            String id = matcher.group(0);
            id = id.replaceAll("\\(", "");
            id = id.replaceAll("\\)", "");
            return id;
        }
        return null;
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
        if (currentProduct.getImage() != null) {
            ImageIcon imageIcon = new ImageIcon(currentProduct.getImage());
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(lbl_productImg.getWidth(), lbl_productImg.getHeight(), Image.SCALE_SMOOTH | Image.SCALE_AREA_AVERAGING);

            // Tạo lại đối tượng ImageIcon với kích thước mới
            imageIcon = new ImageIcon(scaledImage);
            lbl_productImg.setIcon(imageIcon);
            lbl_productImg.setText("");
        } else {
            lbl_productImg.setIcon(null);
            lbl_productImg.setText("Không có hình ảnh mô tả");
        }

//        Update detail form
        if (currentProduct.getType() == Type.BOOK) {
            Book book = (Book) currentProduct;
            txt_bookAuthor.setText(book.getAuthor());
            txt_bookLanguage.setText(book.getLanguage() == null ? "Tiếng Việt" : "Tiếng Anh");
            txt_bookPublisher.setText(book.getPublisher());
            txt_bookPublishDate.setText(book.getPublishYear().toString());
            txt_bookTranslator.setText(book.getTranslator());
            txt_bookQuantityPage.setText(book.getPageQuantity().toString());
            cmb_bookCategory.setSelectedIndex(book.getBookCategory().getValue() - 1);
            cmb_bookHardCover.setSelectedIndex(book.getIsHardCover() ? 0 : 1);
            cmb_bookType.setSelectedIndex(book.getBookOrigin().getValue() - 1);
        } else {
            Stationery stationery = (Stationery) currentProduct;
            txt_stationeryColor.setText(stationery.getColor());
            txt_stationeryOrigin.setText(stationery.getOrigin());
            txt_stationeryWeight.setText(stationery.getWeight().toString());
            txt_stationeryMaterial.setText(stationery.getMaterial());
            cmb_stationeryType.setSelectedIndex(stationery.getStationeryType().getValue() - 1);
            Brand brand = stationery.getBrand();
            cmb_stationeryBrand.setSelectedItem(brand);
        }
    }

    private void renderCurrentPage() {
        lbl_pageNumber.setText(currentPage + "/" + lastPage);
        renderProductsTable(bus.getDataOfPage(currentPage));

//      Toggle button
        btn_previous.setEnabled(currentPage > 1);
        btn_next.setEnabled(currentPage < lastPage);
    }

    private void renderProductsTable(ArrayList<Product> productList) {
        tblModel_products.setRowCount(0);
        for (Product product : productList) {
            if (product == null) {
                continue;
            }
            Object[] newRow = new Object[]{product.getProductID(), product.getName(), FormatNumber.toVND(product.getCostPrice()), FormatNumber.toVND(product.getPrice()), product.getInventory()};
            tblModel_products.addRow(newRow);
        }
    }

    private void renderComboboxType() {
        String currentType = cmbModel_type.getSelectedItem().toString();

        cmb_typeDetail.setEnabled(!currentType.equals("Tất cả"));

        switch (currentType) {

            case "Tất cả":

                return;
            case "Sách":
                cmbModel_subType = new DefaultComboBoxModel(new String[]{"Tất cả", "Văn học", "Kinh tế", "Tâm lý - kỹ năng sống", "Thiếu nhi", "Nuôi dạy con", "Tiểu sử - hồi ký", "Sách giáo khoa - tham khảo", "Sách học ngoại ngữ"});
                cmb_typeDetail.setModel(cmbModel_subType);
                break;
            case "Văn phòng phẩm":
                cmbModel_subType = new DefaultComboBoxModel(new String[]{"Tất cả", "Bút - viết", "Dụng cụ học sinh", "Dụng cụ văn phòng", "Dụng cụ vẽ", "Sản phẩm về giấy", "Sản phẩm khác"});
                cmb_typeDetail.setModel(cmbModel_subType);
                break;
            default:
                break;
        }

    }

    private void renderComboboxTypeEx() {
        String currentType = cbo_typeEx.getSelectedItem().toString();
        cbo_typeEx.setEnabled(!currentType.equals("Tất cả"));
        if (currentType.equals("Tất cả")) {
            cbo_categoryEx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Tất cả"}));
        } else if (currentType.equals("Sách")) {
            cbo_categoryEx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Tất cả", "Văn học", "Kinh tế", "Tâm lý - kỹ năng sống", "Thiếu nhi", "Nuôi dạy con", "Tiểu sử - hồi ký", "Sách giáo khoa - tham khảo", "Sách học ngoại ngữ"}));

        } else {
            cbo_categoryEx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Tất cả", "Bút - viết", "Dụng cụ học sinh", "Dụng cụ văn phòng", "Dụng cụ vẽ", "Sản phẩm về giấy", "Sản phẩm khác"}));

        }
    }

    private void toggleProductDetail() {
        int selectionIndex = cmb_productType.getSelectedIndex();
        boolean isBook = selectionIndex == 0;
        scr_bookDetail.setVisible(isBook);
        scr_stationeryDetail.setVisible(!isBook);

        pnl_rightCenter.revalidate();
        pnl_rightCenter.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnl_exportOption = new javax.swing.JFrame();
        pnl_header1 = new javax.swing.JPanel();
        lbl_title = new javax.swing.JLabel();
        pnl_body = new javax.swing.JPanel();
        pnl_type1 = new javax.swing.JPanel();
        lbl_type1 = new javax.swing.JLabel();
        cbo_typeEx = new javax.swing.JComboBox<>();
        pnl_category = new javax.swing.JPanel();
        lbl_category = new javax.swing.JLabel();
        cbo_categoryEx = new javax.swing.JComboBox<>();
        pnl_footer = new javax.swing.JPanel();
        bnt_cancel = new javax.swing.JButton();
        btn_export = new javax.swing.JButton();
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
        btn_reset = new javax.swing.JButton();
        btn_previous = new javax.swing.JButton();
        lbl_pageNumber = new javax.swing.JLabel();
        btn_next = new javax.swing.JButton();
        btn_generateBarcode = new javax.swing.JButton();
        btn_exportExcel = new javax.swing.JButton();
        pnl_right = new javax.swing.JPanel();
        pnl_control = new javax.swing.JPanel();
        btn_clear = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        btn_add = new javax.swing.JButton();
        pnl_rightCenter = new javax.swing.JPanel();
        scr_productInfo = new javax.swing.JScrollPane();
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
        pnl_containerName = new javax.swing.JPanel();
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
        scr_stationeryDetail = new javax.swing.JScrollPane();
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
        pnl_container27 = new javax.swing.JPanel();
        lbl_stationeryMaterial = new javax.swing.JLabel();
        txt_stationeryMaterial = new javax.swing.JTextField();
        pnl_container28 = new javax.swing.JPanel();
        lbl_stationeryBrand = new javax.swing.JLabel();
        cmb_stationeryBrand = new javax.swing.JComboBox<>();
        scr_bookDetail = new javax.swing.JScrollPane();
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
        pnl_container17 = new javax.swing.JPanel();
        lbl_bookType = new javax.swing.JLabel();
        cmb_bookType = new javax.swing.JComboBox<>();
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
        pnl_container7 = new javax.swing.JPanel();
        pnl_container16 = new javax.swing.JPanel();
        lbl_bookDescription = new javax.swing.JLabel();
        scr_productDescription = new javax.swing.JScrollPane();
        txa_bookDescription = new javax.swing.JTextArea();

        pnl_exportOption.setMinimumSize(new java.awt.Dimension(400, 300));
        pnl_exportOption.setLocationRelativeTo(null);
        pnl_exportOption.setResizable(false);

        pnl_header1.setPreferredSize(new java.awt.Dimension(400, 40));

        lbl_title.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_title.setText("TÙY CHỌN");
        lbl_title.setToolTipText("");
        lbl_title.setPreferredSize(new java.awt.Dimension(95, 40));
        pnl_header1.add(lbl_title);

        pnl_exportOption.getContentPane().add(pnl_header1, java.awt.BorderLayout.NORTH);

        pnl_body.setPreferredSize(new java.awt.Dimension(400, 150));
        pnl_body.setLayout(new javax.swing.BoxLayout(pnl_body, javax.swing.BoxLayout.Y_AXIS));

        pnl_type1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 1, 5));
        pnl_type1.setMaximumSize(new java.awt.Dimension(32805, 50));
        pnl_type1.setPreferredSize(new java.awt.Dimension(100, 50));
        pnl_type1.setLayout(new javax.swing.BoxLayout(pnl_type1, javax.swing.BoxLayout.LINE_AXIS));

        lbl_type1.setText("Loại sản phẩm:");
        lbl_type1.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl_type1.setMinimumSize(new java.awt.Dimension(100, 16));
        lbl_type1.setPreferredSize(new java.awt.Dimension(110, 16));
        pnl_type1.add(lbl_type1);

        cbo_typeEx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Sách", "Văn phòng phẩm" }));
        cbo_typeEx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_typeExActionPerformed(evt);
            }
        });
        pnl_type1.add(cbo_typeEx);

        pnl_body.add(pnl_type1);

        pnl_category.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 1, 5), null));
        pnl_category.setMaximumSize(new java.awt.Dimension(32805, 50));
        pnl_category.setPreferredSize(new java.awt.Dimension(110, 50));
        pnl_category.setLayout(new javax.swing.BoxLayout(pnl_category, javax.swing.BoxLayout.LINE_AXIS));

        lbl_category.setText("Danh mục:");
        lbl_category.setToolTipText("");
        lbl_category.setPreferredSize(new java.awt.Dimension(110, 16));
        pnl_category.add(lbl_category);

        cbo_categoryEx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        pnl_category.add(cbo_categoryEx);

        pnl_body.add(pnl_category);

        pnl_exportOption.getContentPane().add(pnl_body, java.awt.BorderLayout.CENTER);

        pnl_footer.setPreferredSize(new java.awt.Dimension(400, 50));

        bnt_cancel.setText("Hủy");
        bnt_cancel.setPreferredSize(new java.awt.Dimension(72, 40));
        bnt_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnt_cancelActionPerformed(evt);
            }
        });
        pnl_footer.add(bnt_cancel);

        btn_export.setText("Xuất");
        btn_export.setPreferredSize(new java.awt.Dimension(72, 40));
        btn_export.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btn_exportAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btn_export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exportActionPerformed(evt);
            }
        });
        pnl_footer.add(btn_export);

        pnl_exportOption.getContentPane().add(pnl_footer, java.awt.BorderLayout.SOUTH);

        setPreferredSize(new java.awt.Dimension(400, 300));
        setLayout(new java.awt.BorderLayout());

        splitPane_main.setResizeWeight(0.7);
        splitPane_main.setMinimumSize(new java.awt.Dimension(1305, 768));

        pnl_left.setMinimumSize(new java.awt.Dimension(400, 59));
        pnl_left.setPreferredSize(new java.awt.Dimension(800, 768));
        pnl_left.setLayout(new java.awt.BorderLayout());

        pnl_header.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm & Lọc", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
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
        btn_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchActionPerformed(evt);
            }
        });
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
        btn_filter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_filterActionPerformed(evt);
            }
        });
        pnl_container21.add(btn_filter);

        pnl_filter.add(pnl_container21);

        pnl_header.add(pnl_filter);

        pnl_left.add(pnl_header, java.awt.BorderLayout.NORTH);

        pnl_cart.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        pnl_cart.setLayout(new java.awt.BorderLayout());

        tbl_products.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tbl_products.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbl_products.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr_cart.setViewportView(tbl_products);

        pnl_cart.add(scr_cart, java.awt.BorderLayout.CENTER);

        pnl_cartFooter.setMinimumSize(new java.awt.Dimension(509, 50));
        pnl_cartFooter.setPreferredSize(new java.awt.Dimension(800, 50));
        pnl_cartFooter.setLayout(new javax.swing.BoxLayout(pnl_cartFooter, javax.swing.BoxLayout.LINE_AXIS));

        btn_reset.setMinimumSize(new java.awt.Dimension(100, 50));
        btn_reset.setIcon(SVGIcon.getSVGIcon("imgs/public/refresh.svg"));
        btn_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_resetActionPerformed(evt);
            }
        });
        pnl_cartFooter.add(btn_reset);

        btn_previous.setMinimumSize(new java.awt.Dimension(100, 50));
        btn_previous.setIcon(SVGIcon.getSVGIcon("imgs/public/prev.svg"));
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
        lbl_pageNumber.setMaximumSize(null);
        lbl_pageNumber.setMinimumSize(new java.awt.Dimension(100, 50));
        pnl_cartFooter.add(lbl_pageNumber);

        btn_next.setMinimumSize(new java.awt.Dimension(100, 50));
        btn_next.setIcon(SVGIcon.getSVGIcon("imgs/public/next.svg"));
        btn_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nextActionPerformed(evt);
            }
        });
        pnl_cartFooter.add(btn_next);

        btn_generateBarcode.setText("Tạo file barcode");
        btn_generateBarcode.setFocusPainted(false);
        btn_generateBarcode.setMaximumSize(null);
        btn_generateBarcode.setMinimumSize(new java.awt.Dimension(100, 50));
        btn_generateBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_generateBarcodeActionPerformed(evt);
            }
        });
        btn_generateBarcode.setIcon(SVGIcon.getSVGIcon("imgs/public/barcode.svg"));
        pnl_cartFooter.add(btn_generateBarcode);

        btn_exportExcel.setText("Xuất");
        btn_exportExcel.setActionCommand("");
        btn_exportExcel.setMaximumSize(null);
        btn_exportExcel.setMinimumSize(new java.awt.Dimension(100, 50));
        btn_exportExcel.setIcon(SVGIcon.getSVGIcon("imgs/public/excel.svg"));
        btn_exportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exportExcelActionPerformed(evt);
            }
        });
        pnl_cartFooter.add(btn_exportExcel);

        pnl_cart.add(pnl_cartFooter, java.awt.BorderLayout.PAGE_END);

        pnl_left.add(pnl_cart, java.awt.BorderLayout.CENTER);

        splitPane_main.setLeftComponent(pnl_left);

        pnl_right.setMaximumSize(new java.awt.Dimension(500, 2147483647));
        pnl_right.setMinimumSize(new java.awt.Dimension(350, 39));
        pnl_right.setPreferredSize(new java.awt.Dimension(400, 768));
        pnl_right.setLayout(new java.awt.BorderLayout());

        pnl_control.setPreferredSize(new java.awt.Dimension(281, 50));
        pnl_control.setLayout(new java.awt.GridLayout(1, 3));

        btn_clear.setText("Xóa trắng");
        btn_clear.setToolTipText("");
        btn_clear.setIcon(SVGIcon.getSVGIcon("imgs/public/clear.svg"));
        btn_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearActionPerformed(evt);
            }
        });
        pnl_control.add(btn_clear);

        btn_update.setText("Cập nhật");
        btn_update.setIcon(SVGIcon.getSVGIcon("imgs/public/update.svg"));
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        pnl_control.add(btn_update);

        btn_add.setText("Thêm");
        btn_add.setActionCommand("");
        btn_add.setIcon(SVGIcon.getSVGIcon("imgs/public/add.svg"));
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });
        pnl_control.add(btn_add);

        pnl_right.add(pnl_control, java.awt.BorderLayout.SOUTH);

        pnl_rightCenter.setMinimumSize(new java.awt.Dimension(420, 2000));
        pnl_rightCenter.setName(""); // NOI18N
        pnl_rightCenter.setPreferredSize(new java.awt.Dimension(400, 600));
        pnl_rightCenter.setLayout(new javax.swing.BoxLayout(pnl_rightCenter, javax.swing.BoxLayout.Y_AXIS));

        scr_productInfo.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        scr_productInfo.setMaximumSize(new java.awt.Dimension(32767, 800));
        scr_productInfo.setMinimumSize(new java.awt.Dimension(400, 400));

        pnl_productInfo.setMinimumSize(new java.awt.Dimension(400, 500));
        pnl_productInfo.setPreferredSize(new java.awt.Dimension(400, 500));
        pnl_productInfo.setLayout(new javax.swing.BoxLayout(pnl_productInfo, javax.swing.BoxLayout.Y_AXIS));

        pnl_productTop.setMinimumSize(new java.awt.Dimension(400, 250));
        pnl_productTop.setPreferredSize(new java.awt.Dimension(400, 250));
        pnl_productTop.setLayout(new java.awt.GridLayout(1, 0));

        pnl_productTopLeft.setMinimumSize(new java.awt.Dimension(109, 150));
        pnl_productTopLeft.setLayout(new java.awt.BorderLayout());

        lbl_productImg.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;");
        lbl_productImg.setFont(lbl_productImg.getFont().deriveFont((lbl_productImg.getFont().getStyle() | java.awt.Font.ITALIC)));
        lbl_productImg.setForeground(new java.awt.Color(153, 153, 153));
        lbl_productImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_productImg.setMaximumSize(new java.awt.Dimension(200, 200));
        lbl_productImg.setMinimumSize(new java.awt.Dimension(200, 200));
        pnl_productTopLeft.add(lbl_productImg, java.awt.BorderLayout.CENTER);

        btn_selectImg.setText("Chọn hình ảnh");
        btn_selectImg.setIcon(SVGIcon.getSVGIcon("imgs/productManagement/imgEdit.svg"));
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

        pnl_container.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_container.setPreferredSize(new java.awt.Dimension(100, 40));
        pnl_container.setLayout(new javax.swing.BoxLayout(pnl_container, javax.swing.BoxLayout.LINE_AXIS));

        lbl_productId.setText("Mã sản phẩm");
        lbl_productId.setPreferredSize(new java.awt.Dimension(100, 25));
        pnl_container.add(lbl_productId);

        txt_productId.setEditable(false);
        txt_productId.setText(" SP01221232");
        pnl_container.add(txt_productId);

        pnl_orderId.add(pnl_container);

        pnl_container1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 5, 0));
        pnl_container1.setMinimumSize(new java.awt.Dimension(200, 110));
        pnl_container1.setPreferredSize(new java.awt.Dimension(279, 140));
        pnl_container1.setLayout(new javax.swing.BoxLayout(pnl_container1, javax.swing.BoxLayout.Y_AXIS));

        pnl_containerName.setMaximumSize(new java.awt.Dimension(32767, 40));
        pnl_containerName.setLayout(new java.awt.GridLayout(1, 0));

        lbl_productName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_productName.setText("Tên sản phẩm");
        lbl_productName.setMaximumSize(new java.awt.Dimension(74, 49));
        lbl_productName.setPreferredSize(new java.awt.Dimension(100, 25));
        pnl_containerName.add(lbl_productName);

        pnl_container1.add(pnl_containerName);

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
        lbl_productPrice.setMinimumSize(new java.awt.Dimension(110, 30));
        lbl_productPrice.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_container4.add(lbl_productPrice);

        txt_productPrice.setEditable(false);
        txt_productPrice.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_productPrice.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_productPrice.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container4.add(txt_productPrice);

        pnl_productCenter.add(pnl_container4);

        pnl_container5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_container5.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_container5.setLayout(new javax.swing.BoxLayout(pnl_container5, javax.swing.BoxLayout.LINE_AXIS));

        lbl_productInventory.setText("Tồn kho");
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

        scr_productInfo.setViewportView(pnl_productInfo);

        pnl_rightCenter.add(scr_productInfo);
        pnl_rightCenter.add(filler1);

        scr_stationeryDetail.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết văn phòng phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        scr_stationeryDetail.setMinimumSize(new java.awt.Dimension(300, 300));
        scr_stationeryDetail.setPreferredSize(new java.awt.Dimension(420, 300));

        pnl_stationeryDetail.setMaximumSize(new java.awt.Dimension(899999, 9999));
        pnl_stationeryDetail.setMinimumSize(new java.awt.Dimension(425, 200));
        pnl_stationeryDetail.setPreferredSize(new java.awt.Dimension(400, 300));
        pnl_stationeryDetail.setLayout(new javax.swing.BoxLayout(pnl_stationeryDetail, javax.swing.BoxLayout.Y_AXIS));

        pnl_stationery.setLayout(new javax.swing.BoxLayout(pnl_stationery, javax.swing.BoxLayout.Y_AXIS));

        pnl_container23.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnl_container23.setMaximumSize(new java.awt.Dimension(899999, 35));
        pnl_container23.setMinimumSize(new java.awt.Dimension(100, 35));
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

        pnl_container24.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnl_container24.setMaximumSize(new java.awt.Dimension(899999, 35));
        pnl_container24.setMinimumSize(new java.awt.Dimension(100, 35));
        pnl_container24.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container24.setLayout(new javax.swing.BoxLayout(pnl_container24, javax.swing.BoxLayout.LINE_AXIS));

        lbl_stationeryType.setText("Loại VPP");
        lbl_stationeryType.setMinimumSize(new java.awt.Dimension(120, 30));
        lbl_stationeryType.setPreferredSize(new java.awt.Dimension(110, 30));
        lbl_stationeryType.setRequestFocusEnabled(false);
        pnl_container24.add(lbl_stationeryType);

        cmb_stationeryType.setMaximumSize(new java.awt.Dimension(9999, 40));
        cmb_stationeryType.setMinimumSize(new java.awt.Dimension(100, 30));
        cmb_stationeryType.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container24.add(cmb_stationeryType);

        pnl_stationery.add(pnl_container24);

        pnl_container25.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnl_container25.setMaximumSize(new java.awt.Dimension(899999, 35));
        pnl_container25.setMinimumSize(new java.awt.Dimension(100, 35));
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

        pnl_container26.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnl_container26.setMaximumSize(new java.awt.Dimension(899999, 35));
        pnl_container26.setMinimumSize(new java.awt.Dimension(100, 35));
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

        pnl_container27.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnl_container27.setMaximumSize(new java.awt.Dimension(899999, 35));
        pnl_container27.setMinimumSize(new java.awt.Dimension(100, 35));
        pnl_container27.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container27.setLayout(new javax.swing.BoxLayout(pnl_container27, javax.swing.BoxLayout.LINE_AXIS));

        lbl_stationeryMaterial.setText("Chất liệu");
        lbl_stationeryMaterial.setPreferredSize(new java.awt.Dimension(110, 30));
        lbl_stationeryMaterial.setRequestFocusEnabled(false);
        pnl_container27.add(lbl_stationeryMaterial);

        txt_stationeryMaterial.setMaximumSize(new java.awt.Dimension(9999, 40));
        txt_stationeryMaterial.setMinimumSize(new java.awt.Dimension(100, 30));
        txt_stationeryMaterial.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container27.add(txt_stationeryMaterial);

        pnl_stationery.add(pnl_container27);

        pnl_container28.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnl_container28.setMaximumSize(new java.awt.Dimension(899999, 35));
        pnl_container28.setMinimumSize(new java.awt.Dimension(100, 35));
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

        scr_stationeryDetail.setViewportView(pnl_stationeryDetail);

        pnl_rightCenter.add(scr_stationeryDetail);

        scr_bookDetail.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin chi tiết sách", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        scr_bookDetail.setMinimumSize(new java.awt.Dimension(300, 300));

        pnl_bookDetail.setMinimumSize(new java.awt.Dimension(300, 500));
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

        pnl_container17.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnl_container17.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container17.setLayout(new javax.swing.BoxLayout(pnl_container17, javax.swing.BoxLayout.LINE_AXIS));

        lbl_bookType.setText("Nguồn gốc");
        lbl_bookType.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_container17.add(lbl_bookType);

        cmb_bookType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Trong nước", "Ngoài nước" }));
        cmb_bookType.setMaximumSize(new java.awt.Dimension(9999, 40));
        cmb_bookType.setMinimumSize(new java.awt.Dimension(100, 35));
        cmb_bookType.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_container17.add(cmb_bookType);

        pnl_bookDetail.add(pnl_container17);

        pnl_container18.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        pnl_container18.setPreferredSize(new java.awt.Dimension(100, 35));
        pnl_container18.setLayout(new javax.swing.BoxLayout(pnl_container18, javax.swing.BoxLayout.LINE_AXIS));

        lbl_bookCategory1.setText("Danh mục");
        lbl_bookCategory1.setMaximumSize(new java.awt.Dimension(40, 40));
        lbl_bookCategory1.setMinimumSize(new java.awt.Dimension(40, 30));
        lbl_bookCategory1.setPreferredSize(new java.awt.Dimension(110, 40));
        pnl_container18.add(lbl_bookCategory1);

        cmb_bookCategory.setMaximumSize(new java.awt.Dimension(9999, 40));
        cmb_bookCategory.setMinimumSize(new java.awt.Dimension(100, 40));
        cmb_bookCategory.setPreferredSize(new java.awt.Dimension(120, 35));
        pnl_container18.add(cmb_bookCategory);

        pnl_bookDetail.add(pnl_container18);

        pnl_bookCenter.setMinimumSize(new java.awt.Dimension(300, 135));
        pnl_bookCenter.setLayout(new java.awt.GridLayout(2, 2, 5, 5));

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

        lbl_bookHardCover.setText("Loại bìa");
        lbl_bookHardCover.setPreferredSize(new java.awt.Dimension(100, 40));
        pnl_container12.add(lbl_bookHardCover);

        cmb_bookHardCover.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Bìa cứng", "Bìa mềm" }));
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

        pnl_bookDetail.add(pnl_bookCenter);

        pnl_container7.setMinimumSize(new java.awt.Dimension(200, 70));
        pnl_container7.setPreferredSize(new java.awt.Dimension(279, 120));
        pnl_container7.setLayout(new java.awt.BorderLayout());

        pnl_container16.setLayout(new java.awt.GridLayout(1, 0));

        lbl_bookDescription.setText("Mô tả");
        lbl_bookDescription.setPreferredSize(new java.awt.Dimension(100, 20));
        pnl_container16.add(lbl_bookDescription);

        pnl_container7.add(pnl_container16, java.awt.BorderLayout.NORTH);

        scr_productDescription.setMinimumSize(new java.awt.Dimension(100, 100));

        txa_bookDescription.setColumns(15);
        txa_bookDescription.setLineWrap(true);
        txa_bookDescription.setRows(3);
        txa_bookDescription.setTabSize(4);
        txa_bookDescription.setBorder(null);
        scr_productDescription.setViewportView(txa_bookDescription);

        pnl_container7.add(scr_productDescription, java.awt.BorderLayout.CENTER);

        pnl_bookDetail.add(pnl_container7);

        scr_bookDetail.setViewportView(pnl_bookDetail);

        pnl_rightCenter.add(scr_bookDetail);

        pnl_right.add(pnl_rightCenter, java.awt.BorderLayout.CENTER);

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

    private static byte[] getImageBytes(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return fileContent;
    }

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed
        String searchQuery = txt_search.getText();
        if (searchQuery.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng điền mã sản phẩm");
            return;
        }
        renderProductsTable(bus.searchById(searchQuery));
        disablePage();
    }//GEN-LAST:event_btn_searchActionPerformed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        enablePage();
    }//GEN-LAST:event_btn_resetActionPerformed

    private void btn_filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_filterActionPerformed
        String queryName = txt_name.getText();
        Boolean isEmpty = chk_empty.isSelected();
        int type = cmb_type.getSelectedIndex();
        int detailType = cmb_typeDetail.getSelectedIndex();

        renderProductsTable(bus.filter(queryName, isEmpty, type, detailType));
        disablePage();
    }//GEN-LAST:event_btn_filterActionPerformed

    private void btn_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearActionPerformed
        clearAllValue();
    }//GEN-LAST:event_btn_clearActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed

        Product newData = revertObjectFromForm();
        if (bus.updateProduct(currentProduct.getProductID(), newData)) {
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật thông tin sản phẩm thành công!");
            currentProduct = bus.getProduct(currentProduct.getProductID());
            renderCurrentProduct();
            renderCurrentPage();
        } else {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Cập nhật thông tin sản phẩm thất bại!");
        }
    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        Product product = revertObjectFromForm();
        if (product == null) {
            return;
        }

        try {
            if (bus.createProduct(product)) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm thông tin sản phẩm thành công!");
                renderCurrentPage();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Thêm thông tin sản phẩm thất bại!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Notifications.getInstance().show(Notifications.Type.ERROR, "Thêm thông tin sản phẩm thất bại!");
        }

    }//GEN-LAST:event_btn_addActionPerformed

    private void btn_exportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exportExcelActionPerformed
        // TODO add your handling code here:
        pnl_exportOption.setVisible(true);
    }//GEN-LAST:event_btn_exportExcelActionPerformed

    private void cbo_typeExActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_typeExActionPerformed
        renderComboboxTypeEx();        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_typeExActionPerformed

    private void bnt_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnt_cancelActionPerformed
        // TODO add your handling code here:int choice = JOptionPane.showConfirmDialog(
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn không muốn tiếp tục xuất file?",
                "Xác nhận đóng",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            pnl_exportOption.setVisible(false);
        }
    }//GEN-LAST:event_bnt_cancelActionPerformed

    private void btn_exportAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_btn_exportAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_exportAncestorAdded

    private void btn_selectImgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_selectImgActionPerformed
        int isSelected = fileChooser_productImg.showOpenDialog(this);
        System.out.println("This is running");
//        Nếu người dùng có chọn file
        if (isSelected == JFileChooser.APPROVE_OPTION) {
            File fileSelected = fileChooser_productImg.getSelectedFile();
            String path = fileSelected.getPath();

            if (path.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hãy chọn hình ảnh trước khi lưu.");
                return;
            }
            try {

                // Chuyển đổi hình ảnh
                byte[] imageBytes = getImageBytes(fileSelected);
                ImageIcon imageIcon = new ImageIcon(imageBytes);
                Image image = imageIcon.getImage();
                Image scaledImage = image.getScaledInstance(lbl_productImg.getWidth(), -1, Image.SCALE_SMOOTH | Image.SCALE_AREA_AVERAGING);

                // Tạo lại đối tượng ImageIcon với kích thước mới
                imageIcon = new ImageIcon(scaledImage);

                lbl_productImg.setIcon(imageIcon);
                lbl_productImg.revalidate();
                lbl_productImg.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btn_selectImgActionPerformed

    private void btn_exportActionPerformed(java.awt.event.ActionEvent evt) {
        //        System.out.println(cbo_type.getSelectedIndex());        // TODO add your handling code here:
        ArrayList<Product> list = bus.getAll();
        // Hiển thị hộp thoại và kiểm tra nếu người dùng chọn OK
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn đường dẫn và tên file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Lấy đường dẫn và tên file được chọn
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (cbo_typeEx.getSelectedIndex() == 0) {
                createExcelAll(list, filePath + ".xlsx");
            }
            if (cbo_typeEx.getSelectedIndex() == 1) {
                list = bus.filter(1, cbo_categoryEx.getSelectedIndex());
                createExcelBook(list, filePath + ".xlsx");
            }
            if (cbo_typeEx.getSelectedIndex() == 2) {
                list = bus.filter(2, cbo_categoryEx.getSelectedIndex());
                createExcelStationery(list, filePath + ".xlsx");

            }

            pnl_exportOption.setVisible(false);

        }

        // Gọi phương thức để tạo file Excel với đường dẫn và tên file đã chọn
    }//

    private void generateBarcodeFileForProduct(String filepath, String productID, String productName) throws FileNotFoundException, DocumentException, IOException, Exception {
        filepath += ".pdf";
        //Create Document instance.
        Document document = new Document();

        //Create OutputStream instance.
        OutputStream outputStream
                = new FileOutputStream(new File(filepath));

        //Create PDFWriter instance.
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        writer.setLanguage("VN");
        //Open the document.
        document.open();
        document.setMargins(0, 0, 0, 0);

        BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf, 12);

        document.add(new Paragraph(String.format("Mã sản phẩm:  %s - %s", productID, productName), font));

//        table
        PdfPTable table = new PdfPTable(4);
        table.setSpacingBefore(20);
        table.setWidthPercentage(100);
        table.setSplitRows(true);

//            generate barcode
        // Convert the BufferedImage to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(BarcodeGenerator.generateBarcode(productID), "PNG", baos);
        byte[] bytes = baos.toByteArray();

        // Create an Image from the byte array
        com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(bytes);

//        Tạo 100 mã
        int i = 0;
        while (i < 100) {
            table.addCell(image);
            i++;
        }

        document.add(table);
        //Close document and outputStream.
        document.close();
        outputStream.close();

        System.out.println("Pdf created successfully.");
        Desktop d = Desktop.getDesktop();
        d.open(new File(filepath));
    }

    private void btn_generateBarcodeActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn đường dẫn và tên file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Hiển thị hộp thoại và kiểm tra nếu người dùng chọn OK
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Lấy đường dẫn và tên file được chọn
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            try {
                int row = tbl_products.getSelectedRow();

                if (row == -1) {
                    Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng chọn sản phẩm muốn tạo mã");
                    return;
                }

                String id = tblModel_products.getValueAt(row, 0).toString();
                String name = tblModel_products.getValueAt(row, 1).toString();

                generateBarcodeFileForProduct(filePath, id, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clearAllValue() {
        JTextField[] txt_list = new JTextField[]{txt_productId, txt_productCostPrice, txt_productPrice, txt_productInventory, txt_productVAT, txt_bookAuthor, txt_bookLanguage, txt_bookPublishDate, txt_bookPublisher, txt_bookQuantityPage, txt_bookTranslator, txt_stationeryColor, txt_stationeryOrigin, txt_stationeryWeight};

        for (JTextField item : txt_list) {
            item.setText("");
        }

        JTextArea[] txa_list = new JTextArea[]{txa_productName, txa_bookDescription};
        for (JTextArea item : txa_list) {
            item.setText("");
        }

        cmb_productType.setSelectedIndex(0);
        cmb_bookCategory.setSelectedIndex(0);
        cmb_bookType.setSelectedIndex(0);
        cmb_bookHardCover.setSelectedIndex(0);
        cmb_stationeryBrand.setSelectedIndex(0);
        cmb_stationeryType.setSelectedIndex(0);
        fileChooser_productImg.setSelectedFile(null);
        lbl_productImg.setIcon(null);
    }

    public void showMessageFocus(String message, JComponent item) {
        Notifications.getInstance().show(Notifications.Type.ERROR, 5000, message);
        item.requestFocus();

//        Nếu là ô input thì selectAll
        if (item instanceof JTextField) {
            ((JTextField) (item)).selectAll();
        }
        if (item instanceof JTextArea) {
            ((JTextArea) (item)).selectAll();
        }

    }

    public boolean validateForm() {
//        Validate all input form

        String name = txa_productName.getText();
        if (name.isBlank()) {
            showMessageFocus("Tên sản phẩm không được rỗng", txa_productName);
            return false;
        }

        try {
            double costPrice = Double.parseDouble(txt_productCostPrice.getText());
            if (costPrice <= 0) {
                showMessageFocus("Giá nhập phải lớn hơn 0", txt_productCostPrice);
                return false;
            }
        } catch (Exception e) {
            showMessageFocus("Giá nhập phải là số thực (ex: 520.44)", txt_productCostPrice);
            return false;
        }

        try {
            int inventory = Integer.parseInt(txt_productInventory.getText());
            if (inventory < 0) {
                showMessageFocus("Sản phẩm tồn kho phải lớn hơn hoặc bằng 0", txt_productInventory);
                return false;
            }
        } catch (Exception e) {
            showMessageFocus("Sản phẩm tồn kho phải là số nguyên (ex: 1, 20, 500)", txt_productInventory);
            return false;
        }

        try {
            double vat = Double.parseDouble(txt_productVAT.getText());
            if (vat < 0) {
                showMessageFocus("VAT phải lớn hơn hoặc bằng 0", txt_productVAT);
                return false;
            }
        } catch (Exception e) {
            showMessageFocus("VAT phải là số thực (ex: 5.0, 10.0)", txt_productVAT);
            return false;
        }

        Type type = Type.fromInt(cmb_productType.getSelectedIndex() + 1);
        if (type == Type.BOOK) {
            String author = txt_bookAuthor.getText();
            if (author.isBlank()) {
                showMessageFocus("Tác giả sách không được rỗng", txt_bookAuthor);
                return false;
            }

            String publisher = txt_bookPublisher.getText();
            if (publisher.isBlank()) {
                showMessageFocus("Nhà xuất bản sách không được rỗng", txt_bookPublisher);
                return false;
            }

            try {
                int publishYear = Integer.parseInt(txt_bookPublishDate.getText());
//                Thieeus coi lai dieu kien
                if (publishYear < 1900) {
                    showMessageFocus("Năm xuất bản sách không hợp lệ", txt_bookPublishDate);
                    return false;
                }
            } catch (Exception e) {
                showMessageFocus("Năm xuất bản sách phải là số nguyên", txt_bookPublishDate);
                return false;
            }

            String language = txt_bookLanguage.getText();
            if (language.isBlank()) {
                showMessageFocus("Ngôn ngữ sách không được rỗng", txt_bookLanguage);
                return false;
            }

            try {
                int pageQuantity = Integer.parseInt(txt_bookQuantityPage.getText());
                if (pageQuantity < 0) {
                    showMessageFocus("Số trang sách phải lớn hơn 0", txt_bookQuantityPage);
                    return false;
                }
            } catch (Exception e) {
                showMessageFocus("Số trang sách phải là số nguyên", txt_bookQuantityPage);
                return false;
            }
        } else if (type == Type.STATIONERY) {
            String origin = txt_stationeryOrigin.getText();
            if (origin.isBlank()) {
                showMessageFocus("Xuất xứ văn phòng phẩm không được rỗng", txt_stationeryOrigin);
                return false;
            }
            String color = txt_stationeryColor.getText();
            if (color.isBlank()) {
                showMessageFocus("Màu sắc văn phòng phẩm không được rỗng", txt_stationeryColor);
                return false;
            }
            String material = txt_stationeryMaterial.getText();
            if (material.isBlank()) {
                showMessageFocus("Chất liệu văn phòng phẩm không được rỗng", txt_stationeryMaterial);
                return false;
            }
            try {
                Double weight = Double.valueOf(txt_stationeryWeight.getText());
                if (weight <= 0) {
                    showMessageFocus("Trọng lượng phải lớn hơn 0", txt_stationeryWeight);
                    return false;
                }
            } catch (Exception e) {
                showMessageFocus("Trọng lượng phải là số thực (ex: 5.0, 10.0)", txt_stationeryWeight);
                return false;
            }
        }
        return true;
    }

    public Product revertObjectFromForm() {
//        Kiểm tra dữ liệu trước khi lấy
        if (!validateForm()) {
            return null;
        }

        Product proc = null;

        String id = txt_productId.getText();
        String name = txa_productName.getText();
        double costPrice = Double.parseDouble(txt_productCostPrice.getText());
        int inventory = Integer.parseInt(txt_productInventory.getText());
        double vat = Double.parseDouble(txt_productVAT.getText());
        byte[] image = currentProduct.getImage();
        try {
            if (fileChooser_productImg.getSelectedFile() != null) {
                image = getImageBytes(fileChooser_productImg.getSelectedFile());
            }
        } catch (IOException ex) {
        }

        Type type = Type.fromInt(cmb_productType.getSelectedIndex() + 1);

        if (type == Type.BOOK) {
            String author = txt_bookAuthor.getText();
            String translator = txt_bookTranslator.getText();
            int publishYear = Integer.parseInt(txt_bookPublishDate.getText());
            String publisher = txt_bookPublisher.getText();
            String language = txt_bookLanguage.getText();
            int pageQuantity = Integer.parseInt(txt_bookQuantityPage.getText());
            String description = txa_bookDescription.getText();
            BookCategory category = BookCategory.fromInt(cmb_bookCategory.getSelectedIndex() + 1);
            BookType bookType = BookType.fromInt(cmb_bookType.getSelectedIndex() + 1);
            boolean isHardCover = cmb_bookHardCover.getSelectedIndex() == 0;

            try {
                proc = new Book(author, publisher, publishYear, description, pageQuantity, isHardCover, language, translator.isBlank() ? null : translator, bookType, category, id.isBlank() ? "SP11020000" : id, name, costPrice, image, vat, inventory, type);
            } catch (Exception e) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể tạo mới sản phẩm sách này");
                e.printStackTrace();
            }
        } else if (type == Type.STATIONERY) {
            String origin = txt_stationeryOrigin.getText();
            String color = txt_stationeryColor.getText();
            String material = txt_stationeryMaterial.getText();
            Double weight = Double.valueOf(txt_stationeryWeight.getText());
//            Nhớ Convert to brand object
            Brand brand = new Brand(getBrandIDSelected());
            StationeryType stationeryType = StationeryType.fromInt(cmb_stationeryType.getSelectedIndex() + 1);

            try {
                proc = new Stationery(color, weight, material, origin, stationeryType, brand, id.isBlank() ? "SP00000000" : id, name, costPrice, image, vat, inventory, type);
            } catch (Exception ex) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể tạo mới sản phẩm văn phòng phẩm này");
                ex.printStackTrace();
            }
        }
        return proc;
    }

    public void enablePage() {
        currentPage = 1;
        btn_next.setEnabled(true);
        btn_previous.setEnabled(false);
        renderCurrentPage();
    }

    public void disablePage() {
        btn_next.setEnabled(false);
        btn_previous.setEnabled(false);
        lbl_pageNumber.setVisible(false);
    }

    public static void createExcelAll(ArrayList<Product> list, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Product Data");

        // Gộp ô cho tiêu đề
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

        // Thêm dòng thông tin đầu tiên
        Row infoRow = sheet.createRow(0);
        Cell infoCell = infoRow.createCell(0);
        infoCell.setCellValue("Danh sách khách hàng");

        // Thiết lập style cho phần tiêu đề
        CellStyle titleStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        infoCell.setCellStyle(titleStyle);

        Row row_date = sheet.createRow(1);
        Cell cell_date = row_date.createCell(0);
        cell_date.setCellValue("Ngày in: " + new Date());

        Row row_emp = sheet.createRow(2);
        Cell cell_emp = row_emp.createCell(0);
        cell_emp.setCellValue("Nhân viên: " + Application.employee.getEmployeeID() + " - " + Application.employee.getName());

        // Tạo header row
        Row headerRow = sheet.createRow(3);
        String[] columns = {"Mã sản phẩm", "Tên", "Giá nhập", "Chiết khấu", "Giá bán", "Số lượng tồn", "Loại", "Danh mục"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Đổ dữ liệu từ ArrayList vào Excel
        int rowNum = 3;
        for (Product product : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getProductID());
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getCostPrice());
            row.createCell(3).setCellValue(product.getVAT());
            row.createCell(4).setCellValue(product.getPrice()); // Cần xử lý định dạng ngày tháng
            row.createCell(5).setCellValue(product.getInventory());
            row.createCell(6).setCellValue(product.getType().getValue() == 1 ? "Sách" : "Văn phòng phẩm");
            if (product.getType().getValue() == 1) {
                Book book = (Book) product;
                row.createCell(7).setCellValue(getCategoryBook(book.getBookCategory().getValue()));
            } else {
                Stationery stationery = (Stationery) product;
                row.createCell(7).setCellValue(getStationeryType(stationery.getStationeryType().getValue()));

            }

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

    public static void createExcelBook(ArrayList<Product> list, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Product Data");

        // Gộp ô cho tiêu đề
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

        // Thêm dòng thông tin đầu tiên
        Row infoRow = sheet.createRow(0);
        Cell infoCell = infoRow.createCell(0);
        infoCell.setCellValue("Danh sách khách hàng");

        // Thiết lập style cho phần tiêu đề
        CellStyle titleStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        infoCell.setCellStyle(titleStyle);

        Row row_date = sheet.createRow(1);
        Cell cell_date = row_date.createCell(0);
        cell_date.setCellValue("Ngày in: " + new Date());

        Row row_emp = sheet.createRow(2);
        Cell cell_emp = row_emp.createCell(0);
        cell_emp.setCellValue("Nhân viên: " + Application.employee.getEmployeeID() + " - " + Application.employee.getName());

        // Tạo header row
        Row headerRow = sheet.createRow(3);
        String[] columns = {"Mã sản phẩm", "Tên", "Giá nhập", "Chiết khấu", "Giá bán", "Số lượng tồn", "Thể loại", "Tác giả", "Nhà xuất bản", "Dịch giả", "Năm xuất bản", "Loại bìa", "Số trang", "Ngôn ngữ", "Tóm tắt"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
        // Đổ dữ liệu từ ArrayList vào Excel
        int rowNum = 4;
        for (Product product : list) {
            Book book = (Book) product;
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(book.getProductID());
            row.createCell(1).setCellValue(book.getName());
            row.createCell(2).setCellValue(book.getCostPrice());
            row.createCell(3).setCellValue(book.getVAT() + "%");
            row.createCell(4).setCellValue(book.getPrice());
            row.createCell(5).setCellValue(book.getInventory());
            row.createCell(6).setCellValue(getCategoryBook(book.getBookCategory().getValue()));
            row.createCell(7).setCellValue(book.getAuthor());
            row.createCell(8).setCellValue(book.getPublisher());
            row.createCell(9).setCellValue(book.getTranslator());
            row.createCell(10).setCellValue(book.getPublishYear());
            row.createCell(11).setCellValue(book.getIsHardCover() ? "Bìa cứng" : "Bìa mềm");
            row.createCell(12).setCellValue(book.getPageQuantity());
            row.createCell(13).setCellValue(book.getLanguage());
            row.createCell(14).setCellValue(book.getDescription());

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

    public static void createExcelStationery(ArrayList<Product> list, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Product Data");

        // Gộp ô cho tiêu đề
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

        // Thêm dòng thông tin đầu tiên
        Row infoRow = sheet.createRow(0);
        Cell infoCell = infoRow.createCell(0);
        infoCell.setCellValue("Danh sách văn phòng phẩm");

        // Thiết lập style cho phần tiêu đề
        CellStyle titleStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        infoCell.setCellStyle(titleStyle);

        Row row_date = sheet.createRow(1);
        Cell cell_date = row_date.createCell(0);
        cell_date.setCellValue("Ngày in: " + new Date());

        Row row_emp = sheet.createRow(2);
        Cell cell_emp = row_emp.createCell(0);
        cell_emp.setCellValue("Nhân viên: " + Application.employee.getEmployeeID() + " - " + Application.employee.getName());

        // Tạo header row
        Row headerRow = sheet.createRow(3);
        String[] columns = {"STT", "Mã sản phẩm", "Tên", "Giá nhập", "Chiết khấu", "Giá bán", "Số lượng tồn", "Loại", "Màu sắc", "Xuất xứ", "Trọng lượng", "Chất liệu", "Nhãn hàng"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
        // Đổ dữ liệu từ ArrayList vào Excel
        int rowNum = 4;
        int index = 1;
        for (Product product : list) {
            Stationery stationery = (Stationery) product;
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(index++);
            row.createCell(1).setCellValue(stationery.getProductID());
            row.createCell(2).setCellValue(stationery.getName());
            row.createCell(3).setCellValue(stationery.getCostPrice());
            row.createCell(4).setCellValue(stationery.getVAT() + "%");
            row.createCell(5).setCellValue(stationery.getPrice());
            row.createCell(6).setCellValue(stationery.getInventory());
            row.createCell(7).setCellValue(getStationeryType(stationery.getStationeryType().getValue()));
            row.createCell(8).setCellValue(stationery.getColor());
            row.createCell(9).setCellValue(stationery.getOrigin());
            row.createCell(10).setCellValue(stationery.getWeight());
            row.createCell(11).setCellValue(stationery.getMaterial());
            row.createCell(12).setCellValue(stationery.getBrand().toString());

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

    public static String getCategoryBook(int category) {
        switch (category) {
            case 0:
                return "Tất cả";
            case 1:
                return "Văn học";
            case 2:
                return "Kinh tế";
            case 3:
                return "Tâm lý - kỹ năng sống";
            case 4:
                return "Thiếu nhi";
            case 5:
                return "Nuôi dạy con";
            case 6:
                return "Tiểu sử - hồi ký";
            case 7:
                return "Sách giáo khoa - tham khảo";
            case 8:
                return "Sách học ngoại ngữ";

        }
        return null;
    }

    public static String getStationeryType(int stationeryType) {
        switch (stationeryType) {
            case 0:
                return "Tất cả";
            case 1:
                return "Bút - viết";
            case 2:
                return "Dụng cụ học sinh";
            case 3:
                return "Đụng cụ văn phòng";
            case 4:
                return "Dụng cụ vẽ";
            case 5:
                return "Sản phẩm về giấy";
            case 6:
                return "Sản phẩm khác";
        }
        return null;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bnt_cancel;
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_clear;
    private javax.swing.JButton btn_export;
    private javax.swing.JButton btn_exportExcel;
    private javax.swing.JButton btn_filter;
    private javax.swing.JButton btn_generateBarcode;
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_previous;
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_selectImg;
    private javax.swing.JButton btn_update;
    private javax.swing.JComboBox<String> cbo_categoryEx;
    private javax.swing.JComboBox<String> cbo_typeEx;
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
    private javax.swing.JLabel lbl_category;
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
    private javax.swing.JLabel lbl_stationeryMaterial;
    private javax.swing.JLabel lbl_stationeryOrigin;
    private javax.swing.JLabel lbl_stationeryType;
    private javax.swing.JLabel lbl_stationeryWeight;
    private javax.swing.JLabel lbl_title;
    private javax.swing.JLabel lbl_type;
    private javax.swing.JLabel lbl_type1;
    private javax.swing.JLabel lbl_typeDetail;
    private javax.swing.JPanel pnl_body;
    private javax.swing.JPanel pnl_bookAuthor;
    private javax.swing.JPanel pnl_bookCenter;
    private javax.swing.JPanel pnl_bookDetail;
    private javax.swing.JPanel pnl_bookPublisher;
    private javax.swing.JPanel pnl_cart;
    private javax.swing.JPanel pnl_cartFooter;
    private javax.swing.JPanel pnl_category;
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
    private javax.swing.JPanel pnl_container27;
    private javax.swing.JPanel pnl_container28;
    private javax.swing.JPanel pnl_container3;
    private javax.swing.JPanel pnl_container4;
    private javax.swing.JPanel pnl_container5;
    private javax.swing.JPanel pnl_container6;
    private javax.swing.JPanel pnl_container7;
    private javax.swing.JPanel pnl_containerName;
    private javax.swing.JPanel pnl_control;
    private javax.swing.JPanel pnl_empty;
    private javax.swing.JFrame pnl_exportOption;
    private javax.swing.JPanel pnl_filter;
    private javax.swing.JPanel pnl_footer;
    private javax.swing.JPanel pnl_header;
    private javax.swing.JPanel pnl_header1;
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
    private javax.swing.JPanel pnl_type1;
    private javax.swing.JPanel pnl_typeDetail;
    private javax.swing.JScrollPane scr_bookDetail;
    private javax.swing.JScrollPane scr_cart;
    private javax.swing.JScrollPane scr_productDescription;
    private javax.swing.JScrollPane scr_productInfo;
    private javax.swing.JScrollPane scr_productName;
    private javax.swing.JScrollPane scr_stationeryDetail;
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
    private javax.swing.JTextField txt_stationeryMaterial;
    private javax.swing.JTextField txt_stationeryOrigin;
    private javax.swing.JTextField txt_stationeryWeight;
    // End of variables declaration//GEN-END:variables
}

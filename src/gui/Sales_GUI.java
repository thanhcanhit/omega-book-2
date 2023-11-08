/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import bus.Sales_BUS;
import com.formdev.flatlaf.FlatClientProperties;
import entity.Customer;
import entity.Order;
import entity.OrderDetail;
import entity.Product;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import main.Application;
import raven.toast.Notifications;

/**
 *
 * @author thanhcanhit
 */
public class Sales_GUI extends javax.swing.JPanel {

    /**
     * Creates new form Sales_GUI
     */
    private Sales_BUS bus;

    //
    private Order order;
    private Customer customer = null;
    private ArrayList<OrderDetail> cart;
    private DefaultTableModel tblModel_cart;

//    state
    private Double total = 0.0;

    public Sales_GUI() {
        initComponents();
        init();
    }

    private void init() {
        bus = new Sales_BUS();
        txt_orderId.setEditable(false);
        txt_orderDate.setEditable(false);

//        Khởi tạo hóa đơn
        try {
            order = bus.CreateNewOrder();
            txt_orderId.setText(order.getOrderID());
            txt_orderDate.setText(order.getOrderAt().toString());
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 5000, "Không thể tạo hóa đơn mới, vui lòng thử lại lúc khác");
            ex.printStackTrace();
        }

//        table
        cart = new ArrayList<>();
        tblModel_cart = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Giá bán", "Thành tiền"}, 0);
        tbl_cart.setModel(tblModel_cart);
//        Sự kiện nhập giá trị item trong bảng
        tbl_cart.getModel().addTableModelListener((TableModelEvent evt) -> {
            int row = evt.getFirstRow();
            int col = evt.getColumn();
//              Không xử lí nếu row hoặc col = -1 và col không phải là ô nhập số lượng
            if (row == -1 || col == -1 && col != 2) {
                return;
            }

            try {
                int newValue = Integer.parseInt(tblModel_cart.getValueAt(row, col).toString());
                OrderDetail current = cart.get(row);

//            Nếu số lượng mới bằng 0 thì xóa khỏi giỏ hàng
                if (newValue == 0 && JOptionPane.showConfirmDialog(this, "Xóa sản phẩm " + current.getProduct().getProductID() + " ra khỏi giỏ hàng", "Xóa sản phẩm khỏi giỏ", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    cart.remove(current);
                    renderCartTable();
                    return;
                }

                try {
                    if (current.getProduct().getInventory() >= newValue) {
                        current.setQuantity(newValue);
                        renderCartTable();
                    } else {
//                    Trả về giá trị cũ
                        tbl_cart.setValueAt(current.getQuantity(), row, col);
                        Notifications.getInstance().show(Notifications.Type.ERROR, "Số lượng sản phẩm không đủ!");
                    }
                    
                    System.out.println(current.getProduct().getInventory() + " cc: " + newValue);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể cập nhật số lượng mới!");
                }
            } catch (HeadlessException | NumberFormatException e) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Số lượng không hợp lệ");
                renderCartTable();
            }

        });
        renderCartTable();

//        form
        txt_customerName.setEditable(false);
        txt_customerRank.setEditable(false);
        txt_orderCustomerReturn.setEditable(false);
        chk_defaultCustomer.addActionListener(((e) -> {
            if (chk_defaultCustomer.isSelected()) {
                disableCustomerForm();
            } else {
                enableCustomerForm();
            }
        }));

//        Sự kiện thay đổi phương thức thanh toán
        cmb_orderPaymentMethod.addItemListener(((e) -> {
            int index = cmb_orderPaymentMethod.getSelectedIndex();
            if (index == 1) {
                disableUserCash();
            } else {
                enableUserCash();
            }
        }));

//        Gắn sự kiện cho các nút option
        JButton[] options = new JButton[]{btn_option1, btn_option2, btn_option3, btn_option4, btn_option5, btn_option6};
        for (JButton option : options) {
            option.addActionListener((var e) -> {
//                Chuyển từ 1k thành 1000
                String value = option.getText();
                value = value.substring(0, value.indexOf("k"));
                Double dValue = 1000 * Double.parseDouble(value);
                txt_orderCustomerGive.setText(dValue.toString());
            });
        }

//        Sự kiện onChange text
        txt_orderCustomerGive.getDocument().addDocumentListener(new DocumentListener() {
            private void updateCustomerReturn() {
                try {
                    Double customerGive = Double.valueOf(txt_orderCustomerGive.getText());
                    txt_orderCustomerReturn.setText(String.valueOf(customerGive - total));
                } catch (Exception ex) {

                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCustomerReturn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCustomerReturn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCustomerReturn();
            }
        });
    }

    private void enableUserCash() {
        JButton[] items = new JButton[]{btn_option1, btn_option2, btn_option3, btn_option4, btn_option5, btn_option6};
        for (JButton item : items) {
            item.setEnabled(true);
        }
        txt_orderCustomerGive.setEnabled(true);
        txt_orderCustomerReturn.setEnabled(true);
        renderCustomerCash();
    }

    private void disableUserCash() {
        JButton[] items = new JButton[]{btn_option1, btn_option2, btn_option3, btn_option4, btn_option5, btn_option6};
        for (JButton item : items) {
            item.setText("-");
            item.setEnabled(false);
        }

        txt_orderCustomerGive.setText("");
        txt_orderCustomerGive.setEnabled(false);
        txt_orderCustomerReturn.setText("");
        txt_orderCustomerReturn.setEnabled(false);
    }

    private void enableCustomerForm() {
        JTextField[] items = new JTextField[]{txt_customerPhone, txt_customerRank, txt_customerName
        };

        for (JTextField item : items) {
            item.setText("");
            item.setEnabled(true);
        }
    }

    private void disableCustomerForm() {
        JTextField[] items = new JTextField[]{txt_customerPhone, txt_customerRank, txt_customerName
        };

        for (JTextField item : items) {
            item.setText("");
            item.setEnabled(false);
        }
    }

    private void renderCartTable() {
        tblModel_cart.setRowCount(0);

        double totalTemp = 0.0;
        for (OrderDetail item : cart) {
            Object[] newRow = new Object[]{item.getProduct().getProductID(), item.getProduct().getName(), item.getQuantity(), item.getPrice(), item.getLineTotal()};
            totalTemp += item.getLineTotal();
            tblModel_cart.addRow(newRow);
        }

        lbl_total.setText("Tổng tiền: " + utilities.FormatNumber.toVND(totalTemp));
        this.total = totalTemp;
        renderCustomerCash();
    }

    public void renderCustomerCash() {

//        Mảng các nút chọn
        JButton[] options = new JButton[]{btn_option1, btn_option2, btn_option3, btn_option4, btn_option5, btn_option6};

        if (total == 0) {
            Arrays.stream(options).forEach(item -> item.setText("-"));
            return;
        }

//        Tính toán các giá trị gợi ý dựa vào mảng số tiền
        Integer[] roundValue = new Integer[]{1000, 5000, 10000, 20000, 50000, 100000};
//        Loại bỏ giá trị trùng lắp
        Set<Double> values = new HashSet<>();
        IntStream.range(0, roundValue.length).forEach(index -> {
            values.add(total + (roundValue[index] - total % roundValue[index]));
        });

//        Sort set
        List<Double> list = new ArrayList<>(values);
        Collections.sort(list);

//        Gán giá trị cho các nút
        int index = 0;

//      Khi tổng tiền không lẻ dưới 1000 thì nút đầu sẽ trở thành tổng tiền
        if (index == 0 && Math.round(total) % 1000 == 0) {
            options[index].setText(String.format("%.0fk", total / 1000));
        }

        for (Double value : list) {
            options[index].setText(String.format("%.0fk", value / 1000));
            options[index].setVisible(true);
            index++;
        }

//        Ẩn đi các nút không có giá trị
        for (; index < options.length; index++) {
            options[index].setVisible(false);
            options[index].setText("0");
        }

//        rerender
        for (JButton item : options) {
            item.revalidate();
            item.repaint();
        }
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

    private void rerender() {
        Application.showForm(new Sales_GUI());
//        toogleChangeToSearch();
    }

    private void getCustomer(String phone) {
        customer = bus.getCustomerByPhone(phone);
        if (customer == null) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không tìm thấy khách hàng có số điện thoại \"" + phone + "\"");
        } else {
            Notifications.getInstance().show(Notifications.Type.INFO, "Đã thêm khách hàng " + customer.getName());
            renderCustomer();
        }
    }

    private void renderCustomer() {
        txt_customerName.setText(customer.getName());
        txt_customerRank.setText(customer.getRank());
    }

    private void addItemToCart(String productID) {
//        Nếu chưa có trong giỏ hàng
        Product item = bus.getProduct(productID);
        if (item == null) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không tìm thấy sản phẩm có mã " + productID);
        } else {
            try {
//                Thêm vào giỏ hàng
                OrderDetail newLine = new OrderDetail(order, item, 1, item.getPrice());
                cart.add(newLine);
                renderCartTable();
                toggleChangeQuantity();
            } catch (Exception ex) {
                ex.printStackTrace();
                Notifications.getInstance().show(Notifications.Type.ERROR, "Có lỗi xảy ra khi thêm sản phẩm " + productID);
            }
        }
    }

    private void increateItemInCart(OrderDetail detail) {
        try {
            if (detail.getProduct().getInventory() > detail.getQuantity()) {
                detail.setQuantity(detail.getQuantity() + 1);
                renderCartTable();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Số lượng sản phẩm không đủ!");
            }
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
        for (OrderDetail detail : cart) {
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

        //         Nếu chưa có khách hàng sẽ cảnh báo
        if (!chk_defaultCustomer.isSelected() && this.customer == null) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Bạn chưa thêm thông tin khách hàng");
            return;
        }

        //         Nếu chưa có sản phẩm sẽ cảnh báo
        if (cart.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Bạn chưa thêm sản phẩm vào danh sách!");
            return;
        }

        try {
            Notifications.getInstance().show(Notifications.Type.INFO, "Đang lưu trữ hóa đơn...");

            order.setEmployee(Application.employee);
            java.sql.Timestamp now = java.sql.Timestamp.valueOf(LocalDateTime.now());
            order.setOrderAt(now);
            order.setPayment(cmb_orderPaymentMethod.getSelectedIndex() == 1);
            order.setStatus(true);
//                Tạm thời
            order.setPromotion(null);
            order.setOrderDetail(cart);
            if (chk_defaultCustomer.isSelected()) {
//                Khách hàng mặc định
                order.setCustomer(new Customer("KH198210013"));
            } else {
                order.setCustomer(customer);
            }
            boolean isSaved = bus.saveToDatabase(order);

            if (isSaved) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã tạo thành công đơn nhập " + order.getOrderID());
//                Rerender panel
                rerender();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Có lỗi xảy ra khi lưu đơn nhập vào cơ sở dữ liệu" + order.getOrderID());
            }
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể tạo đơn nhập " + order.getOrderID() + ": " + ex.getMessage());
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
        pnl_customerInfo = new javax.swing.JPanel();
        pnl_customerDefault = new javax.swing.JPanel();
        chk_defaultCustomer = new javax.swing.JCheckBox();
        pnl_customerPhone = new javax.swing.JPanel();
        lbl_customerPhone = new javax.swing.JLabel();
        txt_customerPhone = new javax.swing.JTextField();
        pnl_customerName = new javax.swing.JPanel();
        lbl_customerName = new javax.swing.JLabel();
        txt_customerName = new javax.swing.JTextField();
        pnl_customerRank = new javax.swing.JPanel();
        lbl_customerRank = new javax.swing.JLabel();
        txt_customerRank = new javax.swing.JTextField();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));
        pnl_orderInfo = new javax.swing.JPanel();
        pnl_orderId = new javax.swing.JPanel();
        lbl_orderId = new javax.swing.JLabel();
        txt_orderId = new javax.swing.JTextField();
        pnl_orderDate = new javax.swing.JPanel();
        lbl_orderDate = new javax.swing.JLabel();
        txt_orderDate = new javax.swing.JTextField();
        pnl_discount = new javax.swing.JPanel();
        lbl_orderDiscount = new javax.swing.JLabel();
        txt_orderDiscount = new javax.swing.JTextField();
        pnl_orderPay = new javax.swing.JPanel();
        lbl_orderPay = new javax.swing.JLabel();
        txt_orderPay = new javax.swing.JTextField();
        pnl_orderPaymentMethod = new javax.swing.JPanel();
        lbl_orderPaymentMethod = new javax.swing.JLabel();
        cmb_orderPaymentMethod = new javax.swing.JComboBox<>();
        pnl_orderCustomerGive = new javax.swing.JPanel();
        lbl_orderCustomerGive = new javax.swing.JLabel();
        txt_orderCustomerGive = new javax.swing.JTextField();
        pnl_orderCustomerGiveOptions = new javax.swing.JPanel();
        btn_option1 = new javax.swing.JButton();
        btn_option2 = new javax.swing.JButton();
        btn_option3 = new javax.swing.JButton();
        btn_option4 = new javax.swing.JButton();
        btn_option5 = new javax.swing.JButton();
        btn_option6 = new javax.swing.JButton();
        pnl_orderCustomerReturn = new javax.swing.JPanel();
        lbl_orderCustomerReturn = new javax.swing.JLabel();
        txt_orderCustomerReturn = new javax.swing.JTextField();
        pnl_control = new javax.swing.JPanel();
        pnl_btnGroup = new javax.swing.JPanel();
        btn_save = new javax.swing.JButton();
        btn_viewSaves = new javax.swing.JButton();
        btn_cancle = new javax.swing.JButton();
        btn_promotion = new javax.swing.JButton();
        pnl_btnMain = new javax.swing.JPanel();
        btn_submit = new javax.swing.JButton();

        setLayout(new java.awt.GridLayout(1, 0));

        splitPane_main.setResizeWeight(0.7);
        splitPane_main.setMinimumSize(new java.awt.Dimension(1305, 768));

        pnl_left.setMinimumSize(new java.awt.Dimension(600, 59));
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

        pnl_right.setMaximumSize(new java.awt.Dimension(500, 2147483647));
        pnl_right.setMinimumSize(new java.awt.Dimension(300, 657));
        pnl_right.setPreferredSize(new java.awt.Dimension(400, 768));
        pnl_right.setLayout(new java.awt.BorderLayout());

        pnl_info.setLayout(new javax.swing.BoxLayout(pnl_info, javax.swing.BoxLayout.Y_AXIS));

        pnl_customerInfo.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin khách hàng"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnl_customerInfo.setPreferredSize(new java.awt.Dimension(500, 150));
        pnl_customerInfo.setLayout(new javax.swing.BoxLayout(pnl_customerInfo, javax.swing.BoxLayout.Y_AXIS));

        pnl_customerDefault.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_customerDefault.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_customerDefault.setLayout(new java.awt.BorderLayout());

        chk_defaultCustomer.setText("Khách hàng mặc định");
        pnl_customerDefault.add(chk_defaultCustomer, java.awt.BorderLayout.CENTER);

        pnl_customerInfo.add(pnl_customerDefault);

        pnl_customerPhone.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_customerPhone.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_customerPhone.setLayout(new javax.swing.BoxLayout(pnl_customerPhone, javax.swing.BoxLayout.LINE_AXIS));

        lbl_customerPhone.setText("Số điện thoại");
        lbl_customerPhone.setMinimumSize(new java.awt.Dimension(130, 40));
        lbl_customerPhone.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_customerPhone.add(lbl_customerPhone);

        txt_customerPhone.setMaximumSize(new java.awt.Dimension(99999, 35));
        txt_customerPhone.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_customerPhone.setPreferredSize(new java.awt.Dimension(30, 30));
        txt_customerPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_customerPhoneKeyPressed(evt);
            }
        });
        pnl_customerPhone.add(txt_customerPhone);

        pnl_customerInfo.add(pnl_customerPhone);

        pnl_customerName.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_customerName.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_customerName.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_customerName.setLayout(new javax.swing.BoxLayout(pnl_customerName, javax.swing.BoxLayout.LINE_AXIS));

        lbl_customerName.setText("Họ và tên");
        lbl_customerName.setMinimumSize(new java.awt.Dimension(130, 40));
        lbl_customerName.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_customerName.add(lbl_customerName);

        txt_customerName.setMaximumSize(new java.awt.Dimension(99999, 35));
        txt_customerName.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_customerName.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_customerName.add(txt_customerName);

        pnl_customerInfo.add(pnl_customerName);

        pnl_customerRank.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_customerRank.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_customerRank.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_customerRank.setLayout(new javax.swing.BoxLayout(pnl_customerRank, javax.swing.BoxLayout.LINE_AXIS));

        lbl_customerRank.setText("Hạng thành viên");
        lbl_customerRank.setMinimumSize(new java.awt.Dimension(130, 40));
        lbl_customerRank.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_customerRank.add(lbl_customerRank);

        txt_customerRank.setMaximumSize(new java.awt.Dimension(99999, 35));
        txt_customerRank.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_customerRank.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_customerRank.add(txt_customerRank);

        pnl_customerInfo.add(pnl_customerRank);

        pnl_info.add(pnl_customerInfo);
        pnl_info.add(filler1);

        pnl_orderInfo.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin hóa đơn"), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        pnl_orderInfo.setMaximumSize(new java.awt.Dimension(100019, 420));
        pnl_orderInfo.setPreferredSize(new java.awt.Dimension(500, 300));
        pnl_orderInfo.setLayout(new javax.swing.BoxLayout(pnl_orderInfo, javax.swing.BoxLayout.Y_AXIS));

        pnl_orderId.setMaximumSize(new java.awt.Dimension(99999, 40));
        pnl_orderId.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_orderId.setLayout(new javax.swing.BoxLayout(pnl_orderId, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderId.setText("Mã hóa đơn");
        lbl_orderId.setMinimumSize(new java.awt.Dimension(130, 40));
        lbl_orderId.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_orderId.add(lbl_orderId);

        txt_orderId.setMaximumSize(new java.awt.Dimension(99999, 35));
        txt_orderId.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_orderId.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_orderId.add(txt_orderId);

        pnl_orderInfo.add(pnl_orderId);

        pnl_orderDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_orderDate.setMaximumSize(new java.awt.Dimension(99999, 40));
        pnl_orderDate.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_orderDate.setLayout(new javax.swing.BoxLayout(pnl_orderDate, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderDate.setText("Ngày tạo");
        lbl_orderDate.setMinimumSize(new java.awt.Dimension(130, 40));
        lbl_orderDate.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_orderDate.add(lbl_orderDate);

        txt_orderDate.setMaximumSize(new java.awt.Dimension(99999, 35));
        txt_orderDate.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_orderDate.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_orderDate.add(txt_orderDate);

        pnl_orderInfo.add(pnl_orderDate);

        pnl_discount.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_discount.setMaximumSize(new java.awt.Dimension(99999, 40));
        pnl_discount.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_discount.setLayout(new javax.swing.BoxLayout(pnl_discount, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderDiscount.setText("Chiết khấu");
        lbl_orderDiscount.setMinimumSize(new java.awt.Dimension(130, 40));
        lbl_orderDiscount.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_discount.add(lbl_orderDiscount);

        txt_orderDiscount.setMaximumSize(new java.awt.Dimension(99999, 35));
        txt_orderDiscount.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_orderDiscount.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_discount.add(txt_orderDiscount);

        pnl_orderInfo.add(pnl_discount);

        pnl_orderPay.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_orderPay.setMaximumSize(new java.awt.Dimension(99999, 40));
        pnl_orderPay.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_orderPay.setLayout(new javax.swing.BoxLayout(pnl_orderPay, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderPay.setText("Khách phải trả");
        lbl_orderPay.setMinimumSize(new java.awt.Dimension(130, 40));
        lbl_orderPay.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_orderPay.add(lbl_orderPay);

        txt_orderPay.setMaximumSize(new java.awt.Dimension(99999, 35));
        txt_orderPay.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_orderPay.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_orderPay.add(txt_orderPay);

        pnl_orderInfo.add(pnl_orderPay);

        pnl_orderPaymentMethod.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_orderPaymentMethod.setMaximumSize(new java.awt.Dimension(99999, 40));
        pnl_orderPaymentMethod.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_orderPaymentMethod.setLayout(new javax.swing.BoxLayout(pnl_orderPaymentMethod, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderPaymentMethod.setText("Phương thức");
        lbl_orderPaymentMethod.setMinimumSize(new java.awt.Dimension(130, 40));
        lbl_orderPaymentMethod.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_orderPaymentMethod.add(lbl_orderPaymentMethod);

        cmb_orderPaymentMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt", "ATM" }));
        cmb_orderPaymentMethod.setMaximumSize(new java.awt.Dimension(99999, 35));
        cmb_orderPaymentMethod.setMinimumSize(new java.awt.Dimension(100, 35));
        cmb_orderPaymentMethod.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_orderPaymentMethod.add(cmb_orderPaymentMethod);

        pnl_orderInfo.add(pnl_orderPaymentMethod);

        pnl_orderCustomerGive.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_orderCustomerGive.setMaximumSize(new java.awt.Dimension(99999, 40));
        pnl_orderCustomerGive.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_orderCustomerGive.setLayout(new javax.swing.BoxLayout(pnl_orderCustomerGive, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderCustomerGive.setText("Tiền khách đưa");
        lbl_orderCustomerGive.setMinimumSize(new java.awt.Dimension(130, 40));
        lbl_orderCustomerGive.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_orderCustomerGive.add(lbl_orderCustomerGive);

        txt_orderCustomerGive.setMaximumSize(new java.awt.Dimension(99999, 35));
        txt_orderCustomerGive.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_orderCustomerGive.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_orderCustomerGive.add(txt_orderCustomerGive);

        pnl_orderInfo.add(pnl_orderCustomerGive);

        pnl_orderCustomerGiveOptions.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_orderCustomerGiveOptions.setMaximumSize(new java.awt.Dimension(99999, 80));
        pnl_orderCustomerGiveOptions.setPreferredSize(new java.awt.Dimension(561, 60));
        pnl_orderCustomerGiveOptions.setLayout(new java.awt.GridLayout(2, 3, 5, 5));

        btn_option1.setText("Gợi ý 1");
        btn_option1.setMaximumSize(new java.awt.Dimension(72, 40));
        btn_option1.setMinimumSize(new java.awt.Dimension(72, 35));
        btn_option1.setPreferredSize(new java.awt.Dimension(72, 35));
        pnl_orderCustomerGiveOptions.add(btn_option1);

        btn_option2.setMnemonic('2');
        btn_option2.setText("Gợi ý 2");
        btn_option2.setMaximumSize(new java.awt.Dimension(72, 40));
        btn_option2.setMinimumSize(new java.awt.Dimension(72, 35));
        btn_option2.setPreferredSize(new java.awt.Dimension(72, 35));
        pnl_orderCustomerGiveOptions.add(btn_option2);

        btn_option3.setText("Gợi ý 3");
        btn_option3.setMaximumSize(new java.awt.Dimension(72, 40));
        btn_option3.setMinimumSize(new java.awt.Dimension(72, 35));
        btn_option3.setPreferredSize(new java.awt.Dimension(72, 35));
        pnl_orderCustomerGiveOptions.add(btn_option3);

        btn_option4.setText("Gợi ý 4");
        btn_option4.setMaximumSize(new java.awt.Dimension(72, 40));
        btn_option4.setMinimumSize(new java.awt.Dimension(72, 35));
        btn_option4.setPreferredSize(new java.awt.Dimension(72, 35));
        pnl_orderCustomerGiveOptions.add(btn_option4);

        btn_option5.setText("Gợi ý 5");
        btn_option5.setMaximumSize(new java.awt.Dimension(72, 40));
        btn_option5.setMinimumSize(new java.awt.Dimension(72, 35));
        btn_option5.setPreferredSize(new java.awt.Dimension(72, 35));
        pnl_orderCustomerGiveOptions.add(btn_option5);

        btn_option6.setText("Gợi ý 6");
        btn_option6.setMaximumSize(new java.awt.Dimension(72, 40));
        btn_option6.setMinimumSize(new java.awt.Dimension(72, 35));
        btn_option6.setPreferredSize(new java.awt.Dimension(72, 35));
        pnl_orderCustomerGiveOptions.add(btn_option6);

        pnl_orderInfo.add(pnl_orderCustomerGiveOptions);

        pnl_orderCustomerReturn.setMaximumSize(new java.awt.Dimension(99999, 40));
        pnl_orderCustomerReturn.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_orderCustomerReturn.setLayout(new javax.swing.BoxLayout(pnl_orderCustomerReturn, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderCustomerReturn.setText("Tiền thừa");
        lbl_orderCustomerReturn.setMinimumSize(new java.awt.Dimension(130, 40));
        lbl_orderCustomerReturn.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_orderCustomerReturn.add(lbl_orderCustomerReturn);

        txt_orderCustomerReturn.setMaximumSize(new java.awt.Dimension(99999, 35));
        txt_orderCustomerReturn.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_orderCustomerReturn.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_orderCustomerReturn.add(txt_orderCustomerReturn);

        pnl_orderInfo.add(pnl_orderCustomerReturn);

        pnl_info.add(pnl_orderInfo);

        pnl_right.add(pnl_info, java.awt.BorderLayout.CENTER);

        pnl_control.setLayout(new javax.swing.BoxLayout(pnl_control, javax.swing.BoxLayout.PAGE_AXIS));

        pnl_btnGroup.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_btnGroup.setPreferredSize(new java.awt.Dimension(281, 100));
        pnl_btnGroup.setLayout(new java.awt.GridLayout(2, 2, 5, 5));

        btn_save.setText("LƯU TẠM");
        pnl_btnGroup.add(btn_save);

        btn_viewSaves.setText("XỨ LÍ ĐƠN LƯU TẠM");
        pnl_btnGroup.add(btn_viewSaves);

        btn_cancle.setText("HỦY");
        btn_cancle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancleActionPerformed(evt);
            }
        });
        pnl_btnGroup.add(btn_cancle);

        btn_promotion.setText("KHUYẾN MÃI");
        pnl_btnGroup.add(btn_promotion);

        pnl_control.add(pnl_btnGroup);

        pnl_btnMain.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 5, 5));
        pnl_btnMain.setPreferredSize(new java.awt.Dimension(561, 50));
        pnl_btnMain.setLayout(new java.awt.BorderLayout());

        btn_submit.setFont(btn_submit.getFont().deriveFont((float)18));
        btn_submit.setText("THANH TOÁN");
        btn_submit.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_submitActionPerformed(evt);
            }
        });
        pnl_btnMain.add(btn_submit, java.awt.BorderLayout.CENTER);

        pnl_control.add(pnl_btnMain);

        pnl_right.add(pnl_control, java.awt.BorderLayout.SOUTH);

        splitPane_main.setRightComponent(pnl_right);

        add(splitPane_main);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cancleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancleActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Bạn có muốn hủy hóa đơn " + order.getOrderID(), "Xác nhận hủy hóa đơn", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            // Tạo lại trang mới
            rerender();
            Notifications.getInstance().show(Notifications.Type.INFO, "Đã hủy thành công hóa đơn");
        }

    }//GEN-LAST:event_btn_cancleActionPerformed

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed
        handleAddItem();
    }//GEN-LAST:event_btn_searchActionPerformed

    private void txt_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchKeyPressed
        //        Bắt sự kiện bấm enter
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            handleAddItem();
        }
    }//GEN-LAST:event_txt_searchKeyPressed

    private void btn_submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_submitActionPerformed
        handleCreateOrder();
    }//GEN-LAST:event_btn_submitActionPerformed

    private void txt_customerPhoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_customerPhoneKeyPressed
        String phoneInput = txt_customerPhone.getText();
        if (phoneInput.length() == 10) {
            getCustomer(phoneInput);
        }
    }//GEN-LAST:event_txt_customerPhoneKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancle;
    private javax.swing.JButton btn_option1;
    private javax.swing.JButton btn_option2;
    private javax.swing.JButton btn_option3;
    private javax.swing.JButton btn_option4;
    private javax.swing.JButton btn_option5;
    private javax.swing.JButton btn_option6;
    private javax.swing.JButton btn_promotion;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_submit;
    private javax.swing.JButton btn_viewSaves;
    private javax.swing.JCheckBox chk_defaultCustomer;
    private javax.swing.JComboBox<String> cmb_orderPaymentMethod;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel lbl_customerName;
    private javax.swing.JLabel lbl_customerPhone;
    private javax.swing.JLabel lbl_customerRank;
    private javax.swing.JLabel lbl_orderCustomerGive;
    private javax.swing.JLabel lbl_orderCustomerReturn;
    private javax.swing.JLabel lbl_orderDate;
    private javax.swing.JLabel lbl_orderDiscount;
    private javax.swing.JLabel lbl_orderId;
    private javax.swing.JLabel lbl_orderPay;
    private javax.swing.JLabel lbl_orderPaymentMethod;
    private javax.swing.JLabel lbl_total;
    private javax.swing.JPanel pnl_btnGroup;
    private javax.swing.JPanel pnl_btnMain;
    private javax.swing.JPanel pnl_cart;
    private javax.swing.JPanel pnl_cartFooter;
    private javax.swing.JPanel pnl_control;
    private javax.swing.JPanel pnl_customerDefault;
    private javax.swing.JPanel pnl_customerInfo;
    private javax.swing.JPanel pnl_customerName;
    private javax.swing.JPanel pnl_customerPhone;
    private javax.swing.JPanel pnl_customerRank;
    private javax.swing.JPanel pnl_discount;
    private javax.swing.JPanel pnl_header;
    private javax.swing.JPanel pnl_info;
    private javax.swing.JPanel pnl_left;
    private javax.swing.JPanel pnl_orderCustomerGive;
    private javax.swing.JPanel pnl_orderCustomerGiveOptions;
    private javax.swing.JPanel pnl_orderCustomerReturn;
    private javax.swing.JPanel pnl_orderDate;
    private javax.swing.JPanel pnl_orderId;
    private javax.swing.JPanel pnl_orderInfo;
    private javax.swing.JPanel pnl_orderPay;
    private javax.swing.JPanel pnl_orderPaymentMethod;
    private javax.swing.JPanel pnl_right;
    private javax.swing.JScrollPane scr_cart;
    private javax.swing.JSplitPane splitPane_main;
    private javax.swing.JTable tbl_cart;
    private javax.swing.JTextField txt_customerName;
    private javax.swing.JTextField txt_customerPhone;
    private javax.swing.JTextField txt_customerRank;
    private javax.swing.JTextField txt_orderCustomerGive;
    private javax.swing.JTextField txt_orderCustomerReturn;
    private javax.swing.JTextField txt_orderDate;
    private javax.swing.JTextField txt_orderDiscount;
    private javax.swing.JTextField txt_orderId;
    private javax.swing.JTextField txt_orderPay;
    private javax.swing.JTextField txt_search;
    // End of variables declaration//GEN-END:variables
}

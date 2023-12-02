/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import bus.Sales_BUS;
import com.formdev.flatlaf.FlatClientProperties;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import entity.Customer;
import entity.Order;
import entity.OrderDetail;
import entity.Product;
import entity.Promotion;
import enums.DiscountType;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import main.Application;
import raven.toast.Notifications;
import utilities.OrderPrinter;

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
    private Customer defaultCustomer;
    private final JButton[] btnOptionsList;
    private final SalesHandleNativeKey barcodeScannerHandler;

//    state
    private Double total = 0.0;
    private Double discount = 0.0;
    private Double totalAfterDiscount = 0.0;
    private Double customerGive = 0.0;
    private boolean isOldOrder = false;
    private String tempInput = new String();

//    Lớp internal để hiện xử lí native input
    private class SalesHandleNativeKey implements NativeKeyListener {

        @Override
        public void nativeKeyPressed(NativeKeyEvent e) {
            System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()) + "; Temp: " + tempInput);

//                Handle submit
            if (e.getKeyCode() == NativeKeyEvent.VC_ENTER) {
                System.out.println(tempInput);
                String productID = tempInput.trim();

//                    Kiểm tra xem productID có hợp lệ không 
                try {
                    System.out.println("Product ID:" + productID + ";");
                    Product _product = new Product(productID);
                    handleAddItem(productID);
                } catch (Exception er) {
//                  Bỏ qua
                    er.printStackTrace();
                } finally {
//                    reset
                    tempInput = new String();
                }

            }

//                Xử lí nhận biết đang nhập mã sản phẩm
            if (e.getKeyCode() == NativeKeyEvent.VC_S) {
                tempInput = new String();
                tempInput += NativeKeyEvent.getKeyText(e.getKeyCode());
            }

            if (tempInput.equals("S") && e.getKeyCode() == NativeKeyEvent.VC_P) {
                tempInput += NativeKeyEvent.getKeyText(e.getKeyCode());
            }

//            Kiểm tra xem có phải đang nhập mã sản phẩm và đang là số không (VC_1 = 2 và VC_0 = 11)
            if (tempInput.startsWith("SP") && e.getKeyCode() >= NativeKeyEvent.VC_1 && e.getKeyCode() <= NativeKeyEvent.VC_0) {
                tempInput += NativeKeyEvent.getKeyText(e.getKeyCode());
            }

//            Bắt sự kiện bấm các hot key
            if (e.getKeyCode() == NativeKeyEvent.VC_F1) {
                handleCreateOrder();
            }

            if (e.getKeyCode() == NativeKeyEvent.VC_F2) {
                btn_save.doClick();
            }

            if (e.getKeyCode() == NativeKeyEvent.VC_F3) {
                btn_viewSaves.doClick();
            }

            if (e.getKeyCode() == NativeKeyEvent.VC_F4) {
                btn_cancle.doClick();
            }

            if (e.getKeyCode() == NativeKeyEvent.VC_F5) {
                btn_promotion.doClick();
            }

            if (e.getKeyCode() == NativeKeyEvent.VC_F6) {
                chk_defaultCustomer.doClick();
            }

//            Phím tắt hỗ trợ người dùng
//           Focus vào ô nhập khi bấm ESC
            if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
                txt_search.setText("");
                txt_search.requestFocus();
                tempInput = new String();
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent e) {
//                System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        }

        @Override
        public void nativeKeyTyped(NativeKeyEvent e) {
//                System.out.println("Key Typed: " + e.getKeyText(e.getKeyCode()));
        }
    }

    public Sales_GUI() {
        initComponents();
        this.btnOptionsList = new JButton[]{btn_option1, btn_option2, btn_option3, btn_option4, btn_option5, btn_option6, btn_option7, btn_option8, btn_option9};

        init();

        disableCart();

//        Khởi tạo đối tượng sự kiện để có thể gỡ khi rời khỏi trang (sales gui được giữ lại nên có thể gây lỗi ở gui khác)
        barcodeScannerHandler = new SalesHandleNativeKey();
    }

    private void updateTotalAfterDiscount() {
        totalAfterDiscount = total - discount;
        calculateOptionCashGive();
    }

    private void setTotal(double total) {
        this.total = total;
        updateTotalAfterDiscount();
    }

    private void setDiscount(double discount) {
        this.discount = discount;
        updateTotalAfterDiscount();
    }

    private void setCustomerGive(double qty) {
        this.customerGive = qty;
        txt_orderCustomerGive.setText(customerGive.toString());
    }

    private void setOrderCustomer(Customer customer) {
        try {
            this.customer = customer;
            order.setCustomer(customer);
//            Không cho đổi khách hàng
            chk_defaultCustomer.setEnabled(false);
            disableCustomerForm();
            renderCustomer();
//            Add bespromotion
            handleAddBest();
            updateTotalAfterDiscount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disableCart() {
        pnl_left.setEnabled(false);
    }

    private void enableCart() {
        pnl_right.setEnabled(true);
    }

    private void init() {
        bus = new Sales_BUS();
        txt_orderId.setEditable(false);
        txt_orderDate.setEditable(false);
        txt_orderPay.setEditable(false);
        try {
            defaultCustomer = new Customer("KH000000000");
        } catch (Exception ex) {
            Logger.getLogger(Sales_GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

//        Khởi tạo hóa đơn
        try {
            order = bus.CreateNewOrder();
            renderOrder();
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, 5000, "Không thể tạo hóa đơn mới, vui lòng thử lại lúc khác");
            ex.printStackTrace();
        }

//        Hot key
//        table
        cart = new ArrayList<>();
        tblModel_cart = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Giá bán", "VAT", "Tổng tiền", "Tiền giảm", "Thành tiền"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
//                Chỉ cho cột 2 tức là Số lượng mới sửa được
                if (column == 2) {
                    return true;
                }
                return false;
            }

        };
        tbl_cart.setModel(tblModel_cart);
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);

//        Only number input set
        KeyAdapter onlyNumberAdapter = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9')
                        || (c == KeyEvent.VK_BACK_SPACE)
                        || (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        };
        txt_customerPhone.addKeyListener(onlyNumberAdapter);
        txt_orderCustomerGive.addKeyListener(onlyNumberAdapter);

//        Align
        TableColumnModel columnModel = tbl_cart.getColumnModel();
        for (int index : new Integer[]{2, 3, 4, 5, 6, 7}) {
            columnModel.getColumn(index).setCellRenderer(rightAlign);
        }
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
//                    Cập nhật giá trị mới
                        current.setQuantity(newValue);
//                        Lấy danh mục giảm giá
                        double discountAmount = bus.getBestProductPromotionDiscountAmountAvailable(current.getProduct().getProductID());
                        if (discountAmount > 0) {
//                    Lấy phần giảm cho 1 sản phẩm để tăng lên
                            current.setSeasonalDiscount(discountAmount * current.getQuantity());
                        }
                        renderCartTable();
                    } else {
//                    Trả về giá trị cũ
                        tbl_cart.setValueAt(current.getQuantity(), row, col);
                        Notifications.getInstance().show(Notifications.Type.ERROR, "Số lượng sản phẩm không đủ!");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể cập nhật số lượng mới!");
                }
            } catch (HeadlessException | NumberFormatException e) {
                e.printStackTrace();
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
        Integer[] roundValues = new Integer[]{1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000};

        for (int i = 0; i < roundValues.length; i++) {
            final JButton temp = btnOptionsList[i];
            temp.setText(roundValues[i] / 1000 + "k");
            temp.addActionListener((var e) -> {
//                Chuyển từ 1k thành 1000
                String value = temp.getText();
                value = value.substring(0, value.indexOf("k"));
                Double dValue = 1000 * Double.parseDouble(value);
//                setCustomerGive(customerGive + dValue);
                setCustomerGive(dValue);
            });
        }
        calculateOptionCashGive();

//        Sự kiện onChange text
        txt_orderCustomerGive.getDocument().addDocumentListener(new DocumentListener() {
            private void updateCustomerReturn() {
                try {
                    txt_orderCustomerReturn.setText(utilities.FormatNumber.toVND(Double.valueOf(Math.round(customerGive - totalAfterDiscount))));
                    order.setMoneyGiven(customerGive);
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

//        Set tab order
        this.setFocusTraversalPolicy(new FocusTraversalPolicy() {
            @Override
            public Component getComponentAfter(Container aContainer, Component aComponent) {
                if (aComponent == txt_search) {
                    return txt_customerPhone;
                } else if (aComponent == txt_customerPhone) {
                    return txt_orderCustomerGive;
                } else if (aComponent == txt_orderCustomerGive) {
                    return btn_insert000;
                }

                return txt_search;
            }

            @Override
            public Component getComponentBefore(Container aContainer, Component aComponent) {
                if (aComponent == txt_customerPhone) {
                    return txt_search;
                } else if (aComponent == txt_orderCustomerGive) {
                    return txt_customerPhone;
                } else if (aComponent == btn_insert000) {
                    return txt_orderCustomerGive;
                }

                return txt_search;
            }

            @Override
            public Component getFirstComponent(Container aContainer) {
                return txt_search;
            }

            @Override
            public Component getLastComponent(Container aContainer) {
                return txt_orderCustomerGive;
            }

            @Override
            public Component getDefaultComponent(Container aContainer) {
                return txt_search;
            }
        });
    }

    private void enableUserCash() {
        for (JButton item : btnOptionsList) {
            item.setEnabled(true);
        }
        btn_insert000.setEnabled(true);
        txt_orderCustomerGive.setEnabled(true);
        txt_orderCustomerReturn.setEnabled(true);
        setCustomerGive(0);
        calculateOptionCashGive();
    }

    private void disableUserCash() {
        for (JButton item : btnOptionsList) {
            item.setText("-");
            item.setEnabled(false);
        }
        btn_insert000.setEnabled(false);

        txt_orderCustomerGive.setText("");
        txt_orderCustomerGive.setEnabled(false);
        txt_orderCustomerReturn.setText("");
        txt_orderCustomerReturn.setEnabled(false);
    }

    private void renderOrder() {
        txt_orderId.setText(order.getOrderID());
        txt_orderDate.setText(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now()));
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
        txt_customerName.setText("Tài khoản mặc định");
    }

    private void renderCartTable() {
        tblModel_cart.setRowCount(0);

        double totalTemp = 0.0;
        for (OrderDetail item : cart) {
            Object[] newRow = new Object[]{item.getProduct().getProductID(), item.getProduct().getName(), item.getQuantity(), utilities.FormatNumber.toVND(item.getPrice()), String.format("%.2f%%", item.getVAT()), utilities.FormatNumber.toVND(item.getPrice() * item.getQuantity()), utilities.FormatNumber.toVND(item.getSeasonalDiscount()), utilities.FormatNumber.toVND(item.getLineTotal())};
            totalTemp += item.getLineTotal();
            tblModel_cart.addRow(newRow);
        }

        lbl_total.setText("Tổng tiền: " + utilities.FormatNumber.toVND(totalTemp));
        setTotal(totalTemp);

        if (order.getPromotion() != null) {
            Double discountValue = order.getPromotion().getDiscount();
            Double discountAmount = order.getPromotion().getTypeDiscount() == DiscountType.PERCENT ? totalTemp * discountValue / 100 : totalTemp - discountValue;
            setDiscount(discountAmount);
            renderOrderTotal();
        }
        txt_orderPay.setText(utilities.FormatNumber.toVND(total - discount));
        calculateOptionCashGive();
    }

//    Tính toán gợi ý số tiền khách đưa dựa vào 9 mệnh giá tiền
    private void calculateOptionCashGive() {

//        Mảng các nút chọn
        double orderPay = this.totalAfterDiscount;

        if (orderPay == 0) {
            Arrays.stream(btnOptionsList).forEach(item -> item.setText("-"));
            return;
        }

        Integer[] roundValues = new Integer[]{1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000};

//        Tính toán các giá trị gợi ý dựa vào mảng số tiền + Loại bỏ giá trị trùng lắp
        Set<Double> values = new HashSet<>();
        for (Integer item : roundValues) {
            values.add(orderPay + (item - orderPay % item));
        }

//        Sort set
        List<Double> list = new ArrayList<>(values);
        Collections.sort(list);

//        Gán giá trị cho các nút
        int index = 0;

        for (Double value : list) {
            if (index >= btnOptionsList.length) {
                break;
            }

            btnOptionsList[index].setText(String.format("%.0fk (%d)", value / 1000, index + 1));
            btnOptionsList[index].setVisible(true);

//            Set phím tắt nhanh
//            VK_1 = 49
            btnOptionsList[index].setMnemonic(index + 49);
            index++;
        }

//      Khi tổng tiền không lẻ dưới 1000 thì nút đầu sẽ trở thành tổng tiền
        if (Math.round(orderPay) % 1000 == 0) {
            btnOptionsList[0].setText(String.format("%.0fk", orderPay / 1000));
        }

//        Ẩn đi các nút không có giá trị
        for (; index < btnOptionsList.length; index++) {
            if (index >= btnOptionsList.length) {
                break;
            }
            btnOptionsList[index].setVisible(false);
            btnOptionsList[index].setText("0");
            btnOptionsList[index].setMnemonic(0);
        }

//        rerender
        for (JButton item : btnOptionsList) {
            item.revalidate();
            item.repaint();
        }
    }

    private void toggleChangeQuantityField() {
        txt_search.setText("");
        int row = cart.size() - 1;
        tbl_cart.requestFocus();
        tbl_cart.changeSelection(row, 2, false, false);
        tbl_cart.setColumnSelectionInterval(2, 2);
        tbl_cart.setRowSelectionInterval(row, row);
        tbl_cart.editCellAt(row, 2);
    }

    private void rerender() {
        Application.refreshMainView();
    }

    private void getCustomer(String phone) {
        customer = bus.getCustomerByPhone(phone);
        if (customer == null) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không tìm thấy khách hàng có số điện thoại \"" + phone + "\"");
        } else {
            Notifications.getInstance().show(Notifications.Type.INFO, "Đã thêm khách hàng " + customer.getName());
//            Thêm khuyến mãi nếu có
            setOrderCustomer(customer);
        }
    }

    private void renderCustomer() {
//        CHUA CO ID MAC DINH
        if (customer.getCustomerID().equals(defaultCustomer.getCustomerID())) {
            chk_defaultCustomer.setSelected(true);
            disableCustomerForm();
            return;
        }
        txt_customerName.setText(customer.getName());
        txt_customerRank.setText(customer.getRank());
    }

    private void addItemToCart(String productID) {
//        Nếu chưa có trong giỏ hàng
        Product item = bus.getProduct(productID);
        if (item == null) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Không tìm thấy sản phẩm có mã " + productID);
        } else {
            if (item.getInventory() == 0) {
                Notifications.getInstance().show(Notifications.Type.INFO, "Sản phẩm " + productID + " hiện đã hết");
                return;
            }

            try {
//                Tạo dòng mới
                OrderDetail newLine = new OrderDetail(order, item, 1, item.getPrice(), item.getVAT(), 0);
//              Lấy danh mục giảm giá
                double discountAmount = bus.getBestProductPromotionDiscountAmountAvailable(productID);
                if (discountAmount > 0) {
//                    Lấy phần giảm cho 1 sản phẩm để tăng lên
                    newLine.setSeasonalDiscount(discountAmount * newLine.getQuantity());
                    Notifications.getInstance().show(Notifications.Type.INFO, "Sản phẩm này được giảm " + utilities.FormatNumber.toVND(discountAmount) + " trên 1 sản phẩm");
                }
                cart.add(newLine);
                renderCartTable();
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
//                Lấy danh mục giảm giá
                double discountAmount = bus.getBestProductPromotionDiscountAmountAvailable(detail.getProduct().getProductID());
                if (discountAmount > 0) {
//                    Lấy phần giảm cho 1 sản phẩm để tăng lên
                    detail.setSeasonalDiscount(discountAmount * detail.getQuantity());
                }
                renderCartTable();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Số lượng sản phẩm không đủ!");
            }
        } catch (Exception e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể tăng số lượng: " + e.getMessage());
        }
        txt_search.setText("");
    }

    private void handleAddItem() {
        String productID = txt_search.getText();

        handleAddItem(productID);
    }

    private void handleAddItem(String productID) {

//        Bắt buộc nhập thông tin khách hàng trước tiên
        if (customer == null) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Bạn phải thêm thông tin khách hàng trước tiên");
            return;
        }

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

//      Clear txt field
        txt_search.setText("");
    }

    private boolean orderValidate(boolean checkCustomerGive) {
        //         Nếu chưa có khách hàng sẽ cảnh báo
        if (!chk_defaultCustomer.isSelected() && this.customer == null) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Bạn chưa thêm thông tin khách hàng");
            return false;
        }

        //         Nếu chưa có sản phẩm sẽ cảnh báo
        if (cart.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Bạn chưa thêm sản phẩm vào danh sách!");
            return false;
        }

        //         Nếu phương thức là tiền mặt và chưa nhập tiền khách đưa sẽ cảnh báo
        if (cmb_orderPaymentMethod.getSelectedIndex() == 0 && checkCustomerGive && txt_orderCustomerGive.getText().isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Vui lòng nhập số tiền khách đã đưa!");
            return false;
        }

        if (customerGive < totalAfterDiscount) {
            Notifications.getInstance().show(Notifications.Type.WARNING, "Tiền khách đưa ít hơn số tiền cần thanh toán!");
            return false;
        }

        return true;
    }

    private void handleCreateOrder() {
        if (!orderValidate(true)) {
            return;
        }

        try {
            boolean isSaved = isOldOrder ? updateOrder(isOldOrder) : saveOrder(true);

            if (isSaved) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã tạo thành công đơn hàng" + order.getOrderID());
//              
//                Rerender panel
                rerender();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Có lỗi xảy ra khi lưu đơn hàng vào cơ sở dữ liệu" + order.getOrderID());
            }
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể tạo đơn hàng " + order.getOrderID() + ": " + ex.getMessage());
        }

//        tạo file pdf và hiển thị + in file pdf đó
        OrderPrinter printer = new OrderPrinter(order);
        printer.generatePDF();
        OrderPrinter.PrintStatus status = printer.printFile();
        if (status == OrderPrinter.PrintStatus.NOT_FOUND_FILE) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi không thể in hóa đơn: Không tìm thấy file");
        } else if (status == OrderPrinter.PrintStatus.NOT_FOUND_PRINTER) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Lỗi không thể in hóa đơn: Không tìm thấy máy in");
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

        frame_savedOrder = new javax.swing.JFrame();
        lbl_savedOrderTitle = new javax.swing.JLabel();
        pnl_savedOrderControl = new javax.swing.JPanel();
        btn_savedOrderSelect = new javax.swing.JButton();
        btn_savedOrderDelete = new javax.swing.JButton();
        btn_savedOrderDeleteAll = new javax.swing.JButton();
        pnl_main = new javax.swing.JPanel();
        scr_savedOrders = new javax.swing.JScrollPane();
        tbl_savedOrder = new javax.swing.JTable();
        scr_savedOrderDetail = new javax.swing.JScrollPane();
        tbl_saveOrderDetail = new javax.swing.JTable();
        frame_promotionList = new javax.swing.JFrame();
        lbl_promotionList = new javax.swing.JLabel();
        pnl_promotionControl = new javax.swing.JPanel();
        btn_promotionSelect = new javax.swing.JButton();
        btn_promotionSelectBest = new javax.swing.JButton();
        src_promotionList = new javax.swing.JScrollPane();
        tbl_promotionList = new javax.swing.JTable();
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
        btn_insert000 = new javax.swing.JButton();
        pnl_orderCustomerGiveOptions = new javax.swing.JPanel();
        btn_option1 = new javax.swing.JButton();
        btn_option2 = new javax.swing.JButton();
        btn_option3 = new javax.swing.JButton();
        btn_option4 = new javax.swing.JButton();
        btn_option5 = new javax.swing.JButton();
        btn_option6 = new javax.swing.JButton();
        btn_option7 = new javax.swing.JButton();
        btn_option8 = new javax.swing.JButton();
        btn_option9 = new javax.swing.JButton();
        pnl_orderCustomerReturn = new javax.swing.JPanel();
        lbl_orderCustomerReturn = new javax.swing.JLabel();
        txt_orderCustomerReturn = new javax.swing.JTextField();
        pnl_notification = new javax.swing.JPanel();
        lbl_notification = new javax.swing.JLabel();
        pnl_control = new javax.swing.JPanel();
        pnl_btnGroup = new javax.swing.JPanel();
        btn_save = new javax.swing.JButton();
        btn_viewSaves = new javax.swing.JButton();
        btn_cancle = new javax.swing.JButton();
        btn_promotion = new javax.swing.JButton();
        pnl_btnMain = new javax.swing.JPanel();
        btn_submit = new javax.swing.JButton();

        frame_savedOrder.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        frame_savedOrder.setTitle("Xử lí đơn lưu tạm");
        frame_savedOrder.setAlwaysOnTop(true);
        frame_savedOrder.setMinimumSize(new java.awt.Dimension(800, 400));
        frame_savedOrder.setResizable(false);
        frame_savedOrder.setType(java.awt.Window.Type.POPUP);

        lbl_savedOrderTitle.setFont(lbl_savedOrderTitle.getFont().deriveFont(lbl_savedOrderTitle.getFont().getStyle() | java.awt.Font.BOLD, lbl_savedOrderTitle.getFont().getSize()+6));
        lbl_savedOrderTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_savedOrderTitle.setText("Danh sách hóa đơn đã lưu tạm");
        lbl_savedOrderTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        frame_savedOrder.getContentPane().add(lbl_savedOrderTitle, java.awt.BorderLayout.NORTH);

        pnl_savedOrderControl.setMinimumSize(new java.awt.Dimension(10, 50));

        btn_savedOrderSelect.setText("Xử lí đơn");
        btn_savedOrderSelect.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_savedOrderSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_savedOrderSelectActionPerformed(evt);
            }
        });
        pnl_savedOrderControl.add(btn_savedOrderSelect);

        btn_savedOrderDelete.setText("Xóa đơn");
        btn_savedOrderDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_savedOrderDeleteActionPerformed(evt);
            }
        });
        pnl_savedOrderControl.add(btn_savedOrderDelete);

        btn_savedOrderDeleteAll.setText("Xóa toàn bộ đơn lưu tạm");
        btn_savedOrderDeleteAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_savedOrderDeleteAllActionPerformed(evt);
            }
        });
        pnl_savedOrderControl.add(btn_savedOrderDeleteAll);

        frame_savedOrder.getContentPane().add(pnl_savedOrderControl, java.awt.BorderLayout.SOUTH);

        pnl_main.setLayout(new javax.swing.BoxLayout(pnl_main, javax.swing.BoxLayout.LINE_AXIS));

        scr_savedOrders.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        tbl_savedOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "2", "3"},
                {"2", "3", "5"},
                {"1", "4", "6"},
                {null, null, null}
            },
            new String [] {
                "Mã hóa đơn", "Tên khách hàng", "Ngày tạo"
            }
        ));
        tbl_savedOrder.setCellEditor(null);
        tbl_savedOrder.setEditingColumn(0);
        tbl_savedOrder.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbl_savedOrder.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr_savedOrders.setViewportView(tbl_savedOrder);
        tbl_savedOrder.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            int rowIndex = tbl_savedOrder.getSelectedRow();
            if (rowIndex == -1) {
                return;
            }

            String orderId = tbl_savedOrder.getValueAt(rowIndex, 0).toString();
            Order order = bus.getOrder(orderId);
            DefaultTableModel tblModel_savedOrderDetail = new DefaultTableModel(new String[]{"Tên sản phẩm", "Số lượng"}, 50) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tbl_saveOrderDetail.setModel(tblModel_savedOrderDetail);

            tblModel_savedOrderDetail.setRowCount(0);
            for (OrderDetail item : order.getOrderDetail()) {
                tblModel_savedOrderDetail.addRow(new Object[] {item.getProduct().getName(), item.getQuantity()});
            }
        });

        pnl_main.add(scr_savedOrders);

        scr_savedOrderDetail.setMaximumSize(new java.awt.Dimension(300, 32767));

        tbl_saveOrderDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Tên sản phẩm", "Số lượng"
            }
        ));
        tbl_saveOrderDetail.setRequestFocusEnabled(false);
        tbl_saveOrderDetail.setRowSelectionAllowed(false);
        tbl_saveOrderDetail.setShowVerticalLines(true);
        scr_savedOrderDetail.setViewportView(tbl_saveOrderDetail);

        pnl_main.add(scr_savedOrderDetail);

        frame_savedOrder.getContentPane().add(pnl_main, java.awt.BorderLayout.CENTER);

        frame_promotionList.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        frame_promotionList.setTitle("Xử lí đơn lưu tạm");
        frame_promotionList.setAlwaysOnTop(true);
        frame_promotionList.setMinimumSize(new java.awt.Dimension(600, 400));
        frame_promotionList.setResizable(false);
        frame_promotionList.setType(java.awt.Window.Type.POPUP);

        lbl_promotionList.setFont(lbl_promotionList.getFont().deriveFont(lbl_promotionList.getFont().getStyle() | java.awt.Font.BOLD, lbl_promotionList.getFont().getSize()+6));
        lbl_promotionList.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_promotionList.setText("Danh sách khuyến mãi có thể áp dụng");
        lbl_promotionList.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        frame_promotionList.getContentPane().add(lbl_promotionList, java.awt.BorderLayout.NORTH);

        pnl_promotionControl.setMinimumSize(new java.awt.Dimension(10, 50));

        btn_promotionSelect.setText("Lựa chọn");
        btn_submit.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_promotionSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_promotionSelectActionPerformed(evt);
            }
        });
        pnl_promotionControl.add(btn_promotionSelect);

        btn_promotionSelectBest.setText("Lựa chọn tốt nhất");
        btn_submit.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_promotionSelectBest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_promotionSelectBestActionPerformed(evt);
            }
        });
        pnl_promotionControl.add(btn_promotionSelectBest);

        frame_promotionList.getContentPane().add(pnl_promotionControl, java.awt.BorderLayout.SOUTH);

        tbl_promotionList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã khuyến mãi", "Khuyến mãi theo", "Giá trị khuyến mãi"
            }
        ));
        src_promotionList.setViewportView(tbl_promotionList);

        frame_promotionList.getContentPane().add(src_promotionList, java.awt.BorderLayout.CENTER);

        setFocusCycleRoot(true);
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                formAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                formAncestorRemoved(evt);
            }
        });
        setLayout(new java.awt.GridLayout(1, 0));

        splitPane_main.setResizeWeight(0.7);
        splitPane_main.setMinimumSize(new java.awt.Dimension(1305, 768));

        pnl_left.setMinimumSize(new java.awt.Dimension(600, 59));
        pnl_left.setPreferredSize(new java.awt.Dimension(900, 768));
        pnl_left.setLayout(new java.awt.BorderLayout());

        pnl_header.setPreferredSize(new java.awt.Dimension(1366, 50));
        pnl_header.setLayout(new javax.swing.BoxLayout(pnl_header, javax.swing.BoxLayout.LINE_AXIS));

        txt_search.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mã sản phẩm (ESC)");
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

        chk_defaultCustomer.setText("Khách hàng mặc định (F6)");
        chk_defaultCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_defaultCustomerActionPerformed(evt);
            }
        });
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

        txt_customerName.setEditable(false);
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

        txt_customerRank.setEditable(false);
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

        txt_orderId.setEditable(false);
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

        txt_orderDate.setEditable(false);
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

        txt_orderDiscount.setEditable(false);
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

        txt_orderPay.setEditable(false);
        txt_orderPay.setFont(txt_orderPay.getFont().deriveFont(txt_orderPay.getFont().getStyle() | java.awt.Font.BOLD, txt_orderPay.getFont().getSize()+4));
        txt_orderPay.setForeground(new java.awt.Color(0, 102, 255));
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

        btn_insert000.setText("000");
        btn_insert000.setMaximumSize(new java.awt.Dimension(72, 40));
        btn_insert000.setMinimumSize(new java.awt.Dimension(72, 40));
        btn_insert000.setPreferredSize(new java.awt.Dimension(72, 40));
        btn_insert000.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_insert000ActionPerformed(evt);
            }
        });
        pnl_orderCustomerGive.add(btn_insert000);

        pnl_orderInfo.add(pnl_orderCustomerGive);

        pnl_orderCustomerGiveOptions.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_orderCustomerGiveOptions.setMaximumSize(new java.awt.Dimension(99999, 160));
        pnl_orderCustomerGiveOptions.setMinimumSize(new java.awt.Dimension(236, 160));
        pnl_orderCustomerGiveOptions.setPreferredSize(new java.awt.Dimension(561, 60));
        pnl_orderCustomerGiveOptions.setLayout(new java.awt.GridLayout(3, 3, 5, 5));

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

        btn_option7.setText("Gợi ý 7");
        btn_option7.setMaximumSize(new java.awt.Dimension(72, 40));
        btn_option7.setMinimumSize(new java.awt.Dimension(72, 35));
        btn_option7.setPreferredSize(new java.awt.Dimension(72, 35));
        pnl_orderCustomerGiveOptions.add(btn_option7);

        btn_option8.setText("Gợi ý 8");
        btn_option8.setMaximumSize(new java.awt.Dimension(72, 40));
        btn_option8.setMinimumSize(new java.awt.Dimension(72, 35));
        btn_option8.setPreferredSize(new java.awt.Dimension(72, 35));
        pnl_orderCustomerGiveOptions.add(btn_option8);

        btn_option9.setText("Gợi ý 8");
        btn_option9.setMaximumSize(new java.awt.Dimension(72, 40));
        btn_option9.setMinimumSize(new java.awt.Dimension(72, 35));
        btn_option9.setPreferredSize(new java.awt.Dimension(72, 35));
        pnl_orderCustomerGiveOptions.add(btn_option9);

        pnl_orderInfo.add(pnl_orderCustomerGiveOptions);

        pnl_orderCustomerReturn.setMaximumSize(new java.awt.Dimension(99999, 40));
        pnl_orderCustomerReturn.setPreferredSize(new java.awt.Dimension(561, 40));
        pnl_orderCustomerReturn.setLayout(new javax.swing.BoxLayout(pnl_orderCustomerReturn, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderCustomerReturn.setText("Tiền thừa");
        lbl_orderCustomerReturn.setMinimumSize(new java.awt.Dimension(130, 40));
        lbl_orderCustomerReturn.setPreferredSize(new java.awt.Dimension(130, 40));
        pnl_orderCustomerReturn.add(lbl_orderCustomerReturn);

        txt_orderCustomerReturn.setEditable(false);
        txt_orderCustomerReturn.setMaximumSize(new java.awt.Dimension(99999, 35));
        txt_orderCustomerReturn.setMinimumSize(new java.awt.Dimension(100, 35));
        txt_orderCustomerReturn.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_orderCustomerReturn.add(txt_orderCustomerReturn);

        pnl_orderInfo.add(pnl_orderCustomerReturn);

        pnl_notification.setMaximumSize(new java.awt.Dimension(32767, 30));
        pnl_notification.setLayout(new java.awt.GridLayout(1, 0));

        lbl_notification.setFont(lbl_notification.getFont().deriveFont((lbl_notification.getFont().getStyle() | java.awt.Font.ITALIC), lbl_notification.getFont().getSize()-4));
        lbl_notification.setForeground(new java.awt.Color(0, 153, 255));
        lbl_notification.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pnl_notification.add(lbl_notification);

        pnl_orderInfo.add(pnl_notification);

        pnl_info.add(pnl_orderInfo);

        pnl_right.add(pnl_info, java.awt.BorderLayout.CENTER);

        pnl_control.setLayout(new javax.swing.BoxLayout(pnl_control, javax.swing.BoxLayout.PAGE_AXIS));

        pnl_btnGroup.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_btnGroup.setPreferredSize(new java.awt.Dimension(281, 100));
        pnl_btnGroup.setLayout(new java.awt.GridLayout(2, 2, 5, 5));

        btn_save.setText("LƯU TẠM (F2)");
        btn_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_saveActionPerformed(evt);
            }
        });
        pnl_btnGroup.add(btn_save);

        btn_viewSaves.setText("XỨ LÍ ĐƠN TẠM (F3)");
        btn_viewSaves.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_viewSavesActionPerformed(evt);
            }
        });
        pnl_btnGroup.add(btn_viewSaves);

        btn_cancle.setText("HỦY (F4)");
        btn_cancle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancleActionPerformed(evt);
            }
        });
        pnl_btnGroup.add(btn_cancle);

        btn_promotion.setText("KHUYẾN MÃI (F5)");
        btn_promotion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_promotionActionPerformed(evt);
            }
        });
        pnl_btnGroup.add(btn_promotion);

        pnl_control.add(pnl_btnGroup);

        pnl_btnMain.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 5, 5));
        pnl_btnMain.setPreferredSize(new java.awt.Dimension(561, 50));
        pnl_btnMain.setLayout(new java.awt.BorderLayout());

        btn_submit.setFont(btn_submit.getFont().deriveFont((float)18));
        btn_submit.setText("THANH TOÁN (F1)");
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
            if (isOldOrder) {
                boolean isDeleted = bus.deleteOrder(order.getOrderID());

                if (isDeleted) {
                    Notifications.getInstance().show(Notifications.Type.INFO, "Đã hủy thành công hóa đơn lưu tạm");
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể hủy đơn lưu tạm");
                }
            } else {
                Notifications.getInstance().show(Notifications.Type.INFO, "Đã hủy thành công hóa đơn");
            }

            rerender();
        }

    }//GEN-LAST:event_btn_cancleActionPerformed

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchActionPerformed
        handleAddItem();
    }//GEN-LAST:event_btn_searchActionPerformed

    private void txt_searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchKeyPressed
//        Tránh thêm 2 lần khi mà trùng với xử lí quét mã
        if (!tempInput.trim().toLowerCase().equals(txt_search.getText().trim().toLowerCase()) && evt.getKeyCode() == KeyEvent.VK_ENTER) {
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

    private void btn_viewSavesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_viewSavesActionPerformed
//        Căn giữa theo ứng dụng lớn
        frame_savedOrder.setLocationRelativeTo(Application.app);
        renderSavedOrderTable();
        frame_savedOrder.setVisible(true);
    }//GEN-LAST:event_btn_viewSavesActionPerformed

    private void btn_savedOrderSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_savedOrderSelectActionPerformed
        if (!cart.isEmpty()) {
            Notifications.getInstance().show(Notifications.Type.WARNING, 3000, "Đơn hàng hiện tại vẫn chưa xử lí xong, hãy hoàn thành hoặc hủy bỏ đơn hiện tại để xử lí đơn lưu tạm");
            return;
        }

        int row = tbl_savedOrder.getSelectedRow();

//        Nếu đang không chọn dòng nào thì thông báo
        if (row == -1) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng chọn hóa đơn muốn xử lí");
            return;
        }

        String orderID = tbl_savedOrder.getValueAt(row, 0).toString();
        isOldOrder = true;
        loadSavedOrder(orderID);
        frame_savedOrder.dispose();
        Notifications.getInstance().show(Notifications.Type.INFO, "Đã tải lên thông tin của hóa đơn " + orderID);

    }//GEN-LAST:event_btn_savedOrderSelectActionPerformed

    private void btn_savedOrderDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_savedOrderDeleteActionPerformed
        int row = tbl_savedOrder.getSelectedRow();

//        Nếu đang không chọn dòng nào thì thông báo
        if (row == -1) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng chọn hóa đơn muốn xử lí");
            return;
        }
        String orderID = tbl_savedOrder.getValueAt(row, 0).toString();
        if (JOptionPane.showConfirmDialog(frame_savedOrder, String.format("Bạn có muốn xóa đơn hàng đã lưu tạm này? (%s)", orderID), "Xóa đơn lưu tạm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            boolean isDeleted = bus.deleteOrder(orderID);

            if (isDeleted) {
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa thành công đơn lưu tạm " + orderID);
                renderSavedOrderTable();
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể xóa đơn lưu tạm " + orderID + " lỗi không xác định");
            }
        }
    }//GEN-LAST:event_btn_savedOrderDeleteActionPerformed

    private boolean updateOrder(boolean isCompleted) {

        Notifications.getInstance().show(Notifications.Type.INFO, "Đang lưu trữ hóa đơn...");
        try {
            order.setEmployee(Application.employee);
            java.sql.Timestamp now = java.sql.Timestamp.valueOf(LocalDateTime.now());
            order.setOrderAt(now);

            order.setOrderDetail(cart);
            order.setStatus(isCompleted);
//                Gỡ bỏ các khuyến mãi để tránh lỗi về sau
            if (!isCompleted) {
                order.setPromotion(null);
                for (OrderDetail item : order.getOrderDetail()) {
                    item.setSeasonalDiscount(0);
                }
            }

            if (chk_defaultCustomer.isSelected()) {
//                Khách hàng mặc định
                order.setCustomer(customer);
            } else {
                order.setCustomer(customer);
            }
            boolean isATMPayment = cmb_orderPaymentMethod.getSelectedIndex() == 1;
            order.setPayment(isATMPayment);

            if (isATMPayment) {
                order.setMoneyGiven(order.getSubTotal());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return bus.updateInDatabase(order);
    }

    private boolean saveOrder(boolean isComplete) {
        Notifications.getInstance().show(Notifications.Type.INFO, "Đang lưu trữ hóa đơn...");

        try {
            order.setEmployee(Application.employee);
            java.sql.Timestamp now = java.sql.Timestamp.valueOf(LocalDateTime.now());
            order.setOrderAt(now);

//            Đã hoàn thành hay chưa
            order.setStatus(isComplete);

            order.setOrderDetail(cart);
            //                 Gỡ bỏ các khuyến mãi để tránh lỗi về sau khi mà hóa đơn ở trạng thái luuw tạm
            if (!isComplete) {
                order.setPromotion(null);
                for (OrderDetail item : order.getOrderDetail()) {
                    item.setSeasonalDiscount(0);
                }
            }
            boolean isATMPayment = cmb_orderPaymentMethod.getSelectedIndex() == 1;
            order.setPayment(isATMPayment);
            if (isATMPayment) {
                order.setMoneyGiven(order.getSubTotal());
            }
            if (chk_defaultCustomer.isSelected()) {
//                Khách hàng mặc định
                order.setCustomer(defaultCustomer);
            } else {
                order.setCustomer(customer);
            }
            order.setStatus(isComplete);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return bus.saveToDatabase(order);
    }
    private void btn_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_saveActionPerformed
        if (!orderValidate(false)) {
            return;
        }

        if (JOptionPane.showConfirmDialog(Application.app, "Bạn có muốn lưu tạm hóa đơn này để xử lí sau không?", "Xác nhận lưu tạm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

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
                boolean isSaved = isOldOrder ? updateOrder(false) : saveOrder(false);
                if (isSaved) {
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã lưu tạm thành công đơn hàng " + order.getOrderID());
//                Rerender panel
                    rerender();
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Có lỗi xảy ra khi lưu đơn hàng vào cơ sở dữ liệu" + order.getOrderID());
                }
            } catch (Exception ex) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể lưu tạm hóa đơn " + order.getOrderID() + ": " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btn_saveActionPerformed

    private void btn_savedOrderDeleteAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_savedOrderDeleteAllActionPerformed
        int rows = tbl_savedOrder.getRowCount();
        if (JOptionPane.showConfirmDialog(frame_savedOrder, "Bạn có muốn xóa toàn bộ đơn hàng đã lưu tạm này (Không thể phục hồi)?", "Xóa đơn lưu tạm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            for (int row = 0; row < rows; row++) {
                String orderID = tbl_savedOrder.getValueAt(row, 0).toString();
                boolean isDeleted = bus.deleteOrder(orderID);
                if (isDeleted) {
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã xóa thành công toàn bộ đơn lưu tạm " + orderID);
                    renderSavedOrderTable();
                } else {
                    Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể xóa đơn lưu tạm " + orderID + " lỗi không xác định");
                }
            }

        }
    }//GEN-LAST:event_btn_savedOrderDeleteAllActionPerformed

    private void btn_promotionSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_promotionSelectActionPerformed
        int row = tbl_promotionList.getSelectedRow();
        if (row == -1) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng chọn khuyến mãi muốn áp dụng");
        }

        String id = tbl_promotionList.getValueAt(row, 0).toString();
        try {
            Promotion promotion = bus.getPromotion(id);
            if (promotion == null) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Không tìm thấy thông tin khuyến mãi");
                return;
            }
            order.setPromotion(promotion);
            double discountAmount = promotion.getTypeDiscount() == DiscountType.PERCENT ? promotion.getDiscount() / 100 * total : promotion.getDiscount();
//          Cập nhật trạng thái
            setDiscount(discountAmount);
            renderOrderTotal();
//            calculateOptionCashGive();
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã áp dụng khuyến mãi và giảm được " + utilities.FormatNumber.toVND(discountAmount));
            frame_promotionList.dispose();
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể áp dụng khuyến mãi");
        }
    }//GEN-LAST:event_btn_promotionSelectActionPerformed

    private void renderOrderTotal() {
        txt_orderDiscount.setText(utilities.FormatNumber.toVND(discount));
        txt_orderPay.setText(utilities.FormatNumber.toVND(totalAfterDiscount));
        if (order.getPromotion() != null) {
            String promotionDiscountValue = order.getPromotion().getTypeDiscount() == DiscountType.PERCENT ? order.getPromotion().getDiscount() + "%" : utilities.FormatNumber.toVND(order.getPromotion().getDiscount());
            lbl_notification.setText("Khuyến mãi hạng thành viên " + customer.getRank() + " được giảm: " + promotionDiscountValue + " trên tổng hóa đơn");
        }
    }

    private void btn_promotionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_promotionActionPerformed
        if (customer == null) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Bạn phải thêm thông tin khách hàng để làm hành động này");
            return;
        }

        if (customer.getCustomerID() == null ? defaultCustomer.getCustomerID() == null : customer.getCustomerID().equals(defaultCustomer.getCustomerID())) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Khách hàng mặc định không thể sử dụng khuyến mãi trên hóa đơn");
            return;
        }
        renderPromotionOrderTable();
        frame_promotionList.setLocationRelativeTo(null);
        frame_promotionList.setVisible(true);
    }//GEN-LAST:event_btn_promotionActionPerformed

    private void handleAddBest() {
        Promotion bestPromotion = getBestPromotionForCustomer();
        if (bestPromotion != null) {
            try {
                order.setPromotion(bestPromotion);
                double discountAmount = bestPromotion.getTypeDiscount() == DiscountType.PERCENT ? bestPromotion.getDiscount() / 100 * total : bestPromotion.getDiscount();
//          Cập nhật trạng thái
                setDiscount(discountAmount);
                renderOrderTotal();
//            calculateOptionCashGive();
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đã áp dụng khuyến mãi và giảm được " + utilities.FormatNumber.toVND(discountAmount));
                frame_promotionList.dispose();
            } catch (Exception ex) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Không thể áp dụng khuyến mãi");
            }
        }
    }
    private void btn_promotionSelectBestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_promotionSelectBestActionPerformed
        handleAddBest();
    }//GEN-LAST:event_btn_promotionSelectBestActionPerformed

    private void btn_insert000ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_insert000ActionPerformed
        setCustomerGive(Double.parseDouble(txt_orderCustomerGive.getText()) * 1000);
    }//GEN-LAST:event_btn_insert000ActionPerformed

    /*
        Vì panel này luôn luôn được giữ lại trong suốt quá trình sống của ứng dụng, 
        cho nên phải gỡ lắng sự kiện native tránh ảnh hưởng đến cái khác
     */
    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorAdded
        System.out.println("Added");
        GlobalScreen.addNativeKeyListener(barcodeScannerHandler);
    }//GEN-LAST:event_formAncestorAdded

    private void formAncestorRemoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorRemoved
        System.out.println("remove");
        GlobalScreen.removeNativeKeyListener(barcodeScannerHandler);
    }//GEN-LAST:event_formAncestorRemoved

    private void chk_defaultCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_defaultCustomerActionPerformed
        System.out.println("Trigger");
        if (chk_defaultCustomer.isSelected()) {
            setOrderCustomer(defaultCustomer);
        } else {
            setOrderCustomer(customer);
        }

        System.out.println(customer);
    }//GEN-LAST:event_chk_defaultCustomerActionPerformed

    private void renderSavedOrderTable() {
        DefaultTableModel tblModel_savedOrder = new DefaultTableModel(new String[]{"Mã hóa đơn", "Tên khách hàng", "Ngày tạo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Order item : bus.getSavedOrders()) {
            tblModel_savedOrder.addRow(new Object[]{item.getOrderID(), item.getCustomer().getName(), item.getOrderAt()});
        }
        tbl_savedOrder.setModel(tblModel_savedOrder);
    }

    private void loadSavedOrder(String id) {
        Order savedOrder = bus.getOrder(id);
//        update state
        order = savedOrder;
        customer = order.getCustomer();
        cart = order.getOrderDetail();
//        Tự động thêm khuyến mãi có thể áp dụng ở thời điểm hiện tại vào các  sản phẩm
        for (OrderDetail item : cart) {
            double discountAmount = bus.getBestProductPromotionDiscountAmountAvailable(item.getProduct().getProductID());
            if (discountAmount > 0) {
//                    Lấy phần giảm cho 1 sản phẩm để tăng lên
                item.setSeasonalDiscount(discountAmount * item.getQuantity());
                Notifications.getInstance().show(Notifications.Type.INFO, "Sản phẩm " + item.getProduct().getName() + " này được giảm " + utilities.FormatNumber.toVND(discountAmount) + " trên 1 sản phẩm");
            }
        }

//        Update view
        renderOrder();
        renderCartTable();
        renderCustomer();
    }

//    Promotion
    private void renderPromotionOrderTable() {
        DefaultTableModel tblModel_promotionList = new DefaultTableModel(new String[]{"Mã khuyến mãi", "Loại khuyến mãi", "Giá trị khuyến mãi", "Số tiền giảm"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);

//        Align
        TableColumnModel columnModel = tbl_promotionList.getColumnModel();
        for (int index : new Integer[]{1, 2}) {
            columnModel.getColumn(index).setCellRenderer(rightAlign);
        }

        if (customer == null || customer == defaultCustomer) {
            return;
        }

        for (Promotion item : bus.getPromotionOfOrderAvailable(customer.getRankType().getValue())) {
            double discountAmount = item.getTypeDiscount() == DiscountType.PERCENT ? item.getDiscount() / 100 * total : item.getDiscount();
            String discountValue = item.getTypeDiscount() == DiscountType.PERCENT ? String.format("%.2f%%", item.getDiscount()) : utilities.FormatNumber.toVND(item.getDiscount());
            String discountTypeDisplay = item.getTypeDiscount() == DiscountType.PERCENT ? "Theo phần trăm (%)" : "Theo chiết khấu (đ)";
            tblModel_promotionList.addRow(new Object[]{item.getPromotionID(), discountTypeDisplay, discountValue, utilities.FormatNumber.toVND(discountAmount)});
        }
        tbl_promotionList.setModel(tblModel_promotionList);
    }

    private Promotion getBestPromotionForCustomer() {
        Promotion bestPromotion = null;
        if (customer == null || customer == defaultCustomer) {
            return null;
        }
        Double bestDiscountValue = 0.0;
        for (Promotion item : bus.getPromotionOfOrderAvailable(customer.getRankType().getValue())) {
            System.out.println(item.getPromotionID());
            double discountValue = item.getTypeDiscount() == DiscountType.PERCENT ? item.getDiscount() : item.getDiscount();
            if (bestDiscountValue < discountValue) {
                bestPromotion = item;
                bestDiscountValue = discountValue;
            }
        }

        return bestPromotion;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_cancle;
    private javax.swing.JButton btn_insert000;
    private javax.swing.JButton btn_option1;
    private javax.swing.JButton btn_option2;
    private javax.swing.JButton btn_option3;
    private javax.swing.JButton btn_option4;
    private javax.swing.JButton btn_option5;
    private javax.swing.JButton btn_option6;
    private javax.swing.JButton btn_option7;
    private javax.swing.JButton btn_option8;
    private javax.swing.JButton btn_option9;
    private javax.swing.JButton btn_promotion;
    private javax.swing.JButton btn_promotionSelect;
    private javax.swing.JButton btn_promotionSelectBest;
    private javax.swing.JButton btn_save;
    private javax.swing.JButton btn_savedOrderDelete;
    private javax.swing.JButton btn_savedOrderDeleteAll;
    private javax.swing.JButton btn_savedOrderSelect;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_submit;
    private javax.swing.JButton btn_viewSaves;
    private javax.swing.JCheckBox chk_defaultCustomer;
    private javax.swing.JComboBox<String> cmb_orderPaymentMethod;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JFrame frame_promotionList;
    private javax.swing.JFrame frame_savedOrder;
    private javax.swing.JLabel lbl_customerName;
    private javax.swing.JLabel lbl_customerPhone;
    private javax.swing.JLabel lbl_customerRank;
    private javax.swing.JLabel lbl_notification;
    private javax.swing.JLabel lbl_orderCustomerGive;
    private javax.swing.JLabel lbl_orderCustomerReturn;
    private javax.swing.JLabel lbl_orderDate;
    private javax.swing.JLabel lbl_orderDiscount;
    private javax.swing.JLabel lbl_orderId;
    private javax.swing.JLabel lbl_orderPay;
    private javax.swing.JLabel lbl_orderPaymentMethod;
    private javax.swing.JLabel lbl_promotionList;
    private javax.swing.JLabel lbl_savedOrderTitle;
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
    private javax.swing.JPanel pnl_main;
    private javax.swing.JPanel pnl_notification;
    private javax.swing.JPanel pnl_orderCustomerGive;
    private javax.swing.JPanel pnl_orderCustomerGiveOptions;
    private javax.swing.JPanel pnl_orderCustomerReturn;
    private javax.swing.JPanel pnl_orderDate;
    private javax.swing.JPanel pnl_orderId;
    private javax.swing.JPanel pnl_orderInfo;
    private javax.swing.JPanel pnl_orderPay;
    private javax.swing.JPanel pnl_orderPaymentMethod;
    private javax.swing.JPanel pnl_promotionControl;
    private javax.swing.JPanel pnl_right;
    private javax.swing.JPanel pnl_savedOrderControl;
    private javax.swing.JScrollPane scr_cart;
    private javax.swing.JScrollPane scr_savedOrderDetail;
    private javax.swing.JScrollPane scr_savedOrders;
    private javax.swing.JSplitPane splitPane_main;
    private javax.swing.JScrollPane src_promotionList;
    private javax.swing.JTable tbl_cart;
    private javax.swing.JTable tbl_promotionList;
    private javax.swing.JTable tbl_saveOrderDetail;
    private javax.swing.JTable tbl_savedOrder;
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

package gui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import gui.menu.Menu;
import gui.menu.MenuAction;
import javax.swing.JOptionPane;
import main.Application;

/**
 *
 * @author thanhcanhit
 */
public class MainView extends JLayeredPane {

    private Sales_GUI salesForm = new Sales_GUI();

    public MainView() {
        init();
    }

    private void init() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new MainFormLayout());
        menu = new Menu();
        pnl_body = new JPanel(new BorderLayout());
        initMenuArrowIcon();
        btn_menu.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.button.background;"
                + "arc:999;"
                + "focusWidth:0;"
                + "borderWidth:0");
        btn_menu.addActionListener((ActionEvent e) -> {
            setMenuFull(!menu.isMenuFull());
        });
        initMenuEvent();
        setLayer(btn_menu, JLayeredPane.POPUP_LAYER);
        add(btn_menu);
        add(menu);
        add(pnl_body);
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);
        initMenuArrowIcon();
    }

    public void refreshSalesForm() {
        salesForm = new Sales_GUI();
        Application.showForm(salesForm);
    }

    private void initMenuArrowIcon() {
        if (btn_menu == null) {
            btn_menu = new JButton();
        }
        String icon = (getComponentOrientation().isLeftToRight()) ? "menu_left.svg" : "menu_right.svg";
        btn_menu.setIcon(new FlatSVGIcon("imgs/menu/" + icon, 0.8f));
    }

    public static void rerenderMenuByEmployee() {
        menu.rerender();
    }

    private void initMenuEvent() {
        menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
            switch (index) {
                case 0:
                    Application.showForm(salesForm);
                    break;
                case 1:
                    switch (subIndex) {
                        case 1:
                            Application.showForm(new CreatePurchaseOrder_GUI());
                            break;
                        case 2:
                            Application.showForm(new OrderManagement_GUI());
                            break;
                        case 3:
                            Application.showForm(new PurchaseOrderManagement_GUI());
                            break;
                        default:
                            action.cancel();
                            break;
                    }
                    break;
                case 2:
                    switch (subIndex) {
                        case 1:
                            Application.showForm(new CreateReturnOrder_GUI());
                            break;
                        case 2:
                            Application.showForm(new ReturnOrderManagemant_GUI());
                            break;
                        default:
                            action.cancel();
                            break;
                    }
                    break;
                case 3:
                    switch (subIndex) {
                        case 1:
                            Application.showForm(new JPanel());
                            break;
                        case 2:
                            Application.showForm(new OrderPromotionManagement_GUI());
                            break;
                        default:
                            action.cancel();
                            break;
                    }
                    break;
                case 4:
                    Application.showForm(new ProductManagement_GUI());
                    break;
                case 5:
                    Application.showForm(new EmployeeManagement_GUI());
                    break;
                case 6:
                    Application.showForm(new CustomerManagement_GUI());
                    break;
                case 7:
                    switch (subIndex) {
                        case 1:
                            Application.showForm(new StatisticSales_GUI());
                            break;
                        case 2:
                            Application.showForm(new StatisticProduct_GUI());
                            break;
                        case 3:
                            Application.showForm(new StatisticCustomer_GUI());
                            break;
                        default:
                            action.cancel();
                            break;
                    }
                    break;
                case 8:
                    switch (subIndex) {
                        case 1:
                            Application.showForm(new StatementCashCount_GUI());
                            break;
                        case 2:
                            Application.showForm(new StatementAccounting_GUI());
                            break;
                        case 3:
                            Application.showForm(new ViewCashCountSheetList_GUI());
                            break;
                        case 4:
                            Application.showForm(new ViewAcountingVoucherList_GUI());
                            break;
                        default:
                            action.cancel();
                            break;
                    }
                    break;
                case 9:
                    switch (subIndex) {
                        case 1:
                            Application.showForm(new JPanel());
                            break;
                        case 2:
                            Application.showForm(new JPanel());
                            break;
                        case 3:
                            Application.showForm(new JPanel());
                            break;
                        case 4:
                            Application.showForm(new JPanel());
                            break;
                        default:
                            action.cancel();
                            break;
                    }
                    break;
                case 10:
                    if (JOptionPane.showConfirmDialog(this, "Bạn có thật sự muốn đăng xuất", "Xác nhận hành động", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        Application.logout();
                    }

                    break;
                default:
                    action.cancel();
                    break;
            }
        });
    }

    private void setMenuFull(boolean full) {
        String icon;
        if (getComponentOrientation().isLeftToRight()) {
            icon = (full) ? "menu_left.svg" : "menu_right.svg";
        } else {
            icon = (full) ? "menu_right.svg" : "menu_left.svg";
        }
        btn_menu.setIcon(new FlatSVGIcon("imgs/menu/" + icon, 0.8f));
        menu.setMenuFull(full);
        revalidate();
    }

    public void hideMenu() {
        menu.hideMenuItem();
    }

    public void showForm(Component component) {
        pnl_body.removeAll();
        pnl_body.add(component);
        pnl_body.repaint();
        pnl_body.revalidate();
    }

    public void setSelectedMenu(int index, int subIndex) {
        menu.setSelectedMenu(index, subIndex);
    }

    private static Menu menu;
    private JPanel pnl_body;
    private JButton btn_menu;

    private class MainFormLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, 5);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                boolean ltr = parent.getComponentOrientation().isLeftToRight();
                Insets insets = UIScale.scale(parent.getInsets());
                int x = insets.left;
                int y = insets.top;
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int menuWidth = UIScale.scale(menu.isMenuFull() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
                int menuX = ltr ? x : x + width - menuWidth;
                menu.setBounds(menuX, y, menuWidth, height);
                int menuButtonWidth = btn_menu.getPreferredSize().width;
                int menuButtonHeight = btn_menu.getPreferredSize().height;
                int menubX;
                if (ltr) {
                    menubX = (int) (x + menuWidth - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.3f)));
                } else {
                    menubX = (int) (menuX - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.7f)));
                }
                btn_menu.setBounds(menubX, UIScale.scale(30), menuButtonWidth, menuButtonHeight);
                int gap = UIScale.scale(5);
                int bodyWidth = width - menuWidth - gap;
                int bodyHeight = height;
                int bodyx = ltr ? (x + menuWidth + gap) : x;
                int bodyy = y;
                pnl_body.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
            }
        }

    }
}

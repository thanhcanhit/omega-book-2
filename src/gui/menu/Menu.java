package gui.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import main.Application;

/**
 *
 * @author thanhcanhit
 */
public class Menu extends JPanel {

    public static final int STORE_EMPLOYEE = 0;
    public static final int STORE_MANAGER = 1;
    public static final int STORE_HIGH_MANAGER = 2;

    public static JLabel lbl_currentEmployee;
    private static final String menuItems[][] = {
        {"Bán hàng"},
        {"Đơn hàng", "Tạo đơn nhập", "Quản lí đơn bán", "Quản lí đơn nhập"},
        {"Đổi trả", "Quản lí đơn đổi trả", "Tạo đơn đổi trả",},
        {"Khuyến mãi"},
        {"Sản phẩm"},
        {"Nhân viên"},
        {"Khách hàng"},
        {"Thống kê", "Thống kê doanh thu", "Thống kê sản phẩm", "Thống kê khách hàng"},
        {"Báo cáo", "Kiểm tiền", "Kết toán", "Danh sách phiếu kiểm tiền", "Danh sách phiếu kết toán"},
        {"Đăng xuất"}
    };

//    row col
    private static final int employeeItemsBan[][] = {
        {1, 1},
        {2, 0}, {2, 1}, {2, 2},
        {3, 0},
        {5, 0}};

    private static final int storeManagerItemsBan[][] = {
        {3, 0}
    };

    private static final int storeHighManagerItemsBan[][] = {};

    public boolean isMenuFull() {
        return menuFull;
    }

    public void setMenuFull(boolean menuFull) {
        this.menuFull = menuFull;
        if (menuFull) {
            lbl_header.setText(headerName);
            lbl_header.setHorizontalAlignment(getComponentOrientation().isLeftToRight() ? JLabel.LEFT : JLabel.RIGHT);
        } else {
            lbl_header.setText("");
            lbl_header.setHorizontalAlignment(JLabel.CENTER);
        }
        for (Component com : pnl_menu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).setFull(menuFull);
            }
        }
        lightDarkMode.setMenuFull(menuFull);
        toolBarAccentColor.setMenuFull(menuFull);
    }

    public static boolean isBan(String menuItemName) {
//        Nếu nhân viên rỗng thì cấm tất cả
        if (Application.employee == null) {
            return true;
        }

//        Xác định xem tài khoản thuộc loại nào để ban
        int roleIndex = Menu.STORE_EMPLOYEE;
        String roleName = Application.employee.getRole();

        if (roleName.equalsIgnoreCase("Cửa hàng trưởng")) {
            roleIndex = Menu.STORE_MANAGER;
        } else if (roleName.equalsIgnoreCase("Giám sát viên")) {
            roleIndex = Menu.STORE_HIGH_MANAGER;
        }

        for (String banItem : getBanList(roleIndex)) {
            if (banItem.equals(menuItemName)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getBanList(int role) {
        ArrayList<String> banList = new ArrayList<>();
        int[][] itemsBan;
        switch (role) {
            case STORE_EMPLOYEE:
                itemsBan = employeeItemsBan;
                break;
            case STORE_MANAGER:
                itemsBan = storeManagerItemsBan;
                break;
            case STORE_HIGH_MANAGER:
                itemsBan = storeHighManagerItemsBan;
                break;
            default:
                itemsBan = employeeItemsBan;
        }

        for (int[] item : itemsBan) {
            int row = item[0];
            int col = item[1];
            banList.add(menuItems[row][col]);
        }

        return banList;
    }

    private final List<MenuEvent> events = new ArrayList<>();
    private boolean menuFull = true;
    private final String headerName = "Omega Book";

    protected final boolean hideMenuTitleOnMinimum = true;
    protected final int menuTitleLeftInset = 5;
    protected final int menuTitleVgap = 5;
    protected final int menuMaxWidth = 250;
    protected final int menuMinWidth = 60;
    protected final int headerFullHgap = 5;

    public Menu() {
        init();
    }

    public void rerender() {
        this.removeAll();
        init();
    }

    private void init() {
        setLayout(new MenuLayout());
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:20,2,2,2;"
                + "background:$Menu.background;"
                + "arc:10");
        lbl_header = new JLabel(headerName);
        lbl_header.setIcon(new FlatSVGIcon("imgs/icon.svg"));
        lbl_header.setIconTextGap(20);
        lbl_header.putClientProperty(FlatClientProperties.STYLE, ""
                + "border: 5,5,5,5;"
                + "font:$Menu.header.font;"
                + "foreground:$Menu.foreground");

        //  Menu
        scr_main = new JScrollPane();
        pnl_menu = new JPanel(new MenuItemLayout(this));
        pnl_menu.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:5,5,5,5;"
                + "background:$Menu.background");

        scr_main.setViewportView(pnl_menu);
        scr_main.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:null");
        JScrollBar vscroll = scr_main.getVerticalScrollBar();
        vscroll.setUnitIncrement(10);
        vscroll.putClientProperty(FlatClientProperties.STYLE, ""
                + "width:$Menu.scroll.width;"
                + "trackInsets:$Menu.scroll.trackInsets;"
                + "thumbInsets:$Menu.scroll.thumbInsets;"
                + "background:$Menu.ScrollBar.background;"
                + "thumb:$Menu.ScrollBar.thumb");
        createMenu();
        lightDarkMode = new LightDarkMode();
        toolBarAccentColor = new ToolBarAccentColor(this);
        toolBarAccentColor.setVisible(FlatUIUtils.getUIBoolean("AccentControl.show", false));
        add(lbl_header);
        add(scr_main);
        add(lightDarkMode);
        add(toolBarAccentColor);
    }

    private void createMenu() {
        String title = Application.employee == null ? " Chủ tịch Cảnh " : String.format(" %s. %s ", Application.employee.getRole(), Application.employee.getName());
        lbl_currentEmployee = createTitle(title);
        pnl_menu.add(lbl_currentEmployee);

//        Render danh sách menu phía bên trên
        int index = 0;
        for (String[] menuItemName : menuItems) {
            String menuName = menuItemName[0];
            if (menuName.startsWith("~") && menuName.endsWith("~")) {
                pnl_menu.add(createTitle(menuName));
            } else {
                MenuItem menuItem = new MenuItem(this, menuItemName, index++, events);
                pnl_menu.add(menuItem);
            }
        }
    }

    private JLabel createTitle(String title) {
        String menuName = title.substring(1, title.length() - 1);
        JLabel lbTitle = new JLabel(menuName);
        lbTitle.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$Menu.label.font;"
                + "foreground:$Menu.title.foreground");
        return lbTitle;
    }

    public void setSelectedMenu(int index, int subIndex) {
        runEvent(index, subIndex);
    }

    protected void setSelected(int index, int subIndex) {
        int size = pnl_menu.getComponentCount();
        for (int i = 0; i < size; i++) {
            Component com = pnl_menu.getComponent(i);
            if (com instanceof MenuItem) {
                MenuItem item = (MenuItem) com;
                if (item.getMenuIndex() == index) {
                    item.setSelectedIndex(subIndex);
                } else {
                    item.setSelectedIndex(-1);
                }
            }
        }
    }

    protected void runEvent(int index, int subIndex) {
        MenuAction menuAction = new MenuAction();
        for (MenuEvent event : events) {
            event.menuSelected(index, subIndex, menuAction);
        }
        if (!menuAction.isCancel()) {
            setSelected(index, subIndex);
        }
    }

    public void addMenuEvent(MenuEvent event) {
        events.add(event);
    }

    public void hideMenuItem() {
        for (Component com : pnl_menu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).hideMenuItem();
            }
        }
        revalidate();
    }

    public boolean isHideMenuTitleOnMinimum() {
        return hideMenuTitleOnMinimum;
    }

    public int getMenuTitleLeftInset() {
        return menuTitleLeftInset;
    }

    public int getMenuTitleVgap() {
        return menuTitleVgap;
    }

    public int getMenuMaxWidth() {
        return menuMaxWidth;
    }

    public int getMenuMinWidth() {
        return menuMinWidth;
    }

    private JLabel lbl_header;
    private JScrollPane scr_main;
    private JPanel pnl_menu;
    private LightDarkMode lightDarkMode;
    private ToolBarAccentColor toolBarAccentColor;

    private class MenuLayout implements LayoutManager {

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
                Insets insets = parent.getInsets();
                int x = insets.left;
                int y = insets.top;
                int gap = UIScale.scale(5);
                int sheaderFullHgap = UIScale.scale(headerFullHgap);
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int iconWidth = width;
                int iconHeight = lbl_header.getPreferredSize().height;
                int hgap = menuFull ? sheaderFullHgap : 0;
                int accentColorHeight = 0;
                if (toolBarAccentColor.isVisible()) {
                    accentColorHeight = toolBarAccentColor.getPreferredSize().height + gap;
                }

                lbl_header.setBounds(x + hgap, y, iconWidth - (hgap * 2), iconHeight);
                int ldgap = UIScale.scale(10);
                int ldWidth = width - ldgap * 2;
                int ldHeight = lightDarkMode.getPreferredSize().height;
                int ldx = x + ldgap;
                int ldy = y + height - ldHeight - ldgap - accentColorHeight;

                int menux = x;
                int menuy = y + iconHeight + gap;
                int menuWidth = width;
                int menuHeight = height - (iconHeight + gap) - (ldHeight + ldgap * 2) - (accentColorHeight);
                scr_main.setBounds(menux, menuy, menuWidth, menuHeight);

                lightDarkMode.setBounds(ldx, ldy, ldWidth, ldHeight);

                if (toolBarAccentColor.isVisible()) {
                    int tbheight = toolBarAccentColor.getPreferredSize().height;
                    int tbwidth = Math.min(toolBarAccentColor.getPreferredSize().width, ldWidth);
                    int tby = y + height - tbheight - ldgap;
                    int tbx = ldx + ((ldWidth - tbwidth) / 2);
                    toolBarAccentColor.setBounds(tbx, tby, tbwidth, tbheight);
                }
            }
        }
    }
}

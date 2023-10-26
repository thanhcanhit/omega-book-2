package gui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import main.Application;

/**
 *
 * @author thanhcanhit
 */
public class Login_GUI extends javax.swing.JPanel {

    public Login_GUI() {
        initComponents();
        init();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
//        Image background = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/imgs/login/background.png"));
//        g.drawImage(background, 0, 0, this);

    }

    private void init() {
        setLayout(new LoginFormLayout());
        lbl_main.setLayout(new LoginLayout());
        lbl_title.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:$h1.font");
        lbl_main.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Login.background;"
                + "arc:20;"
                + "border:30,40,50,30");

        txt_password.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true;"
                + "showCapsLock:true");
        btn_login.putClientProperty(FlatClientProperties.STYLE, ""
                + "borderWidth:0;"
                + "focusWidth:0");
        txt_user.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mã nhân viên của bạn");
        txt_password.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nhập vào mật khẩu của bạn");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_main = new javax.swing.JPanel();
        btn_login = new javax.swing.JButton();
        lbl_title = new javax.swing.JLabel();
        lbl_user = new javax.swing.JLabel();
        txt_user = new javax.swing.JTextField();
        lbl_password = new javax.swing.JLabel();
        txt_password = new javax.swing.JPasswordField();

        btn_login.setText("Đăng nhập vào OmegaBook");
        btn_login.addActionListener((java.awt.event.ActionEvent evt) -> {
            cmdLoginActionPerformed(evt);
        });

        lbl_title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_title.setText("Omega Book");

        lbl_user.setText("Mã nhân viên");
        lbl_password.setText("Mật khẩu");

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(lbl_main);
        lbl_main.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
                loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(loginLayout.createSequentialGroup()
                                .addGroup(loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(loginLayout.createSequentialGroup()
                                                .addGroup(loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(loginLayout.createSequentialGroup()
                                                                .addGap(15, 15, 15)
                                                                .addComponent(lbl_user))
                                                        .addGroup(loginLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(txt_user, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 36, Short.MAX_VALUE))
                                        .addGroup(loginLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lbl_title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(loginLayout.createSequentialGroup()
                                                                .addGroup(loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(lbl_password)
                                                                        .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(0, 0, Short.MAX_VALUE)))))
                                .addContainerGap())
                        .addGroup(loginLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btn_login)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        loginLayout.setVerticalGroup(
                loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbl_title)
                                .addGap(10, 10, 10)
                                .addComponent(lbl_user)
                                .addGap(5, 5, 5)
                                .addComponent(txt_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(lbl_password)
                                .addGap(5, 5, 5)
                                .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(btn_login)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(204, 204, 204)
                                .addComponent(lbl_main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(319, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(lbl_main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(179, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLoginActionPerformed
        Application.login(null);
    }//GEN-LAST:event_cmdLoginActionPerformed

    private class LoginFormLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
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
                int width = parent.getWidth();
                int height = parent.getHeight();
                int loginWidth = UIScale.scale(320);
                int loginHeight = lbl_main.getPreferredSize().height;
                int x = (width - loginWidth) / 2;
                int y = (height - loginHeight) / 2;
                lbl_main.setBounds(x, y, loginWidth, loginHeight);
            }
        }
    }

    private class LoginLayout implements LayoutManager {

        private final int titleGap = 10;
        private final int textGap = 10;
        private final int labelGap = 5;
        private final int buttonGap = 50;

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets insets = parent.getInsets();
                int height = insets.top + insets.bottom;

                height += lbl_title.getPreferredSize().height;
                height += UIScale.scale(titleGap);
                height += lbl_user.getPreferredSize().height;
                height += UIScale.scale(labelGap);
                height += txt_user.getPreferredSize().height;
                height += UIScale.scale(textGap);

                height += lbl_password.getPreferredSize().height;
                height += UIScale.scale(labelGap);
                height += txt_password.getPreferredSize().height;
                height += UIScale.scale(buttonGap);
                height += btn_login.getPreferredSize().height;
                return new Dimension(0, height);
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
                int width = parent.getWidth() - (insets.left + insets.right);

                lbl_title.setBounds(x, y, width, lbl_title.getPreferredSize().height);
                y += lbl_title.getPreferredSize().height + UIScale.scale(titleGap);

                lbl_user.setBounds(x, y, width, lbl_user.getPreferredSize().height);
                y += lbl_user.getPreferredSize().height + UIScale.scale(labelGap);
                txt_user.setBounds(x, y, width, txt_user.getPreferredSize().height);
                y += txt_user.getPreferredSize().height + UIScale.scale(textGap);

                lbl_password.setBounds(x, y, width, lbl_password.getPreferredSize().height);
                y += lbl_password.getPreferredSize().height + UIScale.scale(labelGap);
                txt_password.setBounds(x, y, width, txt_password.getPreferredSize().height);
                y += txt_password.getPreferredSize().height + UIScale.scale(buttonGap);

                btn_login.setBounds(x, y, width, btn_login.getPreferredSize().height);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_login;
    private javax.swing.JLabel lbl_password;
    private javax.swing.JLabel lbl_title;
    private javax.swing.JLabel lbl_user;
    private javax.swing.JPanel lbl_main;
    private javax.swing.JPasswordField txt_password;
    private javax.swing.JTextField txt_user;
    // End of variables declaration//GEN-END:variables
}

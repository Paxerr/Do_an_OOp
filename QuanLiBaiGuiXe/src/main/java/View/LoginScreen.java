package View;

import Controller.*;
import DataBase.JDBCUtil;
import Model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class LoginScreen extends JFrame {

    public JTextField usernameField;
    public JPasswordField passwordField;
    ActionListener ctrl = new LoginScreenController(this);
    Connection tmp = JDBCUtil.getConnection();
    public User user;

    public LoginScreen() {
        setTitle("Đăng nhập");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 165, 0));

        JLabel titleLabel = new JLabel("Đăng nhập", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Dimension fieldSize = new Dimension(200, 25);

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Tên đăng nhập: *"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setPreferredSize(fieldSize);
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Mật khẩu: *"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(fieldSize);
        loginPanel.add(passwordField, gbc);

        JButton loginBtn = new JButton("Đăng nhập");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginPanel.add(loginBtn, gbc);

        mainPanel.add(loginPanel, BorderLayout.CENTER);

        loginBtn.addActionListener(ctrl);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        User User = new User();
        User.CheckEmpty();
        SwingUtilities.invokeLater(() -> new LoginScreen());

    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.*;
import View.*;
import DataBase.JDBCUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

/**
 *
 * @author Moderator
 */
public class LoginScreenController implements ActionListener {

    private LoginScreen LS;

    public LoginScreenController(LoginScreen LS) {
        this.LS = LS;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String src = e.getActionCommand();
        if (src.equals("Đăng nhập")) {
            
            String ID = LS.usernameField.getText().trim();
            String Password = new String(LS.passwordField.getPassword()).trim();
            
            User user = new User(ID,Password);
            user.Login();
            String tmp = user.getRole();
            switch (tmp) {
                case (" "):
                    JOptionPane.showMessageDialog(LS, "Thông tin đăng nhập không chính xác", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    break;
                case ("Nhân viên"):
                    JOptionPane.showMessageDialog(LS, "Đăng nhập thành công với vai trò " + tmp + " !", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    LS.dispose();
                    new View.SercurityGuardDashboard(ID, tmp).setVisible(true);
                    break;
                case ("Quản lí"):
                    JOptionPane.showMessageDialog(LS, "Đăng nhập thành công với vai trò " + tmp + " !", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    LS.dispose();
                    new View.ManagerDashboard(ID, tmp).setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(LS, "Hệ thống gặp lỗi", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    break;
            }

        }
    }
}

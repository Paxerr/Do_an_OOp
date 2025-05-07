
package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import Controller.ParkingSercurityGuardManagementController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingSercurityGuardManagement extends JFrame {
    private DefaultTableModel securityGuardModel;
    private JTextField idField;
    private JTextField cccdField;
    private JTextField nameField;
    private JComboBox<String> roleCombo;
    private JTextField birthDateField;
    private JComboBox<String> genderCombo;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextField passwordField;
    private ArrayList<Object[]> securityGuardsList;
    private JTable securityGuardTable;
    private JButton addBtn;
    private JButton editBtn;
    private JButton deleteBtn;
    private JButton searchBtn;
    private JButton backBtn;
    private ParkingSercurityGuardManagementController controller;
    private boolean isEditMode = false;
    private int editRowIndex = -1;

    public ParkingSercurityGuardManagement(String username, String role) {
        setTitle("Quản lý nhân viên");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        securityGuardsList = new ArrayList<>();
        controller = new ParkingSercurityGuardManagementController(this);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 165, 0));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 165, 0));
        JLabel titleLabel = new JLabel("Quản lý nhân viên", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Dimension fieldSize = new Dimension(200, 25);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("ID: *"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(15);
        idField.setPreferredSize(fieldSize);
        inputPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("CCCD: *"), gbc);
        gbc.gridx = 1;
        cccdField = new JTextField(15);
        cccdField.setPreferredSize(fieldSize);
        inputPanel.add(cccdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Họ tên: *"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        nameField.setPreferredSize(fieldSize);
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Chức vụ: *"), gbc);
        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{"Quản lý", "Nhân viên"});
        roleCombo.setPreferredSize(fieldSize);
        inputPanel.add(roleCombo, gbc);

        

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Giới tính: *"), gbc);
        gbc.gridx = 1;
        genderCombo = new JComboBox<>(new String[]{"Nam", "Nữ"});
        genderCombo.setPreferredSize(fieldSize);
        inputPanel.add(genderCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Địa chỉ: *"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(15);
        addressField.setPreferredSize(fieldSize);
        inputPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        inputPanel.add(new JLabel("Số điện thoại: *"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        phoneField.setPreferredSize(fieldSize);
        inputPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        inputPanel.add(new JLabel("Mật khẩu: *"), gbc);
        gbc.gridx = 1;
        passwordField = new JTextField(15);
        passwordField.setPreferredSize(fieldSize);
        inputPanel.add(passwordField, gbc);

        mainPanel.add(inputPanel, BorderLayout.WEST);

      
        String[] securityGuardColumns = {"ID", "CCCD", "Họ tên", "Chức vụ", "Giới tính", "Địa chỉ", "SĐT", "Mật khẩu"};
        securityGuardModel = new DefaultTableModel(securityGuardColumns, 0);
        securityGuardTable = new JTable(securityGuardModel);
        JScrollPane securityGuardTableScroll = new JScrollPane(securityGuardTable);
        mainPanel.add(securityGuardTableScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addBtn = new JButton("Thêm");
        editBtn = new JButton("Sửa");
        deleteBtn = new JButton("Xóa");
        searchBtn = new JButton("Tìm kiếm");
        backBtn = new JButton("Quay lại");
        addBtn.addActionListener(controller);
        editBtn.addActionListener(controller);
        deleteBtn.addActionListener(controller);
        searchBtn.addActionListener(controller);
        backBtn.addActionListener(controller);
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(backBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

       
        loadAllEmployees();

        add(mainPanel);
        setVisible(true);
    }

    
    private void loadAllEmployees() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DataBase.JDBCUtil.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Kết nối database thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "SELECT ID, Identifier, Name, Address, PhoneNumber, Gender, Birthday, Role, Password FROM user";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            securityGuardsList.clear();
            securityGuardModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("ID"),
                    rs.getString("Identifier"),
                    rs.getString("Name"),
                    rs.getString("Role"),
                    rs.getString("Birthday"),
                    rs.getString("Gender"),
                    rs.getString("Address"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Password")
                };
                securityGuardsList.add(row);
                securityGuardModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            DataBase.JDBCUtil.closeConnection(conn);
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

   
    public JTextField getIdField() { return idField; }
    public JTextField getCccdField() { return cccdField; }
    public JTextField getNameField() { return nameField; }
    public JComboBox<String> getRoleCombo() { return roleCombo; }
    public JTextField getBirthDateField() { return birthDateField; }
    public JComboBox<String> getGenderCombo() { return genderCombo; }
    public JTextField getAddressField() { return addressField; }
    public JTextField getPhoneField() { return phoneField; }
    public JTextField getPasswordField() { return passwordField; }
    public ArrayList<Object[]> getSecurityGuardsList() { return securityGuardsList; }
    public DefaultTableModel getSecurityGuardModel() { return securityGuardModel; }
    public JTable getSecurityGuardTable() { return securityGuardTable; }
    public JButton getAddBtn() { return addBtn; }
    public JButton getEditBtn() { return editBtn; }
    public JButton getDeleteBtn() { return deleteBtn; }
    public JButton getSearchBtn() { return searchBtn; }
    public JButton getBackBtn() { return backBtn; }
    public boolean isEditMode() { return isEditMode; }
    public void setEditMode(boolean isEditMode) { this.isEditMode = isEditMode; }
    public int getEditRowIndex() { return editRowIndex; }
    public void setEditRowIndex(int editRowIndex) { this.editRowIndex = editRowIndex; }
}

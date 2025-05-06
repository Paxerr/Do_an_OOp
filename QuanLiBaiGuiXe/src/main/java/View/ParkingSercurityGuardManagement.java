package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import Controller.ParkingSercurityGuardManagementController;

public class ParkingSercurityGuardManagement extends JFrame {
    private DefaultTableModel sercurityGuardModel;
    private JTextField cccdField;
    private JTextField nameField;
    private JComboBox<String> roleCombo;
    private JTextField birthDateField;
    private JComboBox<String> genderCombo;
    private JTextField addressField;
    private JTextField phoneField;
    private ArrayList<Object[]> sercurityGuardsList;
    private ParkingSercurityGuardManagementController controller;
    private boolean isEditMode = false;
    private int editRowIndex = -1;
    private String PhoneNumber;

    public ParkingSercurityGuardManagement(String username, String role) {
        setTitle("Quản lý nhân viên");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        

        sercurityGuardsList = new ArrayList<>();
        controller = new ParkingSercurityGuardManagementController();
        
        sercurityGuardsList = new ArrayList<>(controller.getAllPersons());
        if (sercurityGuardsList.isEmpty()) {
            System.out.println("No persons found in database or failed to fetch data.");
        } else {
            System.out.println("Loaded " + sercurityGuardsList.size() + " persons from database.");
        }
        
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
        inputPanel.add(new JLabel("CCCD: *"), gbc);
        gbc.gridx = 1;
        cccdField = new JTextField(15);
        cccdField.setPreferredSize(fieldSize);
        cccdField.setEditable(true);
        inputPanel.add(cccdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Họ tên: *"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        nameField.setPreferredSize(fieldSize);
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Chức vụ: *"), gbc);
        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{"Quản lý", "Nhân VIên"});
        roleCombo.setPreferredSize(fieldSize);
        inputPanel.add(roleCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Ngày sinh: *"), gbc);
        gbc.gridx = 1;
        birthDateField = new JTextField(15);
        birthDateField.setPreferredSize(fieldSize);
        inputPanel.add(birthDateField, gbc);

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

        String[] sercurityGuardColumns = {"Mã NV","CCCD", "Họ tên", "Chức vụ", "Ngày sinh", "Giới tính", "Địa chỉ", "SĐT"};
        sercurityGuardModel = new DefaultTableModel(sercurityGuardColumns, 0);
        JTable sercurityGuardTable = new JTable(sercurityGuardModel);
        JScrollPane sercurityGuardTableScroll = new JScrollPane(sercurityGuardTable);

        for (Object[] guard : sercurityGuardsList) {
            sercurityGuardModel.addRow(guard);
        }
        
        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(sercurityGuardTableScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Thêm");
        JButton editBtn = new JButton("Sửa");
        JButton deleteBtn = new JButton("Xóa");
        JButton searchBtn = new JButton("Tìm kiếm");
        JButton backBtn = new JButton("Quay lại");
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(backBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
    String ID = "ID" + String.format("%04d", sercurityGuardsList.size() + 1);
    String Identifier = cccdField.getText().trim();
    String FullName = nameField.getText().trim();
    String Role = roleCombo.getSelectedItem().toString();
    String birthDate = birthDateField.getText().trim();
    String gender = genderCombo.getSelectedItem().toString();
    String Address = addressField.getText().trim();
    String phoneNumberStr = phoneField.getText().trim(); // Khai báo biến cục bộ

    if (Identifier.isEmpty() || FullName.isEmpty() || birthDate.isEmpty() || Address.isEmpty() || phoneNumberStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int phoneNumber;
    try {
        phoneNumber = Integer.parseInt(phoneNumberStr); // Sử dụng biến đã khai báo
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Số điện thoại phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

            if (controller.addPerson(Identifier, FullName, Address, PhoneNumber, gender)) {
                sercurityGuardsList.add(new Object[]{ID, Identifier, FullName, Role, birthDate, gender, Address, phoneNumber});
                sercurityGuardModel.setRowCount(0);
                for (Object[] guard : sercurityGuardsList) {
                    sercurityGuardModel.addRow(guard);
                }
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            cccdField.setText("");
            nameField.setText("");
            roleCombo.setSelectedIndex(0);
            birthDateField.setText("");
            genderCombo.setSelectedIndex(0);
            addressField.setText("");
            phoneField.setText("");
        });

       deleteBtn.addActionListener(e -> {
            int selectedRow = sercurityGuardTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String identifier = sercurityGuardsList.get(selectedRow)[1].toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (controller.deletePerson(identifier)) {
                    sercurityGuardsList.remove(selectedRow);
                    sercurityGuardModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        
            searchBtn.addActionListener(e -> {
            String searchCCCD = JOptionPane.showInputDialog(this, "Nhập CCCD để tìm kiếm:");
            if (searchCCCD == null || searchCCCD.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập CCCD để tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var searchResults = controller.searchPerson(searchCCCD.trim());
            sercurityGuardModel.setRowCount(0); 
            if (!searchResults.isEmpty()) {
                for (Object[] guard : searchResults) {
                    sercurityGuardModel.addRow(new Object[]{"", guard[0], guard[1], "Nhân viên", "", "", guard[2], guard[3]});
                }
                JOptionPane.showMessageDialog(this, "Tìm thấy nhân viên!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên với CCCD: " + searchCCCD, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            
                sercurityGuardModel.setRowCount(0);
                for (Object[] guard : sercurityGuardsList) {
                    sercurityGuardModel.addRow(guard);
                }
            }
        });
            
           editBtn.addActionListener(e -> {
    if (!isEditMode) {
        
        int selectedRow = sercurityGuardTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        editRowIndex = selectedRow;
        Object[] selectedGuard = sercurityGuardsList.get(selectedRow);

        cccdField.setText(selectedGuard[1].toString());
        cccdField.setEditable(false);
        nameField.setText(selectedGuard[2].toString());
        roleCombo.setSelectedItem(selectedGuard[3].toString());
        birthDateField.setText(selectedGuard[4].toString());
        genderCombo.setSelectedItem(selectedGuard[5].toString());
        addressField.setText(selectedGuard[6].toString());
        phoneField.setText(selectedGuard[7].toString());

        isEditMode = true;
        editBtn.setText("Lưu");
    } else {
        
        String identifier = cccdField.getText().trim();
        String fullName = nameField.getText().trim();
        String roleItem = roleCombo.getSelectedItem().toString();
        String birthDate = birthDateField.getText().trim();
        String gender = genderCombo.getSelectedItem().toString();
        String address = addressField.getText().trim();
        String phoneNumberStr = phoneField.getText().trim();

       
        if (identifier.isEmpty() || fullName.isEmpty() || birthDate.isEmpty() || address.isEmpty() || phoneNumberStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        

        int phoneNumber;
        try {
            phoneNumber = Integer.parseInt(phoneNumberStr);
            if (phoneNumberStr.length() < 10 || phoneNumberStr.length() > 11) {
                JOptionPane.showMessageDialog(this, "Số điện thoại phải có 10 hoặc 11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        System.out.println("Attempting to update person - identifier: " + identifier + ", fullName: " + fullName + ", address: " + address + ", phoneNumber: " + phoneNumber + ", gender: " + gender);
        if (controller.updatePerson(identifier, fullName, address, PhoneNumber, gender)) {
           
            sercurityGuardsList.set(editRowIndex, new Object[]{"ID" + String.format("%04d", editRowIndex + 1), identifier, fullName, roleItem, birthDate, gender, address, phoneNumber});
            sercurityGuardModel.setRowCount(0);
            for (Object[] guard : sercurityGuardsList) {
                sercurityGuardModel.addRow(guard);
            }
            JOptionPane.showMessageDialog(this, "Sửa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            // Reset chế độ và giao diện
            isEditMode = false;
            editBtn.setText("Sửa");
            cccdField.setText("");
            nameField.setText("");
            roleCombo.setSelectedIndex(0);
            birthDateField.setText("");
            genderCombo.setSelectedIndex(0);
            addressField.setText("");
            phoneField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Sửa nhân viên thất bại! Kiểm tra kết nối database hoặc dữ liệu đầu vào.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            
        }
    }
});
            


        add(mainPanel);
        setVisible(true);
    }
}

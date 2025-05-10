package Controller;

import View.ParkingSercurityGuardManagement;
import Model.User;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ParkingSercurityGuardManagementController implements ActionListener {

    private ParkingSercurityGuardManagement view;
    private User user;

    public ParkingSercurityGuardManagementController(ParkingSercurityGuardManagement view) {
        this.view = view;
        this.user = new User();
        loadAllUsers(); 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String src = e.getActionCommand();
        switch (src) {
            case "Thêm":
                addUser();
                break;
            case "Sửa":
                editUser();
                break;
            case "Xóa":
                deleteUser();
                break;
            case "Tìm kiếm":
                searchUser();
                break;
            case "Quay lại":
                goBack();
                break;
            case "Lưu":
                saveEdit();
                break;
            default:
                JOptionPane.showMessageDialog(view, "Hành động không được hỗ trợ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    private void addUser() {
        String id = view.getIdField().getText().trim();
        String identifier = view.getCccdField().getText().trim();
        String name = view.getNameField().getText().trim();
        String role = (String) view.getRoleCombo().getSelectedItem();
        String gender = (String) view.getGenderCombo().getSelectedItem();
        String address = view.getAddressField().getText().trim();
        String phoneNumber = view.getPhoneField().getText().trim();
        String password = view.getPasswordField().getText().trim();

        
        if (!validateInput(id, identifier, name, address, phoneNumber, password)) return;

        if (user.isIdExists(id)) {
            JOptionPane.showMessageDialog(view, "ID đã tồn tại! Vui lòng chọn ID khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        user = new User(id, identifier, name, role, gender, address, phoneNumber, password);
        if (user.addUser()) {
            view.getSecurityGuardsList().add(new Object[]{id, identifier, name, role, gender, address, phoneNumber, password});
            DefaultTableModel model = view.getSecurityGuardModel();
            if (model != null) {
                model.addRow(new Object[]{id, identifier, name, role, gender, address, phoneNumber, password});
                JOptionPane.showMessageDialog(view, "Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(view, "Lỗi: Không thể truy cập bảng dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Thêm nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editUser() {
        int selectedRow = view.getSecurityGuardTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một nhân viên để sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        Object[] selectedGuard = view.getSecurityGuardsList().get(selectedRow);
        view.getIdField().setText(selectedGuard[0].toString());
        view.getCccdField().setText(selectedGuard[1].toString());
        view.getNameField().setText(selectedGuard[2].toString());
        view.getRoleCombo().setSelectedItem(selectedGuard[3].toString());
        view.getGenderCombo().setSelectedItem(selectedGuard[4].toString());
        view.getAddressField().setText(selectedGuard[5].toString());
        view.getPhoneField().setText(selectedGuard[6].toString());
        view.getPasswordField().setText(selectedGuard[7].toString());

        view.setEditRowIndex(selectedRow);
        view.getEditBtn().setText("Lưu");
    }

    private void saveEdit() {
        if (view.getEditRowIndex() == -1) {
            JOptionPane.showMessageDialog(view, "Không có nhân viên nào được chọn để lưu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newId = view.getIdField().getText().trim();
        String identifier = view.getCccdField().getText().trim();
        String name = view.getNameField().getText().trim();
        String role = (String) view.getRoleCombo().getSelectedItem();
        String gender = (String) view.getGenderCombo().getSelectedItem();
        String address = view.getAddressField().getText().trim();
        String phoneNumber = view.getPhoneField().getText().trim();
        String password = view.getPasswordField().getText().trim();

        
        if (!validateInput(newId, identifier, name, address, phoneNumber, password)) return;

        String oldId = view.getSecurityGuardsList().get(view.getEditRowIndex())[0].toString();
        if (!newId.equals(oldId) && user.isIdExists(newId)) {
            JOptionPane.showMessageDialog(view, "ID đã tồn tại! Vui lòng chọn ID khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        user = new User(newId, identifier, name, role, gender, address, phoneNumber, password);
        if (user.updateUser(oldId)) {
            view.getSecurityGuardsList().set(view.getEditRowIndex(), new Object[]{newId, identifier, name, role, gender, address, phoneNumber, password});
            DefaultTableModel model = view.getSecurityGuardModel();
            if (model != null) {
                updateTableRow(view.getEditRowIndex(), new Object[]{newId, identifier, name, role, gender, address, phoneNumber, password});
                JOptionPane.showMessageDialog(view, "Sửa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                view.getEditBtn().setText("Sửa");
                view.setEditRowIndex(-1);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(view, "Lỗi: Không thể truy cập bảng dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Sửa nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        int selectedRow = view.getSecurityGuardTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một nhân viên để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = view.getSecurityGuardsList().get(selectedRow)[0].toString();
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            user = new User();
            if (user.deleteUser(id)) {
                view.getSecurityGuardsList().remove(selectedRow);
                DefaultTableModel model = view.getSecurityGuardModel();
                if (model != null) {
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(view, "Xóa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(view, "Lỗi: Không thể truy cập bảng dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(view, "Xóa nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchUser() {
        String searchIdentifier = JOptionPane.showInputDialog(view, "Nhập CCCD để tìm kiếm:");
        if (searchIdentifier == null || searchIdentifier.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập CCCD để tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = new User();
        List<Object[]> result = user.searchUserByIdentifier(searchIdentifier.trim());
        DefaultTableModel model = view.getSecurityGuardModel();
        if (model != null) {
            if (!result.isEmpty()) {
                model.setRowCount(0);
                view.getSecurityGuardsList().clear();
                for (Object[] row : result) {
                    view.getSecurityGuardsList().add(row);
                    model.addRow(row);
                }
                JOptionPane.showMessageDialog(view, "Tìm thấy nhân viên!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, "Không tìm thấy nhân viên với CCCD: " + searchIdentifier, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadAllUsers();
            }
        } else {
            JOptionPane.showMessageDialog(view, "Lỗi: Không thể truy cập bảng dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goBack() {
        view.dispose();
    }

    private void loadAllUsers() {
        user = new User();
        List<Object[]> users = user.getAllUsers();
        DefaultTableModel model = view.getSecurityGuardModel();
        if (model != null) {
            model.setRowCount(0);
            view.getSecurityGuardsList().clear();
            for (Object[] user : users) {
                view.getSecurityGuardsList().add(user);
                model.addRow(user);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Lỗi: Không thể truy cập bảng dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTableRow(int rowIndex, Object[] rowData) {
        DefaultTableModel model = view.getSecurityGuardModel();
        if (model != null) {
            for (int i = 0; i < rowData.length; i++) {
                model.setValueAt(rowData[i], rowIndex, i);
            }
        }
    }

    private boolean validateInput(String id, String identifier, String name, String address, String phoneNumber, String password) {
        if (id.isEmpty() || identifier.isEmpty() || name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!identifier.matches("\\d{9}|\\d{12}")) {
            JOptionPane.showMessageDialog(view, "CCCD phải có 9 hoặc 12 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (phoneNumber.length() < 10 || phoneNumber.length() > 11) {
            JOptionPane.showMessageDialog(view, "Số điện thoại phải có 10 hoặc 11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void clearFields() {
        view.getIdField().setText("");
        view.getCccdField().setText("");
        view.getNameField().setText("");
        view.getRoleCombo().setSelectedIndex(0);
        view.getGenderCombo().setSelectedIndex(0);
        view.getAddressField().setText("");
        view.getPhoneField().setText("");
        view.getPasswordField().setText("");
    }
}

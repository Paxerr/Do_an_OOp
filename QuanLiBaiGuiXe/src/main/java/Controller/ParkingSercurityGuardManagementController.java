package Controller;

import View.ParkingSercurityGuardManagement;
import DataBase.JDBCUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class ParkingSercurityGuardManagementController implements ActionListener {

    private ParkingSercurityGuardManagement view;

    public ParkingSercurityGuardManagementController(ParkingSercurityGuardManagement view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String src = e.getActionCommand();
        switch (src) {
            case "Thêm":
                themNhanVien();
                break;
            case "Sửa":
                chinhSuaNhanVien();
                break;
            case "Xóa":
                xoaNhanVien();
                break;
            case "Tìm kiếm":
                timKiemNhanVien();
                break;
            case "Quay lại":
                quayLai();
                break;
            case "Lưu":
                luuChinhSua();
                break;
            default:
                JOptionPane.showMessageDialog(view, "Hành động không được hỗ trợ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    private void themNhanVien() {
        String id = view.getIdField().getText().trim();
        String identifier = view.getCccdField().getText().trim();
        String name = view.getNameField().getText().trim();
        String role = (String) view.getRoleCombo().getSelectedItem();
        String gender = (String) view.getGenderCombo().getSelectedItem();
        String address = view.getAddressField().getText().trim();
        String phoneNumber = view.getPhoneField().getText().trim();
        String password = view.getPasswordField().getText().trim();

       
        if (id.isEmpty() || identifier.isEmpty() || name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!identifier.matches("\\d{9}|\\d{12}")) {
            JOptionPane.showMessageDialog(view, "CCCD phải có 9 hoặc 12 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (phoneNumber.length() < 10 || phoneNumber.length() > 11) {
            JOptionPane.showMessageDialog(view, "Số điện thoại phải có 10 hoặc 11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (kiemTraIdTonTai(id)) {
            JOptionPane.showMessageDialog(view, "ID đã tồn tại! Vui lòng chọn ID khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = JDBCUtil.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(view, "Kết nối cơ sở dữ liệu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

     
        String sql = "INSERT INTO user (ID, Identifier, FullName, Address, PhoneNumber, Gender, Role, Password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, identifier);
            pstmt.setString(3, name);
            pstmt.setString(4, address);
            pstmt.setString(5, phoneNumber);
            pstmt.setString(6, gender);
            pstmt.setString(7, role);
            pstmt.setString(8, password);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                
                view.getSecurityGuardsList().add(new Object[]{id, identifier, name, role, gender, address, phoneNumber, password});
                view.getSecurityGuardModel().addRow(new Object[]{id, identifier, name, role, gender, address, phoneNumber, password});
                JOptionPane.showMessageDialog(view, "Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, "Thêm nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi SQL: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(conn);
        }

        xoaTrangCacTruong();
    }

    private void chinhSuaNhanVien() {
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

    private void luuChinhSua() {
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

        // Kiểm tra thông tin đầu vào
        if (newId.isEmpty() || identifier.isEmpty() || name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!identifier.matches("\\d{9}|\\d{12}")) {
            JOptionPane.showMessageDialog(view, "CCCD phải có 9 hoặc 12 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (phoneNumber.length() < 10 || phoneNumber.length() > 11) {
            JOptionPane.showMessageDialog(view, "Số điện thoại phải có 10 hoặc 11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String oldId = view.getSecurityGuardsList().get(view.getEditRowIndex())[0].toString();
        if (!newId.equals(oldId) && kiemTraIdTonTai(newId)) {
            JOptionPane.showMessageDialog(view, "ID đã tồn tại! Vui lòng chọn ID khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = JDBCUtil.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(view, "Kết nối cơ sở dữ liệu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cập nhật bảng user
        String sql = "UPDATE user SET ID = ?, Identifier = ?, FullName = ?, Address = ?, PhoneNumber = ?, Gender = ?, Role = ?, Password = ? WHERE ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newId);
            pstmt.setString(2, identifier);
            pstmt.setString(3, name);
            pstmt.setString(4, address);
            pstmt.setString(5, phoneNumber);
            pstmt.setString(6, gender);
            pstmt.setString(7, role);
            pstmt.setString(8, password);
            pstmt.setString(9, oldId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                // Cập nhật danh sách và bảng hiển thị
                view.getSecurityGuardsList().set(view.getEditRowIndex(), new Object[]{newId, identifier, name, role, gender, address, phoneNumber, password});
                view.getSecurityGuardModel().setValueAt(newId, view.getEditRowIndex(), 0);
                view.getSecurityGuardModel().setValueAt(identifier, view.getEditRowIndex(), 1);
                view.getSecurityGuardModel().setValueAt(name, view.getEditRowIndex(), 2);
                view.getSecurityGuardModel().setValueAt(role, view.getEditRowIndex(), 3);
                view.getSecurityGuardModel().setValueAt(gender, view.getEditRowIndex(), 4);
                view.getSecurityGuardModel().setValueAt(address, view.getEditRowIndex(), 5);
                view.getSecurityGuardModel().setValueAt(phoneNumber, view.getEditRowIndex(), 6);
                view.getSecurityGuardModel().setValueAt(password, view.getEditRowIndex(), 7);
                JOptionPane.showMessageDialog(view, "Sửa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                view.getEditBtn().setText("Sửa");
                view.setEditRowIndex(-1);
            } else {
                JOptionPane.showMessageDialog(view, "Sửa nhân viên thất bại! Kiểm tra kết nối cơ sở dữ liệu hoặc dữ liệu đầu vào.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi SQL: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(conn);
        }

        xoaTrangCacTruong();
    }

    private void xoaNhanVien() {
        int selectedRow = view.getSecurityGuardTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một nhân viên để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = view.getSecurityGuardsList().get(selectedRow)[0].toString();
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = JDBCUtil.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(view, "Kết nối cơ sở dữ liệu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "DELETE FROM user WHERE ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, id);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    view.getSecurityGuardsList().remove(selectedRow);
                    view.getSecurityGuardModel().removeRow(selectedRow);
                    JOptionPane.showMessageDialog(view, "Xóa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(view, "Xóa nhân viên thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Lỗi SQL: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                JDBCUtil.closeConnection(conn);
            }
        }
    }

    private void timKiemNhanVien() {
        String searchIdentifier = JOptionPane.showInputDialog(view, "Nhập CCCD để tìm kiếm:");
        if (searchIdentifier == null || searchIdentifier.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập CCCD để tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = JDBCUtil.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(view, "Kết nối cơ sở dữ liệu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "SELECT ID, Identifier, FullName, Address, PhoneNumber, Gender, Role, Password FROM user WHERE Identifier = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, searchIdentifier.trim());
            ResultSet rs = pstmt.executeQuery();
            view.getSecurityGuardModel().setRowCount(0);
            view.getSecurityGuardsList().clear();
            boolean found = false;
            while (rs.next()) {
                found = true;
                Object[] row = {
                    rs.getString("ID"),
                    rs.getString("Identifier"),
                    rs.getString("FullName"),
                    rs.getString("Role"),
                    rs.getString("Gender"),
                    rs.getString("Address"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Password")
                };
                view.getSecurityGuardsList().add(row);
                view.getSecurityGuardModel().addRow(row);
            }
            if (found) {
                JOptionPane.showMessageDialog(view, "Tìm thấy nhân viên!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, "Không tìm thấy nhân viên với CCCD: " + searchIdentifier, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                view.getSecurityGuardModel().setRowCount(0);
                for (Object[] guard : view.getSecurityGuardsList()) {
                    view.getSecurityGuardModel().addRow(guard);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi SQL: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(conn);
        }
    }

    private void quayLai() {
        JDBCUtil.closeConnection(JDBCUtil.getConnection());
        view.dispose();
    }

    private void xoaTrangCacTruong() {
        view.getIdField().setText("");
        view.getCccdField().setText("");
        view.getNameField().setText("");
        view.getRoleCombo().setSelectedIndex(0);
        view.getGenderCombo().setSelectedIndex(0);
        view.getAddressField().setText("");
        view.getPhoneField().setText("");
        view.getPasswordField().setText("");
    }

    private boolean kiemTraIdTonTai(String id) {
        Connection conn = JDBCUtil.getConnection();
        if (conn == null) return false;

        String sql = "SELECT COUNT(*) FROM user WHERE ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(conn);
        }
        return false;
    }
}

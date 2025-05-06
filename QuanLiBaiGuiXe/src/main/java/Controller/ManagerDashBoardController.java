/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DataBase.JDBCUtil;
import Model.*;
import View.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Moderator
 */
public class ManagerDashBoardController implements ActionListener {

    private ManagerDashboard MD;

    public ManagerDashBoardController(ManagerDashboard ctrl) {
        this.MD = ctrl;
    }

    ParkingTicket Ticket = new ParkingTicket();

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("Thêm xe")) {
            String LicenseNumber = MD.vehiclePlateInputField.getText().trim();
            String VehicleType = MD.vehicleTypeCombo.getSelectedItem().toString();

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" HH:mm MM-dd-yyyy");
            Ticket.setEntryTime(now.format(formatter));

            Ticket.setLicenseNumber(LicenseNumber);
            Ticket.setVehicleType(VehicleType);

            if (LicenseNumber.isEmpty() && (VehicleType.equals("Xe máy") || (VehicleType.equals("Ô tô")))) {
                JOptionPane.showMessageDialog(MD, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (VehicleType.equals("Xe máy")) {
                Ticket.setCost(5000);
            }
            if (VehicleType.equals("Ô tô")) {
                Ticket.setCost(10000);
            }
            if (VehicleType.equals("Xe đạp")) {
                Ticket.setCost(2000);
            }

            Ticket.ParkTheVehicle();

            if ("error".equals(Ticket.getTicketType())) {
                JOptionPane.showMessageDialog(MD, "Lỗi hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            List<ParkingTicket> Result = new ArrayList<>();
            Result = Ticket.SearchVehicle("Refesh");
            MD.vehicleModel.setRowCount(0);
            for (ParkingTicket t : Result) {
                Object[] row = new Object[]{
                    t.getTicketID(),
                    t.getLicenseNumber(),
                    t.getVehicleType(),
                    t.getTicketType(),
                    t.getEntryTime()
                };
                MD.vehicleModel.addRow(row);
            }

            JOptionPane.showMessageDialog(MD, "Thêm xe thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            if ("Vé Thường".equals(Ticket.getTicketType())) {
                ManagerDashboard.CustomOptionPane.showMessage("Vé đã được in", "Thông báo", "Ok!");
            }

            MD.vehiclePlateInputField.setText("");
        }

        if (cmd.equals("Tìm kiếm xe")) {

            List<ParkingTicket> Result = new ArrayList<>();
            String LicenseNumber = MD.vehiclePlateInputField.getText().trim();

            Ticket.setLicenseNumber(LicenseNumber);

            if (LicenseNumber.isEmpty()) {
                Result = Ticket.SearchVehicle("Refesh");
            } else {
                Result = Ticket.SearchVehicle(cmd);
            }
            MD.vehicleModel.setRowCount(0);
            for (ParkingTicket t : Result) {
                Object[] row = new Object[]{
                    t.getTicketID(),
                    t.getLicenseNumber(),
                    t.getVehicleType(),
                    t.getTicketType(),
                    t.getEntryTime()
                };
                MD.vehicleModel.addRow(row);
            }
            MD.vehiclePlateInputField.setText("");
        }

        if (cmd.equals("Tìm kiếm lịch sử xe")) {

            List<ParkingTicket> Result = new ArrayList<>();
            String SearchLicenseNumber = JOptionPane.showInputDialog(MD, "Nhập biển số xe cần tìm (để trống để hiển thị tất cả):");
            Ticket.setLicenseNumber(SearchLicenseNumber);

            Result = Ticket.SearchHistoryVehicle();

            MD.historyModel.setRowCount(0);
            for (ParkingTicket t : Result) {
                Object[] row = new Object[]{
                    t.getTicketID(),
                    t.getLicenseNumber(),
                    t.getVehicleType(),
                    t.getTicketType(),
                    t.getEntryTime(),
                    t.getTimeOut(),
                    t.getCost()
                };
                MD.historyModel.addRow(row);
            }
        }
        if (cmd.equals("Xác nhận rời bãi")) {
            ParkingTicket Ticket = new ParkingTicket();
            int selectedRow = MD.vehicleTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(MD, "Vui lòng chọn một xe để xác nhận rời bãi.");
                return;
            }
            DefaultTableModel model = (DefaultTableModel) MD.vehicleTable.getModel();
            String LicenseNumber = model.getValueAt(selectedRow, 1).toString();
            String EntryTime = model.getValueAt(selectedRow, 4).toString();
            String TicketType = model.getValueAt(selectedRow, 3).toString();
            Ticket.setLicenseNumber(LicenseNumber);
            Ticket.setEntryTime(EntryTime);
            Ticket.setTicketType(TicketType);

            MD.vehicleModel.removeRow(selectedRow);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" HH:mm MM-dd-yyyy");
            Ticket.setTimeOut(now.format(formatter));
            Ticket.GetTheVehicle();
            long ThoiGianGui = Ticket.Charge();

            if (TicketType.equals("Vé Thường")) {
                if (ThoiGianGui < (60 * 24)) {
                    JOptionPane.showMessageDialog(MD, "Số tiền cần thanh toán là : " + Ticket.getCost());
                    return;
                } else {
                    int ThanhToan = Ticket.getCost() * (int) Math.floor(ThoiGianGui / (60 * 24));
                    JOptionPane.showMessageDialog(MD, "Số tiền cần thanh toán là : " + ThanhToan);
                    return;
                }
            }
        }

        if (cmd.equals("Đăng ký vé tháng")) {
            MonthlyParking Monthly = new MonthlyParking();
            int selectedRow = MD.vehicleTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(MD, "Vui lòng chọn một xe để đăng kí vé tháng.");
                return;
            }
            DefaultTableModel model = (DefaultTableModel) MD.vehicleTable.getModel();
            String LicenseNumber = model.getValueAt(selectedRow, 1).toString();
            String VehicleType = model.getValueAt(selectedRow, 2).toString();
            Monthly.setLicenseNumber(LicenseNumber);
            Monthly.setVehicleType(VehicleType);
            Monthly.setCost();

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
            Monthly.setExpireDate(now.format(formatter));

            String CardIDstr = JOptionPane.showInputDialog(MD, "Nhập nhập mã thẻ nếu có :");
            if (CardIDstr.isEmpty()) {
                Monthly.setCardID(-1);
            } else {
                int CardID = Integer.parseInt(CardIDstr);
                Monthly.setCardID(CardID);
            }

            Monthly.Register();

            JOptionPane.showMessageDialog(MD, "Đăng kí vé tháng thành công, Hãy thanh toán " + Monthly.getCost(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        if (cmd.equals("Thêm vé")) {
            List<MonthlyParking> Result = new ArrayList<>();
            MonthlyParking Monthly = new MonthlyParking();
            String CardIDstr = MD.Card_IDField.getText().trim();
            if (CardIDstr.isEmpty()) {
                Monthly.setCardID(-1);
            } else {
                Monthly.setCardID(Integer.parseInt(CardIDstr));
            }
            Monthly.setLicenseNumber(MD.monthlyCardLicensePlateField.getText().trim());
            Monthly.setVehicleType(MD.vehicleTypeCombo.getSelectedItem().toString());
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
            Monthly.setExpireDate(now.format(formatter));
            Monthly.setCost();
            Monthly.Register();
            Result = Monthly.Search("Refesh");
                MD.monthlyCardModel.setRowCount(0);
                for (MonthlyParking t : Result) {
                    Object[] row = new Object[]{
                        t.getCardID(),
                        t.getLicenseNumber(),
                        t.getVehicleType(),
                        t.getExpireDate(),
                        t.getCost()
                    };
                    MD.monthlyCardModel.addRow(row);
                }
            JOptionPane.showMessageDialog(MD, "Đăng kí vé tháng thành công, Hãy thanh toán " + Monthly.getCost(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }

        if(cmd.equals("Tìm kiếm vé theo xe") || cmd.equals("Tìm kiếm vé theo mã")) {
            String cm = cmd;
            MonthlyParking Monthly = new MonthlyParking();
            List<MonthlyParking> Result = new ArrayList<>();
            String LicenseNumber = MD.monthlyCardLicensePlateField.getText().trim();
            String CardIDstr = MD.Card_IDField.getText().trim();
            Monthly.setLicenseNumber(LicenseNumber);
            if(CardIDstr.isEmpty()){
                cm = "Refesh";                
                Monthly.setCardID(-1);
            }

            else
                Monthly.setCardID(Integer.parseInt(CardIDstr));
            
            Result = Monthly.Search(cm);
                MD.monthlyCardModel.setRowCount(0);
                for (MonthlyParking t : Result) {
                    Object[] row = new Object[]{
                        t.getCardID(),
                        t.getLicenseNumber(),
                        t.getVehicleType(),
                        t.getExpireDate(),
                        t.getCost()
                    };
                    MD.monthlyCardModel.addRow(row);
                }
                JOptionPane.showMessageDialog(MD, "Tìm kiếm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                MD.monthlyCardLicensePlateField.setText("");
                MD.Card_IDField.setText("");
        }
        
        if(cmd.equals("Gia hạn")){
            MonthlyParking Monthly = new MonthlyParking();
            int selectedRow = MD.monthlyCardTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(MD, "Vui lòng chọn một vé để gia hạn.");
                return;
            }
            DefaultTableModel model = (DefaultTableModel) MD.monthlyCardTable.getModel();
            String LicenseNumber = model.getValueAt(selectedRow, 1).toString();
            String VehicleType = model.getValueAt(selectedRow, 2).toString();
            
            
            Monthly.setLicenseNumber(LicenseNumber);
            Monthly.setVehicleType(VehicleType);
            Monthly.setCost();
            Monthly.Extend();
            JOptionPane.showMessageDialog(MD, "Gia hạn thành công. Số tiền cần thanh toán là : " + Monthly.getCost());
        }
    }
}

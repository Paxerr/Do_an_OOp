/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DataBase.JDBCUtil;
import Model.*;
import View.*;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Moderator
 */
public class SercurityGuardDashboardController implements ActionListener {

    private SercurityGuardDashboard MD;

    public SercurityGuardDashboardController(SercurityGuardDashboard ctrl) {
        this.MD = ctrl;
    }

    ParkingTicket Ticket = new ParkingTicket();

    public void LoadTableVehicleParking() {
        List<ParkingTicket> Result = Ticket.SearchVehicle("Refesh");
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
    }

    public void LoadSlotLabel() {
        TicketMotorbike tM = new TicketMotorbike();
        TicketCar tC = new TicketCar();
        TicketBicycle tB = new TicketBicycle();
        tM.setCapacity();
        tC.setCapacity();
        tB.setCapacity();
        MD.SlotMLabel.setText("Số lượng xe máy : " + tM.Available() + "/" + tM.getCapacity());
        MD.SlotCLabel.setText("Số lượng Ô tô : " + tC.Available() + "/" + tC.getCapacity());
        MD.SlotBLabel.setText("Số lượng xe đạp : " + tB.Available() + "/" + tB.getCapacity());
    }

    public void LoadMonthlyTickets() {
        MonthlyParking Monthly = new MonthlyParking();
        List<MonthlyParking> Result = Monthly.Search("Refesh");
        MD.monthlyCardModel.setRowCount(0);
        for (MonthlyParking t : Result) {
            Object[] row = new Object[]{
                t.getCardID(),
                t.getLicenseNumber(),
                t.getVehicleType(),
                t.getStartDate(),
                t.getExpireDate(),
                t.getCost()
            };
            MD.monthlyCardModel.addRow(row);
        }
    }

    public void LoadTableParkingHistory() {
        List<ParkingTicket> Result = new ArrayList<>();
        Ticket.setLicenseNumber("");
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

    public void LoadTableHistoryLogin() {
        User User = new User();
        List<Object[]> Result = new ArrayList<>();
        Result = User.SearchHistory("Refesh");
        MD.LoginlogoutModel.setRowCount(0);
        for (Object[] t : Result) {
            Object[] row = new Object[]{
                t[0],
                t[1],
                t[2],
                t[3],
                t[4],};
            MD.LoginlogoutModel.addRow(row);
        }
    }
//Hàm vẽ vé để in

    class TicketPrintable implements Printable {

        private ParkingTicket ticket;

        public TicketPrintable(ParkingTicket ticket) {
            this.ticket = ticket;
        }

        @Override
        public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());

            int y = 20;
            g.drawString("=== VÉ GỬI XE ===", 10, y);
            y += 15;
            g.drawString("Mã vé: " + ticket.getTicketID(), 10, y);
            y += 15;
            g.drawString("Biển số: " + ticket.getLicenseNumber(), 10, y);
            y += 15;
            g.drawString("Loại xe: " + ticket.getVehicleType(), 10, y);
            y += 15;
            g.drawString("Loại vé: " + ticket.getTicketType(), 10, y);
            y += 15;
            g.drawString("Giờ vào: " + ticket.getEntryTime(), 10, y);
            y += 15;
            g.drawString("==================", 10, y);
            y += 20;

            try {
                // Tạo nội dung mã QR
                String qrContent = ticket.getLicenseNumber() + "|" + ticket.getEntryTime() + "|" + "Ve Thuong";

                // Tạo mã QR
                BufferedImage qrImage = generateQRCodeImage(qrContent);

                // Vẽ ảnh QR dưới nội dung văn bản
                g.drawImage(qrImage, 10, y, null);

            } catch (Exception e) {
                e.printStackTrace();
                g.drawString("Lỗi khi tạo mã QR", 10, y + 20);
            }

            return PAGE_EXISTS;
        }

        // Hàm tạo QR cho vé
        private BufferedImage generateQRCodeImage(String text) throws WriterException {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 100, 100);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        }
    }

    public void XacNhanRoiBai() {
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
        String VehicleType = model.getValueAt(selectedRow, 2).toString();
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
                LoadSlotLabel();
                return;
            } else {
                int ThanhToan = Ticket.getCost() * (int) Math.floor(ThoiGianGui / (60 * 24));
                JOptionPane.showMessageDialog(MD, "Số tiền cần thanh toán là : " + ThanhToan);
                LoadSlotLabel();
                return;
            }
        }

    }

    public void ThemXe() {
        String LicenseNumber = MD.vehiclePlateInputField.getText().trim();
        String VehicleType = MD.vehicleTypeCombo.getSelectedItem().toString();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" HH:mm MM-dd-yyyy");

        if (LicenseNumber.isEmpty() && (VehicleType.equals("Xe máy") || (VehicleType.equals("Ô tô")))) {
            JOptionPane.showMessageDialog(MD, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (VehicleType.equals("Xe máy")) {
            TicketMotorbike Ticket = new TicketMotorbike();
            if (Ticket.getCost1() == -1) {
                JOptionPane.showMessageDialog(MD, "Giá vé chưa được thiết lập !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Ticket.Available() >= Ticket.getCapacity()) {
                JOptionPane.showMessageDialog(MD, "Hết chỗ !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Ticket.setEntryTime(now.format(formatter));
            Ticket.setLicenseNumber(LicenseNumber);
            Ticket.setVehicleType(VehicleType);
            Ticket.ParkTheVehicle();

            if ("error".equals(Ticket.getTicketType())) {
                JOptionPane.showMessageDialog(MD, "Lỗi hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(MD, "Thêm xe thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            if ("Vé Thường".equals(Ticket.getTicketType())) {
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setJobName("In vé gửi xe");
                job.setPrintable(new TicketPrintable(Ticket)); // TicketPrintable là lớp để vẽ vé

                boolean doPrint = job.printDialog();
                if (doPrint) {
                    try {
                        job.print();
                        ManagerDashboard.CustomOptionPane.showMessage("Vé đã được in", "Thông báo", "Ok!");
                    } catch (PrinterException ex) {
                        JOptionPane.showMessageDialog(MD, "Lỗi khi in vé: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(MD, "Bạn đã hủy in vé", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        if (VehicleType.equals("Ô tô")) {
            TicketCar Ticket = new TicketCar();
            if (Ticket.getCost1() == -1) {
                JOptionPane.showMessageDialog(MD, "Giá vé chưa được thiết lập !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Ticket.Available() >= Ticket.getCapacity()) {
                JOptionPane.showMessageDialog(MD, "Hết chỗ !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Ticket.setEntryTime(now.format(formatter));
            Ticket.setLicenseNumber(LicenseNumber);
            Ticket.setVehicleType(VehicleType);
            Ticket.ParkTheVehicle();
            if ("error".equals(Ticket.getTicketType())) {
                JOptionPane.showMessageDialog(MD, "Lỗi hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(MD, "Thêm xe thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            if ("Vé Thường".equals(Ticket.getTicketType())) {
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setJobName("In vé gửi xe");
                job.setPrintable(new TicketPrintable(Ticket)); // TicketPrintable là lớp bạn phải tạo để vẽ vé

                boolean doPrint = job.printDialog();
                if (doPrint) {
                    try {
                        job.print();
                        ManagerDashboard.CustomOptionPane.showMessage("Vé đã được in", "Thông báo", "Ok!");
                    } catch (PrinterException ex) {
                        JOptionPane.showMessageDialog(MD, "Lỗi khi in vé: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(MD, "Bạn đã hủy in vé", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        if (VehicleType.equals("Xe đạp")) {
            TicketBicycle Ticket = new TicketBicycle();
            if (Ticket.getCost1() == -1) {
                JOptionPane.showMessageDialog(MD, "Giá vé chưa được thiết lập !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Ticket.Available() >= Ticket.getCapacity()) {
                JOptionPane.showMessageDialog(MD, "Hết chỗ !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Ticket.setEntryTime(now.format(formatter));
            Ticket.setLicenseNumber(LicenseNumber);
            Ticket.setVehicleType(VehicleType);
            Ticket.ParkTheVehicle();
            if ("error".equals(Ticket.getTicketType())) {
                JOptionPane.showMessageDialog(MD, "Lỗi hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(MD, "Thêm xe thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            if ("Vé Thường".equals(Ticket.getTicketType())) {
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setJobName("In vé gửi xe");
                job.setPrintable(new TicketPrintable(Ticket)); // TicketPrintable là lớp bạn phải tạo để vẽ vé

                boolean doPrint = job.printDialog();
                if (doPrint) {
                    try {
                        job.print();
                        ManagerDashboard.CustomOptionPane.showMessage("Vé đã được in", "Thông báo", "Ok!");
                    } catch (PrinterException ex) {
                        JOptionPane.showMessageDialog(MD, "Lỗi khi in vé: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(MD, "Bạn đã hủy in vé", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        }

        LoadTableVehicleParking();
        MD.vehiclePlateInputField.setText("");
        LoadTableParkingHistory();
        LoadSlotLabel();
    }

    public void TimKiemXe(String cmd) {

        List<ParkingTicket> Result = new ArrayList<>();
        String LicenseNumber = MD.vehiclePlateInputField.getText().trim();

        Ticket.setLicenseNumber(LicenseNumber);

        if (LicenseNumber.isEmpty()) {
            Result = Ticket.SearchVehicle("Refesh");
        } else {
            Result = Ticket.SearchVehicle(cmd);
        }
        if (Result.isEmpty()) {
            JOptionPane.showMessageDialog(MD, "Không tìm thấy xe", "Lỗi !", JOptionPane.INFORMATION_MESSAGE);
            return;
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

    public void TimKiemLichSuXe() {

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

    public void DangKyVeThang() {
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
        if (VehicleType.equals("Xe máy")) {
            MonthlyTicketMotorbike tmp = new MonthlyTicketMotorbike();
            Monthly.setCost(tmp.getCost1());
        }
        if (VehicleType.equals("Ô tô")) {
            MonthlyTicketCar tmp = new MonthlyTicketCar();
            Monthly.setCost(tmp.getCost1());
        }
        if (VehicleType.equals("Xe đạp")) {
            MonthlyTicketBicycle tmp = new MonthlyTicketBicycle();
            Monthly.setCost(tmp.getCost1());
        }

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
        if (Monthly.getCost() == -1) {
            JOptionPane.showMessageDialog(MD, "Giá vé chưa được thiết lập !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Monthly.Register();

        JOptionPane.showMessageDialog(MD, "Đăng kí vé tháng thành công, Hãy thanh toán " + Monthly.getCost(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void ThemVe() {
        List<MonthlyParking> Result = new ArrayList<>();

        String CardIDstr = MD.Card_IDField.getText().trim();
        String LicenseNumber = MD.monthlyCardLicensePlateField.getText().trim();
        String VehicleType = MD.monthlyCardTypeCombo.getSelectedItem().toString();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        String ExpireDate = now.format(formatter);
        String StartDate = now.format(formatter);

        if (CardIDstr.isEmpty()) {
            CardIDstr = "-1";
        }

        if (LicenseNumber.isEmpty() && (!(VehicleType.equals("Xe đạp")))) {
            JOptionPane.showMessageDialog(MD, "Vui lòng nhập đầy đủ thông tin", "Lỗi!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if ("Xe máy".equals(VehicleType)) {
            MonthlyTicketMotorbike M = new MonthlyTicketMotorbike();
            M.setLicenseNumber(LicenseNumber);
            M.setVehicleType(VehicleType);
            M.setStartDate(StartDate);
            M.setExpireDate(ExpireDate);
            M.setCardID(Integer.parseInt(CardIDstr));
            M.Register();
            if (M.getCost1() == -1) {
                JOptionPane.showMessageDialog(MD, "Giá vé chưa được thiết lập !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (M.getCost() == -2) {
                JOptionPane.showMessageDialog(MD, "Xe đã được đăng kí !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Result = M.Search("Refesh");
            JOptionPane.showMessageDialog(MD, "Đăng kí vé tháng thành công, Hãy thanh toán " + M.getCost1(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else if ("Xe đạp".equals(VehicleType)) {
            MonthlyTicketBicycle Monthly = new MonthlyTicketBicycle();
            Monthly.setVehicleType(VehicleType);
            Monthly.setStartDate(StartDate);
            Monthly.setExpireDate(ExpireDate);
            Monthly.setCardID(Integer.parseInt(CardIDstr));
            Monthly.Register();
            if (Monthly.getCost1() == -1) {
                JOptionPane.showMessageDialog(MD, "Giá vé chưa được thiết lập !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Monthly.getCost() == -2) {
                JOptionPane.showMessageDialog(MD, "Xe đã được đăng kí !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Result = Monthly.Search("Refesh");
            JOptionPane.showMessageDialog(MD, "Đăng kí vé tháng thành công, Hãy thanh toán " + Monthly.getCost1(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else if ("Ô tô".equals(VehicleType)) {
            MonthlyTicketCar Monthly = new MonthlyTicketCar();
            Monthly.setLicenseNumber(LicenseNumber);
            Monthly.setVehicleType(VehicleType);
            Monthly.setStartDate(StartDate);
            Monthly.setExpireDate(ExpireDate);
            Monthly.setCardID(Integer.parseInt(CardIDstr));
            Monthly.Register();
            if (Monthly.getCost1() == -1) {
                JOptionPane.showMessageDialog(MD, "Giá vé chưa được thiết lập !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Monthly.getCost() == -2) {
                JOptionPane.showMessageDialog(MD, "Xe đã được đăng kí !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Result = Monthly.Search("Refesh");
            JOptionPane.showMessageDialog(MD, "Đăng kí vé tháng thành công, Hãy thanh toán " + Monthly.getCost1(), "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        }

        MD.monthlyCardModel.setRowCount(0);
        for (MonthlyParking t : Result) {
            Object[] row = new Object[]{
                t.getCardID(),
                t.getLicenseNumber(),
                t.getVehicleType(),
                t.getStartDate(),
                t.getExpireDate(),
                t.getCost()
            };
            MD.monthlyCardModel.addRow(row);
        }
        MD.monthlyCardLicensePlateField.setText("");
        MD.Card_IDField.setText("");
    }

    public void TimKiemVeThang(String cmd) {
        String cm = cmd;
        MonthlyParking Monthly = new MonthlyParking();
        List<MonthlyParking> Result = new ArrayList<>();
        String LicenseNumber = MD.monthlyCardLicensePlateField.getText().trim();
        String CardIDstr = MD.Card_IDField.getText().trim();
        Monthly.setLicenseNumber(LicenseNumber);
        if (CardIDstr.isEmpty() && LicenseNumber.isEmpty()) {
            cm = "Refesh";

        } else if (CardIDstr.isEmpty()) {
            Monthly.setCardID(-1);
        } else {
            Monthly.setCardID(Integer.parseInt(CardIDstr));
        }

        Result = Monthly.Search(cm);

        MD.monthlyCardModel.setRowCount(0);
        for (MonthlyParking t : Result) {
            Object[] row = new Object[]{
                t.getCardID(),
                t.getLicenseNumber(),
                t.getVehicleType(),
                t.getStartDate(),
                t.getExpireDate(),
                t.getCost()
            };
            MD.monthlyCardModel.addRow(row);
        }
        JOptionPane.showMessageDialog(MD, "Tìm kiếm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        MD.monthlyCardLicensePlateField.setText("");
        MD.Card_IDField.setText("");
    }

    public void GiaHan() {
        MonthlyParking Monthly = new MonthlyParking();
        int selectedRow = MD.monthlyCardTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(MD, "Vui lòng chọn một vé để gia hạn.");
            return;
        }
        DefaultTableModel model = (DefaultTableModel) MD.monthlyCardTable.getModel();
        String LicenseNumber = model.getValueAt(selectedRow, 1).toString();
        String VehicleType = model.getValueAt(selectedRow, 2).toString();
        String ExpireDate = model.getValueAt(selectedRow, 3).toString();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        LocalDateTime now1 = now.plusMonths(1);
        String now2 = (now1.format(formatter));
        if (now2.equals(model.getValueAt(selectedRow, 4).toString())) {
            JOptionPane.showMessageDialog(MD, "Vé đã được gia hạn tháng sau !", "Lỗi !", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Monthly.setLicenseNumber(LicenseNumber);
        Monthly.setVehicleType(VehicleType);
        Monthly.setExpireDate(ExpireDate);
        Monthly.setCost();
        Monthly.Extend();
        JOptionPane.showMessageDialog(MD, "Gia hạn thành công. Số tiền cần thanh toán là : " + Monthly.getCost());
        LoadMonthlyTickets();
    }

    public void TimKiemMaNV() {
        User User = new User();
        List<Object[]> Result = new ArrayList<>();

        String SearchloginNV = JOptionPane.showInputDialog(MD, "Nhập mã NV cần tìm (để trống để hiển thị tất cả):");

        if (SearchloginNV.isEmpty()) {
            Result = User.SearchHistory("Refesh");
        } else {
            User.setID(SearchloginNV);
            Result = User.SearchHistory("Tìm kiếm");
        }
        MD.LoginlogoutModel.setRowCount(0);
        for (Object[] t : Result) {
            Object[] row = new Object[]{
                t[0],
                t[1],
                t[2],
                t[3],
                t[4],};
            MD.LoginlogoutModel.addRow(row);
        }
        JOptionPane.showMessageDialog(MD, "Tìm kiếm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void SuaGia() {

        String TicketType = MD.CostTypeCombo.getSelectedItem().toString().trim();
        String VehicleType = MD.CostTypeVehicleCombo.getSelectedItem().toString().trim();
        String Coststr = MD.CostField.getText().trim();

        if (Coststr.isEmpty()) {
            JOptionPane.showMessageDialog(MD, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int Cost = Integer.parseInt(Coststr);
        ParkingTicket T = new ParkingTicket();
        switch (TicketType) {
            case ("Vé tháng"):
                if (VehicleType.equals("Xe máy")) {
                    MonthlyTicketMotorbike a = new MonthlyTicketMotorbike();
                    a.setVehicleType(VehicleType);
                    a.setCost1(Cost);
                }
                if (VehicleType.equals("Xe đạp")) {
                    MonthlyTicketBicycle b = new MonthlyTicketBicycle();
                    b.setVehicleType(VehicleType);
                    b.setCost1(Cost);

                }
                if (VehicleType.equals("Ô tô")) {
                    MonthlyTicketCar c = new MonthlyTicketCar();
                    c.setVehicleType(VehicleType);
                    c.setCost1(Cost);
                }
                break;
            case ("Vé thường"):
                if (VehicleType.equals("Xe máy")) {
                    TicketMotorbike d = new TicketMotorbike();
                    d.setVehicleType(VehicleType);
                    d.setCost1(Cost);
                }
                if (VehicleType.equals("Xe đạp")) {
                    TicketBicycle g = new TicketBicycle();
                    g.setVehicleType(VehicleType);
                    g.setCost1(Cost);
                }
                if (VehicleType.equals("Ô tô")) {
                    TicketCar f = new TicketCar();
                    f.setVehicleType(VehicleType);
                    f.setCost1(Cost);
                }
                break;
        }
        JOptionPane.showMessageDialog(MD, "Đã đặt lại giá vé của " + VehicleType + " là : " + Cost, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void SuaSoLuongGuiXe() {
        String VehicleType = MD.CostTypeVehicleCombo.getSelectedItem().toString().trim();
        String CapacityStr = MD.SlotField.getText().trim();

        if (CapacityStr.isEmpty()) {
            JOptionPane.showMessageDialog(MD, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int Capacity = Integer.parseInt(CapacityStr);
        if (VehicleType.equals("Xe máy")) {
            TicketMotorbike a = new TicketMotorbike();
            a.setCapacity(Capacity);
        }
        if (VehicleType.equals("Xe đạp")) {
            TicketBicycle b = new TicketBicycle();
            b.setCapacity(Capacity);

        }
        if (VehicleType.equals("Ô tô")) {
            TicketCar c = new TicketCar();
            c.setCapacity(Capacity);
        }

        JOptionPane.showMessageDialog(MD, "Đã đặt lại sức chứa tối đa của " + VehicleType + " là : " + Capacity, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void XoaVeThang() {
        MonthlyParking Ticket = new MonthlyParking();
        int selectedRow = MD.monthlyCardTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(MD, "Vui lòng chọn một vé để xóa.");
            return;
        }
        DefaultTableModel model = (DefaultTableModel) MD.monthlyCardTable.getModel();
        String LicenseNumber = model.getValueAt(selectedRow, 1).toString();
        Ticket.setLicenseNumber(LicenseNumber);

        MD.monthlyCardModel.removeRow(selectedRow);

        boolean a = Ticket.Delete();
        if (a) {
            JOptionPane.showMessageDialog(MD, "Xóa vé thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(MD, "Xóa vé thất bại", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        LoadMonthlyTickets();
    }

    public class QRScanner {

        public static void main(String[] args) {
            Webcam webcam = Webcam.getDefault();
            webcam.open();

            JFrame window = new JFrame("Quét vé xe - Camera");
            WebcamPanel panel = new WebcamPanel(webcam);
            panel.setFPSDisplayed(true);
            window.add(panel);
            window.pack();
            window.setVisible(true);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            new Thread(() -> {
                while (true) {
                    BufferedImage image = webcam.getImage();
                    if (image != null) {
                        try {
                            LuminanceSource source = new BufferedImageLuminanceSource(image);
                            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                            Result result = new MultiFormatReader().decode(bitmap);

                            if (result != null) {
                                String data = result.getText();
                                System.out.println("Đã quét được: " + data);
                                // TODO: Xử lý dữ liệu (truy vấn CSDL, kiểm tra vé, xóa vé,...)
                                break;
                            }
                        } catch (NotFoundException e) {
                            // Không tìm thấy mã, tiếp tục vòng lặp
                        }
                    }

                    try {
                        Thread.sleep(100); // nghỉ chút cho đỡ tốn CPU
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                webcam.close();
                window.dispose();
            }).start();
        }
    }

    public void startCameraScanner() {
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            JOptionPane.showMessageDialog(MD, "Không tìm thấy camera!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        webcam.setViewSize(new java.awt.Dimension(640, 480));
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);

        JFrame window = new JFrame("Quét vé tự động");
        window.add(panel);
        window.pack();
        window.setVisible(true);

        new Thread(() -> {
            while (true) {
                BufferedImage image = webcam.getImage();
                if (image != null) {
                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    try {
                        Result result = new MultiFormatReader().decode(bitmap);
                        String scannedText = result.getText();//string quet dc;
                        String[] XuLi = scannedText.split("\\|");
                        String LicenseNumber = XuLi[0];
                        String EntryTime = XuLi[1];
                        String TicketType = XuLi[2];
                        int choice = JOptionPane.showOptionDialog(
                                null,
                                "Biển số : " + LicenseNumber + " Thời gian vào : " + EntryTime,
                                "Thông báo",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                new String[]{"Xác nhận rời bãi"},
                                "Xác nhận rời bãi"
                        );
                        if (choice == 0) {
                            ParkingTicket Ticket = new ParkingTicket();
                            Ticket.setLicenseNumber(LicenseNumber);
                            Ticket.setEntryTime(EntryTime);
                            List<ParkingTicket> ResultSearch = new ArrayList<>();
                            ResultSearch = Ticket.SearchVehicle("Tìm kiếm xe");
                            if (ResultSearch.isEmpty()) {
                                JOptionPane.showMessageDialog(MD, "Xe đã lấy hoặc không gửi !", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            } else {
                                LocalDateTime now = LocalDateTime.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" HH:mm MM-dd-yyyy");
                                Ticket.setTimeOut(now.format(formatter));
                                Ticket.GetTheVehicle();
                                long ThoiGianGui = Ticket.Charge();

                                if ("Ve Thuong".equals(TicketType)) {
                                    if (ThoiGianGui < (60 * 24)) {
                                        JOptionPane.showMessageDialog(MD, "Số tiền cần thanh toán là : " + Ticket.getCost());
                                        LoadSlotLabel();
                                        LoadTableVehicleParking();
                                    } else {
                                        int ThanhToan = Ticket.getCost() * (int) Math.floor(ThoiGianGui / (60 * 24));
                                        JOptionPane.showMessageDialog(MD, "Số tiền cần thanh toán là : " + ThanhToan);
                                        LoadSlotLabel();
                                        LoadTableVehicleParking();
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(MD, "Quét vé tháng thành công");
                                    LoadTableVehicleParking();
                                }
                            }

                        }

                    } catch (NotFoundException e) {
                        // không tìm thấy mã, tiếp tục quét
                    }

                    try {
                        Thread.sleep(200); // giảm tải CPU
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("Thêm xe")) {
            ThemXe();
        }

        if (cmd.equals("Tìm kiếm xe")) {
            TimKiemXe(cmd);
        }

        if (cmd.equals("Tìm kiếm lịch sử xe")) {
            TimKiemLichSuXe();
        }

        if (cmd.equals("Xác nhận rời bãi")) {
            XacNhanRoiBai();
        }

        if (cmd.equals("Đăng ký vé tháng")) {
            DangKyVeThang();
        }

        if (cmd.equals("Thêm vé")) {
            ThemVe();
        }

        if (cmd.equals("Tìm kiếm vé theo xe") || cmd.equals("Tìm kiếm vé theo mã")) {
            TimKiemVeThang(cmd);
        }

        if (cmd.equals("Gia hạn")) {
            GiaHan();
        }

        if (cmd.equals("Tìm kiếm theo mã NV")) {
            TimKiemMaNV();
        }

        if (cmd.equals("Sửa giá")) {
            SuaGia();
        }

        if (cmd.equals("Chỉnh số lượng gửi xe")) {
            SuaSoLuongGuiXe();
        }

        if (cmd.equals("Xóa vé")) {
            XoaVeThang();
        }

    }

}
package Controller;

import Model.MonthlyParking;
import Model.ParkingTicket;
import View.StatisticsScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.YearMonth;
import javax.swing.JOptionPane;

public class StatisticsScreenController implements ActionListener {

    private StatisticsScreen view;

    public StatisticsScreenController(StatisticsScreen view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("Tính doanh thu")) {
            calculateRevenue();
        } else if (cmd.equals("Quay lại")) {
            view.dispose(); // Đóng cửa sổ StatisticsScreen
        }
    }

    private void calculateRevenue() {
        try {
            // Lấy giá trị từ các JComboBox
            int startMonth = Integer.parseInt((String) view.getStartMonthCombo().getSelectedItem());
            int startYear = Integer.parseInt((String) view.getStartYearCombo().getSelectedItem());
            int endMonth = Integer.parseInt((String) view.getEndMonthCombo().getSelectedItem());
            int endYear = Integer.parseInt((String) view.getEndYearCombo().getSelectedItem());

            // Kiểm tra giá trị hợp lệ
            if (startMonth < 1 || startMonth > 12 || endMonth < 1 || endMonth > 12) {
                JOptionPane.showMessageDialog(view, "Tháng phải từ 1 đến 12!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tạo đối tượng YearMonth
            YearMonth startPeriod = YearMonth.of(startYear, startMonth);
            YearMonth endPeriod = YearMonth.of(endYear, endMonth);

            // Gọi Model để tính doanh thu
            ParkingTicket parkingTicket = new ParkingTicket();
            parkingTicket.setLicenseNumber(""); // Gán giá trị rỗng để tránh NullPointerException
            long regularTicketRevenue = parkingTicket.calculateRevenueForPeriod(startPeriod, endPeriod);

            MonthlyParking monthlyParking = new MonthlyParking();
            long monthlyTicketRevenue = monthlyParking.calculateRevenueForPeriod(startPeriod, endPeriod);

            long totalRevenue = regularTicketRevenue + monthlyTicketRevenue;

            // Cập nhật giao diện
            view.getRegularTicketRevenueLabel().setText("Doanh thu vé lượt: " + regularTicketRevenue + " VNĐ");
            view.getMonthlyTicketRevenueLabel().setText("Doanh thu vé tháng: " + monthlyTicketRevenue + " VNĐ");
            view.getTotalRevenueLabel().setText("Tổng doanh thu: " + totalRevenue + " VNĐ");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Giá trị tháng hoặc năm không hợp lệ: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi trong khoảng thời gian: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tính toán doanh thu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
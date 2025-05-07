package View;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class StatisticsScreen extends JFrame {
    private JLabel regularTicketRevenueLabel;
    private JLabel monthlyTicketRevenueLabel;
    private JLabel totalRevenueLabel;
    private JComboBox<String> startMonthCombo;
    private JComboBox<String> startYearCombo;
    private JComboBox<String> endMonthCombo;
    private JComboBox<String> endYearCombo;

    public StatisticsScreen(String username, String role) {
        setTitle("Thống kê");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tạo bộ chọn tháng/năm bắt đầu
        JPanel startPanel = new JPanel(new FlowLayout());
        startMonthCombo = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
        startYearCombo = new JComboBox<>(new String[]{"2020", "2021", "2022", "2023", "2024", "2025", "2026"});
        startYearCombo.setSelectedItem("2025"); // Mặc định là năm hiện tại
        startPanel.add(new JLabel("Tháng bắt đầu: "));
        startPanel.add(startMonthCombo);
        startPanel.add(new JLabel(" / "));
        startPanel.add(startYearCombo);

        // Tạo bộ chọn tháng/năm kết thúc
        JPanel endPanel = new JPanel(new FlowLayout());
        endMonthCombo = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
        endYearCombo = new JComboBox<>(new String[]{"2020", "2021", "2022", "2023", "2024", "2025", "2026"});
        endYearCombo.setSelectedItem("2025"); // Mặc định là năm hiện tại
        endPanel.add(new JLabel("Tháng kết thúc: "));
        endPanel.add(endMonthCombo);
        endPanel.add(new JLabel(" / "));
        endPanel.add(endYearCombo);

        // Thêm bộ chọn vào giao diện
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(startPanel, gbc);

        gbc.gridy = 1;
        mainPanel.add(endPanel, gbc);

        // Label hiển thị doanh thu
        regularTicketRevenueLabel = new JLabel("Doanh thu vé lượt: 0 VNĐ");
        monthlyTicketRevenueLabel = new JLabel("Doanh thu vé tháng: 0 VNĐ");
        totalRevenueLabel = new JLabel("Tổng doanh thu: 0 VNĐ");

        gbc.gridy = 2;
        mainPanel.add(regularTicketRevenueLabel, gbc);

        gbc.gridy = 3;
        mainPanel.add(monthlyTicketRevenueLabel, gbc);

        gbc.gridy = 4;
        mainPanel.add(totalRevenueLabel, gbc);

        // Nút tính doanh thu và quay lại
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton calculateRevenueBtn = new JButton("Tính doanh thu");
        JButton backBtn = new JButton("Quay lại");
        buttonPanel.add(calculateRevenueBtn);
        buttonPanel.add(backBtn);

        gbc.gridy = 5;
        mainPanel.add(buttonPanel, gbc);

        calculateRevenueBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chức năng chưa được triển khai!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });

        backBtn.addActionListener(e -> dispose());

        add(mainPanel);
        setVisible(true);
    }
}
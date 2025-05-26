/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Model.MonthlyTicketBicycle;
import Model.MonthlyTicketCar;
import Model.MonthlyTicketMotorbike;
import Model.TicketBicycle;
import Model.TicketCar;
import Model.TicketMotorbike;
import Model.User;
import View.LoginScreen;
import View.ManagerDashboard;
import View.SercurityGuardDashboard;

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

            //set giá vé
            TicketMotorbike tM = new TicketMotorbike();
            tM.setCost1();
            TicketBicycle tB = new TicketBicycle();
            tB.setCost1();
            TicketCar tC = new TicketCar();
            tC.setCost1();
            MonthlyTicketBicycle MtB = new MonthlyTicketBicycle();
            MtB.setCost1();
            MonthlyTicketCar MtC = new MonthlyTicketCar();
            MtC.setCost1();
            MonthlyTicketMotorbike MtM = new MonthlyTicketMotorbike();
            MtM.setCost1();
            StringBuilder message = new StringBuilder();

            String ID = LS.usernameField.getText().trim();
            String Password = new String(LS.passwordField.getPassword()).trim();

            User user = new User(ID, Password);
            user.Login();
            String tmp = user.getRole();
            switch (tmp) {
                case (" "):
                    JOptionPane.showMessageDialog(LS, "Thông tin đăng nhập không chính xác", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    break;
                case ("Nhân viên"):
                    JOptionPane.showMessageDialog(LS, "Đăng nhập thành công với vai trò " + tmp + " !", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    LS.dispose();
                    SercurityGuardDashboard db = new SercurityGuardDashboard(ID, tmp);
                    SercurityGuardDashboardController ct = new SercurityGuardDashboardController(db);
                    ct.LoadTableVehicleParking();  
                    ct.LoadMonthlyTickets();
                    ct.LoadTableHistoryLogin();
                    ct.LoadTableParkingHistory();
                    ct.LoadSlotLabel();
                    db.setVisible(true);
                        if (tM.getCost1() == -1) {
                            message.append("- Giá gửi thường cho xe máy : Chưa cài \n");
                        }
                        else message.append("- Giá gửi thường cho xe máy : " + tM.getCost1() + "\n");
                        
                        
                        if (tB.getCost1() == -1) {
                            message.append("- Giá Gửi thường cho xe đạp : Chưa cài \n");
                        }
                        else message.append("- Giá gửi thường cho xe đạp : " + tB.getCost1() + "\n");
                        
                        
                        if (tC.getCost1() == -1) {
                            message.append("- Giá gửi thường cho ô tô : Chưa cài \n");
                        }
                        else message.append("- Giá gửi thường cho ô tô : " + tC.getCost1() + "\n");
                        
                        
                        if (MtB.getCost1() == -1) {
                            message.append("- Giá gửi tháng cho xe đạp : Chưa cài \n");
                        }
                        else message.append("- Giá gửi tháng cho xe đạp : " + MtB.getCost1() + "\n");
                        
                        
                        if (MtC.getCost1() == -1) {
                            message.append("- Giá gửi tháng cho ô tô : Chưa cài \n");
                        }
                        else message.append("- Giá gửi tháng cho ô tô : " + MtC.getCost1() + "\n");
                        
                        
                        if (MtM.getCost1() == -1) {
                            message.append("- Giá gửi tháng cho xe máy : Chưa cài \n");
                        }
                        else message.append("- Giá gửi tháng cho xe máy : " + MtM.getCost1() + "\n");

                        if(tM.getCapacity() == -1) message.append("- Số lượng xe máy tối đa : Chưa cài \n");
                        else message.append("- Số lượng xe máy tối đa : " + tM.getCapacity() + "\n");
                        
                        if(tC.getCapacity() == -1) message.append("- Số lượng Ô tô tối đa : Chưa cài \n");
                        else message.append("- Số lượng Ô tô tối đa : " + tC.getCapacity() + "\n");
                        
                        if(tB.getCapacity() == -1) message.append("- Số lượng xe đạp tối đa : Chưa cài \n");
                        else message.append("- Số lượng xe đạp tối đa : " + tB.getCapacity() + "\n");
                        
                        
                        JOptionPane.showMessageDialog(LS,
                            "Thông báo cài đặt hệ thống:\n\n" + message
                            + "\nSau khi đăng nhập, hãy vào cài đặt để chỉnh sửa.",
                            "Thông báo!", JOptionPane.ERROR_MESSAGE);
                    break;
                case ("Quản lý"):
                    JOptionPane.showMessageDialog(LS, "Đăng nhập thành công với vai trò " + tmp + " !", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    LS.dispose();
                    ManagerDashboard dashboard = new ManagerDashboard(ID, tmp);
                    ManagerDashBoardController controller = new ManagerDashBoardController(dashboard);
                    controller.LoadTableVehicleParking();  
                    controller.LoadMonthlyTickets();
                    controller.LoadTableHistoryLogin();
                    controller.LoadTableParkingHistory();
                    controller.LoadSlotLabel();
                    dashboard.setVisible(true);

                    if (tM.getCost1() == -1) {
                            message.append("- Giá gửi thường cho xe máy : Chưa cài \n");
                        }
                        else message.append("- Giá gửi thường cho xe máy : " + tM.getCost1() + "\n");
                        
                        
                        if (tB.getCost1() == -1) {
                            message.append("- Giá Gửi thường cho xe đạp : Chưa cài \n");
                        }
                        else message.append("- Giá gửi thường cho xe đạp : " + tB.getCost1() + "\n");
                        
                        
                        if (tC.getCost1() == -1) {
                            message.append("- Giá gửi thường cho ô tô : Chưa cài \n");
                        }
                        else message.append("- Giá gửi thường cho ô tô : " + tC.getCost1() + "\n");
                        
                        
                        if (MtB.getCost1() == -1) {
                            message.append("- Giá gửi tháng cho xe đạp : Chưa cài \n");
                        }
                        else message.append("- Giá gửi tháng cho xe đạp : " + MtB.getCost1() + "\n");
                        
                        
                        if (MtC.getCost1() == -1) {
                            message.append("- Giá gửi tháng cho ô tô : Chưa cài \n");
                        }
                        else message.append("- Giá gửi tháng cho ô tô : " + MtC.getCost1() + "\n");
                        
                        
                        if (MtM.getCost1() == -1) {
                            message.append("- Giá gửi tháng cho xe máy : Chưa cài \n");
                        }
                        else message.append("- Giá gửi tháng cho xe máy : " + MtM.getCost1() + "\n");

                        if(tM.getCapacity() == -1) message.append("- Số lượng xe máy tối đa : Chưa cài \n");
                        else message.append("- Số lượng xe máy tối đa : " + tM.getCapacity() + "\n");
                        
                        if(tC.getCapacity() == -1) message.append("- Số lượng Ô tô tối đa : Chưa cài \n");
                        else message.append("- Số lượng Ô tô tối đa : " + tC.getCapacity() + "\n");
                        
                        if(tB.getCapacity() == -1) message.append("- Số lượng xe đạp tối đa : Chưa cài \n");
                        else message.append("- Số lượng xe đạp tối đa : " + tB.getCapacity() + "\n");
                        
                        
                        JOptionPane.showMessageDialog(LS,
                            "Thông báo cài đặt hệ thống:\n\n" + message
                            + "\nSau khi đăng nhập, hãy vào cài đặt để chỉnh sửa.",
                            "Thông báo!", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(LS, "Hệ thống gặp lỗi", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    break;
            }

        }
    }
}

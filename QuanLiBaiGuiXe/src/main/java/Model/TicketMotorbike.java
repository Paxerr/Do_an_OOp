/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import DataBase.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Moderator
 */
public class TicketMotorbike extends ParkingTicket {

    
    
    public TicketMotorbike() {
        super();
    }
    static private int Cost1;
    static private int Capacity = 0;
    
    public int getCost1(){
        return Cost1;
    }
    
           public int Available(){
        ResultSet Result = null;
        Connection tmp = JDBCUtil.getConnection();
        PreparedStatement state = null;
        int kq;
        try {
            String sql = "Select COUNT(*) FROM parkingticket Where TimeOut = ? AND VehicleType = ?";
            state = tmp.prepareStatement(sql);
            state.setString(1, "Đang gửi");
            state.setString(2, "Xe máy");
            Result = state.executeQuery();
            if(Result.next()){
                kq = Result.getInt(1);
            }
            else kq = 0;
            
            if (Result != null) {
                Result.close();
            }
            if (state != null) {
                state.close();
            }
            if (tmp != null) {
                tmp.close();
            }
            return kq;

        } catch (Exception e) {
            e.printStackTrace();
            return kq = -1;
        }
    }
    
    public void setCapacity(){
        ResultSet KetQuaTruyVan = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try{
            tmp = JDBCUtil.getConnection();
            String Check = "SELECT * From setting";
            state = tmp.prepareStatement(Check);
            KetQuaTruyVan = state.executeQuery();
            if (!KetQuaTruyVan.next()) {
                this.Capacity = 0;
            } else {
                this.Capacity = KetQuaTruyVan.getInt("CapacityMotorbike");
                if (KetQuaTruyVan.wasNull()) {
                    this.Capacity = 0;
                }
            }
            if (KetQuaTruyVan != null) {
                    KetQuaTruyVan.close();
                }
                if (state != null) {
                    state.close();
                }
                if (tmp != null) {
                    tmp.close();
                }
        } catch (Exception e) {
            this.Capacity = -1;
            e.printStackTrace();
        }
    }
    
    public void setCapacity(int Capacity) {
        this.Capacity = Capacity;
        ResultSet KetQuaTruyVan = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try {
            tmp = JDBCUtil.getConnection();

            String Check = "SELECT * From setting";
            state = tmp.prepareStatement(Check);
            KetQuaTruyVan = state.executeQuery();
            if (KetQuaTruyVan.next()) {
                String SuaCapacity = "UPDATE setting SET CapacityMotorbike = ?  WHERE STT = ?";
                state = tmp.prepareStatement(SuaCapacity);
                state.setString(1, this.Capacity + "");
                state.setString(2, "0");
                state.executeUpdate();
            }
            else {
                String ThemCapacity = "INSERT INTO setting (STT, CapacityMotorbike) VALUES (? , ?)";
                state = tmp.prepareStatement(ThemCapacity);
                state.setString(1, "0");
                state.setString(2, String.valueOf(this.Capacity));     
                state.executeUpdate();
            }
                
                if (KetQuaTruyVan != null) {
                    KetQuaTruyVan.close();
                }
                if (state != null) {
                    state.close();
                }
                if (tmp != null) {
                    tmp.close();
                }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getCapacity(){
        return this.Capacity;
    }
    
    public void setCost1(){
        ResultSet KetQuaTruyVan = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try{
            tmp = JDBCUtil.getConnection();
            String Check = "SELECT * From setting";
            state = tmp.prepareStatement(Check);
            KetQuaTruyVan = state.executeQuery();
            if (!KetQuaTruyVan.next()) {
                this.Cost1 = -1;
            } else {
                this.Cost1 = KetQuaTruyVan.getInt("Motorbike");
                if (KetQuaTruyVan.wasNull()) {
                    this.Cost1 = -1;
                }
            }
            if (KetQuaTruyVan != null) {
                    KetQuaTruyVan.close();
                }
                if (state != null) {
                    state.close();
                }
                if (tmp != null) {
                    tmp.close();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setCost1(int Cost) {
        this.Cost1 = Cost;
        ResultSet KetQuaTruyVan = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try {
            tmp = JDBCUtil.getConnection();

            String Check = "SELECT * From setting";
            state = tmp.prepareStatement(Check);
            KetQuaTruyVan = state.executeQuery();
            if (KetQuaTruyVan.next()) {
                String SuaGia = "UPDATE setting SET Motorbike = ?  WHERE STT = ?";
                state = tmp.prepareStatement(SuaGia);
                state.setString(1, this.Cost1 + "");
                state.setString(2, "0");
                state.executeUpdate();
            }
            else {
                String ThemGia = "INSERT INTO setting (STT, Motorbike) VALUES (? , ?)";
                state = tmp.prepareStatement(ThemGia);
                state.setString(1, "0");
                state.setString(2, String.valueOf(this.Cost1));     
                state.executeUpdate();
            }
                
                if (KetQuaTruyVan != null) {
                    KetQuaTruyVan.close();
                }
                if (state != null) {
                    state.close();
                }
                if (tmp != null) {
                    tmp.close();
                }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ParkTheVehicle() {
        ResultSet KetQuaTruyVan = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try {
            tmp = JDBCUtil.getConnection();
            String LayID = "SELECT * From ParkingTicket ORDER BY TicketID DESC";
            String ThemVeXe = "INSERT INTO parkingticket (TicketID, LicenseNumber, VehicleType, TicketType, EntryTime, Cost, TimeOut) VALUES (?, ?, ?, ?, ?, ?, ?)";
            String TimLoaiVe = "SELECT * From monthlyparking Where LicenseNumber = ?";
            state = tmp.prepareStatement(TimLoaiVe);
            state.setString(1, this.LicenseNumber);
            KetQuaTruyVan = state.executeQuery();
            if (KetQuaTruyVan.next()) {
                TicketType = "Vé Tháng";
            } else {
                TicketType = "Vé Thường";
            }
            this.setTimeOut("Đang gửi");
            state = tmp.prepareStatement(LayID);
            KetQuaTruyVan = state.executeQuery();
            if (KetQuaTruyVan.next()) {
                this.TicketID = KetQuaTruyVan.getInt("TicketID") + 1;
            } else {
                this.TicketID = 0;
            }

            if (this.VehicleType.equals("Xe đạp")) {
                this.LicenseNumber = this.TicketID + "";
            }
            state = tmp.prepareStatement(ThemVeXe);
            state.setString(1, Integer.toString(this.TicketID));
            state.setString(2, this.LicenseNumber);
            state.setString(3, this.VehicleType);
            state.setString(4, this.TicketType);
            state.setString(5, this.EntryTime);
            state.setInt(6, this.Cost1);
            state.setString(7, this.TimeOut);
            int rowsAffected = state.executeUpdate();
            if (KetQuaTruyVan != null) {
                KetQuaTruyVan.close();
            }
            if (state != null) {
                state.close();
            }
            if (tmp != null) {
                tmp.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.Cost1 = -2;
            this.TicketType = "error";
        }
    }

}

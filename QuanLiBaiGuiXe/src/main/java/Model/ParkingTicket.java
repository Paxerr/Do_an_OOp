/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import DataBase.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ParkingTicket extends Vehicle {

    protected int TicketID;
    protected String TicketType;
    protected String EntryTime;
    protected String TimeOut;

    public ParkingTicket() {

    }

    ;
    public ParkingTicket(int TicketID, String LicenseNumber, String VehicleType, String TicketType, String EntryTime, String TimeOut, int Cost) {
        this.Cost = Cost;
        this.EntryTime = EntryTime;
        this.VehicleType = VehicleType;
        this.LicenseNumber = LicenseNumber;
        this.TicketID = TicketID;
        this.TimeOut = TimeOut;
        this.TicketType = TicketType;
    }

    public void setTicketType(String TicketType) {
        this.TicketType = TicketType;
    }

    public String getTicketType() {
        return TicketType;
    }

    public int getTicketID() {
        return TicketID;
    }

    public String getEntryTime() {
        return EntryTime;
    }

    public String getTimeOut() {
        return TimeOut;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public int getCost() {
        return Cost;
    }

    public void setTicketID(int TicketID) {
        this.TicketID = TicketID;
    }

    public void setEntryTime(String entryTime) {
        this.EntryTime = entryTime;
    }

    public void setTimeOut(String timeOut) {
        this.TimeOut = timeOut;
    }

    public void setVehicleType(String VehicleType) {
        this.VehicleType = VehicleType;
    }

    public void setCost(int Cost) {
        this.Cost = Cost;
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
            state.setInt(6, this.Cost);
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
            this.TicketType = "error";
        }
    }

    public List<ParkingTicket> SearchVehicle(String TypeOfSearch) {
        List<ParkingTicket> ResultSearch = new ArrayList<>();
        ResultSet Result = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try {
            String TimKiemXe = null;
            tmp = JDBCUtil.getConnection();
            if (TypeOfSearch.equals("Tìm kiếm xe")) {
                TimKiemXe = "SELECT * From parkingticket WHERE LicenseNumber = ? AND TimeOut = ?";
                state = tmp.prepareStatement(TimKiemXe);
                state.setString(1, this.LicenseNumber);
                state.setString(2, "Đang gửi");
            } else if (TypeOfSearch.equals("Refesh")) {
                TimKiemXe = "SELECT * From parkingticket WHERE TimeOut = ?";
                state = tmp.prepareStatement(TimKiemXe);
                state.setString(1, "Đang gửi");
            };

            Result = state.executeQuery();

            while (Result.next()) {
                String LicenseNumber = Result.getString("LicenseNumber");
                int TicketID = Result.getInt("TicketID");
                String VehicleType = Result.getString("VehicleType");
                String TicketType = Result.getString("TicketType");
                String EntryTime = Result.getString("EntryTime");
                String TimeOut = Result.getString("TimeOut");
                int Cost = Result.getInt("Cost");
                
                ParkingTicket t = new ParkingTicket(TicketID, LicenseNumber, VehicleType, TicketType, EntryTime, TimeOut, Cost);
                ResultSearch.add(t);
            }

            if (Result != null) {
                Result.close();
            }
            if (state != null) {
                state.close();
            }
            if (tmp != null) {
                tmp.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.TicketType = "error";
        }
        return ResultSearch;
    }
    public List<ParkingTicket> SearchHistoryVehicle(){
        List<ParkingTicket> ResultSearch = new ArrayList<>();
        ResultSet Result = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try {
            String TimKiemXe = null;
            tmp = JDBCUtil.getConnection();
            if (!(this.LicenseNumber.isEmpty())) {
                TimKiemXe = "SELECT * From parkingticket WHERE LicenseNumber = ?";
                state = tmp.prepareStatement(TimKiemXe);
                state.setString(1, this.LicenseNumber);
            } else {
                TimKiemXe = "SELECT * From parkingticket";
                state = tmp.prepareStatement(TimKiemXe);
            };

            Result = state.executeQuery();

            while (Result.next()) {
                int TicketID = Result.getInt("TicketID");
                String LicenseNumber = Result.getString("LicenseNumber");
                String VehicleType = Result.getString("VehicleType");
                String TicketType = Result.getString("TicketType");
                String EntryTime = Result.getString("EntryTime");
                String TimeOut = Result.getString("TimeOut");
                int Cost = Result.getInt("Cost");
                ParkingTicket t = new ParkingTicket(TicketID, LicenseNumber, VehicleType, TicketType, EntryTime, TimeOut, Cost);
                ResultSearch.add(t);
            }

            if (Result != null) {
                Result.close();
            }
            if (state != null) {
                state.close();
            }
            if (tmp != null) {
                tmp.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.TicketType = "error";
        }
        return ResultSearch;
    }
    
    public void GetTheVehicle(){
        ResultSet Result = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try {
            tmp = JDBCUtil.getConnection();
            String GetTheVehicle = "UPDATE parkingticket SET TimeOut = ?  WHERE LicenseNumber = ?";
            String LayCostXe = "SELECT * FROM parkingticket WHERE LicenseNumber = ?";
            
            state = tmp.prepareStatement(LayCostXe);
            state.setString(1, this.LicenseNumber);
            Result = state.executeQuery();
            if(Result.next())
                this.Cost = Result.getInt("Cost");
            
            state = tmp.prepareStatement(GetTheVehicle);
            state.setString(1, this.TimeOut);
            state.setString(2, this.LicenseNumber);
            state.executeUpdate();
            
            if (Result != null) {
                Result.close();
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
    
    public long Charge(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" HH:mm dd-MM-yyyy");
        LocalDateTime EntryTime = LocalDateTime.parse(this.EntryTime, formatter);
        LocalDateTime TimeOut = LocalDateTime.parse(this.TimeOut, formatter);
        long minutes = Duration.between(EntryTime, TimeOut).toMinutes();
        return minutes;
    }
}

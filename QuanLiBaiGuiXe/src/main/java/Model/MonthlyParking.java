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
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class MonthlyParking extends Vehicle{
    protected int CardID;
    protected String ExpireDate;
    protected String StartDate;

    private MonthlyParking(String LicenseNumber, int CardID, String VehicleType, String StartDate, String ExpireDate, int Cost) {
        this.LicenseNumber = LicenseNumber;
        this.CardID = CardID;
        this.VehicleType = VehicleType;
        this.ExpireDate = ExpireDate;
        this.Cost = Cost;
        this.StartDate = StartDate;
    }

    public MonthlyParking() {
    }
    public int getCardID() {
        return CardID;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    public String getStartDate() {
        return StartDate;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public String getLicenseNumber() {
        return LicenseNumber;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public int getCost() {
        return Cost;
    }

    public void setCardID(int CardID) {
        this.CardID = CardID;
    }

    public void setExpireDate(String ExpireDate) {
        this.ExpireDate = ExpireDate;
    }

    public void setLicenseNumber(String LicenseNumber) {
        this.LicenseNumber = LicenseNumber;
    }

    public void setVehicleType(String VehicleType) {
        this.VehicleType = VehicleType;
    }

    public void setCost(int Cost) {
        this.Cost = Cost;
    }
    public void Register(){
        ResultSet KetQuaTruyVan = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try {
            tmp = JDBCUtil.getConnection();
            String LayID = "SELECT * From monthlyparking ORDER BY CardID DESC";
            String ThemXeThang = "INSERT INTO monthlyparking (LicenseNumber, Cost, VehicleType,StartDate, ExpireDate, CardID) VALUES (?,?, ?, ?, ?, ?)";
            
            if(this.CardID == -1){
                state = tmp.prepareStatement(LayID);
                KetQuaTruyVan = state.executeQuery();
                if (KetQuaTruyVan.next()) {
                    this.CardID = KetQuaTruyVan.getInt("CardID") + 1;
                } else {
                    this.CardID = 0;
                }
            }
            state = tmp.prepareStatement(ThemXeThang);
            state.setString(1, this.LicenseNumber);
            state.setString(2, Integer.toString(this.Cost));
            state.setString(3, this.VehicleType);
            state.setString(4, this.StartDate);
            state.setString(5, this.ExpireDate);
            state.setString(6, Integer.toString(this.CardID));
            state.executeUpdate();
            
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
     public List<MonthlyParking> Search(String TypeOfSearch){
        List<MonthlyParking> ResultSearch = new ArrayList<>();
        ResultSet Result = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try {
            String TimKiemXe = null;
            tmp = JDBCUtil.getConnection();
            if (TypeOfSearch.equals("Refesh")) {
                TimKiemXe = "SELECT * From monthlyparking";
                state = tmp.prepareStatement(TimKiemXe);
                
            }else if (TypeOfSearch.equals("Tìm kiếm vé theo xe")) {
                TimKiemXe = "SELECT * From monthlyparking WHERE LicenseNumber = ?";
                state = tmp.prepareStatement(TimKiemXe);
                state.setString(1, this.LicenseNumber);
                
            }else if (TypeOfSearch.equals("Tìm kiếm vé theo mã")){
                TimKiemXe = "SELECT * From monthlyparking WHERE CardID = ?";
                state = tmp.prepareStatement(TimKiemXe);
                state.setString(1, Integer.toString(this.CardID));
            }

            Result = state.executeQuery();

            while (Result.next()) {
                String LicenseNumber = Result.getString("LicenseNumber");
                int CardID = Result.getInt("CardID");
                String VehicleType = Result.getString("VehicleType");
                String StartDate = Result.getString("StartDate");
                String ExpireDate = Result.getString("ExpireDate");
                int Cost = Result.getInt("Cost");
                
                MonthlyParking t = new MonthlyParking(LicenseNumber, CardID, VehicleType,StartDate, ExpireDate, Cost);
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
        }
        return ResultSearch;
     }
     
     public void Extend(){
        ResultSet Result = null;
        Connection tmp = null;
        PreparedStatement state = null;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
        YearMonth entry = YearMonth.parse(this.ExpireDate, formatter);
        YearMonth updated = entry.plusMonths(1);
        this.ExpireDate = updated.format(formatter);
        
        try {
            tmp = JDBCUtil.getConnection();
            String Extend = "UPDATE monthlyparking SET ExpireDate = ?  WHERE LicenseNumber = ?";
            state = tmp.prepareStatement(Extend);
            state.setString(1, this.ExpireDate);
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

public long calculateRevenueForPeriod(YearMonth start, YearMonth end) {
    long revenue = 0;
    List<MonthlyParking> monthlyList = Search("Refesh"); 
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");

    for (MonthlyParking m : monthlyList) {
        if (m.getExpireDate() == null || m.getExpireDate().isEmpty()) {
            continue; 
        }

        YearMonth expireDate = YearMonth.parse(m.getExpireDate(), formatter);
        if ((expireDate.compareTo(start) >= 0) && (expireDate.compareTo(end) <= 0)) {
            revenue += m.getCost();
        }
    }

    return revenue;
}
    
}

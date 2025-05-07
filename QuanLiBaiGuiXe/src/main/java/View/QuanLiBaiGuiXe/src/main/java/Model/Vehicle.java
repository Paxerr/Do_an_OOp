package Model;

import DataBase.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Vehicle {

    Vehicle() {
    }
    ;
    protected String LicenseNumber;
    protected String VehicleType;
    protected int Cost;

    public int GetIn() {
        return 1;
    }

    public int GetOut() {
        return 1;
    }

    public void setCost() {
        if(this.VehicleType.equals("Xe máy")) this.Cost = 3000;
        else if(this.VehicleType.equals("Ô tô")) this.Cost = 5000;
        else System.out.print("Lỗi set Cost");
    }

    public int getCost() {
        return Cost;
    }

    public String getLicenseNumber() {
        return LicenseNumber;
    }

    public void setLicenseNumber(String LicenseNumber) {
        this.LicenseNumber = LicenseNumber;
    }

    public String getVehicleType() {
        return VehicleType;
    }
}

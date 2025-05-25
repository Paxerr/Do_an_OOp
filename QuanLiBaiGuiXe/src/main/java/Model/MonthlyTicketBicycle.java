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
public class MonthlyTicketBicycle extends MonthlyParking {

    public MonthlyTicketBicycle() {

    }
    static private int Cost1;

    public int getCost1() {
        return Cost1;
    }

    public void setCost1() {
        ResultSet KetQuaTruyVan = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try {
            tmp = JDBCUtil.getConnection();
            String Check = "SELECT * From cost";
            state = tmp.prepareStatement(Check);
            KetQuaTruyVan = state.executeQuery();
            if (!KetQuaTruyVan.next()) {
                this.Cost1 = -1;
            } else {
                this.Cost1 = KetQuaTruyVan.getInt("MonthlyBicycle");
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
        Cost1 = Cost;
        ResultSet KetQuaTruyVan = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try {
            tmp = JDBCUtil.getConnection();

            String LayID = "SELECT * From cost";
            state = tmp.prepareStatement(LayID);
            KetQuaTruyVan = state.executeQuery();
            if (KetQuaTruyVan.next()) {
                String SuaGia = "UPDATE cost SET MonthlyBicycle = ?  WHERE STT = ?";
                state = tmp.prepareStatement(SuaGia);
                state.setString(1, this.Cost1 + "");
                state.setString(2, "0");
                state.executeUpdate();
            } else {
                String ThemGia = "INSERT INTO cost ( STT, MonthlyBicycle) VALUES (? , ?)";
                state = tmp.prepareStatement(ThemGia);
                state.setString(1, "0");
                state.setString(2, this.Cost1 + "");
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

    public void Register() {
        ResultSet KetQuaTruyVan = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try {
            tmp = JDBCUtil.getConnection();
            String LayID = "SELECT * From monthlyparking ORDER BY CardID DESC";
            String ThemXeThang = "INSERT INTO monthlyparking (LicenseNumber, Cost, VehicleType, ExpireDate, CardID) VALUES (?, ?, ?, ?, ?)";

            if (this.CardID == -1) {
                state = tmp.prepareStatement(LayID);
                KetQuaTruyVan = state.executeQuery();
                if (KetQuaTruyVan.next()) {
                    this.CardID = KetQuaTruyVan.getInt("CardID") + 1;
                } else {
                    this.CardID = 0;
                }
            }
            if (this.VehicleType.equals("Xe đạp")) {
                this.LicenseNumber = this.CardID + "";
            }
            state = tmp.prepareStatement(ThemXeThang);
            state.setString(1, this.LicenseNumber);
            state.setString(2, Integer.toString(this.Cost1));
            state.setString(3, this.VehicleType);
            state.setString(4, this.ExpireDate);
            state.setString(5, Integer.toString(this.CardID));
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
            this.Cost = -2;
            e.printStackTrace();
        }
    }

}

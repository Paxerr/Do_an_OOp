package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtil {

    private static final String URL = "jdbc:mysql://localhost:3306";
    private static final String USER = "root";
    private static final String PASSWORD = "quyet1110";

    public static Connection getConnection() {
        Connection c = null;
        Statement statement = null;
        try {

            c = DriverManager.getConnection(URL, USER, PASSWORD);
            String[] queries = {
                "CREATE DATABASE IF NOT EXISTS hehe",
                "CREATE TABLE IF NOT EXISTS hehe.loginhistory ("
                + "STT INT NOT NULL,"
                + "ID VARCHAR(45),"
                + "FullName VARCHAR(45),"
                + "Role VARCHAR(45),"
                + "LoginTime VARCHAR(100),"
                + "PRIMARY KEY (STT))",
                "CREATE TABLE IF NOT EXISTS hehe.monthlyparking ("
                + "LicenseNumber VARCHAR(20) NOT NULL,"
                + "Cost VARCHAR(45),"
                + "VehicleType VARCHAR(45),"
                + "ExpireDate VARCHAR(45),"
                + "CardID INT,"
                + "PRIMARY KEY (LicenseNumber))",
                "CREATE TABLE IF NOT EXISTS hehe.parkingticket ("
                + "TicketID INT NOT NULL,"
                + "TicketType VARCHAR(45),"
                + "EntryTime VARCHAR(45),"
                + "TimeOut VARCHAR(255),"
                + "LicenseNumber VARCHAR(45),"
                + "VehicleType VARCHAR(45),"
                + "Cost INT,"
                + "PRIMARY KEY (TicketID))",
                "CREATE TABLE IF NOT EXISTS hehe.user ("
                + "ID VARCHAR(10) NOT NULL,"
                + "Password VARCHAR(45),"
                + "Role VARCHAR(20),"
                + "FullName VARCHAR(45),"
                + "Gender VARCHAR(10),"
                + "Address VARCHAR(255),"
                + "Identifier VARCHAR(45),"
                + "PhoneNumber VARCHAR(45),"
                + "PRIMARY KEY (ID))",
                "CREATE TABLE IF NOT EXISTS hehe.cost ("
                + "STT INT NOT NULL,"
                + "Motorbike INT,"
                + "Bicycle INT,"
                + "Car INT,"
                + "MonthlyMotorbike INT,"
                + "MonthlyBicycle INT,"
                + "MonthlyCar INT,"
                + "PRIMARY KEY (STT))"
            };

            statement = c.createStatement();
            Statement stmt = c.createStatement();
            


            for (String query : queries) {
                stmt.executeUpdate(query);
            }
            
            String useDBSQL = "USE hehe";
            statement.executeUpdate(useDBSQL);

        } catch (SQLException e) {
            System.err.println("Kết nối thất bại: " + e.getMessage());
            e.printStackTrace();
        }
        return c;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

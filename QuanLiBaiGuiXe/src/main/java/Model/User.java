/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import DataBase.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Moderator
 */
public class User extends Person {

    private String ID, Password, Role;

    public User(String ID, String Password) {
        this.ID = ID;
        this.Password = Password;
        this.Role = null;
    }

    ;
    public User() {

    }

    public User(String ID, String identifier, String fullName, String role, String gender, String address, String phoneNumber, String password) {
        this.ID = ID;
        this.Identifier = identifier;
        this.FullName = fullName;
        this.Role = role;
        this.Gender = gender;
        this.Address = address;
        this.PhoneNumber = phoneNumber;
        this.Password = password;
    }
    
    public String getID() {
        return ID;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }

    public String getRole() {
        return Role;
    }

    public String getPassword() {
        return Password;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void CheckEmpty() {
        ResultSet KetQuaTruyVan = null;
        Connection tmp = null;
        PreparedStatement state = null;
        String sql;
        try {
            tmp = JDBCUtil.getConnection();
            sql = "SELECT * From user";

            state = tmp.prepareStatement(sql);
            KetQuaTruyVan = state.executeQuery();

            if (!(KetQuaTruyVan.next())) {
                sql = "INSERT INTO User (ID, Password, Role) VALUES ( ?, ?, ?)";
                state = tmp.prepareStatement(sql);
                state.setString(1, "1");
                state.setString(2, "1");
                state.setString(3, "Quản lí");
                state.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Login() {
        ResultSet KetQuaTruyVan = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try {
            tmp = JDBCUtil.getConnection();
            String sql = "SELECT * From User Where ID = ? AND Password = ?";
            state = tmp.prepareStatement(sql);
            state.setString(1, this.ID);
            state.setString(2, this.Password);
            KetQuaTruyVan = state.executeQuery();
            if (KetQuaTruyVan.next()) {
                this.Role = KetQuaTruyVan.getString("Role");
                this.FullName = KetQuaTruyVan.getString("FullName");
                this.Address = KetQuaTruyVan.getString("Address");
                this.PhoneNumber = KetQuaTruyVan.getString("PhoneNumber");
                this.Gender = KetQuaTruyVan.getString("Gender");
                this.Identifier = KetQuaTruyVan.getString("Identifier");
            } else {
                this.Role = " ";
            }

            int STT;
            String LaySTT = "SELECT * From loginhistory ORDER BY STT DESC";
            state = tmp.prepareStatement(LaySTT);
            KetQuaTruyVan = state.executeQuery();
            if (KetQuaTruyVan.next()) {
                STT = KetQuaTruyVan.getInt("STT") + 1;
            } else {
                STT = 0;
            }

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm MM-dd-yyyy");

            sql = "INSERT INTO loginhistory (STT, ID, FullName, Role, LoginTime) VALUES (?, ?, ?, ?, ?)";
            state = tmp.prepareStatement(sql);
            state.setString(1, Integer.toString(STT));
            state.setString(2, this.ID);
            state.setString(3, this.FullName);
            state.setString(4, this.Role);
            state.setString(5, now.format(formatter));
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
            this.Role = "error";
        }
    }

    public List<Object[]> SearchHistory(String TypeOfSearch) {
        List<Object[]> ResultSearch = new ArrayList<>();
        ResultSet Result = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try {
            String TimKiem = null;
            tmp = JDBCUtil.getConnection();
            if (TypeOfSearch.equals("Tìm kiếm")) {
                TimKiem = "SELECT * From loginhistory WHERE ID = ?";
                state = tmp.prepareStatement(TimKiem);
                state.setString(1, this.ID);
            } else if (TypeOfSearch.equals("Refesh")) {
                TimKiem = "SELECT * From loginhistory";
                state = tmp.prepareStatement(TimKiem);
            };

            Result = state.executeQuery();

            while (Result.next()) {
                int STT = Result.getInt("STT");
                String ID = Result.getString("ID");
                String FullName = Result.getString("FullName");
                String Role = Result.getString("Role");
                String LoginTime = Result.getString("LoginTime");

                Object[] t = new Object[]{
                    STT,
                    ID,
                    FullName,
                    Role,
                    LoginTime
                };
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
    
    public boolean addUser() {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "INSERT INTO user (ID, Identifier, FullName, Address, PhoneNumber, Gender, Role, Password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, this.ID);
            stmt.setString(2, this.Identifier);
            stmt.setString(3, this.FullName);
            stmt.setString(4, this.Address);
            stmt.setString(5, this.PhoneNumber);
            stmt.setString(6, this.Gender);
            stmt.setString(7, this.Role);
            stmt.setString(8, this.Password);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    
    public boolean updateUser(String oldId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "UPDATE user SET ID = ?, Identifier = ?, FullName = ?, Address = ?, PhoneNumber = ?, Gender = ?, Role = ?, Password = ? WHERE ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, this.ID);
            stmt.setString(2, this.Identifier);
            stmt.setString(3, this.FullName);
            stmt.setString(4, this.Address);
            stmt.setString(5, this.PhoneNumber);
            stmt.setString(6, this.Gender);
            stmt.setString(7, this.Role);
            stmt.setString(8, this.Password);
            stmt.setString(9, oldId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    
    public boolean deleteUser(String id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "DELETE FROM user WHERE ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    
    public List<Object[]> searchUserByIdentifier(String identifier) {
        List<Object[]> resultSearch = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT ID, Identifier, FullName, Address, PhoneNumber, Gender, Role, Password FROM user WHERE Identifier = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, identifier);
            result = stmt.executeQuery();

            while (result.next()) {
                Object[] row = {
                    result.getString("ID"),
                    result.getString("Identifier"),
                    result.getString("FullName"),
                    result.getString("Role"),
                    result.getString("Gender"),
                    result.getString("Address"),
                    result.getString("PhoneNumber"),
                    result.getString("Password")
                };
                resultSearch.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(result, stmt, conn);
        }
        return resultSearch;
    }

   
    public List<Object[]> getAllUsers() {
        List<Object[]> resultSearch = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT ID, Identifier, FullName, Address, PhoneNumber, Gender, Role, Password FROM user";
            stmt = conn.prepareStatement(sql);
            result = stmt.executeQuery();

            while (result.next()) {
                Object[] row = {
                    result.getString("ID"),
                    result.getString("Identifier"),
                    result.getString("FullName"),
                    result.getString("Role"),
                    result.getString("Gender"),
                    result.getString("Address"),
                    result.getString("PhoneNumber"),
                    result.getString("Password")
                };
                resultSearch.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(result, stmt, conn);
        }
        return resultSearch;
    }

   
    public boolean isIdExists(String id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM user WHERE ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            result = stmt.executeQuery();
            if (result.next()) {
                return result.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(result, stmt, conn);
        }
        return false;
    }

    
    private void closeResources(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

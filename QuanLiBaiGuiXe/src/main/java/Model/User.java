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

/**
 *
 * @author Moderator
 */
public class User extends Person{
    private String ID, Password, Role;

    public User(String ID,String Password){
        this.ID = ID;
        this.Password = Password;
        this.Role = null;
    };
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
    public void Login(){
        ResultSet KetQuaTruyVan = null;
        Connection tmp = null;
        PreparedStatement state = null;
        try{
            tmp = JDBCUtil.getConnection();
            String sql = "SELECT * From User Where ID = ? AND Password = ?";
            state = tmp.prepareStatement(sql);
            state.setString(1,this.ID);
            state.setString(2,this.Password);
            KetQuaTruyVan = state.executeQuery();
            if(KetQuaTruyVan.next()){
                this.Role = KetQuaTruyVan.getString("Role");
                if(KetQuaTruyVan != null) KetQuaTruyVan.close();
                if(state != null) state.close();
                if(tmp != null) tmp.close();
            }
            else{
                if(KetQuaTruyVan != null) KetQuaTruyVan.close();
                if(state != null) state.close();
                if(tmp != null) tmp.close();
                this.Role =  " ";
            }      
        } 
        catch(Exception e) {
            e.printStackTrace();
            this.Role = "error";
            }
    }
}
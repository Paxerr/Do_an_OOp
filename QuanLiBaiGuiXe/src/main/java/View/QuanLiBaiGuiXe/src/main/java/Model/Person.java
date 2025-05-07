/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class Person {
    protected String FullName, Address, PhoneNumber, Identifier,Gender;
    Person(){};

    public Person(String FullName, String Address, String PhoneNumber, String Identifier, String Gender) {
        this.FullName = FullName;
        this.Address = Address;
        this.PhoneNumber = PhoneNumber;
        this.Identifier = Identifier;
        this.Gender = Gender;
    }
    
    public String getFullName() {
        return FullName;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getIdentifier() {
        return Identifier;
    }
    
    public String getGender(){
        return Gender;
    }
    
    public void setGender(String Gender){
        this.Gender= Gender;
    }
    
    public void setFullName(String FullName) {
        this.FullName = FullName;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public void setIdentifier(String Identifier) {
        this.Identifier = Identifier;
    } 
}

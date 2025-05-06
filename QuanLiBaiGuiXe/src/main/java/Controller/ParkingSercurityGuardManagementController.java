package Controller;

import Model.Person;
import java.util.ArrayList;
import java.util.List;

public class ParkingSercurityGuardManagementController {
    private List<Person> persons;

    public ParkingSercurityGuardManagementController() {
        persons = new ArrayList<>();
    }

    public boolean addPerson(String Identifier, String FullName, String Address, String PhoneNumber , String Gender) {
        persons.add(new Person(Identifier, FullName, Address, PhoneNumber, Gender));
        return true;
    }

    public boolean deletePerson(String identifier) {
        return persons.removeIf(p -> p.getIdentifier().equals(identifier));
    }

    public List<Object[]> searchPerson(String cccd) {
        List<Object[]> result = new ArrayList<>();
        for (Person p : persons) {
            if (p.getIdentifier().equals(cccd)) {
                result.add(new Object[]{
                    p.getIdentifier(),
                    p.getFullName(),
                    p.getAddress(),
                    p.getPhoneNumber(),
                    p.getGender()
                });
            }
        }
        return result;
    }

    public boolean updatePerson(String Identifier, String FullName, String Address, String PhoneNumber, String Gender) {
        for (Person p : persons) {
            if (p.getIdentifier().equals(Identifier)) {
                p.setFullName(FullName);
                p.setAddress(Address);
                p.setPhoneNumber(PhoneNumber);
                p.setGender(Gender);
                return true;
            }
        }
        return false;
    }

    public List<Object[]> getAllPersons() {
        List<Object[]> result = new ArrayList<>();
        int index = 1;
        for (Person p : persons) {
            result.add(new Object[]{
                "ID" + String.format("%04d", index++),
                p.getIdentifier(),
                p.getFullName(),
                "Nhân viên",
                "",
                p.getGender(),
                p.getAddress(),
                p.getPhoneNumber()
            });
        }
        return result;
    }

}

package model;

import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;

//==============================================================================
public class ScoutTableModel
{
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty middleName;
    private final SimpleStringProperty dateOfBirth;
    private final SimpleStringProperty phoneNumber;
    private final SimpleStringProperty email;
    private final SimpleStringProperty troopID;
    private final SimpleStringProperty status;

    //----------------------------------------------------------------------------
    public ScoutTableModel(Vector<String> scoutData)
    {
        firstName =  new SimpleStringProperty(scoutData.elementAt(0));
        lastName =  new SimpleStringProperty(scoutData.elementAt(1));
        middleName =  new SimpleStringProperty(scoutData.elementAt(2));
        dateOfBirth =  new SimpleStringProperty(scoutData.elementAt(4));
        phoneNumber =  new SimpleStringProperty(scoutData.elementAt(5));
        email =  new SimpleStringProperty(scoutData.elementAt(6));
        troopID =  new SimpleStringProperty(scoutData.elementAt(7));
        status =  new SimpleStringProperty(scoutData.elementAt(8));
    }

    //----------------------------------------------------------------------------
    public String getFirstName() {
        return firstName.get();
    }

    //----------------------------------------------------------------------------
    public void setFirstName(String fn) {
        firstName.set(fn);
    }

    //----------------------------------------------------------------------------
    public String getLastName() {
        return lastName.get();
    }

    //----------------------------------------------------------------------------
    public void setLastName(String ln) {
        lastName.set(ln);
    }

    //----------------------------------------------------------------------------
    public String getMiddleName() {
        return middleName.get();
    }

    //----------------------------------------------------------------------------
    public void setMiddleName(String mn) {
        middleName.set(mn);
    }

    //----------------------------------------------------------------------------
    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    //----------------------------------------------------------------------------
    public void setDateOfBirth(String dob)
    {
        dateOfBirth.set(dob);
    }
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    //----------------------------------------------------------------------------
    public void setPhoneNumber(String number) {
        phoneNumber.set(number);
    }

    //----------------------------------------------------------------------------
    public String getEmail() {
        return email.get();
    }

    //----------------------------------------------------------------------------
    public void setEmail(String em) {
        email.set(em);
    }

    //----------------------------------------------------------------------------
    public String getTroopID() {
        return troopID.get();
    }

    //----------------------------------------------------------------------------
    public void setTroopID(String troop) {
        troopID.set(troop);
    }

    //----------------------------------------------------------------------------
    public String getStatus() {
        return status.get();
    }

    //----------------------------------------------------------------------------
    public void setStatus(String stat) {
        status.set(stat);
    }

}
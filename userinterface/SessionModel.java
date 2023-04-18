package userinterface;
import java.util.Vector;
import javafx.beans.property.SimpleStringProperty;
public class SessionModel {
    private final SimpleStringProperty sessionID;
    private final SimpleStringProperty startDate;
    private final SimpleStringProperty startTime;
    private final SimpleStringProperty endTime;
    private final SimpleStringProperty startingCash;
    private final SimpleStringProperty endingCash;
    private final SimpleStringProperty totalCheckTransactionAmount;
    private final SimpleStringProperty notes;
    public SessionModel(Vector<String> SessionData) {
        sessionID =  new SimpleStringProperty(SessionData.elementAt(0));
        startDate =  new SimpleStringProperty(SessionData.elementAt(1));
        startTime =  new SimpleStringProperty(SessionData.elementAt(2));
        endTime =  new SimpleStringProperty(SessionData.elementAt(4));
        startingCash =  new SimpleStringProperty(SessionData.elementAt(5));
        endingCash =  new SimpleStringProperty(SessionData.elementAt(6));
        totalCheckTransactionAmount =  new SimpleStringProperty(SessionData.elementAt(7));
        notes =  new SimpleStringProperty(SessionData.elementAt(8));
    }
    //----------------------------------------------------------------------------
    public String getSessionID() {
        return sessionID.get();
    }
    public void setSessionID(String fn) {
        sessionID.set(fn);
    }
    //----------------------------------------------------------------------------
    public String getStartDate() {
        return startDate.get();
    }
    public void setStartDate(String fn) {
        startDate.set(fn);
    }
    //----------------------------------------------------------------------------
    public String getStartTime() {
        return startTime.get();
    }
    public void setStartTime(String fn) {
        startTime.set(fn);
    }
    //----------------------------------------------------------------------------
    public String getEndTime() {
        return endTime.get();
    }
    public void setEndTime(String fn) {
        endTime.set(fn);
    }
    //----------------------------------------------------------------------------
    public String getStartingCash() {
        return startingCash.get();
    }
    public void setStartingCash(String fn) {
        startingCash.set(fn);
    }
    //----------------------------------------------------------------------------
    public String getEndingCash() {
        return endingCash.get();
    }
    public void setEndingCash(String fn) {
        endingCash.set(fn);
    }
    //----------------------------------------------------------------------------
    public String getTotalCheckTransactionAmount() {
        return totalCheckTransactionAmount.get();
    }
    public void setTotalCheckTransactionAmount(String fn) {
        totalCheckTransactionAmount.set(fn);
    }
    //----------------------------------------------------------------------------
    public String getNotes() {
        return notes.get();
    }
    public void setNotes(String fn) {
        notes.set(fn);
    }
}
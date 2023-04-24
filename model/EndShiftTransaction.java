package model;
import javafx.scene.Scene;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import exception.InvalidPrimaryKeyException;
import userinterface.View;
import userinterface.ViewFactory;
public class EndShiftTransaction extends Transaction {
    private double totalCheckSales;
    private double endCash;
    private String transactionErrorMessage = "";
    private String accountUpdateStatusMessage = "";
    protected Session mySession;
    public EndShiftTransaction() {
        super();
    }
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelDeposit", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");
        dependencies.setProperty("RegisterTreeInfo", "TransactionError");
        dependencies.setProperty("UpdateTreeFormView", "TransactionError");
        dependencies.setProperty("EndShift", "TransactionError");
        myRegistry.setDependencies(dependencies);
    }
    private void endCashTransaction() throws InvalidPrimaryKeyException {
        Session currentSession = new Session();
        String sessionID = (String)currentSession.getState("sessionID");
        TransactionCollection transactions = new TransactionCollection();
        transactions.retrieveSession(sessionID);
        double startCash = Double.parseDouble((String)currentSession.getState("startingCash"));
        double cashSales = (double)transactions.getState("cashSales");
        endCash = startCash + cashSales;
        totalCheckSales = (double)transactions.getState("checkSales");
    }
    public void processTransaction(Properties props) {
        try {
            String sessionID= getSessionID();
            mySession= new Session(sessionID);
            String startDate = (String) mySession.getState("startDate");
            props.setProperty("startDate", startDate);
            //-------
            String startTime = (String) mySession.getState("startTime");
            props.setProperty("startTime", startTime);
            //-------
            String endTime = (String) mySession.getState("endTime");
            props.setProperty("endTime", endTime);
            //-------
            String startingCash = (String) mySession.getState("startingCash");
            props.setProperty("startingCash", startingCash);
            //-------
            String endingCash = (String) mySession.getState("endingCash");
            props.setProperty("endingCash", endingCash);
            //-------
            String totalCheckTransactionAmount = (String) mySession.getState("totalCheckTransactionAmount");
            props.setProperty("totalCheckTransactionAmount", totalCheckTransactionAmount);
            //-------
            String notes = (String) mySession.getState("notes");
            props.setProperty("notes", notes);
            //-------
            endSession(endTime,endingCash,totalCheckTransactionAmount, notes);
        } catch(InvalidPrimaryKeyException e){
            transactionErrorMessage="Error cannot do this 2.";
        }
    }
    public Object getState(String key) {
        switch (key) {
            case "TransactionError":
                return transactionErrorMessage;
            case "UpdateStatusMessage":
                return accountUpdateStatusMessage;
            case "sessionID":
                if (mySession != null) {
                    return mySession.getState("sessionID");
                } else {
                    return "Undefined";
                }
            case "startDate":
                if (mySession != null) {
                    return mySession.getState("startDate");
                } else {
                    return "Undefined";
                }
            case "startTime":
                if (mySession != null) {
                    return mySession.getState("startTime");
                } else {
                    return "Undefined";
                }
            case "endTime":
                if (mySession != null) {
                    return mySession.getState("endTime");
                } else {
                    return "Undefined";
                }
            case "startingCash":
                if (mySession != null) {
                    return mySession.getState("startingCash");
                } else {
                    return "Undefined";
                }
            case "endingCash":
                if (mySession != null) {
                    return mySession.getState("endingCash");
                } else {
                    return "Undefined";
                }
            case "totalCheckTransactionAmount":
                if (mySession != null) {
                    return mySession.getState("totalCheckTransactionAmount");
                } else {
                    return "Undefined";
                }
            case "notes":
                if (mySession != null) {
                    return mySession.getState("notes");
                } else {
                    return "Undefined";
                }
        }
        return null;
    }
    private void endSession(String endTime, String endCash, String totalCheckSales, String notes) {
        if(notes.length() > 500) {
            stateChangeRequest("NotesTooLong", "");
            return;
        }
        //String endTime = Instant.now().atZone(ZoneId.of("America/New_York")).truncatedTo(ChronoUnit.MINUTES).toString();
        //int colon = endTime.indexOf(":");
        //endTime = endTime.substring(colon - 2, colon + 2);
        //--
        Properties props = new Properties();
        props.setProperty("endTime", endTime);
        props.setProperty("endingCash", String.valueOf(endCash));
        props.setProperty("totalCheckTransactionAmount", String.valueOf(totalCheckSales));
        props.setProperty("notes", notes);
        //--
        //mySession.update(props);
        stateChangeRequest("submitEndShift", props);
    }
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob")) {
            doYourJob();
        } else  if (key.equals("EndShiftDataViewTrans")) {
            processTransaction((Properties)value);
        }
        else  if (key.equals("submitEndShift")) {
            processUpdate((Properties)value);
        }
        myRegistry.updateSubscribers(key, this);
    }
    public void processUpdate(Properties props) {
        mySession = new Session(props);
        mySession.update();
        transactionErrorMessage = "Session Ended Successfully";
    }
    //------------------------------------------------------
    protected Scene createView() {
        Scene currentScene = myViews.get("EndShiftDataView");
        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("EndShiftDataView", this);
            assert newView != null;
            currentScene = new Scene(newView);
            myViews.put("EndShiftDataView", currentScene);
        }
        return currentScene;
    }
    public static String getSessionID()
    {
        try {
            Session session = new Session();
            String id = (String) session.getState("sessionID");
            return id;
        } catch (InvalidPrimaryKeyException exp) {
            return null;
        }
    }
}
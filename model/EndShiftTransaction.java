package model;
import javafx.scene.Scene;
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
            String sessionID= props.getProperty("sessionID");
            mySession= new Session(sessionID);
            //TransactionCollection.retrieve(String.valueOf(mySession));
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
            showEndShiftDataView();
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
        Scene currentScene = myViews.get("EndShiftView");
        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("EndShiftView", this);
            assert newView != null;
            currentScene = new Scene(newView);
            myViews.put("EndShiftView", currentScene);
        }
        return currentScene;
    }
    protected void showEndShiftDataView() {
        View newsView = ViewFactory.createView("EndShiftDataView", this);
        assert newsView != null;
        Scene newsScene = new Scene(newsView);
        swapToView(newsScene);
    }
}
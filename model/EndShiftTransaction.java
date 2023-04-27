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
        try {
            mySession = new Session("","");
            String sessionId = mySession.getSessionId();
            TransactionCollection transactions = new TransactionCollection();
            transactions.getAllTransactionsForSession(sessionId);
            transactions.computeAndSetTotalAmountTrans();
            double startCash = Double.parseDouble((String) mySession.getState("startingCash"));
            double cashSales = (double)transactions.getState("cashSales");
            endCash = startCash + cashSales;
            System.out.println(endCash);
            String x = Double.toString(endCash);
            totalCheckSales = (double)transactions.getState("checkSales");
            String y = Double.toString(totalCheckSales);
            mySession.stateChangeRequest("endingCash", x);
            mySession.stateChangeRequest("totalCheckTransactionAmount", y);
        }
        catch(InvalidPrimaryKeyException e){
            transactionErrorMessage="ERROR: No Open Shift.";
        }

    }
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelDeposit", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");
        //dependencies.setProperty("RegisterTreeInfo", "TransactionError");
        //dependencies.setProperty("UpdateTreeFormView", "TransactionError");
        dependencies.setProperty("EndShift", "TransactionError");
        myRegistry.setDependencies(dependencies);
    }

    public void processTransaction(Properties props) {
        System.out.println("Process");

        String sessionId = mySession.getSessionId();
            props.setProperty("sessionID", sessionId);

            String t = props.getProperty("endTime");
            mySession.stateChangeRequest("endTime", t);
            String n = props.getProperty("notes");
            mySession.stateChangeRequest("notes", n);

            //System.out.println(mySession.dependencies.getProperty("endTime"));
            mySession.update();
            transactionErrorMessage = "Shift closed Successfully";

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
        } else  if (key.equals("EndShift")) {
            System.out.println("stateChange");
            processTransaction((Properties)value);
        }
        myRegistry.updateSubscribers(key, this);
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

    public static String getSessionID(char c) {
        try {
            Session session = new Session();
            String id = (String) session.getState("sessionID");
            System.out.println("The id is " +id);
            String startTime = (String) session.getState("startTime");
            String startDate = (String) session.getState("startDate");
            String startCash = (String) session.getState("startingCash");
            System.out.println("ID is " + id + "starting cash is " + startCash + "start time is " + startTime);
            if (c == 'i')
                return id;
            else if (c == 'c')
                return startCash;
            else if (c == 't')
                return startTime;
            else if (c == 'd')
                return startDate;
        } catch (InvalidPrimaryKeyException exp) {
            return null;
        }
        return null;
    }
}
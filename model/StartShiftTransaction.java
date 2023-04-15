package model;

// system imports
import javafx.scene.Scene;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the Start Shift for the Tree Sales application */
//==============================================================
public class StartShiftTransaction extends Transaction {

    private Scout myScout;
    private ScoutCollection myScoutCollection;

    // GUI Components

    private String transactionErrorMessage = "";


    /**
     * Constructor for this class.
     */
    //----------------------------------------------------------
    public StartShiftTransaction() {
        super();
    }


    //----------------------------------------------------------
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelRegisterScout", "CancelTransaction");
        dependencies.setProperty("RegisterWithScoutInfo", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }


    //----------------------------------------------------------
    public void processTransaction(Properties props) {
        String troopID = props.getProperty("troopID");
        try {
            Scout scout = new Scout(troopID);
            transactionErrorMessage = "ERROR: Scout with troopID: " + troopID + " already exists!";
        } catch (InvalidPrimaryKeyException ex) {
            myScout = new Scout(props);
            myScout.update();

            transactionErrorMessage += myScout.getState("UpdateStatusMessage");

        }


    }

    //-----------------------------------------------------------
    public Object getState(String key) {
        if (key.equals("TransactionError") == true) {
            return transactionErrorMessage;
        }

        return null;
    }

    //-----------------------------------------------------------
    public void stateChangeRequest(String key, Object value) {
        // DEBUG System.out.println("DepositTransaction.sCR: key: " + key);

        if (key.equals("DoYourJob") == true) {
            doYourJob();
        } else if (key.equals("RegisterWithScoutInfo") == true) {
            processTransaction((Properties) value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    /**
     * Create the view of this class. And then the super-class calls
     * swapToView() to display the view in the frame
     */
    //------------------------------------------------------
    protected Scene createView() {
        Scene currentScene = myViews.get("StartShiftTransactionView");

        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("StartShiftTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("StartShiftTransactionView", currentScene);

            return currentScene;
        } else {
            return currentScene;
        }
    }
}
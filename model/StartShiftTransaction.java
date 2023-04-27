package model;

import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import event.Event;
import exception.InvalidPrimaryKeyException;
import userinterface.StartShiftTransactionView;
import userinterface.View;
import userinterface.ViewFactory;

public class StartShiftTransaction extends Transaction {
    private Session mySession;
    private ScoutCollection myFullScoutCollection;
    private ScoutCollection myChosenScoutCollection;
    private HashMap<String, String> scoutNameToID = new HashMap<>();
    private ArrayList<String> listOfScoutNames = new ArrayList<>();
    private Scout myChosenScout;
    private Shift myShift;
    private String transactionErrorMessage = "";

    public StartShiftTransaction() {
        super();
        myFullScoutCollection = new ScoutCollection();
        myFullScoutCollection.findAllScouts();
        for (int cnt = 0; cnt < myFullScoutCollection.getSize(); cnt++) {
            Vector<Scout> scoutList = (Vector)myFullScoutCollection.getState("Scouts");
            Scout scout = scoutList.elementAt(cnt);
            String scoutID = scout.getState("scoutID").toString();
            String scoutName = scout.getState("firstName").toString() + " " + scout.getState("lastName").toString() + " " + scout.getState("troopID").toString();
            scoutNameToID.put(scoutName, scoutID); // Store the mapping between scout name and ID
            listOfScoutNames.add(scoutName);
        }
        myChosenScoutCollection = new ScoutCollection();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelShift", "CancelTransaction");
        dependencies.setProperty("OpenSession", "TransactionError");
        dependencies.setProperty("StartShiftForScouts", "ScoutAddedForShift");

        myRegistry.setDependencies(dependencies);
    }

    public void processTransaction(Properties sessionProps) {
        sessionProps.setProperty("endingCash", "");
        sessionProps.setProperty("totalCheckTransactionAmount", "");
        sessionProps.setProperty("notes", "");
        mySession = new Session(sessionProps);
        mySession.update();

        transactionErrorMessage += mySession.getState("UpdateStatusMessage");
    }

    private void startShiftForScouts(Properties shiftProps) {
       // if (sessionIsOpen() == true) {
            String scoutID = (String) shiftProps.getProperty("scoutID");
            myChosenScout = myFullScoutCollection.retrieve(scoutID);
            myChosenScoutCollection.addChosenScout(myChosenScout);
            String sessionID = (String) mySession.getState("sessionID");
            shiftProps.setProperty("sessionID", sessionID);
            Shift newShift = new Shift(shiftProps);
            newShift.update();
        }
       // else
           // transactionErrorMessage = "There is not an open session";
    //}

    private boolean sessionIsOpen() {
        String session = ((String) mySession.getState("sessionID"));
        if(session.length()==0)
            return false;
        else
            return true;
    }

    public Object getState(String key) {
        if (key.equals("TransactionError")) {
            return transactionErrorMessage;
        }
        else  if (key.equals("ChosenScouts")) {
            return myChosenScoutCollection;
        }
        else  if (key.equals("ListOfAllScouts")) {
                return listOfScoutNames;
        }
        else  if (key.equals("ListOfAllScoutsMappedToID")) {
            return scoutNameToID;
        }

        return null;
    }

    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob")) {
            doYourJob();
        } else if (key.equals("OpenSession")) {
            processTransaction((Properties) value);
        } else if (key.equals("StartShiftForScouts")) {
            startShiftForScouts((Properties) value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    protected Scene createView() {
        Scene currentScene = myViews.get("StartShiftTransactionView");

        if (currentScene == null) {
            View newView = ViewFactory.createView("StartShiftTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("StartShiftTransactionView", currentScene);

            return currentScene;
        } else {
            return currentScene;
        }
    }
}

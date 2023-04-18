// specify the package
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
public class StartShiftTransaction extends Transaction
{

    private Session mySession;
    private ScoutCollection myScoutCollection;
    private Scout myScout;
    private Shift myShift;

    // GUI Components

    private String transactionErrorMessage = "";


    /**
     * Constructor for this class.
     *
     *
     */
    //----------------------------------------------------------
    public StartShiftTransaction()
    {
        super();
    }




    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelShift", "CancelTransaction");
        dependencies.setProperty("OpenSession", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }


    //----------------------------------------------------------
    public void processTransaction(Properties sessionProps)
    {
            sessionProps.setProperty("endingCash", "");
            sessionProps.setProperty("totalCheckTransactionsAmount", "");
            sessionProps.setProperty("notes", "");
            mySession = new Session(sessionProps);
            mySession.update();

            transactionErrorMessage += mySession.getState("UpdateStatusMessage");

    }

    private void startShiftForScouts(Properties shiftProps) {
        System.out.println("I've reached startshift method");
        String sessionID = (String) mySession.getState("sessionID");
        System.out.println(sessionID);
        shiftProps.setProperty("sessionID",sessionID);
        Shift newShift = new Shift(shiftProps);
        newShift.update();

    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("TransactionError") == true)
        {
            return transactionErrorMessage;
        }

        return null;
    }

    //-----------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        // DEBUG System.out.println("DepositTransaction.sCR: key: " + key);

        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("OpenSession")==true)
        {
            processTransaction((Properties)value);
        }
        else if (key.equals("StartShiftForScouts") == true) {
            startShiftForScouts((Properties) value);
        }
        else
            if(key.equals("ScoutSelectedForShift")==true) {
                String scoutID = (String) value;
                //Properties props =(Properties)value;
                //String scoutID = props.getProperty("scoutID");
                myScout = myScoutCollection.retrieve(scoutID);
            }

        myRegistry.updateSubscribers(key, this);
    }

    /**
     * Create the view of this class. And then the super-class calls
     * swapToView() to display the view in the frame
     */
    //------------------------------------------------------
    protected Scene createView()
    {
        Scene currentScene = myViews.get("StartShiftTransactionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("StartShiftTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("StartShiftTransactionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------




}
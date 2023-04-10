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

/** The class containing the DepositTransaction for the ATM application */
//==============================================================
public class AddTreeTypeTransaction extends Transaction
{

    private TreeType myTreeType;

    // GUI Components

    private String transactionErrorMessage = "";


    /**
     * Constructor for this class.
     *
     *
     */
    //----------------------------------------------------------
    public AddTreeTypeTransaction()
    {
        super();
    }




    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelAddTreeType", "CancelTransaction");
        dependencies.setProperty("AddTreeTypeWithInfo", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * This method encapsulates all the logic of adding a Tree Type with the barcode prefix
     */
    //----------------------------------------------------------
    public void processTransaction(Properties props)
    {
        String barcodePrefix = props.getProperty("barcodePrefix");
        try {
            TreeType tt = new TreeType(barcodePrefix);
            transactionErrorMessage = "ERROR: Tree Type with prefix: " + barcodePrefix + " already exists!";
        }
        catch (InvalidPrimaryKeyException ex) {
            myTreeType = new TreeType(props);
            myTreeType.update();

            transactionErrorMessage = "Tree Type successfully added";

        }


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
        if (key.equals("AddTreeTypeWithInfo")==true)
        {
            processTransaction((Properties)value);
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
        Scene currentScene = myViews.get("AddTreeTypeTransactionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AddTreeTypeTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("AddTreeTypeTransactionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------




}
// specify the package
package model;

// system imports
import javafx.scene.Scene;
import java.util.Properties;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the DepositTransaction for the ATM application */
//==============================================================
public class RemoveTreeTransaction extends Transaction
{

    private Account myAccount;
    private String depositAmount; // needed for GUI only

    // GUI Components

    private String transactionErrorMessage = "";
    private String accountUpdateStatusMessage = "";

    /**
     * Constructor for this class.
     *
     *
     */
    //----------------------------------------------------------
    public RemoveTreeTransaction()
            throws Exception
    {
        super();
    }

    /**
     * Constructor for this class.
     * <p>
     * Transaction remembers all the account IDs for this customer.
     * It uses AccountCatalog to create this list of account IDs.
     */


    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelDeposit", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");
        dependencies.setProperty("AccountNumber", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * This method encapsulates all the logic of creating the account,
     * verifying ownership, crediting, etc. etc.
     */
    //----------------------------------------------------------
    public void processTransaction(Properties props)
    {

        if (props.getProperty("barcode") != null)
        {

            createAndShowTreeCollectionView();

        }
    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("TransactionError") == true)
        {
            return transactionErrorMessage;
        }
        else
        if (key.equals("UpdateStatusMessage") == true)
        {
            return accountUpdateStatusMessage;
        }
        else
        if (key.equals("AccountNumberList") == true)
        {
            return myAccountIDs;
        }
        else
        if (key.equals("Account") == true)
        {
            return myAccount;
        }
        else
        if (key.equals("DepositAmount") == true)
        {
            return depositAmount;
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
        if (key.equals("RemoveTreeWithTreeInfo")==true)
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
        Scene currentScene = myViews.get("RemoveTreeTransactionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("RemoveTreeTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("RemoveTreeTransactionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------


    //------------------------------------------------------
    protected void createAndShowTreeCollectionView()
    {

        // create our initial view
        View newView = ViewFactory.createView("TreeCollectionView", this);
        Scene newScene = new Scene(newView);

        myViews.put("TreeCollectionView", newScene);

        // make the view visible by installing it into the frame
        swapToView(newScene);
    }

}
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
public class RemoveScoutTransaction extends Transaction
{

    private Account myAccount;
    private String depositAmount; // needed for GUI only

    // GUI Components

    private String transactionErrorMessage = "";
    private String accountUpdateStatusMessage = "";
    private String query = "";
    private static final String myTableName = "Scout";

    /**
     * Constructor for this class.
     *
     *
     */
    //----------------------------------------------------------
    public RemoveScoutTransaction()
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
        if (props.getProperty("AccountNumber") != null)
        {
            String accountNumber = props.getProperty("AccountNumber");
            try
            {

                myAccount = createAccount(accountNumber);
                boolean isOwner = myAccount.verifyOwnership(myCust);
                if (isOwner == false)
                {
                    transactionErrorMessage = "ERROR: Deposit Transaction: Not owner of selected account!!";
                    new Event(Event.getLeafLevelClassName(this), "processTransaction",
                            "Failed to verify ownership of account number : " + accountNumber + ".",
                            Event.ERROR);
                }
                else
                {
                    //createAndShowDepositAmountView();
                }
            }
            catch (InvalidPrimaryKeyException ex)
            {
                transactionErrorMessage = "ACCOUNT FAILURE: Contact bank immediately!!";
                new Event(Event.getLeafLevelClassName(this), "processTransaction",
                        "Failed to create account for number : " + accountNumber + ". Reason: " + ex.toString(),
                        Event.ERROR);

            }
        }
        else
        if (props.getProperty("Amount") != null)
        {
            String amount = props.getProperty("Amount");
            depositAmount = amount;

            myAccount.credit(amount);
            myAccount.update();
            accountUpdateStatusMessage = (String)myAccount.getState("UpdateStatusMessage");
            transactionErrorMessage = accountUpdateStatusMessage;

            createAndShowReceiptView();

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
        if (key.equals("RegisterWithScoutInfo")==true)
        {
            Properties props =(Properties)value;
            String fn = props.getProperty("firstName");
            String ln = props.getProperty("lastName");
            if ((fn.length()==0||fn==null)&&(ln.length()==0||ln==null))
            query = "SELECT * FROM "+myTableName;
            else if (fn==null || fn.length()==0)
            {
                query = "SELECT * FROM "+myTableName+" WHERE (lastName LIKE '%"+ln+"%')";
            }
            else if (ln==null || ln.length()==0)
            {
                query = "SELECT * FROM "+myTableName+" WHERE (firstName LIKE '%"+fn+"%')";
            }
            else
                query = "SELECT * FROM "+myTableName+" WHERE ((firstName LIKE '%"+fn+"%') AND (lastName LIKE '%"+ln+"%'))";

            ScoutCollection sc = new ScoutCollection();
            sc.helper(query);

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
        Scene currentScene = myViews.get("RegisterScoutTransactionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("RegisterScoutTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("RegisterScoutTransactionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------


    //------------------------------------------------------
    protected void createAndShowReceiptView()
    {

        // create our initial view
        View newView = ViewFactory.createView("DepositReceipt", this);
        Scene newScene = new Scene(newView);

        myViews.put("DepositReceipt", newScene);

        // make the view visible by installing it into the frame
        swapToView(newScene);
    }

}

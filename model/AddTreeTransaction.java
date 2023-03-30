package model;

import javafx.scene.Scene;
import java.util.Properties;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;

import userinterface.View;
import userinterface.ViewFactory;
public class AddTreeTransaction extends Transaction
{

    private Tree myTree;

    // GUI Components

    private String transactionErrorMessage = "";
    /**
     * Constructor for this class.
     *
     *
     */
    //----------------------------------------------------------
    public AddTreeTransaction()

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
        dependencies.setProperty("CancelTree", "CancelTransaction");
        dependencies.setProperty("AddTreeInfo", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * This method encapsulates all the logic of creating the account,
     * verifying ownership, crediting, etc. etc.
     */
    //----------------------------------------------------------
    public void processTransaction(Properties props)
    {

        String barcode = props.getProperty("barcode");
        try
        {
            Tree tree = new Tree(barcode);
            transactionErrorMessage = "ERROR: Tree with barcode: " + barcode + "Already exists!";
        }
        catch (InvalidPrimaryKeyException ex)
        {
            myTree = new Tree(props);
            myTree.update();
            transactionErrorMessage += myTree.getState("UpdateStatusMessage");
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


    public void stateChangeRequest(String key, Object value)
    {
        // DEBUG System.out.println("DepositTransaction.sCR: key: " + key);

        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("AddTreeInfo")==true)
        {
            processTransaction((Properties)value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    protected Scene createView()
    {
        Scene currentScene = myViews.get("AddTreeView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AddTreeView", this);
            currentScene = new Scene(newView);
            myViews.put("AddTreeView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }
}

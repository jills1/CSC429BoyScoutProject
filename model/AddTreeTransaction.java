package model;

import javafx.scene.Scene;
import impresario.IView;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;

import userinterface.View;
import userinterface.ViewFactory;
public class AddTreeTransaction extends Transaction
{

    private Tree myTree;
    protected Properties dependencies;

    // GUI Components

    private String transactionMessage = "";
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
        String barcodePrefix = barcode.substring(0,2);
        System.out.println("Extracted barcode prefix: " + barcodePrefix);
        try
        {
            Tree t = new Tree(barcode);
        }
        catch (InvalidPrimaryKeyException ex)
        {
            try {
                TreeType treeType = new TreeType(barcodePrefix);
                String treeTypeID = (String)treeType.getState("treeTypeID");
                props.setProperty("treeType", treeTypeID);
                myTree = new Tree(props);
                myTree.setOldFlag(false);
                myTree.update();
                transactionMessage ="Tree Successfully added";

            }
            catch (InvalidPrimaryKeyException excep)
            {

                transactionMessage = "ERROR: Invalid barcode, no associated tree type found!";
            }

        }
    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("TransactionError") == true)
        {
            return transactionMessage;
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
            Properties p = (Properties)value;
            processTransaction(p);
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

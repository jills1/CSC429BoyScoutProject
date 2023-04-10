// specify the package
package model;

// system imports
import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

// project imports

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

    protected  Tree myTree;
    /**
     * Constructor for this class.
     *
     *
     */
    //----------------------------------------------------------
    public RemoveTreeTransaction()

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
        dependencies.setProperty("searchTree", "TransactionError");

        dependencies.setProperty("RemoveTreeWithTreeInfo", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * This method encapsulates all the logic of creating the account,
     * verifying ownership, crediting, etc. etc.
     */
    //----------------------------------------------------------
    public void processTransaction(Properties props){
        System.out.println("I'm getting to processTransaction");
        System.out.println("I'm getting into try");
        String barcode= props.getProperty("barcode");
        if(myTree.getState("status").equals("Sold")){
            transactionErrorMessage="Error: its has been sold";
            return;
        }
        System.out.println("i'm getting to myTree.deleteStateInDatabase");
        myTree.deleteStateInDatabase();
        transactionErrorMessage=(String)myTree.getState("UpdateStatusMessage");
    }
    //-----------------------------------------------------------
    public void searchTree(Properties props){

        try{

            String barcode= props.getProperty("barcode");
            myTree= new Tree(barcode);
            String treeBarcode=(String)myTree.getState("barcode");
            System.out.println("tree barcode"+ treeBarcode);
            props.setProperty("barcode", treeBarcode);
            String treeStatus=(String)myTree.getState("status");
            props.setProperty("status", treeStatus);
            System.out.println("status: "+treeStatus);
            String treeNotes=(String)myTree.getState("notes");
            props.setProperty("notes", treeNotes);
            transactionErrorMessage="Click Submit to delete tree";

            createView2();

        } catch(InvalidPrimaryKeyException e){
            System.out.println("getting to catch");
            transactionErrorMessage="Error this barcode does not exist.";
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

        if (key.equals("barcode") == true)
        {
            if (myTree != null) {
                return myTree.getState("barcode");
            }
            else
                return "Undefined";
        }
        else
        if (key.equals("status") == true)
        {
            if (myTree != null) {
                return myTree.getState("status");
            }
            else
                return "Undefined";
        }
        else
        if (key.equals("notes") == true)
        {
            if (myTree != null) {
                return myTree.getState("notes");
            }
            else
                return "Undefined";
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
        else
        if (key.equals("searchTree")==true)
        {
            searchTree((Properties)value);
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
    protected void  createView2()

    {


        View newView = ViewFactory.createView("RemoveView", this);
        Scene newScene = new Scene(newView);

        myViews.put("RemoveView", newScene);

        // make the view visible by installing it into the frame
        swapToView(newScene);
    }


    //------------------------------------------------------

    


}
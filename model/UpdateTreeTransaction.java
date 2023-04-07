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

/**j The class containing the Update Tree Transaction for the Tree Sales application */
//==============================================================
public class UpdateTreeTransaction extends Transaction
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
    public UpdateTreeTransaction()

    {
        super();
    }




    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelUpdate", "CancelTransaction");
        dependencies.setProperty("searchTree", "transactionErrorMessage");

        dependencies.setProperty("RemoveTreeWithTreeInfo", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * This method encapsulates all the logic of creating the account,
     * verifying ownership, crediting, etc. etc.
     */
    //----------------------------------------------------------
    public void processTransaction(Properties props){
        try {
            String barcode = props.getProperty("barcode");
            String barcodePrefix = barcode.substring(0, 2);
            String date = String.valueOf(java.time.LocalDateTime.now());
            String dUS = date.substring(0,10);
            System.out.println("getting to processTransaction");

            String treeStatus = props.getProperty("status");
            myTree.stateChangeRequest("status", treeStatus);
            System.out.println("The new status is " + treeStatus);
            TreeType treeType = new TreeType(barcodePrefix);
            String treeTypeID = (String) treeType.getState("treeTypeID");
            props.setProperty("treeType",treeTypeID);
            myTree.stateChangeRequest("treeType", treeTypeID);
            props.setProperty("dateStatusUpdate", dUS);
            myTree.stateChangeRequest("dateStatusUpdate", dUS);

            myTree = new Tree(props);

            myTree.setOldFlag(false);

            myTree.update();
        }
        catch (InvalidPrimaryKeyException excep)
        {

            transactionErrorMessage = "ERROR: Invalid barcode, no associated tree type found!";
        }



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
            createAndShowSelectedScoutToUpdateView();

        } catch(InvalidPrimaryKeyException e){
            transactionErrorMessage="No Tree with this barcode.";
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
        if (key.equals("UpdateTreeWithTreeInfo")==true)
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
        Scene currentScene = myViews.get("UpdateTreeTransactionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("UpdateTreeTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("UpdateTreeTransactionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected void  createAndShowSelectedScoutToUpdateView()

    {


        View newView = ViewFactory.createView("SelectedScoutToUpdateView", this);
        Scene newScene = new Scene(newView);

        myViews.put("SelectedScoutToUpdateView", newScene);

        // make the view visible by installing it into the frame
        swapToView(newScene);
    }


    //------------------------------------------------------



}
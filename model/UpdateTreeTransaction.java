package model;
import javafx.scene.Scene;
import java.util.Properties;
import exception.InvalidPrimaryKeyException;

// project imports

import userinterface.View;
import userinterface.ViewFactory;
//==============================================================
public class UpdateTreeTransaction extends Transaction {
    private Account myAccount;
    private String depositAmount;
    private String transactionErrorMessage = "";
    private String accountUpdateStatusMessage = "";
    private String barcode;
    private String treeType;
    private String notes;
    private String status;
    private String dateStatusUpdate;
    protected  Tree myTree;
    //----------------------------------------------------------
    public UpdateTreeTransaction() {
        super();
    }
    //----------------------------------------------------------
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelDeposit", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");
        dependencies.setProperty("AccountNumber", "TransactionError");
        myRegistry.setDependencies(dependencies);
    }
    //----------------------------------------------------------
    public void processTransaction(Properties props) {
        try {
            String barcode= props.getProperty("barcode");
            myTree= new Tree(barcode);
            String treeType = (String) myTree.getState("treeType");
            props.setProperty("treeType", treeType);
            //-------
            String treeStatus = (String) myTree.getState("status");
            props.setProperty("status", treeStatus);
            //-------
            String treeNotes = (String) myTree.getState("notes");
            props.setProperty("notes", treeNotes);
            //-------
            String treeDateStatusUpdate = (String) myTree.getState("dateStatusUpdate");
            props.setProperty("dateStatusUpdate", treeDateStatusUpdate);
            //-------
            createAndShowUpdateTreeFormTransactionView();
        } catch(InvalidPrimaryKeyException e){
                transactionErrorMessage="Error cannot do this 2.";
        }
    }
    //-----------------------------------------------------------
    public Object getState(String key) {
        if (key.equals("TransactionError") == true) {
            return transactionErrorMessage;
        } else if (key.equals("UpdateStatusMessage") == true) {
            return accountUpdateStatusMessage;
        } else if (key.equals("treeType") == true) {
            if (myTree != null) {
                return myTree.getState("treeType");
            } else {
                return "Undefined";
            }
        } else if (key.equals("notes") == true) {
            if (myTree != null) {
                return myTree.getState("notes");
            } else {
                return "Undefined";
            }
        } else if (key.equals("status") == true) {
            if (myTree != null) {
                return myTree.getState("status");
            } else {
                return "Undefined";
            }
        } else if (key.equals("dateStatusUpdate") == true) {
            if (myTree != null) {
                return myTree.getState("dateStatusUpdate");
            } else {
                return "Undefined";
            }
        }
        return null;
    }
    //-----------------------------------------------------------
    //State Change
    public void stateChangeRequest(String key, Object value) {
        // DEBUG System.out.println("DepositTransaction.sCR: key: " + key);
        if (key.equals("DoYourJob") == true) {
            doYourJob();
        } else  if (key.equals("UpdateTreeInfo")==true) {
            processTransaction((Properties)value);

        }
        myRegistry.updateSubscribers(key, this);
    }
    //------------------------------------------------------
    //Create View.java within view factory
    protected Scene createView() {
        Scene currentScene = myViews.get("UpdateTreeView");
        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("UpdateTreeView", this);
            currentScene = new Scene(newView);
            myViews.put("UpdateTreeView", currentScene);
            return currentScene;
        } else {
            return currentScene;
        }
    }
    //------------------------------------------------------
    protected void createAndShowUpdateTreeFormTransactionView() {
        View newView = ViewFactory.createView("UpdateTreeFormView", this);
        Scene newScene = new Scene(newView);
        //myViews.put("UpdateTreeFormTransactionView", newScene);
        swapToView(newScene);
        // return newScreen;
    }
    //------------------------------------------------------
    protected void createAndShowReceiptView() {
        // create our initial view
        View newView = ViewFactory.createView("DepositReceipt", this);
        Scene newScene = new Scene(newView);
        myViews.put("DepositReceipt", newScene);
        swapToView(newScene);
    }
}
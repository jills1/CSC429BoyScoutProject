package model;
import javafx.scene.Scene;
import java.util.Properties;
import exception.InvalidPrimaryKeyException;

// project imports

import userinterface.View;
import userinterface.ViewFactory;
//==============================================================
public class UpdateTreeTypeTransaction extends Transaction {
    private String transactionErrorMessage = "";
    private final String accountUpdateStatusMessage = "";
    protected  TreeType myTree;
    //----------------------------------------------------------
    public UpdateTreeTypeTransaction() {
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
            //String treeTypeID= props.getProperty("treeTypeID");
            String treeTypeID = (String) myTree.getState("treeTypeID");
            props.setProperty("treeTypeID", treeTypeID);
            myTree= new TreeType(treeTypeID);
            //-------
            String typeDescription = (String) myTree.getState("typeDescription");
            props.setProperty("typeDescription", typeDescription);
            //-------
            String cost = (String) myTree.getState("cost");
            props.setProperty("cost", cost);
            //-------
            String barcodePrefix = (String) myTree.getState("barcodePrefix");
            props.setProperty("barcodePrefix", barcodePrefix);
            //-------
            createAndShowUpdateTreeTypeFormView();
        } catch(InvalidPrimaryKeyException e){
            transactionErrorMessage="Error cannot do this 2.";
        }
    }
    //-----------------------------------------------------------
    public Object getState(String key) {
        switch (key) {
            case "TransactionError":
                return transactionErrorMessage;
            case "UpdateStatusMessage":
                return accountUpdateStatusMessage;
            case "treeTypeID":
                if (myTree != null) {
                    return myTree.getState("treeTypeID");
                } else {
                    return "Undefined";
                }
            case "typeDescription":
                if (myTree != null) {
                    return myTree.getState("typeDescription");
                } else {
                    return "Undefined";
                }
            case "cost":
                if (myTree != null) {
                    return myTree.getState("cost");
                } else {
                    return "Undefined";
                }
            case "barcodePrefix":
                if (myTree != null) {
                    return myTree.getState("barcodePrefix");
                } else {
                    return "Undefined";
                }
            default:
                return null;
        }
    }
    //-----------------------------------------------------------
    //State Change
    public void stateChangeRequest(String key, Object value) {
        // DEBUG System.out.println("DepositTransaction.sCR: key: " + key);
        if (key.equals("DoYourJob")) {
            doYourJob();
        } else  if (key.equals("UpdateTreeTypeFormView")) {
            processTransaction((Properties)value);

        }
        myRegistry.updateSubscribers(key, this);
    }
    //------------------------------------------------------
    //Create View.java within view factory
    protected Scene createView() {
        Scene currentScene = myViews.get("UpdateTreeTypeView");
        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("UpdateTreeTypeView", this);
            currentScene = new Scene(newView);
            myViews.put("UpdateTreeTypeView", currentScene);
            return currentScene;
        } else {
            return currentScene;
        }
    }
    //------------------------------------------------------
    protected void createAndShowUpdateTreeTypeFormView() {
        View newView = ViewFactory.createView("UpdateTreeTypeFormView", this);
        Scene newsScene = new Scene(newView);
        myViews.put("UpdateTreeTypeFormView", newsScene);
        swapToView(newsScene);
        // return newScreen;
    }
}
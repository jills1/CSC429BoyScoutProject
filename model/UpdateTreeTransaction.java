package model;
import javafx.scene.Scene;
import java.util.Properties;
import java.util.Vector;

// project imports

import userinterface.View;
import userinterface.ViewFactory;
//==============================================================
public class UpdateTreeTransaction extends Transaction {
    private Account myAccount;
    private String depositAmount;
    protected Vector<String> myTree;
    //protected Scout myScout;
    private String transactionErrorMessage = "";
    private String accountUpdateStatusMessage = "";
    private String barcode;
    private String treeType;
    private String notes;
    private String status;
    private String dateStatusUpdate;

    //----------------------------------------------------------
    public UpdateTreeTransaction() throws Exception {
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
    //Change View process Transaction
    public void processTransaction(Properties props) {
        createAndShowUpdateTreeFormTransactionView();
//        if (props.getProperty("AccountNumber") != null) {
//            String accountNumber = props.getProperty("AccountNumber");
//            try {
//                myAccount = createAccount(accountNumber);
//                boolean isOwner = myAccount.verifyOwnership(myCust);
//                if (isOwner == false) {
//                    transactionErrorMessage = "ERROR: Deposit Transaction: Not owner of selected account!!";
//                    new Event(Event.getLeafLevelClassName(this), "processTransaction", "Failed to verify ownership of account number : " + accountNumber + ".", Event.ERROR);
//                } else {
//                    //createAndShowDepositAmountView();
//                }
//            }
//            catch (InvalidPrimaryKeyException ex) {
//                transactionErrorMessage = "ACCOUNT FAILURE: Contact bank immediately!!";
//                new Event(Event.getLeafLevelClassName(this), "processTransaction", "Failed to create account for number : " + accountNumber + ". Reason: " + ex.toString(), Event.ERROR);
//            }
//        } else if (props.getProperty("Amount") != null) {
//            String amount = props.getProperty("Amount");
//            depositAmount = amount;
//            myAccount.credit(amount);
//            myAccount.update();
//            accountUpdateStatusMessage = (String)myAccount.getState("UpdateStatusMessage");
//            transactionErrorMessage = accountUpdateStatusMessage;
//            createAndShowReceiptView();
//        }
    }
    //-----------------------------------------------------------
    public Object getState(String key) {
        if (key.equals("TransactionError") == true) {
            return transactionErrorMessage;
        } else if (key.equals("UpdateStatusMessage") == true) {
            return accountUpdateStatusMessage;
        } else if (key.equals("AccountNumberList") == true) {
            return myAccountIDs;
        } else if (key.equals("Account") == true) {
            return myAccount;
        } else if (key.equals("DepositAmount") == true) {
            return depositAmount;
        } else if (key.equals("myTree") == true) {
            return myTree;
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
            String scoutID = (String)value;
            //Properties props =(Properties)value;
            //String scoutID = props.getProperty("scoutID");
            myTree = myTree.getClass(getEntryListView());
            createAndShowUpdateTreeFormTransactionView();
        }
        myRegistry.updateSubscribers(key, this);
    }
    //------------------------------------------------------
    //Create View.java within view factory
    protected Scene createView() {
        Scene currentScene = myViews.get("UpdateTreeTransactionView");
        if (currentScene == null) {
            // create our initial view
            View newView = ViewFactory.createView("UpdateTreeTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("UpdateTreeTransactionView", currentScene);
            return currentScene;
        } else {
            return currentScene;
        }
    }
    //------------------------------------------------------
//    protected void createAndShowUpdateTreeTransactionView() {
//        View newView = ViewFactory.createView("UpdateTreeTransactionView", this);
//        Scene newScene = new Scene(newView);
//        myViews.put("UpdateTreeTransactionView", newScene);
//        swapToView(newScene);
//    }
    protected void createAndShowUpdateTreeFormTransactionView() {
        View newView = ViewFactory.createView("UpdateTreeFormTransactionView", this);
        Scene newScene = new Scene(newView);
        //myViews.put("UpdateTreeFormTransactionView", newScene);
        swapToView(newScene);
        // return newScreen;
    }
    //------------------------------------------------------
    //Receipt
    protected void createAndShowReceiptView() {
        // create our initial view
        View newView = ViewFactory.createView("DepositReceipt", this);
        Scene newScene = new Scene(newView);
        myViews.put("DepositReceipt", newScene);
        // make the view visible by installing it into the frame
        swapToView(newScene);
    }
}
package model;

// system imports
import javafx.scene.Scene;
import java.util.Properties;

// project imports
import exception.InvalidPrimaryKeyException;


import userinterface.View;
import userinterface.ViewFactory;
public class SellTreeTransaction extends Transaction {

    protected Tree myTree;

    protected TreeType myTreeType;
    protected Session mySession;
    protected SellTransaction myTransaction;

    protected boolean sessionTest = true;
    // GUI Components

    private String transactionMessage = "";
    protected String sellTreeStatusMessage = "";

    private static final String myTableName = "Transaction";

    public SellTreeTransaction(){
        super ();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("SellTree", "SellATreeTransaction");
        dependencies.setProperty("CancelSale", "CancelTransaction");
        dependencies.setProperty("OK", "SellATreeTransaction");
        dependencies.setProperty("TreeChosen", "TransactionError");
        dependencies.setProperty("UpdateTreeInfo", "TransactionError");
        dependencies.setProperty("UpdateTransactionInfo", "TransactionError");
        //dependencies.setProperty("UpdateTransactionInfo", "TreeStatus");


        myRegistry.setDependencies(dependencies);
    }

    protected Scene createView() {
        Scene currentScene = myViews.get("SellTreeTransactionView");

        if (currentScene == null)
        {
            View newView = ViewFactory.createView("SellTreeTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("SellTreeTransactionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    public Object getState(String key)
        {
            if (key.equals("TransactionError") == true)
            {
                return transactionMessage;
            }
            else
            if (key.equals("UpdateStatusMessage") == true)
            {
                return sellTreeStatusMessage;
            }
            else
            if (key.equals("TreeChosen") == true)
            {
                if (myTree == null) {
                    return null;
                } else
                    return myTree;
            }
            else
            if (myTree != null)
            {
                Object val = myTree.getState(key);
                if (val != null)
                {
                    return val;
                }
            }
            if (key.equals("status") == true)
            {
                if (myTree != null) {
                    return myTree.getState("status");
                }
                else
                    return "Undefined";
            }
            if (key.equals("notes") == true)
            {
                if (myTree != null) {
                    return myTree.getState("notes");
                }
                else
                    return "Undefined";
            }
            if (key.equals("cost") == true)
            {
                if (myTreeType != null) {
                    return myTreeType.getState("cost");
                }
                else
                    return "Undefined";
            }
            /*if (key.equals("sessionTest") == true)
            {
                sessionTest();
                return sessionTest;
            }*/

            return null;
    }

    public void stateChangeRequest(String key, Object value) {

        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("SubmitSellTree")==true)
        {
            Properties props =(Properties)value;
            try {
                myTree = getTreeFromBarcode(props);
            }
            catch (Exception ex)
            {
                transactionMessage = "Error getting Tree";
            }


            try
            {
            }
            catch (Exception ex)
            {
                transactionMessage = "Error getting Tree";
            }

        }
        if (key.equals("SubmitSellTreeType")==true) {
            Properties props = (Properties) value;
            try {
                myTreeType = getTreeTypeFromBarcode(props);
            }
            catch (Exception ex) {
                transactionMessage = "Error getting TreeType";
            }


            try {

            } catch (Exception ex) {
                transactionMessage = "Error getting Tree";
            }
        }
        else
        if (key.equals("TreeChosen") == true)
        {
            String barcode = (String)value;

        }
        else if  (key.equals("UpdateTransactionInfo") == true)
        {
            processTransaction((Properties)value);
        }
        else if  (key.equals("UpdateTreeInfo") == true)
        {
            processTree((Properties)value);
        }
        myRegistry.updateSubscribers(key, this);


    }

    public Tree getTreeFromBarcode(Properties value) throws InvalidPrimaryKeyException {
        String tempBarcode = (String)value.getProperty("barcode");
        Tree theTree = new Tree(tempBarcode);

        return(theTree);
    }
    public TreeType getTreeTypeFromBarcode(Properties value) throws InvalidPrimaryKeyException {
        String tempBarcode = (String)value.getProperty("barcodePrefix");
         TreeType theTreeType = new TreeType(tempBarcode);

        return(theTreeType);
    }

    public void processTree(Properties props) {
            myTree= new Tree(props);
           myTree.update();
    }


    public void processTransaction(Properties props) {
        try {
            mySession= new Session("","");
            String sessionId = mySession.getSessionId();
                props.setProperty("sessionID", sessionId);
                myTransaction = new SellTransaction(props);
                props.setProperty("transactionType", "Tree Sale");
                //-------------------------------------
                String treeBarcode = props.getProperty("barcode");
                props.setProperty("barcode", treeBarcode);
                //---------------------------------------
                String paymentMethod = props.getProperty("paymentMethod");
                props.setProperty("paymentMethod", paymentMethod);
                //---------------------------------------
                String costEntered = props.getProperty("transactionAmount");
                props.setProperty("transactionAmount", costEntered);
                //---------------------------------------
                String custNameEntered = props.getProperty("customerName");
                props.setProperty("customerName", custNameEntered);
                //----------------------------------------
                String custPhoneEntered = props.getProperty("customerPhone");
                props.setProperty("customerPhone", custPhoneEntered);
                //----------------------------------------
                String custEmailEntered = props.getProperty("customerEmail");
                props.setProperty("customerEmail", custEmailEntered);
                //-----------------------------------------
                String transactionDate = props.getProperty("transactionDate");
                props.setProperty("transactionDate", transactionDate);
                //------------------------------------------
                String transactionTime = props.getProperty("transactionTime");
                props.setProperty("transactionTime", transactionTime);
                //------------------------------------------
                String dateStatusUpdated = props.getProperty("dateStatusUpdate");
                props.setProperty("dateStatusUpdate", dateStatusUpdated);
                //-------------------------------------------------------

                myTransaction = new SellTransaction(props);

                props.setProperty("sessionID", sessionId);

                //------
                myTransaction.update();
            transactionMessage += myTransaction.getState("UpdateStatusMessage");
            //}
        } catch(InvalidPrimaryKeyException e){
            transactionMessage="ERROR: No Open Session.";
        }
    }
}

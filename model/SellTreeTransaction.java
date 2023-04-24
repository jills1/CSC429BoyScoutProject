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
    // GUI Components

    protected String transactionErrorMessage = "";
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
        //dependencies.setProperty("SearchScoutInfo", "TransactionError");
        dependencies.setProperty("TreeChosen", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    protected Scene createView() {
    System.out.println( "getting to createView");
        Scene currentScene = myViews.get("SellTreeTransactionView");

        if (currentScene == null)
        {
            // create our initial view
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
                return transactionErrorMessage;
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

            return null;
    }

    public void stateChangeRequest(String key, Object value) {
        // DEBUG System.out.println("DepositTransaction.sCR: key: " + key);

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
                transactionErrorMessage = "Error getting Tree";
            }


            try
            {

                //processTransaction(props);
            }
            catch (Exception ex)
            {
                transactionErrorMessage = "Error getting Tree";
            }

        }
        if (key.equals("SubmitSellTreeType")==true) {
            Properties props = (Properties) value;
            try {
                myTreeType = getTreeTypeFromBarcode(props);
            }
            catch (Exception ex) {
                transactionErrorMessage = "Error getting TreeType";
            }


            try {

                //processTransaction(props);
            } catch (Exception ex) {
                transactionErrorMessage = "Error getting Tree";
            }
        }
        else
        if (key.equals("TreeChosen") == true)
        {
            String barcode = (String)value;
            //myTree = ;

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
       try {
            Properties props3 = new Properties();
            String barcode= props.getProperty("barcode");
            myTree= new Tree(barcode);
            //-------------------------------------
            props.setProperty("barcode", barcode);
            //--------------------------------------
            String treeStatus = (String) myTree.getState("status");
            props.setProperty("status", "Sold");
            //-------
            String treeNotes = (String) myTree.getState("notes");
            props.setProperty("notes", treeNotes);
            //-------
            String treeDateStatusUpdate = (String) myTree.getState("dateStatusUpdate");
            props.setProperty("dateStatusUpdate", treeDateStatusUpdate);
            //------
           myTree.update();
        } catch(InvalidPrimaryKeyException e){
            transactionErrorMessage="Error cannot do this 3.";
        }
    }
    public void processTransaction(Properties props) {
        try {
            //Properties props3 = new Properties();
            mySession= new Session("","");
            String sessionId = mySession.getSessionId();
            props.setProperty("sessionID", sessionId);
            myTransaction = new SellTransaction(props);
            props.setProperty("transactionType", "Tree Sale");
            //-------------------------------------
            String treeBarcode = props.getProperty("barcode");
            System.out.println("tree barcode is: "+treeBarcode);
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
            System.out.println("transaction date is: "+transactionDate);
            props.setProperty("transactionDate", transactionDate);
            //------------------------------------------
            String transactionTime = props.getProperty("transactionTime");
            props.setProperty("transactionTime", transactionTime);
            System.out.println("transaction time is: "+transactionTime);

            //------------------------------------------
            String dateStatusUpdated = props.getProperty("dateStatusUpdate");
            props.setProperty("dateStatusUpdate", dateStatusUpdated);
            System.out.println("date status update is: "+dateStatusUpdated);
           //-------------------------------------------------------

            myTransaction = new SellTransaction(props);

            props.setProperty("sessionID",sessionId);

            System.out.println("sessionID is: "+sessionId);
            //------
            myTransaction.update();
        } catch(InvalidPrimaryKeyException e){
            transactionErrorMessage="Error cannot do this 4.";
        }
    }
}

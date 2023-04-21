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
            props3.setProperty("barcode", barcode);
            //--------------------------------------
            String treeStatus = (String) myTree.getState("status");
            props3.setProperty("status", "Sold");
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
            Properties props3 = new Properties();
            myTransaction = new SellTransaction(props3);
            props3.setProperty("transactionType", "Tree Sale");
            //-------------------------------------
            String treeBarcode = props.getProperty("barcode");
            System.out.println("tree barcode is: "+treeBarcode);
            props3.setProperty("barcode", treeBarcode);
            //---------------------------------------
            String paymentMethod = props.getProperty("paymentMethod");
            props3.setProperty("paymentMethod", paymentMethod);
            //---------------------------------------
            String costEntered = props.getProperty("transactionAmount");
            props3.setProperty("transactionAmount", costEntered);
            //---------------------------------------
            String custNameEntered = props.getProperty("customerName");
            props3.setProperty("customerName", custNameEntered);
            //----------------------------------------
            String custPhoneEntered = props.getProperty("customerPhone");
            props3.setProperty("customerPhone", custPhoneEntered);
            //----------------------------------------
            String custEmailEntered = props.getProperty("customerEmail");
            props3.setProperty("customerEmail", custEmailEntered);
            //-----------------------------------------
            String transactionDate = props.getProperty("transactionDate");
            System.out.println("transaction date is: "+transactionDate);
            props3.setProperty("transactionDate", transactionDate);
            //------------------------------------------
            String  transactionTime = props.getProperty("transactionTime");
            props3.setProperty("transactionTime", transactionTime);
            //------------------------------------------
            String dateStatusUpdated = props.getProperty("dateStatusUpdate");
            props3.setProperty("dateStatusUpdate", dateStatusUpdated);
           //-------------------------------------------------------
            mySession= new Session("","");
            String sessionId = mySession.getSessionId();
            props3.setProperty("sessionID",sessionId);
            System.out.println("sessionID is: "+sessionId);
            //------
            myTransaction.update();
        } catch(InvalidPrimaryKeyException e){
            transactionErrorMessage="Error cannot do this 4.";
        }
    }
}

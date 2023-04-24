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
        dependencies.setProperty("TreeChosen", "TransactionError");

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
                transactionErrorMessage = "Error getting Tree";
            }


            try
            {
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

            } catch (Exception ex) {
                transactionErrorMessage = "Error getting Tree";
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

    /*public void sessionTest() {
        try {
            mySession= new Session("","");
            System.out.println("session created");
            String sessionId = mySession.getSessionId();
            System.out.println("the id is : " + sessionId);
            if ((sessionId.length() == 0)) {
                sessionTest = false;
                System.out.println("session test failed");
            }
            else {
                sessionTest= true;
                System.out.println("session test worked");
            }
        }
        catch(InvalidPrimaryKeyException e){
            transactionErrorMessage="Error cannot do this 5.";
        }
    }*/

    public void processTransaction(Properties props) {
        try {
            mySession= new Session("","");
            String sessionId = mySession.getSessionId();
            /*if ((sessionId.length() == 0)) {
                sessionTest();
                transactionErrorMessage="Error : Session not found transaction aborted.";
            }
            else {*/
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
            //}
        } catch(InvalidPrimaryKeyException e){
            transactionErrorMessage="Error cannot do this 4.";
        }
    }
}

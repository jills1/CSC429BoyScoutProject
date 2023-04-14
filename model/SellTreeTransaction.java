package model;

// system imports
import javafx.scene.Scene;
import java.util.Properties;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;


import userinterface.View;
import userinterface.ViewFactory;
public class SellTreeTransaction extends Transaction {

    protected Tree myTree;
    // GUI Components

    protected String transactionErrorMessage = "";
    protected String sellTreeStatusMessage = "";

    protected static final String myTableName = "Tree";

    public SellTreeTransaction(){
        super ();
    }

    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("SellTree", "SellATreeTransaction");
        dependencies.setProperty("CancelUpdate", "CancelTransaction");
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

            try
            {

                //processTransaction(props);
            }
            catch (Exception ex)
            {
                transactionErrorMessage = "Error getting Tree";
            }

        }
        else
        if (key.equals("TreeChosen") == true)
        {
            String barcode = (String)value;
            //myTree = ;

        }
        myRegistry.updateSubscribers(key, this);


    }
}

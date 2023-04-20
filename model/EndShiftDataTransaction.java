package model;

import javafx.scene.Scene;
import impresario.IView;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;

import userinterface.View;
import userinterface.ViewFactory;
public class EndShiftDataTransaction extends Transaction
{
    private Session mySession;
    protected Properties dependencies;
    private String transactionMessage = "";

    public EndShiftDataTransaction()
    {
        super();
    }
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelShift", "CancelTransaction");
        dependencies.setProperty("EndShift", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }

    public void processTransaction(Properties props)
    {
        String id = props.getProperty("sessionID");
        try {
            Session session = new Session(id);
            transactionMessage = "Error: session with ID already exists";
        }
        catch (InvalidPrimaryKeyException ex) {
            mySession  = new Session(props);
            mySession.update();
            transactionMessage += mySession.getState("UpdateStatusMessage");
        }
    }

    public Object getState(String key)
    {
        if (key.equals("TransactionError") == true)
        {
            return transactionMessage;
        }
        return null;
    }

    public void stateChangeRequest(String key, Object value)
    {
        // DEBUG System.out.println("DepositTransaction.sCR: key: " + key);

        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("EndShift")==true)
        {
            Properties p = (Properties)value;
            processTransaction(p);
        }

        myRegistry.updateSubscribers(key, this);
    }

    protected Scene createView()
    {
        Scene currentScene = myViews.get("EndShiftDataView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("EndShiftDataView", this);
            currentScene = new Scene(newView);
            myViews.put("EndShiftDataView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    public String getSessionID()
    {
        try {
            Session session = new Session();
            String id = (String) session.getState("sessionID");
            return id;
        } catch (InvalidPrimaryKeyException exp) {
            return null;
        }
    }
}

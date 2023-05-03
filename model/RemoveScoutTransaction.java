// specify the package
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

/** The class containing the RemoveScoutTransaction for the Tree Sales application */

//==============================================================
public class RemoveScoutTransaction extends Transaction
{

    protected ScoutCollection myScoutCollection;
    protected Scout myScout;
    // GUI Components

    protected String transactionErrorMessage = "";
    protected String scoutUpdateStatusMessage = "";

    protected static final String myTableName = "Scout";

    /**
     * Constructor for this class.
     *
     *
     */
    //----------------------------------------------------------
    public RemoveScoutTransaction()

    {
        super();
    }




    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelScoutSearch", "CancelTransaction");
        dependencies.setProperty("CancelScoutList", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");
        dependencies.setProperty("SearchScoutInfo", "TransactionError");
        dependencies.setProperty("ScoutSelected", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }


    //----------------------------------------------------------
    public void processTransaction(Properties props)
    {
        String fn = props.getProperty("firstName");
        String ln = props.getProperty("lastName");
        System.out.println(props.getProperty("firstName"));
        System.out.println(props.getProperty("lastName"));

        //myScoutCollection = new ScoutCollection();
        // DEBUG System.out.println("Jalen has not messed this up 1");
        myScoutCollection.findScoutsWithNameLike(fn, ln);
        // DEBUG System.out.println("If you see this Jalen has not messed this up");
        // DEBUG System.out.println("Let us see if we get to see the view");
        createAndShowScoutCollectionView();
       // DEBUG System.out.println("The view is showing");


    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {
        // DEBUG System.out.println("IS JALEN STOOPID HERE? -- key sent: " + key);
        if (key.equals("TransactionError") == true)
        {
            return transactionErrorMessage;
        }
        else
        if (key.equals("UpdateStatusMessage") == true)
        {
            return scoutUpdateStatusMessage;
        }
        else
            if (key.equals("ScoutList") == true)
            {
                return myScoutCollection;
            }
        if (key.equals("SelectedScout") == true)
        {
            return myScout;
        }
        else
        if (myScout != null)
        {
            // DEBUG System.out.println("JALEN IS STOOPID -- getting to my scout for key: " + key);
            Object val = myScout.getState(key);
            if (val != null)
            {
                return val;
            }
        }
        else
        if (myScoutCollection != null)
        {

            Object val = myScoutCollection.getState(key);
            if (val != null)
            {
                return val;
            }
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
        if (key.equals("SearchScoutInfo")==true)
        {
            Properties props =(Properties)value;

                try
                {
                    myScoutCollection = new ScoutCollection();

                    processTransaction(props);
                }
                catch (Exception ex)
                {
                    transactionErrorMessage = "Error getting Scout list";
                }

        }
        else
            if (key.equals("ScoutSelected") == true)
            {
                // DEBUG System.out.println("JALEN SHOULD BE SMART -- getting to key ScoutSelected in sCR");
                String scoutTroopID = (String)value;
                // DEBUG System.out.println("JALEN SHOULD BE SMART -- received scout troop ID: " + scoutTroopID);

               myScout = myScoutCollection.retrieveByTroopID(scoutTroopID);
               /* DEBUG if (myScout == null) System.out.println("JALEN IS NOT SMART -- got a null scout for scout troop ID: " + scoutTroopID);
               else System.out.println("JALEN IS SMART -- got a defined scout for scout troop ID: " + scoutTroopID);
                */
               createAndShowScoutSelectedView();



            }
            else
            if(key.equals("UpdateStatusInactive") == true)
            {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDateTime now = LocalDateTime.now();
                myScout.stateChangeRequest("status","Inactive");
                myScout.stateChangeRequest("dateStatusUpdated",dtf.format(now));
                myScout.update();

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
        Scene currentScene = myViews.get("RemoveScoutTransactionView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("RemoveScoutTransactionView", this);
            currentScene = new Scene(newView);
            myViews.put("RemoveScoutTransactionView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------


    //------------------------------------------------------
    protected void createAndShowScoutCollectionView()
    {

        // create our initial view
        View newView = ViewFactory.createView("ScoutCollectionView", this);
        Scene newScene = new Scene(newView);

        myViews.put("ScoutCollectionView", newScene);

        // make the view visible by installing it into the frame
        swapToView(newScene);
    }
    protected void createAndShowScoutSelectedView()
    {


        View newView = ViewFactory.createView("ScoutSelectedView", this);
        Scene newScene = new Scene(newView);

        myViews.put("ScoutSelectedView", newScene);

        // make the view visible by installing it into the frame
        swapToView(newScene);
    }

}
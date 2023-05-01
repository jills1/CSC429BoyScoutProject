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

/** The class containing the UpdateScoutTransaction for the Tree Sales application */
//==============================================================
public class UpdateScoutTransaction extends Transaction
{

    protected ScoutCollection myScoutCollection;
    protected Scout myScout;
    // GUI Components

    protected String transactionErrorMessage = "";
    protected String scoutUpdateStatusMessage = "";

    protected static final String myTableName = "Scout";

    /**
     * Constructor for this class.
     * @throws Exception
     *
     *
     */
    //----------------------------------------------------------
    public UpdateScoutTransaction()

    {
        super();

    }




    //----------------------------------------------------------
    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("SearchUpdateScout", "UpdateTransaction");
        dependencies.setProperty("CancelUpdate", "CancelTransaction");
        dependencies.setProperty("OK", "UpdateTransaction");
        dependencies.setProperty("SearchScoutInfo", "TransactionError");
        dependencies.setProperty("ScoutChosen", "TransactionError");

        myRegistry.setDependencies(dependencies);
    }


    //----------------------------------------------------------
    public void processTransaction(Properties props)
    {
        // Getting the first name and last name from the search
        String fn = props.getProperty("firstName");
        String ln = props.getProperty("lastName");


        myScoutCollection.findScoutsWithNameLike(fn, ln);
        creatAndShowSelectUpdateScoutView();


    }

    //-----------------------------------------------------------
    public Object getState(String key)
    {
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
        else
        if (key.equals("ScoutChosen") == true)
        {
            if (myScout == null) {
                return null;
            } else
                return myScout;
        }
        else
        if (myScout != null)
        {
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

        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("SubmitSearchUpdateScout")==true)
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
        if (key.equals("ScoutChosen") == true)
        {
            String troopID = (String)value;
            myScout = myScoutCollection.retrieveByTroopID(troopID);
            creatAndShowUpdateScoutInfoView();



        }

        else
        if(key.equals("UpdateScoutInfo") == true)
        {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            Scout tempScout = new Scout((Properties)value);
            myScout.stateChangeRequest("scoutID", tempScout.getScoutID());
            myScout.stateChangeRequest("firstName", tempScout.getFirstName());
            myScout.stateChangeRequest("lastName", tempScout.getLastName());
            myScout.stateChangeRequest("middleName", tempScout.getMiddleName());
            myScout.stateChangeRequest("dateOfBirth", tempScout.getDateOfBirth());
            myScout.stateChangeRequest("phoneNumber",tempScout.getPhoneNumber());
            myScout.stateChangeRequest("email", tempScout.getEmail());
            myScout.stateChangeRequest("troopID", tempScout.getTroopID());
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
        Scene currentScene = myViews.get("SearchUpdateScoutView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchUpdateScoutView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchUpdateScoutView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------


    //------------------------------------------------------
    protected void creatAndShowSelectUpdateScoutView()
    {

        // create our initial view
        View newView = ViewFactory.createView("SelectUpdateScoutView", this);
        Scene newScene = new Scene(newView);

        myViews.put("SelectUpdateScoutView", newScene);

        // display the view
        swapToView(newScene);
    }

    protected void creatAndShowUpdateScoutInfoView()
    {


        View newView = ViewFactory.createView("UpdateScoutInfoView", this);
        Scene newScene = new Scene(newView);

        myViews.put("UpdateScoutInfoView", newScene);

        // display the view
        swapToView(newScene);
    }


}
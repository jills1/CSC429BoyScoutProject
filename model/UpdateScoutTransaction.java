package model;
import javafx.scene.Scene;
import java.util.Properties;
import userinterface.View;
import userinterface.ViewFactory;
public class UpdateScoutTransaction extends Transaction {
    protected ScoutCollection myScoutCollection;
    protected Scout myScout;
    // GUI Components
    protected String transactionErrorMessage = "";
    protected String scoutUpdateStatusMessage = "";

    protected static final String myTableName = "Scout";
    public UpdateScoutTransaction(){super();}
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("SearchUpdateScout", "UpdateTransaction");
        dependencies.setProperty("CancelUpdate", "CancelTransaction");
        dependencies.setProperty("OK", "UpdateTransaction");
        dependencies.setProperty("SearchScoutInfo", "TransactionError");
        dependencies.setProperty("ScoutChosen", "TransactionError");
        myRegistry.setDependencies(dependencies);
    }
    public void processTransaction(Properties props)
    {
        String fn = props.getProperty("firstName");
        String ln = props.getProperty("lastName");
        myScoutCollection.findScoutsWithNameLike(fn, ln);
        createAndShowSelectUpdateScoutView();
    }
    public Object getState(String key)
    {
        if (key.equals("TransactionError"))
        {
            return transactionErrorMessage;
        }
        else
        if (key.equals("UpdateStatusMessage"))
        {
            return scoutUpdateStatusMessage;
        }
        else
        if (key.equals("ScoutList"))
        {
            return myScoutCollection;
        }
        else
        if (key.equals("ScoutChosen"))
        {
            System.out.println("Scout required");
            if (myScout == null) {
                System.out.println("Scout null");
                return null;
            } else
                return myScout;
        }
        else
        if (myScout != null)
        {
            System.out.println("State required");
            Object val = myScout.getState(key);
            return val;
        }
        else
        if (myScoutCollection != null)
        {
            Object val = myScoutCollection.getState(key);
            return val;
        }


        return null;
    }

    //-----------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        // DEBUG System.out.println("DepositTransaction.sCR: key: " + key);

        switch (key) {
            case "DoYourJob":
                doYourJob();
                break;
            case "SubmitSearchUpdateScout":
                Properties props = (Properties) value;
                System.out.println("Props given");


                try {
                    System.out.println("try tested");
                    myScoutCollection = new ScoutCollection();
                    System.out.println("collection created");

                    processTransaction(props);
                } catch (Exception ex) {
                    transactionErrorMessage = "Error getting Scout list";
                }

                break;
            case "ScoutChosen":
                System.out.println("Scout chosen from table");
                String troopID = (String) value;
                System.out.println("Troop ID: " + troopID);
                //myScout = myScoutCollection.retrieveByTroopID(troopID);
                createAndShowUpdateScoutInfoView();


                break;
            case "UpdateScoutInfo":
                //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                //LocalDateTime now = LocalDateTime.now();
                Scout tempScout = new Scout((Properties) value);
                myScout.stateChangeRequest("scoutID", tempScout.getScoutID());
                myScout.stateChangeRequest("firstName", tempScout.getFirstName());
                myScout.stateChangeRequest("lastName", tempScout.getLastName());
                myScout.stateChangeRequest("middleName", tempScout.getMiddleName());
                myScout.stateChangeRequest("dateOfBirth", tempScout.getDateOfBirth());
                myScout.stateChangeRequest("phoneNumber", tempScout.getPhoneNumber());
                myScout.stateChangeRequest("email", tempScout.getEmail());
                myScout.stateChangeRequest("troopID", tempScout.getTroopID());
                //myScout.stateChangeRequest("dateStatusUpdated",dtf.format(now));
                myScout.update();
                break;
        }
        myRegistry.updateSubscribers(key, this);
    }
    protected Scene createView() {
        Scene currentScene = myViews.get("SearchUpdateScoutView");
        if (currentScene == null) {
            View newView = ViewFactory.createView("SearchUpdateScoutView", this);
            assert newView != null;
            currentScene = new Scene(newView);
            myViews.put("SearchUpdateScoutView", currentScene);
            return currentScene;
        } else {
            return currentScene;
        }
    }
    protected void createAndShowSelectUpdateScoutView() {
        View newView = ViewFactory.createView("SelectUpdateScoutView", this);
        assert newView != null;
        Scene newScene = new Scene(newView);
        myViews.put("SelectUpdateScoutView", newScene);
        swapToView(newScene);
    }
    protected void createAndShowUpdateScoutInfoView() {
        View newView = ViewFactory.createView("UpdateScoutInfoView", this);
        Scene newScene = new Scene(newView);
        myViews.put("UpdateScoutInfoView", newScene);
        swapToView(newScene);
    }

}
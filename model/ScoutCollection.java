// specify the package
package model;

// system imports
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;
import model.Scout;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the scoutCollection for the ATM application */
//==============================================================
public class ScoutCollection  extends EntityBase implements IView
{
    private static final String myTableName = "Scouts";

    private Vector<Scout> scouts;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public ScoutCollection( AccountHolder cust) throws
            Exception
    {
        super(myTableName);

        if (cust == null)
        {
            new Event(Event.getLeafLevelClassName(this), "<init>",
                    "Missing scout holder information", Event.FATAL);
            throw new Exception
                    ("UNEXPECTED ERROR: scoutCollection.<init>: scout holder information is null");
        }

        String scoutHolderId = (String)cust.getState("ID");

        if (scoutHolderId == null)
        {
            new Event(Event.getLeafLevelClassName(this), "<init>",
                    "Data corrupted: scout Holder has no id in database", Event.FATAL);
            throw new Exception
                    ("UNEXPECTED ERROR: scoutCollection.<init>: Data corrupted: scout holder has no id in repository");
        }

        String query = "SELECT * FROM " + myTableName + " WHERE (OwnerID = " + scoutHolderId + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            scouts = new Vector<Scout>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextscoutData = (Properties)allDataRetrieved.elementAt(cnt);

                Scout scout = new Scout(nextscoutData);

                if (scout != null)
                {
                    addScout(scout);
                }
            }

        }
        else
        {
            throw new InvalidPrimaryKeyException("No scouts for customer : "
                    + scoutHolderId + ". Name : " + cust.getState("Name"));
        }

    }

    //----------------------------------------------------------------------------------
    private void addScout(Scout a)
    {
        //scouts.add(a);
        int index = findIndexToAdd(a);
        scouts.insertElementAt(a,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Scout a)
    {
        //users.add(u);
        int low=0;
        int high = scouts.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            Scout midSession = scouts.elementAt(middle);

            int result = Scout.compare(a,midSession);

            if (result ==0)
            {
                return middle;
            }
            else if (result<0)
            {
                high=middle-1;
            }
            else
            {
                low=middle+1;
            }


        }
        return low;
    }


    /**
     *
     */
    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("Scouts"))
            return scouts;
        else
        if (key.equals("ScoutList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {

        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public Scout retrieve(String scoutNumber)
    {
        Scout retValue = null;
        for (int cnt = 0; cnt < scouts.size(); cnt++)
        {
            Scout nextAcct = scouts.elementAt(cnt);
            String nextAccNum = (String)nextAcct.getState("scoutNumber");
            if (nextAccNum.equals(scoutNumber) == true)
            {
                retValue = nextAcct;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    //------------------------------------------------------
    protected void createAndShowView()
    {

        Scene localScene = myViews.get("scoutCollectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("scoutCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("scoutCollectionView", localScene);
        }
        // make the view visible by installing it into the frame
        swapToView(localScene);

    }

    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
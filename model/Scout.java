// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IView;

//import userinterface.View;
//import userinterface.ViewFactory;

/** The class containing the Account for the ATM application */
//==============================================================
public class Scout extends EntityBase implements IView
{
    private static final String myTableName = "Scout";

    protected Properties dependencies;

    // GUI Components

    private String updateStatusMessage = "";

    //---------------------------------------------------------
    public Scout()
    {
        super(myTableName);
    }

    // constructor for this class
    //----------------------------------------------------------
    public Scout(String scoutId)
            throws InvalidPrimaryKeyException
    {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (scoutId = " + scoutId + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one account. More than that is an error
            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple scouts matching id : "
                        + scoutId + " found.");
            }
            else
            {
                // copy all the retrieved data into persistent state
                Properties retrievedScoutData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedScoutData.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedScoutData.getProperty(nextKey);


                    if (nextValue != null)
                    {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        // If no account found for this user name, throw an exception
        else
        {
            throw new InvalidPrimaryKeyException("No scout matching id : "
                    + scoutId+ " found.");
        }
    }

    // Can also be used to create a NEW Account (if the system it is part of
    // allows for a new account to be set up)
    //----------------------------------------------------------
    public Scout(Properties props)
    {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements() == true)
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }

    //-----------------------------------------------------------------------------------
    private void setDependencies()
    {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("UpdateStatusMessage") == true)
            return updateStatusMessage;

        return persistentState.getProperty(key);
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        persistentState.setProperty(key, (String) value);
        myRegistry.updateSubscribers(key, this);
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }


    //-----------------------------------------------------------------------------------
    public static int compare(Scout a, Scout b)
    {
        String aNum = (String)a.getState("scoutId");
        String bNum = (String)b.getState("scoutId");

        return aNum.compareTo(bNum);
    }

    //-----------------------------------------------------------------------------------
    public void update() //save
    {
        updateStateInDatabase();
    }

    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase()
    {
        try
        {
            // update
            if (persistentState.getProperty("scoutId") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("scoutId",
                        persistentState.getProperty("scoutId"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Scout data for scout ID : " + persistentState.getProperty("scoutId") + " updated successfully in database!";
            }
            else
            {
                //insert
                Integer bookId =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("scoutId", "" + bookId.intValue());
                updateStatusMessage = "Scout data for new scout : " +  persistentState.getProperty("scoutId")
                        + "installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing scout data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }


    /**
     * This method is needed solely to enable the Account information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("scoutID"));
        v.addElement(persistentState.getProperty("lastName"));
        v.addElement(persistentState.getProperty("firstName"));
        v.addElement(persistentState.getProperty("middleName"));
        v.addElement(persistentState.getProperty("dateOfBirth"));
        v.addElement(persistentState.getProperty("phoneNumber"));
        v.addElement(persistentState.getProperty("email"));
        v.addElement(persistentState.getProperty("troopID"));
        v.addElement(persistentState.getProperty("status"));

        return v;
    }


    //-----------------------------------------------------------------------------------
    //this will be in every persistable
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }


    public String toString()
    {
        return "Scout: " + persistentState.getProperty("firstName") + "; Last Name: " +
                persistentState.getProperty("lastName") + "; Troop ID: " +
                persistentState.getProperty("troopID") ;
    }
    public String toString1()
    {
        String retVal = "";

        Enumeration allKeys = persistentState.propertyNames();
        while (allKeys.hasMoreElements() == true) {
            String nextKey = (String) allKeys.nextElement();
            String nextValue = persistentState.getProperty(nextKey);
            retVal += nextKey + ": " + nextValue + "; ";
        }
        return retVal;
    }

}



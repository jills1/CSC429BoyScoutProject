package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.*;

import database.*;
public class Shift extends EntityBase implements IView
{
    private static final String myTableName = "Shift";

    protected Properties dependencies;
    private String updateStatusMessage ="";



    public Shift(String shiftID) throws InvalidPrimaryKeyException
    {
        super(myTableName);
        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (shiftID = " + shiftID + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();
            // There should be EXACTLY one account. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple shifts matching inputted IDs : " + shiftID + " found.");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedTreeData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();
                Enumeration allKeys = retrievedTreeData.propertyNames();
                while (allKeys.hasMoreElements() == true) {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedTreeData.getProperty(nextKey);
                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        } else {
            throw new InvalidPrimaryKeyException("No shift matching inputted ID : " + shiftID+ " found.");
        }
    }


    public Shift(Properties props)
    {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();

        while (allKeys.hasMoreElements() == true) {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);
            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }

    private void setDependencies() {
        dependencies = new Properties();
        myRegistry.setDependencies(dependencies);
    }

    public Object getState(String key)
    {
        if (key.equals("UpdateStatusMessage") == true) {
            return updateStatusMessage;
        }
        return persistentState.getProperty(key);
    }

    public void stateChangeRequest(String key, Object value)
    {
        persistentState.setProperty(key, (String) value);
        myRegistry.updateSubscribers(key, this);
    }
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }
    //-----------------------------------------------------------------------------------
    public static int compare(Tree a, Tree b) {
        String aNum = (String)a.getState("shiftID");
        String bNum = (String)b.getState("shiftID");
        return aNum.compareTo(bNum);
    }
    //-----------------------------------------------------------------------------------
    public void update() {
        updateStateInDatabase();
    }

    private void updateStateInDatabase()
    {
        try
        {
            // update
            if (persistentState.getProperty("shiftID") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("shiftID",
                        persistentState.getProperty("shiftID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Shift Updated!";
            }
            else
            {
                //insert
                Integer shiftId =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("shiftID", "" + shiftId.intValue());
                updateStatusMessage = "Scout added to Shift!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing session data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }

    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();
        v.addElement(persistentState.getProperty("shiftID"));
        v.addElement(persistentState.getProperty("sessionID"));
        v.addElement(persistentState.getProperty("scoutID"));
        v.addElement(persistentState.getProperty("companionName"));
        v.addElement(persistentState.getProperty("scoutStartTime"));
        v.addElement(persistentState.getProperty("scoutEndTime"));
        v.addElement(persistentState.getProperty("companionHours"));

        return v;
    }

    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }

    public String toString()
    {
        return "shift: " + persistentState.getProperty("shiftID") ;
    }
    public String toString1() {
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

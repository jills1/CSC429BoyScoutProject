package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.*;

import database.*;
public class Session extends EntityBase implements IView
{
    private static final String myTableName = "Session";

    protected Properties dependencies;
    private String updateStatusMessage ="";



    public Session(String sessionID) throws InvalidPrimaryKeyException
    {
        super(myTableName);
        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (sessionID = " + sessionID + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();
            // There should be EXACTLY one account. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple sessions matching inputted IDs : " + sessionID + " found.");
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
            throw new InvalidPrimaryKeyException("No session matching inputted ID : " + sessionID+ " found.");
        }
    }

    public Session() throws InvalidPrimaryKeyException
    {
        super(myTableName);
        persistentState = new Properties();
        setDependencies();
        String query = "Select * FROM Session WHERE ((endTime IS Null) OR (endTime = ''))";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();
            // There should be EXACTLY one account. More than that is an error
            if (size != 1) {

            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedSessionData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();
                Enumeration allKeys = retrievedSessionData.propertyNames();
                while (allKeys.hasMoreElements() == true) {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedSessionData.getProperty(nextKey);
                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        } else {

        }
    }
    public Session(Properties props)
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
        String aNum = (String)a.getState("sessionID");
        String bNum = (String)b.getState("sessionID");
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
            if (persistentState.getProperty("sessionID") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("sessionID",
                        persistentState.getProperty("sessionID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Session Updated!";
            }
            else
            {
                //insert
                Integer sessionId =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("sessionID", "" + sessionId.intValue());
                updateStatusMessage = "Session Started!";
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
        v.addElement(persistentState.getProperty("sessionID"));
        v.addElement(persistentState.getProperty("startDate"));
        v.addElement(persistentState.getProperty("startTime"));
        v.addElement(persistentState.getProperty("endTime"));
        v.addElement(persistentState.getProperty("startingCash"));
        v.addElement(persistentState.getProperty("endingCash"));
        v.addElement(persistentState.getProperty("totalCheckTransactionAmount"));
        v.addElement(persistentState.getProperty("notes"));

        return v;
    }

    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }

    public String toString()
    {
        return "session: " + persistentState.getProperty("sessionID") ;
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

    public void deleteStateInDatabase(){
        try {
            Properties whereClause = new Properties();
            whereClause.setProperty("sessionID",persistentState.getProperty("sessionID"));
            deletePersistentState( mySchema, whereClause );
            updateStatusMessage = "Session with ID" + persistentState.getProperty("sessionID")+"DELETED successfully!";

        }
        catch(SQLException ex){
            updateStatusMessage ="Error in deleting session data in database!";
        }
    }
    public Session(String endingCash, String Ok) throws InvalidPrimaryKeyException {
        super(myTableName);
        setDependencies();
        //String sessionId= "SELECT * FROM " +  ""
        String query2 = "SELECT * FROM " + myTableName + " WHERE ((endingCash IS NULL) OR (endingCash = ''))";
        Vector<Properties> allDataRetrieved2 = getSelectQueryResult(query2);
        if (allDataRetrieved2 != null) {
            int size = allDataRetrieved2.size();
            // There should be EXACTLY one account. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple sessions with endingCash = NULL ");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedTreeData = allDataRetrieved2.elementAt(0);
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
            throw new InvalidPrimaryKeyException("No open session found.");
        }
    }
    public String getSessionId() {


        System.out.println(getState("sessionID"));
        return(String)getState("sessionID");

    }

}

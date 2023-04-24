package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.*;

import database.*;
public class Session extends EntityBase implements IView {
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
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();
            if (size != 1) {
            } else {
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
        } else {}
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
    private void getSessionID() {
        String query = "SELECT * FROM " + myTableName + " WHERE endCash IS NULL";
        Vector<Properties> allData = getSelectQueryResult(query);
        if(allData == null || allData.size() != 1) return;
        Properties data = allData.elementAt(0);
        persistentState = new Properties();
        Enumeration keys = data.propertyNames();
        while(keys.hasMoreElements()) {
            String nextKey = (String)keys.nextElement();
            String value = data.getProperty(nextKey);
            if(value != null) {
                persistentState.setProperty(nextKey, value);
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
    public void stateChangeRequest(String key, Object value) {
        persistentState.setProperty(key, (String) value);
        myRegistry.updateSubscribers(key, this);
    }
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }
    //-----------------------------------------------------------------------------------
    public static int compare(Session a, Session b) {
        String aNum = (String)a.getState("sessionID");
        String bNum = (String)b.getState("sessionID");
        return aNum.compareTo(bNum);
    }
    //-----------------------------------------------------------------------------------
    public void update() {
        updateStateInDatabase();
    }
    private void updateStateInDatabase() {
        try {
            String sessionID = persistentState.getProperty("sessionID");
            boolean sessionExists = sessionID != null;
            if(sessionExists) {
                // update
                Properties whereClause = new Properties();
                whereClause.setProperty("sessionID", sessionID);
                updatePersistentState(mySchema, persistentState, whereClause);
                String startDate = (String)getState("startDate");
                String startTime = (String)getState("startTime");
                updateStatusMessage = "Data for session starting on " + startDate + " at " + startTime + "saved successfully";
            } else {
                // insert
                Integer newSessionID = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("sessionID", "" + newSessionID.intValue());
                String startDate = (String)getState("startDate");
                String startTime = (String)getState("startTime");
                updateStatusMessage = "Data for new session starting on " + startDate + " at " + startTime + "saved successfully";
            }
        } catch (SQLException e) {
            updateStatusMessage = "Error in saving session data to the database.";
        }
    }
    public Vector<String> getEntryListView() {
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
}
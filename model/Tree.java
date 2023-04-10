package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.*;

import database.*;
//import userinterface.View;
//import userinterface.ViewFactory;

import impresario.IView;
public class Tree extends EntityBase implements IView {
    private static final String myTableName = "Tree";
    protected Properties dependencies;
    private boolean oldFlag = true;
    // GUI Components
    private String updateStatusMessage = "";
    //---------------------------------------------------------
    public Tree() {
        super(myTableName);
    }
    //----------------------------------------------------------
    public Tree(String barcode) throws InvalidPrimaryKeyException {
        super(myTableName);
        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (barcode = " + barcode + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();
            // There should be EXACTLY one account. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple trees matching inputted barcode : " + barcode + " found.");
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
            throw new InvalidPrimaryKeyException("No tree matching inputted barcode : " + barcode+ " found.");
        }
    }
    // Can also be used to create a NEW Account (if the system it is part of
    // allows for a new account to be set up)
    //----------------------------------------------------------
    public Tree(Properties props)
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
    //-----------------------------------------------------------------------------------
    private void setDependencies() {
        dependencies = new Properties();
        myRegistry.setDependencies(dependencies);
    }



    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("UpdateStatusMessage") == true) {
            return updateStatusMessage;
        }
        return persistentState.getProperty(key);
    }
    //----------------------------------------------------------------
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
        String aNum = (String)a.getState("barcode");
        String bNum = (String)b.getState("barcode");
        return aNum.compareTo(bNum);
    }
    //-----------------------------------------------------------------------------------
    public void update() {
        updateStateInDatabase();
    }
    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase() {
        try
        {
            if (oldFlag)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("barcode", persistentState.getProperty("barcode"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Data for Tree : " + persistentState.getProperty("barcode") + " updated successfully in database!";
            }
            else
            {
                insertPersistentState(mySchema, persistentState);
                oldFlag = true;
            }
        }
        catch (SQLException ex) {
            updateStatusMessage = "Error in installing tree data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }

    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();
        v.addElement(persistentState.getProperty("barcode"));
        v.addElement(persistentState.getProperty("treeType"));
        v.addElement(persistentState.getProperty("notes"));
        v.addElement(persistentState.getProperty("status"));
        v.addElement(persistentState.getProperty("dateStatusUpdate"));

        return v;

    }
    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
    public String toString()
    {
        return "Tree: " + persistentState.getProperty("barcode") ;
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

    protected void setOldFlag(boolean val)
    {
        oldFlag = val;
    }
    public void deleteStateInDatabase(){
        try {
            Properties whereClause = new Properties();
            whereClause.setProperty("barcode",persistentState.getProperty("barcode"));
            deletePersistentState( mySchema, whereClause );
            updateStatusMessage = "The tree with barcode" + persistentState.getProperty("barcode")+"DELETED successfully!";

        }
        catch(SQLException ex){
            updateStatusMessage ="Error in deleting tree data in database!";
        }
    }

}
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
public class Tree extends EntityBase implements IView
{
    private static final String myTableName = "Tree";

    protected Properties dependencies;

    // GUI Components

    private String updateStatusMessage = "";

    //---------------------------------------------------------
    public Tree()
    {
        super(myTableName);
    }

    // constructor for this class
    //----------------------------------------------------------
    public Tree(String barcode)
            throws InvalidPrimaryKeyException
    {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (barcode = " + barcode + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one tree at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one account. More than that is an error
            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple trees matching barcode : "
                        + barcode + " found.");
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
        // If no tree found for this user barcode, throw an exception
        else
        {
            throw new InvalidPrimaryKeyException("No tree matching barcode : "
                    + barcode+ " found.");
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
    public static int compare(Tree a, Tree b)
    {
        String aNum = (String)a.getState("barcode");
        String bNum = (String)b.getState("barcode");

        return aNum.compareTo(bNum);
    }

    //-----------------------------------------------------------------------------------
    public void update() //save
    {
        updateStateInDatabase();
    }
    //-----------------------------------------------------------------------------------
    public void remove()
    {


    }
    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase()
    {
        try
        {
            // update
            if (persistentState.getProperty("barcode") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("barcode",
                        persistentState.getProperty("barcode"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Tree data for barcode : " + persistentState.getProperty("barcode") + " updated successfully in database!";
            }
            else
            {
                //insert
                Integer barcode =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("barcode", "" + barcode.intValue());
                updateStatusMessage = "Tree data for new Tree : " +  persistentState.getProperty("barcode")
                        + "installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing Tree data in database!";
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

        v.addElement(persistentState.getProperty("barcode"));
        v.addElement(persistentState.getProperty("treeType"));
        v.addElement(persistentState.getProperty("notes"));
        v.addElement(persistentState.getProperty("ststus"));
        v.addElement(persistentState.getProperty("dateStatusUpdated"));


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
        return "Tree: " + persistentState.getProperty("barcode") + "; Tree Tyoe: " +
                persistentState.getProperty("treeType") + "; notes: " +
                persistentState.getProperty("notes") + "; status: " +
                persistentState.getProperty("status")+ "; dateStatusUpdated: " +
                persistentState.getProperty("dateStatusUpdated") ;
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



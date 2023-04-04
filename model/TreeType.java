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

/** The class containing the Tree Type information for the Tree Sales application */
//==============================================================
public class TreeType extends EntityBase implements IView
{
    private static final String myTableName = "TreeType";

    protected Properties dependencies;

    // GUI Components

    private String updateStatusMessage = "";

    //---------------------------------------------------------
    public TreeType()
    {
        super(myTableName);
    }

    // constructor for this class
    //----------------------------------------------------------
    public TreeType(String barcodePrefix)
            throws InvalidPrimaryKeyException
    {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (barcodePrefix = " + barcodePrefix + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one TreeType. More than that is an error
            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple tree types matching barcode prefix : "
                        + barcodePrefix + " found.");
            }
            else
            {
                // copy all the retrieved data into persistent state
                Properties retrievedTreeTypeData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedTreeTypeData.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedTreeTypeData.getProperty(nextKey);


                    if (nextValue != null)
                    {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        // If no treeType found for this prefix, throw an exception
        else
        {
            throw new InvalidPrimaryKeyException("No tree type matching tree type prefix : "
                    + barcodePrefix + " found.");
        }
    }

    /** Constructor that takes in a Properties object to populate. Can also be used to create a NEW TreeType */
    //----------------------------------------------------------
    public TreeType(Properties props)
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
    public static int compare(TreeType a, TreeType b)
    {
        String aNum = (String)a.getState("barcodePrefix");
        String bNum = (String)b.getState("barcodePrefix");

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
            if (persistentState.getProperty("barcodePrefix") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("barcodePrefix",
                        persistentState.getProperty("barcodePrefix"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "TreeType data updated successfully in database!";
            }
            else
            {
                //insert
                Integer barcodePrefix =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("barcodePrefix", "" + barcodePrefix.intValue());
                updateStatusMessage = "Tree Type data for new Tree Type installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing Tree Type data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }


    /**
     * This method is needed solely to enable the Tree Type information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("treeTypeID"));
        v.addElement(persistentState.getProperty("typeDescription"));
        v.addElement(persistentState.getProperty("cost"));
        v.addElement(persistentState.getProperty("barcodePrefix"));


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
        return "TreeType: " + persistentState.getProperty("treeTypeID") + "; cost: " +
                persistentState.getProperty("cost") + "; Barcode Prefix: " +
                persistentState.getProperty("barcodePrefix") ;
    }



}



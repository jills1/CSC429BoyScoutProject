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

/** The class containing the SellTransaction information for the Tree Sales application */
//==============================================================
public class SellTransaction extends EntityBase implements IView
{
    private static final String myTableName = "Transaction";

    protected Properties dependencies;

    // GUI Components

    private String updateStatusMessage = "";

    //---------------------------------------------------------
    public SellTransaction()
    {
        super(myTableName);
    }

    // constructor for this class
    //----------------------------------------------------------
    public SellTransaction(String ID)
            throws InvalidPrimaryKeyException
    {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (ID = " + ID + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one SellTransaction. More than that is an error
            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple transactions matching id : "
                        + ID + " found.");
            }
            else
            {
                // copy all the retrieved data into persistent state
                Properties retrievedSellTransactionData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedSellTransactionData.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedSellTransactionData.getProperty(nextKey);


                    if (nextValue != null)
                    {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        // If no scout found for this troop id, throw an exception
        else
        {
            throw new InvalidPrimaryKeyException("No transaction matching id : "
                    + ID + " found.");
        }
    }

    /** Constructor that takes in a Properties object to populate. Can also be used to create a NEW Transaction */
    //----------------------------------------------------------
    public SellTransaction(Properties props)
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
    public static int compare(SellTransaction a, SellTransaction b)
    {
        String aNum = (String)a.getState("ID");
        String bNum = (String)b.getState("ID");

        return aNum.compareTo(bNum);
    }

    //-----------------------------------------------------------------------------------
    public void update() //save
    {
        updateStateInDatabase();
    }

    public String getID()
    {
        return (String)persistentState.getProperty("ID");
    }

    //---------------------------------------------------------------
    public String getSessionID()
    {
        return (String)persistentState.getProperty("sessionID");
    }
    //---------------------------------------------------------------
    public String getTransactionType()
    {
        return (String)persistentState.getProperty("transactionType");
    }
    //----------------------------------------------------------------
    public String getBarcode()
    {
        return (String)persistentState.getProperty("barcode");
    }
    //----------------------------------------------------------------
    public String getTransactionAmount()
    {
        return (String)persistentState.getProperty("transactionAmount");
    }
    //----------------------------------------------------------------
    public String getPaymentMethod()
    {
        return (String)persistentState.getProperty("paymentMethod");
    }
    //----------------------------------------------------------------
    public String getCustomerName()
    {
        return (String)persistentState.getProperty("customerName");
    }
    //----------------------------------------------------------------
    public String getCustomerPhone()
    {
        return (String)persistentState.getProperty("customerPhone");
    }
    //-----------------------------------------------------------------------------------
    public String getTransactionDate()
    {
        return (String)persistentState.getProperty("transactionDate");
    }
    //----------------------------------------------------------------
    public String getCustomerEmail()
    {
        return (String)persistentState.getProperty("customerEmail");
    }
    //----------------------------------------------------------------
    public String getTransactionTime()
    {
        return (String)persistentState.getProperty("transactionTime");
    }

    //-----------------------------------------------------------------------------------
    public String getDateStatusUpdated()
    {
        return (String)persistentState.getProperty("dateStatusUpdated");
    }
    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase()
    {
        try
        {
            // update
            if (persistentState.getProperty("ID") != null)
            {
                Properties whereClause = new Properties();
                whereClause.setProperty("ID",
                        persistentState.getProperty("ID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Transaction data updated successfully in database!";
            }
            else
            {
                //insert
                Integer scoutId =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("ID", "" + scoutId.intValue());
                updateStatusMessage = "Transaction data for new transaction installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing scout data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }


    /**
     * This method is needed solely to enable the Scout information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("ID"));
        v.addElement(persistentState.getProperty("sessionID"));
        v.addElement(persistentState.getProperty("transactionType"));
        v.addElement(persistentState.getProperty("barcode"));
        v.addElement(persistentState.getProperty("transactionAmount"));
        v.addElement(persistentState.getProperty("paymentMethod"));
        v.addElement(persistentState.getProperty("customerName"));
        v.addElement(persistentState.getProperty("customerPhone"));
        v.addElement(persistentState.getProperty("customerEmail"));
        v.addElement(persistentState.getProperty("transactionDate"));
        v.addElement(persistentState.getProperty("transactionTime"));
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
        return "Transaction: " + getSessionID() + "; Barcode: " +
                getBarcode() + "; Payment Method: " +
                getPaymentMethod() ;
    }



}


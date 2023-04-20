package model;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import exception.InvalidPrimaryKeyException;
import database.*;
import impresario.IView;
import model.EntityBase;
public class TransactionClass extends EntityBase implements IView {
    private static final String myTableName = "Transaction";
    protected Properties dependencies;
    private String updateStatusMessage = "";
    public TransactionClass() {super(myTableName);}
    public TransactionClass(String transactionID) throws InvalidPrimaryKeyException {
        super(myTableName);
        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (transactionID = " + transactionID + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        // You must get one account at least
        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();
            // There should be EXACTLY one Scout. More than that is an error
            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple scouts matching troop id : " + transactionID + " found.");
            } else {
                // copy all the retrieved data into persistent state
                Properties retrievedTransactionData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();
                Enumeration allKeys = retrievedTransactionData.propertyNames();
                while (allKeys.hasMoreElements()) {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedTransactionData.getProperty(nextKey);
                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }
            }
        } else {
            throw new InvalidPrimaryKeyException("No scout matching troop id : " + transactionID + " found.");
        }
    }
    public TransactionClass(Properties props) {
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
    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage")){
            return updateStatusMessage;
        }
        return persistentState.getProperty(key);
    }
    public void stateChangeRequest(String key, Object value) {
        persistentState.setProperty(key, (String) value);
        myRegistry.updateSubscribers(key, this);
    }
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
    public static int compare(TransactionClass a, TransactionClass b) {
        String aNum = (String)a.getState("transactionID");
        String bNum = (String)b.getState("transactionID");
        return aNum.compareTo(bNum);
    }
    public void update() {updateStateInDatabase();}
    //---------------------------------------------------------------
    public String getTransactionID() {return (String)persistentState.getProperty("transactionID");}
    public String getSessionID() {return (String)persistentState.getProperty("sessionID");}
    public String getTransactionType() {return (String)persistentState.getProperty("transactionType");}
    public String getBarcode() {return (String)persistentState.getProperty("barcode");}
    public String getTransactionAmount() {return (String)persistentState.getProperty("transactionAmount");}
    public String getPaymentMethod() {return (String)persistentState.getProperty("paymentMethod");}
    public String getCustomerName() {return (String)persistentState.getProperty("customerName");}
    public String getCustomerPhone() {return (String)persistentState.getProperty("customerPhone");}
    public String getCustomerEmail() {return (String)persistentState.getProperty("customerEmail");}
    public String getLastName() {return (String)persistentState.getProperty("transactionDate");}
    public String getTransactionDate() {return (String)persistentState.getProperty("transactionTime");}
    public String getDateStatusUpdated() {return (String)persistentState.getProperty("dateStatusUpdated");}
    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase() {
        try {
            if (persistentState.getProperty("scoutID") != null) {
                Properties whereClause = new Properties();
                whereClause.setProperty("scoutID", persistentState.getProperty("scoutID"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Scout data updated successfully in database!";
            } else {
                //insert
                Integer scoutId = insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("scoutID", "" + scoutId.intValue());
                updateStatusMessage = "Scout data for new scout installed successfully in database!";
            }
        } catch (SQLException ex) {
            updateStatusMessage = "Error in installing scout data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }
    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<String>();
        v.addElement(persistentState.getProperty("transactionID"));
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
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
    public String toString() {
        return "Scout: " + persistentState.getProperty("firstName") + "; Last Name: " +
                persistentState.getProperty("lastName") + "; Troop ID: " +
                persistentState.getProperty("troopID") ;
    }
}

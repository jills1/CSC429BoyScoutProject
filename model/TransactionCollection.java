package model;
import java.util.Properties;
import java.util.Vector;
import impresario.IView;
public class TransactionCollection  extends EntityBase implements IView {
    private double totalCashSales;
    private double totalCheckSales;
    private static final String myTableName = "Transaction";
    private Vector<TransactionClass> transactionList;
    public TransactionCollection() {super(myTableName); transactionList = new Vector<>();}
    private void addTransaction(TransactionClass a) {
        int index = findIndexToAdd(a);
        System.out.println("Jalen found the index to add: " + index);
        transactionList.insertElementAt(a,index); // To build up a collection sorted on some key
    }
    private int findIndexToAdd(TransactionClass a) {
        //users.add(u);
        int low=0;
        int high = transactionList.size()-1;
        System.out.println("find index to add: " + high);
        int middle;
        while (low <=high) {
            middle = (low+high)/2;
            TransactionClass transactionClass = transactionList.elementAt(middle);
            System.out.println("middle value at: " + middle);
            int result = TransactionClass.compare(a,transactionClass);
            System.out.println("Result is: " + result);
            if (result ==0) {
                return middle;
            } else if (result<0) {
                high=middle-1;
            } else {
                low=middle+1;
            }
        }
        return low;
    }
    public Object getState(String key)
    {
        if (key.equals("Scouts"))
            return transactionList;
        else
        if (key.equals("ScoutList"))
            return this;
        return null;
    }
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }
    public TransactionClass retrieve(String transactionId) {
        TransactionClass retValue = null;
        for (int cnt = 0; cnt < transactionList.size(); cnt++) {
            TransactionClass nextSct = transactionList.elementAt(cnt);
            String nextSctId = (String)nextSct.getState("transactionID");
            if (nextSctId.equals(transactionId) == true) {
                retValue = nextSct;
                return retValue; // we should say 'break;' here
            }
        }
        return retValue;
    }
    public void retrieveSession(String sessionID) {
        String query = "SELECT * FROM " + myTableName + " WHERE (sessionID = " + sessionID + " )";
        Vector<Properties> info = getSelectQueryResult(query);
        transactionList.clear();
        for(int e = 0; e < info.size(); e++) {
            Properties selectInfo = info.elementAt(e);
            TransactionClass trans = new TransactionClass(selectInfo);
            transactionList.add(trans);
        }
        totalAmountTrans();
    }
    private void totalAmountTrans() {
        for(int i = 0; i < transactionList.size(); i++) {
            TransactionClass t = transactionList.elementAt(i);
            String paymentMethod = (String)t.getState("paymentMethod");
            if(paymentMethod.equals("cash")){
                double amount = Double.parseDouble((String)t.getState("transactionAmount"));
                totalCashSales += amount;
            } else if(paymentMethod.equals("check")) {
                double amount = Double.parseDouble((String)t.getState("transactionAmount"));
                totalCheckSales += amount;
            }
        }
    }
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
    private void helper(String query) {
        Vector allDataRetrieved = getSelectQueryResult(query);
        for(int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
            Properties nextTransactionData = (Properties) allDataRetrieved.elementAt(cnt);
            TransactionClass trans = new TransactionClass(nextTransactionData);
            addTransaction(trans);
        }
    }
}
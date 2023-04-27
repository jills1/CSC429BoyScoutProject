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
        if (key.equals("Transactions"))
            return transactionList;
        else
        if (key.equals("TransactionList"))
            return this;
        else
        if (key.equals("cashSales"))
            return totalCashSales;
        else
        if (key.equals("checkSales"))
            return totalCheckSales;
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
    public void getAllTransactionsForSession(String sessionID) {
        String query = "SELECT * FROM " + myTableName + " WHERE (sessionID = " + sessionID + " )";
        Vector<Properties> info = getSelectQueryResult(query);
        transactionList.clear();
        for(int e = 0; e < info.size(); e++) {
            Properties selectInfo = info.elementAt(e);
            TransactionClass trans = new TransactionClass(selectInfo);
            transactionList.add(trans);
        }
    }
    public void computeAndSetTotalAmountTrans() {
        System.out.println("Size of retrieved transactions: " + transactionList.size());
        for(int i = 0; i < transactionList.size(); i++) {
            TransactionClass t = transactionList.elementAt(i);
            String paymentMethod = (String)t.getState("paymentMethod");
            System.out.println("Payment method for transaction: " + paymentMethod);
            if(paymentMethod.equals("Cash")){
                double amount = Double.parseDouble((String)t.getState("transactionAmount"));
                System.out.println("Amount of cash transaction: " + amount);
                totalCashSales += amount;
                System.out.println("Total cash sales so far: " + totalCashSales);
            } else if(paymentMethod.equals("Check")) {
                double amount = Double.parseDouble((String)t.getState("transactionAmount"));
                System.out.println("Amount of check transaction: " + amount);
                totalCheckSales += amount;
                System.out.println("Total check sales so far: " + totalCheckSales);
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
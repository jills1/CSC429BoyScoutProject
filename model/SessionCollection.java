package model;
import java.util.Properties;
import java.util.Vector;
import impresario.IView;
public class SessionCollection  extends EntityBase implements IView {
    private static final String myTableName = "Session";
    private Vector<Session> sessionList;
    public SessionCollection() {
        super(myTableName);
        sessionList = new Vector<>();
    }
    private void addSession(Session a) {
        int index = findIndexToAdd(a);
        //System.out.println("Jalen found the index to add: " + index);
        sessionList.insertElementAt(a,index); // To build up a collection sorted on some key
    }
    private int findIndexToAdd(Session a) {
        //users.add(u);
        int low=0;
        int high = sessionList.size()-1;
        System.out.println("find index to add: " + high);
        int middle;
        while (low <=high) {
            middle = (low+high)/2;
            Session midSession = sessionList.elementAt(middle);
            System.out.println("middle value at: " + middle);
            int result = Session.compare(a,midSession);
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
    public Object getState(String key) {
        if (key.equals("Scouts")){
            return sessionList;
        } else if (key.equals("ScoutList")) {
            return this;
        }
        return null;
    }
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }
    public Session retrieve(String sessionId)
    {
        Scout retValue = null;
        for (int cnt = 0; cnt < sessionList.size(); cnt++)
        {
            Session nextSct = sessionList.elementAt(cnt);
            String nextSctId = (String)nextSct.getState("sessionID");
            if (nextSctId.equals(sessionId) == true)
            {
                retValue = nextSct;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }
    public Session retrieveByTroopID(String troopId) {
        Scout retValue = null;
        for (int cnt = 0; cnt < sessionList.size(); cnt++) {
            Scout nextSct = sessionList.elementAt(cnt);
            String nextSctId = (String)nextSct.getState("troopID");
            if (nextSctId.equals(troopId) == true) {
                retValue = nextSct;
                return retValue; // we should say 'break;' here
            }
        }
        return retValue;
    }
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
    public void findScoutsWithNameLike(String fn, String ln) {
        String query = "";
        if ((fn==null||fn.length()==0)&&(ln==null||ln.length()==0))
            query = "SELECT * FROM "+myTableName+" WHERE status = 'Active'";
        else if (fn==null || fn.length()==0)
        {
            query = "SELECT * FROM "+myTableName+" WHERE (lastName LIKE '%"+ln+"%' AND status = 'Active')";
        }
        else if (ln==null || ln.length()==0)
        {
            query = "SELECT * FROM "+myTableName+" WHERE (firstName LIKE '%"+fn+"%' AND status = 'Active')";
        }
        else
            query = "SELECT * FROM "+myTableName+" WHERE ((firstName LIKE '%"+fn+"%') AND (lastName LIKE '%"+ln+"%') AND status = 'Active')";
        System.out.println(query);
        helper(query);
    }
    private void helper(String query) {
        Vector allDataRetrieved = getSelectQueryResult(query);
        System.out.println("If you see this Jalen worked the query successfully");
        //scoutList = new Vector<Scout>();
        for(int cnt = 0; cnt < allDataRetrieved.size(); cnt++) {
            System.out.println("Jalen is at iteration: " + cnt + " of adding the scout");
            Properties nextScoutData = (Properties) allDataRetrieved.elementAt(cnt);
            Scout scout = new Scout(nextScoutData);
            System.out.println("Jalen finished creating the scout for iteration: " + cnt);
            addScout(scout);
            System.out.println("Jalen finished adding the scout for iteration: " + cnt);
        }
        System.out.println("If you see this Jalen did not mess up adding the scout to the list");
    }
}

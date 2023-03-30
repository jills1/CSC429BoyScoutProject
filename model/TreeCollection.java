// specify the package
package model;

// system imports
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the AccountCollection for the ATM application */
//==============================================================
public class TreeCollection  extends EntityBase implements IView
{
    private static final String myTableName = "Tree";

    private Vector<Tree> trees;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public TreeCollection( AccountHolder cust) throws
            Exception
    {
        super(myTableName);

        if (cust == null)
        {
            new Event(Event.getLeafLevelClassName(this), "<init>",
                    "Missing Tree information", Event.FATAL);
            throw new Exception
                    ("UNEXPECTED ERROR: TreeCollection.<init>: account holder information is null");
        }

        String treeHolderId = (String)cust.getState("ID");

        if (treeHolderId == null)
        {
            new Event(Event.getLeafLevelClassName(this), "<init>",
                    "Data corrupted: Tree has no barcode in database", Event.FATAL);
            throw new Exception
                    ("UNEXPECTED ERROR: TreeCollection.<init>: Data corrupted: account holder has no id in repository");
        }

        String query = "SELECT * FROM " + myTableName + " WHERE (OwnerID = " + treeHolderId + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            trees = new Vector<Tree>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextAccountData = (Properties)allDataRetrieved.elementAt(cnt);

                Tree tree = new Tree(nextAccountData);

                if (tree != null)
                {
                    addAccount(tree);
                }
            }

        }
        else
        {
            throw new InvalidPrimaryKeyException("No accounts for customer : "
                    + treeHolderId + ". Name : " + cust.getState("Name"));
        }

    }

    //----------------------------------------------------------------------------------
    private void addAccount(Tree a)
    {
        //accounts.add(a);
        int index = findIndexToAdd(a);
        trees.insertElementAt(a,index); // To build up a collection sorted on some key
    }

    //----------------------------------------------------------------------------------
    private int findIndexToAdd(Tree a)
    {
        //users.add(u);
        int low=0;
        int high = trees.size()-1;
        int middle;

        while (low <=high)
        {
            middle = (low+high)/2;

            Tree midSession = trees.elementAt(middle);

            int result = Tree.compare(a,midSession);

            if (result ==0)
            {
                return middle;
            }
            else if (result<0)
            {
                high=middle-1;
            }
            else
            {
                low=middle+1;
            }


        }
        return low;
    }


    /**
     *
     */
    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("Trees"))
            return trees;
        else
        if (key.equals("TreeList"))
            return this;
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {

        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public Tree retrieve(String treeBarcode)
    {
        Tree retValue = null;
        for (int cnt = 0; cnt < trees.size(); cnt++)
        {
            Tree nextTree = trees.elementAt(cnt);
            String nextTreeBarcode = (String)nextTree.getState("barcode");
            if (nextTreeBarcode.equals(treeBarcode) == true)
            {
                retValue = nextTree;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    //------------------------------------------------------
    protected void createAndShowView()
    {

        Scene localScene = myViews.get("TreeCollectionView");

        if (localScene == null)
        {
            // create our new view
            View newView = ViewFactory.createView("TreeCollectionView", this);
            localScene = new Scene(newView);
            myViews.put("TreeCollectionView", localScene);
        }
        // make the view visible by installing it into the frame
        swapToView(localScene);

    }

    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}

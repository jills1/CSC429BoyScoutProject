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
import model.Scout;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;


/** The class containing the scoutCollection for the ATM application */
//==============================================================
public class ScoutCollection  extends EntityBase implements IView
{
	private static final String myTableName = "Scout";


	protected Vector<Scout> scoutList;
	// GUI Components

	// constructor for this class
	//----------------------------------------------------------
	public ScoutCollection()
	{
		super(myTableName);

		scoutList = new Vector<>();
	}

	//---------------------------------------------------------------------------------
	public int getSize()
	{
		return scoutList.size();
	}

	//----------------------------------------------------------------------------------
	private void addScout(Scout a)
	{
		//scouts.add(a);
		int index = findIndexToAdd(a);
		System.out.println("Jalen found the index to add: " + index);
		scoutList.insertElementAt(a,index); // To build up a collection sorted on some key
	}

	//---------------------------------------------------------------------------------
	public void addChosenScout(Scout a)
	{
		addScout(a);
	}

	//----------------------------------------------------------------------------------
	private int findIndexToAdd(Scout a)
	{
		//users.add(u);
		int low=0;
		int high = scoutList.size()-1;
		System.out.println("find index to add: " + high);
		int middle;

		while (low <=high)
		{
			middle = (low+high)/2;

			Scout midSession = scoutList.elementAt(middle);
			System.out.println("middle value at: " + middle);
			int result = Scout.compare(a,midSession);
			System.out.println("Result is: " + result);
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
		if (key.equals("Scouts"))
			return scoutList;
		else
		if (key.equals("ScoutList"))
			return this;
		return null;
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		
		myRegistry.updateSubscribers(key, this);
	}

	//----------------------------------------------------------
	public Scout retrieve(String scoutId)
	{
		Scout retValue = null;
		for (int cnt = 0; cnt < scoutList.size(); cnt++)
		{
			Scout nextSct = scoutList.elementAt(cnt);
			String nextSctId = (String)nextSct.getState("scoutID");
			if (nextSctId.equals(scoutId) == true)
			{
				retValue = nextSct;
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

	public Scout retrieveByTroopID(String troopId)
	{
		Scout retValue = null;
		for (int cnt = 0; cnt < scoutList.size(); cnt++)
		{
			Scout nextSct = scoutList.elementAt(cnt);
			String nextSctId = (String)nextSct.getState("troopID");
			if (nextSctId.equals(troopId) == true)
			{
				retValue = nextSct;
				return retValue; // we should say 'break;' here
			}
		}

		return retValue;
	}

	//------------------------------------------------------
	protected void createAndShowView()
	{

		Scene localScene = myViews.get("scoutCollectionView");

		if (localScene == null)
		{
				// create our new view
				View newView = ViewFactory.createView("scoutCollectionView", this);
				localScene = new Scene(newView);
				myViews.put("scoutCollectionView", localScene);
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

	//-----------------------------------------------------------------------------------
	public void findScoutsWithNameLike(String fn, String ln)
	{
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

	//------------------------------------------------------------------------------------
	public void findAllScouts()
	{
		String query = "";
		query = "SELECT * FROM "+myTableName+" WHERE status = 'Active'";
		helper(query);
	}

	//------------------------------------------------------------------------------------
	private void helper(String query)
	{


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

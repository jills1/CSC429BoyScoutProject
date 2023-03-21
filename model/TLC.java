// specify the package
package model;

// system imports
import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.scene.Scene;

// project imports
import impresario.IModel;
import impresario.ISlideShow;
import impresario.IView;
import impresario.ModelRegistry;

import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import event.Event;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

/** The class containing the Teller  for the ATM application */
//==============================================================
public class TLC implements IView, IModel
// This class implements all these interfaces (and does NOT extend 'EntityBase')
// because it does NOT play the role of accessing the back-end database tables.
// It only plays a front-end role. 'EntityBase' objects play both roles.
{
	// For Impresario
	private Properties dependencies;
	private ModelRegistry myRegistry;



	// GUI Components
	private Hashtable<String, Scene> myViews;
	private Stage	  	myStage;

	private String loginErrorMessage = "";
	private String transactionErrorMessage = "";

	// constructor for this class
	//----------------------------------------------------------
	public TLC()
	{
		myStage = MainStageContainer.getInstance();
		myViews = new Hashtable<String, Scene>();

		// STEP 3.1: Create the Registry object - if you inherit from
		// EntityBase, this is done for you. Otherwise, you do it yourself
		myRegistry = new ModelRegistry("TLC");
		if(myRegistry == null)
		{
			new Event(Event.getLeafLevelClassName(this), "TLC",
				"Could not instantiate Registry", Event.ERROR);
		}

		// STEP 3.2: Be sure to set the dependencies correctly
		setDependencies();

		// Set up the initial view
		createAndShowTLCView();
	}

	//-----------------------------------------------------------------------------------
	private void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty("RegisterScout", "TransactionError");
		dependencies.setProperty("UpdateScout", "TransactionError");
		dependencies.setProperty("RemoveScout", "TransactionError");
		dependencies.setProperty("AddTree", "TransactionError");
		dependencies.setProperty("UpdateTree", "TransactionError");
		dependencies.setProperty("RemoveTree", "TransactionError");
		dependencies.setProperty("AddTreeType", "TransactionError");
		dependencies.setProperty("UpdateTreeType", "TransactionError");
		dependencies.setProperty("StartShift", "TransactionError");
		dependencies.setProperty("EndShift", "TransactionError");
		dependencies.setProperty("SellTree", "TransactionError");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * Method called from client to get the value of a particular field
	 * held by the objects encapsulated by this object.
	 *
	 * @param	key	Name of database column (field) for which the client wants the value
	 *
	 * @return	Value associated with the field
	 */
	//----------------------------------------------------------
	public Object getState(String key)
	{

		if (key.equals("TransactionError") == true)
		{
			return transactionErrorMessage;
		}

		else
			return "";
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		// STEP 4: Write the sCR method component for the key you
		// just set up dependencies for
		// DEBUG System.out.println("Teller.sCR: key = " + key);


		if (key.equals("CancelTransaction") == true)
		{
			createAndShowTLCView();
		}
		else
		if ((key.equals("RegisterScout") == true) || (key.equals("UpdateScout") == true) ||
			(key.equals("RemoveScout") == true) || (key.equals("AddTree") == true) ||
			(key.equals("UpdateTree") == true) || (key.equals("RemoveTree") == true) ||
			(key.equals("AddTreeType") == true) || (key.equals("UpdateTreeType") == true) ||
			(key.equals("StartShift") == true) || (key.equals("EndShift") == true) ||
			(key.equals("SellTree") == true))
		{
			String transType = key;


				doTransaction(transType);




		}


		myRegistry.updateSubscribers(key, this);
	}

	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		// DEBUG System.out.println("Teller.updateState: key: " + key);

		stateChangeRequest(key, value);
	}

	/**
	 * Login AccountHolder corresponding to user name and password.
	 */
	//----------------------------------------------------------



	/**
	 * Create a Transaction depending on the Transaction type (deposit,
	 * withdraw, transfer, etc.). Use the AccountHolder holder data to do the
	 * create.
	 */
	//----------------------------------------------------------
	public void doTransaction(String transactionType)
	{
		try
		{
			Transaction trans = TransactionFactory.createTransaction(transactionType);

			trans.subscribe("CancelTransaction", this);
			trans.stateChangeRequest("DoYourJob", "");
		}
		catch (Exception ex)
		{
			transactionErrorMessage = "FATAL ERROR: TRANSACTION FAILURE: Unrecognized transaction!!";
			new Event(Event.getLeafLevelClassName(this), "createTransaction",
					"Transaction Creation Failure: Unrecognized transaction " + ex.toString(),
					Event.ERROR);
		}
	}

	//----------------------------------------------------------
	private void createAndShowTLCView()
	{
		Scene currentScene = (Scene)myViews.get("TLCView");
		
		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("TLCView", this); // USE VIEW FACTORY
			currentScene = new Scene(newView);
			myViews.put("TLCView", currentScene);
		}
				

		// make the view visible by installing it into the frame
		swapToView(currentScene);
		
	}

	//------------------------------------------------------------



	/** Register objects to receive state updates. */
	//----------------------------------------------------------
	public void subscribe(String key, IView subscriber)
	{
		// DEBUG: System.out.println("Cager[" + myTableName + "].subscribe");
		// forward to our registry
		myRegistry.subscribe(key, subscriber);
	}

	/** Unregister previously registered objects. */
	//----------------------------------------------------------
	public void unSubscribe(String key, IView subscriber)
	{
		// DEBUG: System.out.println("Cager.unSubscribe");
		// forward to our registry
		myRegistry.unSubscribe(key, subscriber);
	}



	//-----------------------------------------------------------------------------
	public void swapToView(Scene newScene)
	{

		
		if (newScene == null)
		{
			System.out.println("TLC.swapToView(): Missing view for display");
			new Event(Event.getLeafLevelClassName(this), "swapToView",
				"Missing view for display ", Event.ERROR);
			return;
		}

		myStage.setScene(newScene);
		myStage.sizeToScene();
		
			
		//Place in center
		WindowPosition.placeCenter(myStage);

	}

}


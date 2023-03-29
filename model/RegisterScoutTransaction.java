// specify the package
package model;

// system imports
import javafx.scene.Scene;
import java.util.Properties;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;

import userinterface.View;
import userinterface.ViewFactory;

/** The class containing the DepositTransaction for the ATM application */
//==============================================================
public class RegisterScoutTransaction extends Transaction
{

	private Scout myScout;

	// GUI Components

	private String transactionErrorMessage = "";


	/**
	 * Constructor for this class.
	 *
	 *
	 */
	//----------------------------------------------------------
	public RegisterScoutTransaction()
	{
		super();
	}

	/**
	 * Constructor for this class.
	 * <p>
	 * Transaction remembers all the account IDs for this customer.
	 * It uses AccountCatalog to create this list of account IDs.
	 */


	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty("CancelRegisterScout", "CancelTransaction");
		dependencies.setProperty("RegisterWithScoutInfo", "TransactionError");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * This method encapsulates all the logic of registering a Scout with the troop
	 */
	//----------------------------------------------------------
	public void processTransaction(Properties props)
	{
		String troopID = props.getProperty("troopID");
		try {
			Scout scout = new Scout(troopID);
			transactionErrorMessage = "ERROR: Scout with troopID: " + troopID + " already exists!";
		}
		catch (InvalidPrimaryKeyException ex) {
			myScout = new Scout(props);
			myScout.update();
			transactionErrorMessage += myScout.getState("UpdateStatusMessage");

		}


	}

	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("TransactionError") == true)
		{
			return transactionErrorMessage;
		}

		return null;
	}

	//-----------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		// DEBUG System.out.println("DepositTransaction.sCR: key: " + key);

		if (key.equals("DoYourJob") == true)
		{
			doYourJob();
		}
		else
		if (key.equals("RegisterWithScoutInfo")==true)
		{
			processTransaction((Properties)value);
		}

		myRegistry.updateSubscribers(key, this);
	}

	/**
	 * Create the view of this class. And then the super-class calls
	 * swapToView() to display the view in the frame
	 */
	//------------------------------------------------------
	protected Scene createView()
	{
		Scene currentScene = myViews.get("RegisterScoutTransactionView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("RegisterScoutTransactionView", this);
			currentScene = new Scene(newView);
			myViews.put("RegisterScoutTransactionView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}

	//------------------------------------------------------




}
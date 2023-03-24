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
public class ScoutUpdateSearchTransaction extends Transaction
{

	private Account myAccount;
	private String depositAmount; // needed for GUI only

	// GUI Components

	private String transactionErrorMessage = "";
	private String accountUpdateStatusMessage = "";

	/**
	 * Constructor for this class.
	 *
	 *
	 */
	//----------------------------------------------------------
	public ScoutUpdateSearchTransaction()
			throws Exception
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
		dependencies.setProperty("CancelDeposit", "CancelTransaction");
		dependencies.setProperty("OK", "CancelTransaction");
		dependencies.setProperty("AccountNumber", "TransactionError");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * This method encapsulates all the logic of creating the account,
	 * verifying ownership, crediting, etc. etc.
	 */
	//----------------------------------------------------------
	public void processTransaction(Properties props)
	{
		creatAndShowSelectUpdateScoutView();
			/*	}
		//if (props.getProperty("AccountNumber") != null)
		//{
			/*String accountNumber = props.getProperty("AccountNumber");
			try
			{

				myAccount = createAccount(accountNumber);
				boolean isOwner = myAccount.verifyOwnership(myCust);
				if (isOwner == false)
				{
					transactionErrorMessage = "ERROR: Deposit Transaction: Not owner of selected account!!";
					new Event(Event.getLeafLevelClassName(this), "processTransaction",
							"Failed to verify ownership of account number : " + accountNumber + ".",
							Event.ERROR);
				}
				else
				{*/
					
			//}
			/*catch (InvalidPrimaryKeyException ex)
			{
				transactionErrorMessage = "ACCOUNT FAILURE: Contact bank immediately!!";
				new Event(Event.getLeafLevelClassName(this), "processTransaction",
						"Failed to create account for number : " + accountNumber + ". Reason: " + ex.toString(),
						Event.ERROR);

			}
		}
		else
		if (props.getProperty("Amount") != null)
		{
			String amount = props.getProperty("Amount");
			depositAmount = amount;

			myAccount.credit(amount);
			myAccount.update();
			accountUpdateStatusMessage = (String)myAccount.getState("UpdateStatusMessage");
			transactionErrorMessage = accountUpdateStatusMessage;

			createAndShowReceiptView();

		}*/
	}

	private Scene creatAndShowSelectUpdateScoutView() {
		System.out.println("reached");
		Scene currentScene = myViews.get("SelectUpdateScoutView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("SelectUpdateScoutView", this);
			currentScene = new Scene(newView);
			myViews.put("SelectUpdateScoutView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}

	//-----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("TransactionError") == true)
		{
			return transactionErrorMessage;
		}
		else
		if (key.equals("UpdateStatusMessage") == true)
		{
			return accountUpdateStatusMessage;
		}
		else
		if (key.equals("AccountNumberList") == true)
		{
			return myAccountIDs;
		}
		else
		if (key.equals("Account") == true)
		{
			return myAccount;
		}
		else
		if (key.equals("DepositAmount") == true)
		{
			return depositAmount;
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
		if (key.equals("SubmitSearchUpdateScout")==true)
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
		Scene currentScene = myViews.get("SearchUpdateScoutView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("SearchUpdateScoutView", this);
			currentScene = new Scene(newView);
			myViews.put("SearchUpdateScoutView", currentScene);

			return currentScene;
		}
		else
		{
			return currentScene;
		}
	}

	//------------------------------------------------------


	//------------------------------------------------------
	protected void createAndShowReceiptView()
	{

		// create our initial view
		View newView = ViewFactory.createView("DepositReceipt", this);
		Scene newScene = new Scene(newView);

		myViews.put("DepositReceipt", newScene);

		// make the view visible by installing it into the frame
		swapToView(newScene);
	}

}
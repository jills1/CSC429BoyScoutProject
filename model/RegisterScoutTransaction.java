package model;
// system imports
import javafx.scene.Scene;
import java.util.Properties;
// project imports
import exception.InvalidPrimaryKeyException;
import userinterface.View;
import userinterface.ViewFactory;
//==============================================================
public class RegisterScoutTransaction extends Transaction {
	private Scout myScout;
	private String transactionErrorMessage = "";
	public RegisterScoutTransaction()
	{
		super();
	}
	protected void setDependencies() {
		dependencies = new Properties();
		dependencies.setProperty("CancelRegisterScout", "CancelTransaction");
		dependencies.setProperty("RegisterWithScoutInfo", "TransactionError");
		myRegistry.setDependencies(dependencies);
	}
	public void processTransaction(Properties props) {
		String troopID = props.getProperty("troopID");
		try {
			Scout scout = new Scout(troopID);
			transactionErrorMessage = "ERROR: Scout with troopID: " + troopID + " already exists!";
		} catch (InvalidPrimaryKeyException ex) {
			myScout = new Scout(props);
			myScout.update();
			transactionErrorMessage += myScout.getState("UpdateStatusMessage");
		}
	}
	public Object getState(String key) {
		if (key.equals("TransactionError")) {
			return transactionErrorMessage;
		}
		return null;
	}
	public void stateChangeRequest(String key, Object value) {
		if (key.equals("DoYourJob")) {
			doYourJob();
		} else if (key.equals("RegisterWithScoutInfo")) {
			processTransaction((Properties)value);
		}
		myRegistry.updateSubscribers(key, this);
	}
	protected Scene createView() {
		Scene currentScene = myViews.get("RegisterScoutView");
		if (currentScene == null) {
			View newView = ViewFactory.createView("RegisterScoutView", this);
			assert newView != null;
			currentScene = new Scene(newView);
			myViews.put("RegisterScoutView", currentScene);
			return currentScene;
		} else {
			return currentScene;
		}
	}
}
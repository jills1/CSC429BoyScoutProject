// specify the package
package model;

// system imports
import java.util.Vector;
import javax.swing.JFrame;

// project imports

/** The class containing the TransactionFactory for the ATM application */
//==============================================================
public class TransactionFactory
{

	/**
	 *
	 */
	//----------------------------------------------------------
	public static Transaction createTransaction(String transType) {
		//Transaction retValue = null;
		switch(transType) {
			case "RegisterScout": return new RegisterScoutTransaction();
			//case "UpdateScout": return new UpdateScoutTransaction();
			case "RemoveScout": return new RemoveScoutTransaction();
			case "AddTree": return new AddTreeTransaction();
			case "UpdateTree": return new UpdateTreeTransaction();
			case "RemoveTree": return new RemoveTreeTransaction();
			case "AddTreeType": return new AddTreeTypeTransaction();
			case "UpdateTreeType": return new UpdateTreeTypeTransaction();
			//case "StartShift": return new StartShiftTransaction();
			//case "EndShift": return new EndShiftTransaction();
			//case "SellTree": return new SellTreeTransaction();
			//case "Logout": return new LogoutTransaction();
			default: return null;
		}
		//return retValue;
	}
}

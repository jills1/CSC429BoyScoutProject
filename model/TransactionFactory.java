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
	public static Transaction createTransaction(String transType)

	{
		Transaction retValue = null;

		if (transType.equals("RegisterScout") == true)
		{
			retValue = new RegisterScoutTransaction();
		}
		else
		if (transType.equals("AddTree") == true)
		{
			retValue = new AddTreeTransaction();
		}
		else
		if (transType.equals("UpdateScout") == true)
		{
			retValue = new UpdateScoutTransaction();
		}

		else
		if (transType.equals("RemoveScout") == true)
		{
			retValue = new RemoveScoutTransaction();
		}

		else
		if (transType.equals("AddTreeType") == true)
		{
			retValue = new AddTreeTypeTransaction();
		}
		else
		if (transType.equals("UpdateTreeType") == true)
		{
			retValue = new UpdateTreeTypeTransaction();
		}

		else
		if (transType.equals("UpdateTree") == true)
		{
			retValue = new UpdateTreeTransaction();
		}
		else
		if (transType.equals("RemoveTree") == true)
		{
			retValue = new RemoveTreeTransaction();
		}
		else
		if (transType.equals("StartShift") == true)
		{
			retValue = new StartShiftTransaction();
		}
		else
		if (transType.equals("SellTree") == true)
		{
			retValue = new SellTreeTransaction();
		}
		else
		if (transType.equals("EndShift") == true)
		{
			retValue = new EndShiftTransaction();
		}
		return retValue;
	}
}

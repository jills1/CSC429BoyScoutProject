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
		throws Exception
	{
		Transaction retValue = null;

		if (transType.equals("RegisterScout") == true)
		{
			retValue = new RegisterScoutTransaction();
		}
		else
		if (transType.equals("AddTreeType") == true)
		{
			retValue = new AddTreeTypeTransaction();
		}

		else
		if (transType.equals("RemoveScout") == true)
		{
			retValue = new RemoveScoutTransaction();
		}
		/*
		else
		if (transType.equals("BalanceInquiry") == true)
		{
			retValue = new BalanceInquiryTransaction(cust);
		}
		else
		if (transType.equals("ImposeServiceCharge") == true)
		{
			retValue = new ImposeServiceChargeTransaction(cust);
		}
*/
		return retValue;
	}
}

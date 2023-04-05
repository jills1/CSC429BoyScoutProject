package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
		if(viewName.equals("TellerView") == true)
		{
			return new TellerView(model);
		}
		else if(viewName.equals("TLCView") == true)
		{
			return new TLCView(model);
		}
		else if(viewName.equals("TreeCollectionView") == true)
		{
			return new TreeCollectionView(model);
		}
		else if(viewName.equals("AccountView") == true)
		{
			return new AccountView(model);
		}
		else if(viewName.equals("AccountHolderIDEntryView") == true)
		{
			return new AccountHolderIDEntryView(model);
		}
		else if(viewName.equals("RegisterScoutTransactionView") == true)
		{
			return new RegisterScoutTransactionView(model);
		}
		/*else if(viewName.equals("AddTreeTypeTransactionView") == true)
		{
			return new AddTreeTypeTransactionView(model);
		}*/
		else if(viewName.equals("RemoveScoutTransactionView") == true)
		{
			return new RemoveScoutTransactionView(model);
		}
		else if(viewName.equals("ScoutCollectionView") == true)
		{
			return new ScoutCollectionView(model);
		}
		else if(viewName.equals("RemoveTreeTransactionView") == true)
		{
			return new RemoveTreeTransactionView(model);
		}
		else if(viewName.equals("RemoveView") == true)
		{
			return new RemoveView(model);
		}
		else if(viewName.equals("DepositReceipt") == true)
		{
			return new DepositReceipt(model);
		}
		else if(viewName.equals("TransferReceipt") == true)
		{
			return new TransferReceipt(model);
		}
		else
			return null;
	}


	/*
	public static Vector createVectorView(String viewName, IModel model)
	{
		if(viewName.equals("SOME VIEW NAME") == true)
		{
			//return [A NEW VECTOR VIEW OF THAT NAME TYPE]
		}
		else
			return null;
	}
	*/

}

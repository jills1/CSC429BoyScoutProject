package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model) {

		if(viewName.equals("RegisterScoutTransactionView") == true)
		{
			return new RegisterScoutTransactionView(model);
		}
		else if(viewName.equals("UpdateScoutTransactionView") == true)
		{
			return new UpdateScoutTransactionView(model);
		}
		else if(viewName.equals("RemoveScoutTransactionView") == true)
		{
			return new RemoveScoutTransactionView(model);
		}
		else if(viewName.equals("AddTreeTransactionView") == true)
		{
			return new AddTreeTransactionView(model);
		}
		else if(viewName.equals("UpdateTreeTransactionView") == true)
		{
			return new UpdateTreeTransactionView(model);
		}
		else if(viewName.equals("RemoveTreeTransactionView") == true)
		{
			return new RemoveTreeTransactionView(model);
		}
		else if(viewName.equals("AddTreeTypeTransactionView") == true)
		{
			return new AddTreeTypeTransactionView(model);
		}
		else if(viewName.equals("UpdateTreeTypeTransactionView") == true)
		{
			return new UpdateTreeTypeTransactionView(model);
		}
		else if(viewName.equals("StartShiftTransactionView") == true)
		{
			return new StartShiftTransactionView(model);
		}
		else if(viewName.equals("EndShiftTransactionView") == true)
		{
			return new EndShiftTransactionView(model);
		}
		else if(viewName.equals("SellTreeTransactionView") == true)
		{
			return new SellTreeTransactionView(model);
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

package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
		if(viewName.equals("UpdateTreeView") == true)
		{
			return new UpdateTreeView(model);
		}
		else if(viewName.equals("UpdateTreeFormView") == true)
		{
			return new UpdateTreeFormView(model);
		}
		else if(viewName.equals("TLCView") == true)
		{
			return new TLCView(model);
		}
		else if(viewName.equals("UpdateTreeTypeFormView") == true)
		{
			return new UpdateTreeTypeFormView(model);
		}
		else if(viewName.equals("UpdateTreeTypeView") == true)
		{
			return new UpdateTreeTypeView(model);
		}
		else if(viewName.equals("AddTreeView") == true)
		{
			return new AddTreeView(model);
		}
		else if(viewName.equals("RegisterScoutTransactionView") == true)
		{
			return new RegisterScoutTransactionView(model);
		}
		else if(viewName.equals("AddTreeTypeTransactionView") == true)
		{
			return new AddTreeTypeTransactionView(model);
		}
		else if(viewName.equals("RemoveScoutTransactionView") == true)
		{
			return new RemoveScoutTransactionView(model);
		}
		else if(viewName.equals("ScoutCollectionView") == true)
		{
			return new ScoutCollectionView(model);
		}
		else if(viewName.equals("ScoutSelectedView") == true)
		{
			return new ScoutSelectedView(model);
		}
		else if(viewName.equals("AddTreeTypeTransactionView") == true)
		{
			return new AddTreeTypeTransactionView(model);
		}
		else if(viewName.equals("UpdateTreeTransactionView") == true)
		{
			return new UpdateTreeTransactionView(model);
		}
		else if(viewName.equals("UpdateTreeFormTransactionView") == true)
		{
			return new UpdateTreeFormTransactionView(model);
		}
		else if(viewName.equals("SelectedScoutToUpdateView") == true)
		{
			return new SelectedScoutToUpdateView(model);
		}
		else if(viewName.equals("SearchUpdateScoutView") == true)
		{
			return new SearchUpdateScoutView(model);
		}
		else if(viewName.equals("SelectUpdateScoutView") == true)
		{
			return new SelectUpdateScoutView(model);
		}
		else if(viewName.equals("UpdateScoutInfoView") == true)
		{
			return new UpdateScoutInfoView(model);
		}
		else if(viewName.equals("RemoveTreeTransactionView") == true)
		{
			return new RemoveTreeTransactionView(model);
		}
		else if(viewName.equals("RemoveView") == true)
		{
			return new RemoveView(model);
		}
		else if(viewName.equals("EndShiftDataView") == true)
		{
			return new EndShiftDataView(model);
		}
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

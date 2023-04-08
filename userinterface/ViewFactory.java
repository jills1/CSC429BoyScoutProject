package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model) {
		switch (viewName) {
			case "TLCView": return new TLCView(model);
			//--
			case "RegisterScoutView": return new RegisterScoutView(model);
			case "UpdateScoutView": return new UpdateScoutView(model);
			case "RemoveScoutView": return new RemoveScoutView(model);
			case "AddTreeView": return new AddTreeView(model);
			case "UpdateTreeView": return new UpdateTreeView(model);
			case "RemoveTreeView": return new RemoveTreeView(model);
			//case "AddTreeTypeTransactionView": return new UpdateScoutInfoView(model);
			//case "UpdateTreeTypeTransactionView": return new UpdateScoutListView(model);
			//case "StartShiftTransactionView": return new RemoveScoutIDView(model);
			//case "RemoveScoutListView": return new RemoveScoutListView(model);
			//case "RemoveScoutConfirmView": return new RemoveScoutConfirmView(model);
			case "UpdateTreeTypeView": return new UpdateTreeTypeView(model);
			//--
			case "UpdateTreeFormView": return new UpdateTreeFormView(model);
			case "UpdateTreeTypeFormView": return new UpdateTreeTypeFormView(model);
			case "SelectUpdateScoutView": return new SelectUpdateScoutView(model);
			case "SearchUpdateScoutView": return new SearchUpdateScoutView(model);
			//case "RemoveScoutView2": return new RemoveScoutView2(model);
		}
		return null;
	}
}
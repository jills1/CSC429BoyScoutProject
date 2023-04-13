package userinterface;

import impresario.IModel;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model) {
		switch (viewName) {
			case "TLCView": return new TLCView(model);
			//----------------------------------------
			case "RegisterScoutView": return new RegisterScoutView(model);
			//--
			case "UpdateScoutView": return new UpdateScoutView(model);
			//--
			case "RemoveScoutView": return new RemoveScoutView(model);
			//--
			case "AddTreeView": return new AddTreeView(model);
			//--
			case "UpdateTreeView": return new UpdateTreeView(model);
			//--
			case "RemoveTreeView": return new RemoveTreeView(model);
			//--
			//case "AddTreeTypeView": return new AddTreeTypeView(model);
			//--
			case "UpdateTreeTypeView": return new UpdateTreeTypeView(model);
			//--
			//case "StartShiftView": return new StartShiftView(model);
			//--
			//case "EndShiftView": return new EndShiftView(model);
			//--
			//case "SellTreeView": return new SellTreeView(model);
			//----------------------------------------
			case "UpdateTreeFormView": return new UpdateTreeFormView(model);
			case "UpdateTreeTypeFormView": return new UpdateTreeTypeFormView(model);
			case "SelectUpdateScoutView": return new SelectUpdateScoutView(model);
			case "SearchUpdateScoutView": return new SearchUpdateScoutView(model);
			case "ScoutCollectionView": return new ScoutCollectionView(model);
			case "ScoutSelectedView": return new ScoutSelectedView(model);
			//case "RemoveScoutView2": return new RemoveScoutView2(model);
			//case "EndShiftFormView": return new EndShiftFormView(model);
		}
		return null;
	}
}
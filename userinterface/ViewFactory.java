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
		
		else if(viewName.equals("AccountHolderIDEntryView") == true)
		{
			return new AccountHolderIDEntryView(model);
		}
		else if(viewName.equals("RegisterScoutTransactionView") == true)
		{
			return new RegisterScoutTransactionView(model);
		}
		
		else if(viewName.equals("SearchUpdateScoutView") == true)
		{
			return new SearchUpdateScoutView(model);
		}
		else if(viewName.equals("WithdrawTransactionView") == true)
		{
			return new WithdrawTransactionView(model);
		}
		else if(viewName.equals("TransferTransactionView") == true)
		{
			return new TransferTransactionView(model);
		}
		else if(viewName.equals("BalanceInquiryTransactionView") == true)
		{
			return new BalanceInquiryTransactionView(model);
		}
		else if(viewName.equals("BalanceInquiryReceipt") == true)
		{
			return new BalanceInquiryReceipt(model);
		}
		else if(viewName.equals("WithdrawReceipt") == true)
		{
			return new WithdrawReceipt(model);
		}
		else if(viewName.equals("DepositReceipt") == true)
		{
			return new DepositReceipt(model);
		}
		else if(viewName.equals("TransferReceipt") == true)
		{
			return new TransferReceipt(model);
		}
		else if(viewName.equals("SelectUpdateScoutView") == true)
		{
			return new SelectUpdateScoutView(model);
		}
		else if(viewName.equals("UpdateScoutInfoView") == true)
		{
			return new UpdateScoutInfoView(model);
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

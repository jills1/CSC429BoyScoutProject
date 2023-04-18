// specify the package
package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

// project imports
import impresario.IModel;

/** The class containing the Transaction Choice View  for the ATM application */
//==============================================================
public class TLCView extends View
{

	// other private data
	private final int labelWidth = 120;
	private final int labelHeight = 25;

	// GUI components

	private Button registerScoutButton;
	private Button updateScoutButton;
	private Button removeScoutButton;
	private Button addTreeButton;
	private Button updateTreeButton;
	private Button removeTreeButton;
	private Button addTreeTypeButton;
	private Button updateTreeTypeButton;
	private Button startShiftButton;
	private Button endShiftButton;
	private Button sellTreeButton;


	private Button cancelButton;

	private MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public TLCView(IModel teller)
	{
		super(teller, "TransactionChoiceView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// Add a title for this panel
		container.getChildren().add(createTitle());
		
		// how do you add white space?
		container.getChildren().add(new Label(" "));

		// create our GUI components, add them to this Container
		container.getChildren().add(createFormContents());

		container.getChildren().add(createStatusLog("             "));

		getChildren().add(container);

		populateFields();

		myModel.subscribe("TransactionError", this);
	}

	// Create the labels and fields
	//-------------------------------------------------------------
	private VBox createTitle()
	{
		VBox container = new VBox(10);
		Text titleText = new Text("        Troop209          ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setWrappingWidth(300);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);
		container.getChildren().add(titleText);

		String accountHolderGreetingName = (String)myModel.getState("Name");
		Text welcomeText = new Text("Welcome" + accountHolderGreetingName + "!");
		welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		welcomeText.setWrappingWidth(300);
		welcomeText.setTextAlignment(TextAlignment.CENTER);
		welcomeText.setFill(Color.DARKGREEN);
		container.getChildren().add(welcomeText);

		Text inquiryText = new Text("What do you wish to do today?");
		inquiryText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		inquiryText.setWrappingWidth(300);
		inquiryText.setTextAlignment(TextAlignment.CENTER);
		inquiryText.setFill(Color.BLACK);
		container.getChildren().add(inquiryText);
	
		return container;
	}


	// Create the navigation buttons
	//-------------------------------------------------------------
	private VBox createFormContents()
	{

		VBox container = new VBox(15);

		// create the buttons, listen for events, add them to the container
		HBox dCont = new HBox(10);
		dCont.setAlignment(Pos.CENTER);
		registerScoutButton = new Button("Register a Scout");
		registerScoutButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		registerScoutButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	myModel.stateChangeRequest("RegisterScout", null);
            	     }
        	});
		dCont.getChildren().add(registerScoutButton);

		container.getChildren().add(dCont);

		HBox wCont = new HBox(10);
		wCont.setAlignment(Pos.CENTER);
		updateScoutButton = new Button("Update a Scout");
		updateScoutButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		updateScoutButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	myModel.stateChangeRequest("UpdateScout", null);
            	     }
        	});
		wCont.getChildren().add(updateScoutButton);

		container.getChildren().add(wCont);

		HBox tCont = new HBox(10);
		tCont.setAlignment(Pos.CENTER);
		removeScoutButton = new Button("Remove a Scout");
		removeScoutButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		removeScoutButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	myModel.stateChangeRequest("RemoveScout", null);
            	     }
        	});
		tCont.getChildren().add(removeScoutButton);

		container.getChildren().add(tCont);

		HBox biCont = new HBox(10);
		biCont.setAlignment(Pos.CENTER);
		addTreeButton = new Button("Add a Tree");
		addTreeButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		addTreeButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	myModel.stateChangeRequest("AddTree", null);
            	     }
        	});
		biCont.getChildren().add(addTreeButton);

		container.getChildren().add(biCont);

		HBox iscCont = new HBox(10);
		iscCont.setAlignment(Pos.CENTER);
		updateTreeButton = new Button("Update a Tree");
		updateTreeButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		updateTreeButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	 myModel.stateChangeRequest("UpdateTree", null);
            	     }
        	});
		iscCont.getChildren().add(updateTreeButton);

		container.getChildren().add(iscCont);

		HBox d1Cont = new HBox(10);
		d1Cont.setAlignment(Pos.CENTER);
		removeTreeButton = new Button("Remove a Tree");
		removeTreeButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		removeTreeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("RemoveTree", null);
			}
		});
		d1Cont.getChildren().add(removeTreeButton);

		container.getChildren().add(d1Cont);

		HBox w1Cont = new HBox(10);
		w1Cont.setAlignment(Pos.CENTER);
		addTreeTypeButton = new Button("Add a Tree Type");
		addTreeTypeButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		addTreeTypeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("AddTreeType", null);
			}
		});
		w1Cont.getChildren().add(addTreeTypeButton);

		container.getChildren().add(w1Cont);

		HBox t1Cont = new HBox(10);
		t1Cont.setAlignment(Pos.CENTER);
		updateTreeTypeButton = new Button("Update a Tree Type");
		updateTreeTypeButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		updateTreeTypeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("UpdateTreeType", null);
			}
		});
		t1Cont.getChildren().add(updateTreeTypeButton);

		container.getChildren().add(t1Cont);

		HBox bi1Cont = new HBox(10);
		bi1Cont.setAlignment(Pos.CENTER);
		startShiftButton = new Button("Start a Shift");
		startShiftButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		startShiftButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("StartShift", null);
			}
		});
		bi1Cont.getChildren().add(startShiftButton);

		container.getChildren().add(bi1Cont);

		HBox isc1Cont = new HBox(10);
		isc1Cont.setAlignment(Pos.CENTER);
		endShiftButton = new Button("End A Shift");
		endShiftButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		endShiftButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("EndShift", null);
			}
		});
		isc1Cont.getChildren().add(endShiftButton);

		container.getChildren().add(isc1Cont);

		HBox isc11Cont = new HBox(10);
		isc11Cont.setAlignment(Pos.CENTER);
		sellTreeButton = new Button("Sell a Tree");
		sellTreeButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		sellTreeButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				myModel.stateChangeRequest("SellTree", null);
			}
		});
		isc11Cont.getChildren().add(sellTreeButton);

		container.getChildren().add(isc11Cont);

		HBox doneCont = new HBox(10);
		doneCont.setAlignment(Pos.CENTER);
		cancelButton = new Button("Logout");
		cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	myModel.stateChangeRequest("Logout", null);    
            	     }
        	});
		doneCont.getChildren().add(cancelButton);

		container.getChildren().add(doneCont);

		return container;
	}

	// Create the status log field
	//-------------------------------------------------------------
	private MessageView createStatusLog(String initialMessage)
	{

		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
	public void populateFields()
	{

	}
	

	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		if (key.equals("TransactionError") == true)
		{
			// display the passed text
			displayErrorMessage((String)value);
		}
	}

	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}
}



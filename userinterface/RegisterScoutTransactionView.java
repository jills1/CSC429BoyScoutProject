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
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Vector;

// project imports
import impresario.IModel;
import model.Scout;

/** The class containing the Register Scout Transaction View  for the Tree Sales application */
//==============================================================
public class RegisterScoutTransactionView extends View
{

	// Model

	// GUI components
	private TextField firstName;
	private TextField middleName;
	private TextField lastName;
	private DatePicker dateOfBirth; // Changed to DatePicker
	private TextField phoneNumber;
	private TextField email;
	private TextField troopID;
	private ComboBox<String> status; // Changed to ComboBox

	private Button submitButton;
	private Button cancelButton;

	// For showing error message
	private MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public RegisterScoutTransactionView(IModel trans)
	{
		super(trans, "RegisterScoutTransactionView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// create our GUI components, add them to this panel
		container.getChildren().add(createTitle());
		container.getChildren().add(createFormContent());

		// Error message area
		container.getChildren().add(createStatusLog("                          "));

		getChildren().add(container);

		populateFields();

		myModel.subscribe("TransactionError", this);
	}


	// Create the label (Text) for the title
	//-------------------------------------------------------------
	private Node createTitle()
	{

		Text titleText = new Text("       Troop 209          ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);

		return titleText;
	}

	// Create the main data entry fields
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(10);

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Label firstNameLabel = new Label("First Name : ");
		grid.add(firstNameLabel, 0, 0);

		firstName = new TextField();
		firstName.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(firstName, 1, 0);

		Label lastNameLabel = new Label("Last Name : ");
		grid.add(lastNameLabel, 0, 1);

		lastName = new TextField();
		lastName.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(lastName, 1, 1);

		Label middleNameLabel = new Label("Middle Name : ");
		grid.add(middleNameLabel, 0, 2);

		middleName = new TextField();
		middleName.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(middleName, 1, 2);

		Label dateOfBirthLabel = new Label("Date of Birth : ");
		grid.add(dateOfBirthLabel, 0, 3);

		dateOfBirth = new DatePicker();

		grid.add(dateOfBirth, 1, 3);

		Label phoneNumLabel = new Label("Phone Number : ");
		grid.add(phoneNumLabel, 0, 4);

		phoneNumber = new TextField();
		phoneNumber.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(phoneNumber, 1, 4);

		Label emailLabel = new Label("Email : ");
		grid.add(emailLabel, 0, 5);

		email = new TextField();
		email.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(email, 1, 5);

		Label troopIDLabel = new Label("Troop ID : ");
		grid.add(troopIDLabel, 0, 6);

		troopID = new TextField();
		troopID.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				processAction(e);
			}
		});
		grid.add(troopID, 1, 6);

		Label statusLabel = new Label("Status : ");
		grid.add(statusLabel, 0, 7);

		status = new ComboBox<String>(); // Changed to ComboBox
		status.getItems().addAll("Active", "Inactive");

		grid.add(status, 1, 7);

		submitButton = new Button("Submit");
		submitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				clearErrorMessage();

				processAction(e);
			}
		});

		cancelButton = new Button("Back");
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {

				//----------------------------------------------------------
				clearErrorMessage();
				myModel.stateChangeRequest("CancelRegisterScout", null);
			}
		});

		HBox btnContainer = new HBox(100);
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.getChildren().add(submitButton);
		btnContainer.getChildren().add(cancelButton);

		vbox.getChildren().add(grid);
		vbox.getChildren().add(btnContainer);

		return vbox;
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



	//----------------------------------------------------------
	private void processAction(Event evt)
	{
		//clearErrorMessage();

		String lastNameEntered = lastName.getText();
		String firstNameEntered = firstName.getText();
		String middleNameEntered = middleName.getText();
		String dateOfBirthEntered = (dateOfBirth.getValue() != null) ? dateOfBirth.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null; // Changed to DatePicker
		String phoneNumberEntered = phoneNumber.getText();
		String emailEntered = email.getText();
		String troopIDEntered = troopID.getText();
		String statusEntered = status.getValue(); // Changed to ComboBox


		if ((lastNameEntered == null) || (lastNameEntered.length() == 0))
		{
			displayErrorMessage("Please enter a last name");
		}
		else if ((firstNameEntered == null) || (firstNameEntered.length() == 0))
		{
			displayErrorMessage("Please enter a first name");
		}
		else if ((middleNameEntered == null) || (middleNameEntered.length() == 0))
		{
			displayErrorMessage("Please enter a middle name");
		}
		else if ((dateOfBirthEntered == null) || (dateOfBirthEntered.length() == 0))
		{
			displayErrorMessage("Please enter a valid birth date");
		}
		else if ((phoneNumberEntered == null) || (phoneNumberEntered.length() == 0))
		{
			displayErrorMessage("Please enter a phone number");
		}
		else if(phoneNumberEntered.length()!=10)
		{
			displayErrorMessage("Phone number must be in format xxxxxxxxxx");
		}
		else if ((emailEntered == null) || (emailEntered.length() == 0))
		{
			displayErrorMessage("Please enter an email");
		}
		else if ((troopIDEntered == null) || (troopIDEntered.length() == 0))
		{
			displayErrorMessage("Please enter a troopID");
		}
		else if ((statusEntered == null) || (statusEntered.length() == 0))
		{
			displayErrorMessage("Please enter a valid status");
		}
		else
			processScoutInfo(lastNameEntered,firstNameEntered,middleNameEntered,dateOfBirthEntered,phoneNumberEntered,emailEntered,troopIDEntered,statusEntered);
	}

	//---------------------------------------------------------------------------------------
	private void processScoutInfo(String lastName, String firstName, String middleName, String dateOfBirth, String phoneNumber,String email, String troopID, String status)
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDateTime now = LocalDateTime.now();

		Properties props = new Properties();
		props.setProperty("lastName", lastName);
		props.setProperty("firstName", firstName);
		props.setProperty("middleName", middleName);
		props.setProperty("dateOfBirth", dateOfBirth);
		props.setProperty("phoneNumber", phoneNumber);
		props.setProperty("email", email);
		props.setProperty("troopID", troopID);
		props.setProperty("status", status);
		props.setProperty("dateStatusUpdated",dtf.format(now));
		myModel.stateChangeRequest("RegisterWithScoutInfo", props);

	}
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}


	/**
	 * Required by interface, but has no role here
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		clearErrorMessage();
		
		if (key.equals("TransactionError"))
		{
			String messageToDisplay = (String)value;
			if (messageToDisplay.startsWith("ERR")) {
				displayErrorMessage(messageToDisplay);
			}
			else {
				displayMessage(messageToDisplay);
			}

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

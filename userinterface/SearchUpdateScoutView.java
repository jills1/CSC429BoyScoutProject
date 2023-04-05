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
import javafx.scene.control.ComboBox;
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

import java.util.Properties;

// project imports
import impresario.IModel;
import model.Scout;

/** The class containing the Account Holder ID Entry View for the 'Impose Service Charge' functionality of the  ATM application */
//==============================================================
public class SearchUpdateScoutView extends View
{
	//Element to process data
	protected Properties myProps;

	// GUI components
	protected TextField firstName;
    protected TextField lastName;

	protected Button cancelButton;
	private Button submitButton;

	// For showing error message
	protected MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public SearchUpdateScoutView(IModel account)
	{
		super(account, "SearchUpdateScoutView");

		// create a container for showing the contents
		VBox container = new VBox(10);
		container.setPadding(new Insets(15, 5, 5, 5));

		// create our GUI components, add them to this panel
		container.getChildren().add(createTitle());
		container.getChildren().add(createFormContent());

		// Error message area
		container.getChildren().add(createStatusLog("                                            "));

		getChildren().add(container);
		
		populateFields();

	}

	// Create the title container
	//-------------------------------------------------------------
	private Node createTitle()
	{
		Text titleText = new Text("       Search for Scout          ");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFill(Color.DARKGREEN);

		return titleText;
	}

	// Create the main form content
	//-------------------------------------------------------------
	private VBox createFormContent()
	{
		VBox vbox = new VBox(10);

		GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
       	grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text prompt = new Text("Please enter the first name and last name of the desired Scout below");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text scoutFNLabel = new Text("  First Name        : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        scoutFNLabel.setFont(myFont);
        grid.add(scoutFNLabel, 0, 1);

        firstName = new TextField();
        firstName.setEditable(true);
        firstName.setOnAction(new EventHandler<ActionEvent>() {

  		     @Override
  		     public void handle(ActionEvent e) {
  		    	clearErrorMessage();
  		     	myModel.stateChangeRequest("FirstNameEntered", firstName.getText());
       	     }
        });
        
        grid.add(firstName, 1, 1);


            Text scoutLNLabel = new Text("  Last Name        : ");
            scoutLNLabel.setFont(myFont);
            grid.add(scoutLNLabel, 0, 2);
    
            lastName = new TextField();
            lastName.setEditable(true);
            lastName.setOnAction(new EventHandler<ActionEvent>() {
    
                   @Override
                   public void handle(ActionEvent e) {
                      clearErrorMessage();
                       myModel.stateChangeRequest("LastNameEntered", firstName.getText());
                    }
            });
            
            grid.add(lastName, 1, 2);

            HBox doneCont = new HBox(10);
            doneCont.setAlignment(Pos.CENTER);

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
				/**
				 * Process the Cancel button.
				 * The ultimate result of this action is that the transaction will tell the teller to
				 * to switch to the transaction choice view. BUT THAT IS NOT THIS VIEW'S CONCERN.
				 * It simply tells its model (controller) that the deposit transaction was canceled, and leaves it
				 * to the model to decide to tell the teller to do the switch back.
				 */
				//----------------------------------------------------------
				clearErrorMessage();
				myModel.stateChangeRequest("CancelDeposit", null);
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
	// ----------------------------------------------------------------------------------

	private void processAction(ActionEvent e) {
		
		// Getting the results from the text fields
		String lastNameEntry = lastName.getText();
		String firstNameEntry = firstName.getText();

		//Send the properties
		sendScoutSearch(lastNameEntry, firstNameEntry);
		
	}
	// --------------------------------------------------------------------------------------------------------
	private void sendScoutSearch(String lastName, String firstName) { 

		myProps = new Properties();
		//Setting up the search parameter in the 
		myProps.setProperty("lastName", lastName);
		myProps.setProperty("firstName", firstName);
		System.out.println("Props collected");
		
		System.out.println("Props sent");
		//Sending the search
		myModel.stateChangeRequest("SubmitSearchUpdateScout", myProps);
	}
	//-------------------------------------------------------------------------------------------------------
	// Create the status log field
	//-------------------------------------------------------------
	protected MessageView createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
	public void populateFields()
	{

	}

	/**
	 * Update method
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
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
	 * Display info message
	 */
	//----------------------------------------------------------
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
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

//---------------------------------------------------------------
//	Revision History:
//



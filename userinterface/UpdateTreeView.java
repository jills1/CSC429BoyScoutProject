package userinterface;
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
import java.util.Vector;

// project imports
import impresario.IModel;
import model.Scout;
//==============================================================
public class UpdateTreeView extends View {
    private TextField barcode;
    private Button submitButton;
    private Button cancelButton;
    private MessageView statusLog;
    //-------------------------------------------------------------
    public UpdateTreeTransactionView(IModel trans) {
        super(trans, "UpdateTreeTransactionView");
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
    }
    //-------------------------------------------------------------
    // Creates Title for Container
    private Node createTitle() {
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
/--------------------------------------------------------------------
        Label firstNameLabel = new Label("First Name : ");
        grid.add(firstNameLabel, 0, 0);

        firstName = new TextField();
        firstName.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(barcode, 1, 0);
//------------------------------------------------------------------
        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();

                processAction(e);
            }
        });
//----------------------------------------------------------------
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


    /**
     * Process account number selected by user.
     * Action is to pass this info on to the transaction object.
     */
    //----------------------------------------------------------
    private void processAction(Event evt)
    {
        //clearErrorMessage();

        String lastNameEntered = lastName.getText();
        String firstNameEntered = firstName.getText();
        String middleNameEntered = middleName.getText();
        String dateOfBirthEntered = dateOfBirth.getText();
        String phoneNumberEntered = phoneNumber.getText();
        String emailEntered = email.getText();
        String troopIDEntered = troopID.getText();

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
        else if ((emailEntered == null) || (emailEntered.length() == 0))
        {
            displayErrorMessage("Please enter an email");
        }
        else if ((troopIDEntered == null) || (troopIDEntered.length() == 0))
        {
            displayErrorMessage("Please enter a troopID");
        }
        else
            processScoutInfo(lastNameEntered,firstNameEntered,middleNameEntered,dateOfBirthEntered,phoneNumberEntered,emailEntered,troopIDEntered);
    }
    private void processScoutInfo(String lastName, String firstName, String middleName, String dateOfBirth, String phoneNumber,String email, String troopID)
    {
        Properties props = new Properties();
        props.setProperty("lastName", lastName);
        props.setProperty("firstName", firstName);
        props.setProperty("middleName", middleName);
        props.setProperty("dateOfBirth", dateOfBirth);
        props.setProperty("phoneNumber", phoneNumber);
        props.setProperty("email", email);
        props.setProperty("troopID", troopID);
        myModel.stateChangeRequest("RegisterWithScoutInfo", props);
        Scout scout = new Scout(props);
        scout.update();
        displayMessage("Successfully added new Scout");
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
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

/** The class containing the Start Shift Transaction View  for the Tree Sales application */
//==============================================================
public class StartShiftTransactionView extends View
{

    // Model

    // GUI components

    private DatePicker date; // Changed to DatePicker
    private TextField startTime;
    private TextField endTime;

    private TextField companion;
    private TextField companionHours;
    private ComboBox<String> listOfScouts;


    private Button submitButton;
    private Button cancelButton;

    // For showing error message
    private MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public StartShiftTransactionView(IModel trans)
    {
        super(trans, "StartShiftTransactionView");

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



        Label dateLabel = new Label("Date of Shift Start : ");
        grid.add(dateLabel, 0, 0);

        date = new DatePicker();

        grid.add(date, 1, 0);

        Label startLabel = new Label("Start Time : ");
        grid.add(startLabel, 0, 1);

        startTime = new TextField();
        startTime.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(startTime, 1, 1);

        Label endLabel = new Label("End Time : ");
        grid.add(endLabel, 0, 2);

        endTime = new TextField();
        endTime.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(endTime, 1, 2);

        Label statusLabel = new Label("List of Scouts : ");
        grid.add(statusLabel, 0, 4);
        listOfScouts = new ComboBox<String>();
        listOfScouts.getItems().addAll();//put Scouts in here;
        grid.add(listOfScouts, 1, 4);


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

        String dateEntered = (date.getValue() != null) ? date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null; // Changed to DatePicker
        String startTimeEntered = startTime.getText();
        String endTimeEntered = endTime.getText();
        String listOfScoutsEntered = listOfScouts.getValue();




         if ((dateEntered == null) || (dateEntered.length() == 0))
        {
            displayErrorMessage("Please enter a valid date");
        }
        else if ((startTimeEntered == null) || (startTimeEntered.length() == 0))
        {
            displayErrorMessage("Please enter a start time");
        }

        else if ((endTimeEntered == null) || (endTimeEntered.length() == 0))
        {
            displayErrorMessage("Please enter an end time");
        }

        else if ((listOfScoutsEntered == null) || (listOfScoutsEntered.length()!=5))
        {
            displayErrorMessage("Scouts selected can't be null");
        }

        //else
           // processScoutInfo(lastNameEntered,firstNameEntered,middleNameEntered,dateOfBirthEntered,phoneNumberEntered,emailEntered,troopIDEntered);
    }

    //---------------------------------------------------------------------------------------
    private void processScoutInfo(String lastName, String firstName, String middleName, String dateOfBirth, String phoneNumber,String email, String troopID)
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
        props.setProperty("status", "Active");
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

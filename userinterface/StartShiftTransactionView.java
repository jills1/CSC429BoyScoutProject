// specify the package
package userinterface;

// system imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

// project imports
import impresario.IModel;
import model.Account;
import model.AccountCollection;
import model.Scout;
import model.ScoutCollection;

/** The class containing the Start Shift Transaction View  for the Tree Sales application */
//==============================================================
public class StartShiftTransactionView extends View
{

    // Model

    // GUI components

    private DatePicker date; // Changed to DatePicker
    private TextField startTime;
    private TextField endTime;
    private TextField startCash;
    private HashMap<String, String> scoutNameToID = new HashMap<>();

    private TextField scoutStartTime;
    private TextField scoutEndTime;
    protected TableView<ScoutTableModel> tableOfScouts;


    private TextField companion;
    private TextField companionHours;
    private ComboBox<String> listOfScouts;


    private Button submitButton;
    private Button startSessionButton;
    private Button cancelButton;
    private Button addButton;

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
        myModel.subscribe("ScoutAddedForShift", this);
    }
    protected void populateFields()
    {
        populateListOfScoutsComboBox();
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

        Label scashLabel = new Label("Starting Cash : ");
        grid.add(scashLabel, 0, 3);

        startCash = new TextField();
        startCash.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(startCash, 1, 3);

        startSessionButton = new Button("Start Shift");
        grid.add(startSessionButton,2,3);
        startSessionButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();

                processAction(e);
            }
        });

        Label statusLabel = new Label("List of Scouts : ");
        grid.add(statusLabel, 0, 4);
        listOfScouts = new ComboBox<String>();

        populateListOfScoutsComboBox();
       /* listOfScouts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                processScoutID(e);
            }
        });

          */
        grid.add(listOfScouts, 1, 4);

        Label companionLabel = new Label("Companion Name : ");
        grid.add(companionLabel, 0, 5);

        companion = new TextField();
        companion.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(companion, 1, 5);

        Label hourLabel = new Label("Companion Hours : ");
        grid.add(hourLabel, 2, 5);

        companionHours = new TextField();
        companionHours.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(companionHours, 3, 5);

        Label scStartLabel = new Label("Scout Start Time : ");
        grid.add(scStartLabel, 0, 6);

        scoutStartTime = new TextField();
        scoutStartTime.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(scoutStartTime, 1, 6);

        Label scEndLabel = new Label("Scout End Time : ");
        grid.add(scEndLabel, 0, 7);

        scoutEndTime = new TextField();
        scoutEndTime.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(scoutEndTime, 1, 7);

        addButton = new Button("Add Scout to Shift");
        grid.add(addButton,2,7);
        addButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();

                processOtherAction(e);
            }
        });
        addButton.setDisable(true);

// Create a new VBox for tableOfScouts
        VBox tableVbox = new VBox(10);
        tableVbox.setAlignment(Pos.CENTER);

        Label scoutListLabel = new Label("Scouts Selected For Shift");
        scoutListLabel.setFont(new Font("Arial", 18)); // Set the font size to 18
        tableVbox.getChildren().add(scoutListLabel);

        tableOfScouts = new TableView<ScoutTableModel>();
        tableOfScouts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableOfScouts.setPrefHeight(200); // Added to resize the table

        TableColumn fnColumn = new TableColumn("First Name") ;
        fnColumn.setMinWidth(100);
        fnColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("firstName"));

        TableColumn lnColumn = new TableColumn("Last Name") ;
        lnColumn.setMinWidth(100);
        lnColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("lastName"));

        TableColumn mnColumn = new TableColumn("Middle Name") ;
        mnColumn.setMinWidth(100);
        mnColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("middleName"));

        TableColumn dobColumn = new TableColumn("Date Of Birth") ;
        dobColumn.setMinWidth(100);
        dobColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("dateOfBirth"));

        TableColumn emailColumn = new TableColumn("Email") ;
        emailColumn.setMinWidth(100);
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("email"));

        TableColumn phoneColumn = new TableColumn("Phone Number") ;
        phoneColumn.setMinWidth(100);
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("phoneNumber"));

        TableColumn troopColumn = new TableColumn("Troop ID") ;
        troopColumn.setMinWidth(100);
        troopColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("troopID"));

        TableColumn statColumn = new TableColumn("Status") ;
        statColumn.setMinWidth(100);
        statColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("status"));

        tableOfScouts.getColumns().addAll(fnColumn,
                lnColumn, mnColumn, dobColumn,emailColumn,phoneColumn,troopColumn,statColumn);

        tableOfScouts.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
                    //processShiftInfo();
                }
            }
        });


        tableVbox.getChildren().add(tableOfScouts);

        submitButton = new Button("Start Shift");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();

                processAction(e);
            }
        });

        cancelButton = new Button("Return");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                //----------------------------------------------------------
                clearErrorMessage();
                myModel.stateChangeRequest("CancelShift", null);
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);

        btnContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(tableVbox); // Add the new VBox containing tableOfScouts
        vbox.getChildren().add(btnContainer);

        return vbox;
    }

    private void processOtherAction(ActionEvent e) {
        String selectedScoutName = listOfScouts.getSelectionModel().getSelectedItem().toString();
        String selectedScoutID = scoutNameToID.get(selectedScoutName);
        String compNameEntered = companion.getText();
        String compHoursEntered = companionHours.getText();
        String scStartEntered = scoutStartTime.getText();
        String scEndEntered = scoutEndTime.getText();

        if ((compNameEntered == null) || (compNameEntered.length() == 0)) {
            displayErrorMessage("Please enter a Companion name");
        } else if ((compHoursEntered == null) || (compHoursEntered.length() == 0)) {
            displayErrorMessage("Please enter Companion Hours");
        } else if ((scStartEntered == null) || (scStartEntered.length() == 0)) {
            displayErrorMessage("Please enter a Scout start time");
        } else if ((scEndEntered == null) || (scEndEntered.length() == 0)) {
            displayErrorMessage("Please enter a Scout end time");
        } else {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime scoutStartTimeObj = LocalTime.parse(scStartEntered, formatter);
                LocalTime scoutEndTimeObj = LocalTime.parse(scEndEntered, formatter);
                if (scoutStartTimeObj.isBefore(scoutEndTimeObj)) {
                    processShiftInfo(selectedScoutID,compNameEntered,compHoursEntered,scStartEntered,scEndEntered);
                    listOfScouts.getItems().remove(selectedScoutName); // Remove the scout from the combobox only if validation passes
                } else {
                    displayErrorMessage("ERROR: Scout start time must be before scout end time!");
                }
            } catch (DateTimeParseException excep) {
                displayErrorMessage("ERROR: Invalid time format!");
            }
        }
    }



    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------




    //----------------------------------------------------------
    private void processAction(Event evt)
    {

        String dateEntered = (date.getValue() != null) ? date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null; // Changed to DatePicker
        String startTimeEntered = startTime.getText();
        String endTimeEntered = endTime.getText();
        String startCashEntered = startCash.getText();





         if ((dateEntered == null) || (dateEntered.length() == 0))
        {
            displayErrorMessage("Please enter a valid date");
        }
        else if ((startTimeEntered == null) || (startTimeEntered.length() == 0))
        {
            displayErrorMessage("Please enter a start time");
        }

        else
            processSessionInfo(dateEntered,startTimeEntered,endTimeEntered,startCashEntered);
    }



    //---------------------------------------------------------------------------------------
    private boolean validate(String date, String startTime, String endTime, String cash)
    {
        try {
            double cashVal = Double.parseDouble(cash);
            if (cashVal > 0)
            {

            }
            else
            {
                return false;
            }

        }
        catch (Exception excep)
        {
            return false;
        }
        return true;
    }

    //---------------------------------------------------------------------------------------
    private void processSessionInfo(String date,String startTime, String endTime, String cash)
    {
        if (validate(date, startTime, endTime, cash) == true) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime startTimeObj = LocalTime.parse(startTime, formatter);
                LocalTime endTimeObj = LocalTime.parse(endTime, formatter);

                if (startTimeObj.isBefore(endTimeObj)) {
                    Properties sessionProps = new Properties();
                    sessionProps.setProperty("startDate", date);
                    sessionProps.setProperty("startTime", startTime);
                    sessionProps.setProperty("endTime", endTime);
                    sessionProps.setProperty("startingCash", cash);

                    myModel.stateChangeRequest("OpenSession", sessionProps);
                } else {
                    displayErrorMessage("ERROR: Start time must be before end time!");
                }
            } catch (DateTimeParseException e) {
                displayErrorMessage("ERROR: Invalid time format!");
            }
        } else {
            displayErrorMessage("ERROR: Invalid shift start data!");
        }
    }
    private void processShiftInfo(String scoutId, String compName, String compHours, String scStart, String scEnd) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime scoutStartTimeObj = LocalTime.parse(scStart, formatter);
            LocalTime scoutEndTimeObj = LocalTime.parse(scEnd, formatter);

            if (scoutStartTimeObj.isBefore(scoutEndTimeObj)) {
                Properties shiftProps = new Properties();

                shiftProps.setProperty("scoutID", scoutId);
                shiftProps.setProperty("scoutStartTime", scStart);
                shiftProps.setProperty("scoutEndTime", scEnd);
                shiftProps.setProperty("companionName", compName);
                shiftProps.setProperty("companionHours", compHours);

                myModel.stateChangeRequest("StartShiftForScouts", shiftProps);
            } else {
                displayErrorMessage("ERROR: Scout start time must be before scout end time!");
            }
        } catch (DateTimeParseException e) {
            displayErrorMessage("ERROR: Invalid scout time format!");
        }
    }
    private void populateListOfScoutsComboBox() {
        scoutNameToID = (HashMap)myModel.getState("ListOfAllScoutsMappedToID");
        ArrayList<String> allScoutNames = (ArrayList)myModel.getState("ListOfAllScouts");
        listOfScouts.getItems().clear();
        for (int cnt = 0; cnt < allScoutNames.size(); cnt++)
        {
            String nextScoutName = allScoutNames.get(cnt);
            listOfScouts.getItems().add(nextScoutName);
        }


    }

    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }
    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {

        ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            ScoutCollection scoutCollection = (ScoutCollection)myModel.getState("ChosenScouts");

            Vector entryList = (Vector)scoutCollection.getState("Scouts");
            Enumeration entries = entryList.elements();

            while (entries.hasMoreElements() == true)
            {
                Scout nextScout = (Scout)entries.nextElement();
                Vector<String> view = nextScout.getEntryListView();

                // add this list entry to the list
                ScoutTableModel nextTableRowData = new ScoutTableModel(view);
                tableData.add(nextTableRowData);

            }

            tableOfScouts.setItems(tableData);
        }
        catch (Exception e) {//SQLException e) {
            // Need to handle this exception
        }
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
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        startSessionButton.setDisable(true);
                addButton.setDisable(false);
            }


        }
        else if (key.equals("ScoutAddedForShift")) {
            System.out.println("Scout added for shift");
            displayMessage("Scout added successfully to shift!");
            getEntryTableModelValues();
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

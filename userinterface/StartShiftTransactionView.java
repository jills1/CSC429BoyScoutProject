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
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

// project imports
import impresario.IModel;
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

    private TextField scoutStartTime;
    private TextField scoutEndTime;
    protected TableView<ScoutTableModel> tableOfScouts;


    private TextField companion;
    private TextField companionHours;
    private ComboBox<String> listOfScouts;


    private Button submitButton;
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
    }
    protected void populateFields()
    {
        getEntryTableModelValues();
    }

    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {

        ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            ScoutCollection scoutCollection = (ScoutCollection)myModel.getState("ScoutList");

            Vector entryList = (Vector)scoutCollection.getState("Scouts");
            // DEBUG System.out.println("Size of scout list retrieved: " + entryList.size());
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
            System.out.println(e.toString());
            e.printStackTrace();
        }
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

        addButton = new Button("Submit");
        grid.add(addButton,2,7);
        addButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();

                processAction(e);
            }
        });

        Text prompt = new Text("Select From list Of Scouts");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 8, 1, 1);

        tableOfScouts = new TableView<ScoutTableModel>();
        tableOfScouts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

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
                    processScoutSelected();
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(90, 50);
        scrollPane.setContent(tableOfScouts);
        grid.add(tableOfScouts,0,9);

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
    private void processScoutSelected()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();

        Properties props = new Properties();

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

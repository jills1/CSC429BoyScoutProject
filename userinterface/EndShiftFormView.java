package userinterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
// project imports
import impresario.IModel;
import model.*;
public class EndShiftFormView extends View {
    protected TableView<ScoutTableModel> tableOfScouts;
    private TextField sessionID;
    private Button submitButton;
    private Button cancelButton;
    private MessageView statusLog;
    //-------------------------------------------------------------
    public EndShiftFormView(IModel trans) {
        super(trans, "EndShiftFormView");
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());
        container.getChildren().add(createStatusLog("                          "));
        getChildren().add(container);
        myModel.subscribe("TransactionError", this);
    }
    private Node createTitle() {
        Text titleText = new Text("Troop 209");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        return titleText;
    }
    private VBox createFormContent() {
        VBox vbox = new VBox(10);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text prompt = new Text("Select Session to End by clicking");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);
        //--
        tableOfScouts = new TableView<ScoutTableModel>();
        tableOfScouts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //--
        TableColumn firstNameColumn = new TableColumn("First Name");
        firstNameColumn.setMinWidth(100);
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("firstName"));
        //--
        TableColumn lastNameColumn = new TableColumn("Last Name");
        lastNameColumn.setMinWidth(100);
        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("lastName"));
        //--
        TableColumn middleNameColumn = new TableColumn("Middle Name");
        middleNameColumn.setMinWidth(100);
        middleNameColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("middleName"));
        //--
        TableColumn dateOfBirthColumn = new TableColumn("Date of Birth");
        dateOfBirthColumn.setMinWidth(100);
        dateOfBirthColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("dateOfBirth"));
        //--
        TableColumn phoneNumberColumn = new TableColumn("Phone Number");
        phoneNumberColumn.setMinWidth(100);
        phoneNumberColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("phoneNumber"));
        //--
        TableColumn emailColumn = new TableColumn("Email");
        emailColumn.setMinWidth(100);
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("email"));
        //--
        TableColumn troopIDColumn = new TableColumn("Troop ID");
        troopIDColumn.setMinWidth(100);
        troopIDColumn.setCellValueFactory(
                new PropertyValueFactory<ScoutTableModel, String>("troopID"));
        //--
        tableOfScouts.getColumns().addAll(lastNameColumn, firstNameColumn, middleNameColumn, dateOfBirthColumn, phoneNumberColumn, emailColumn, troopIDColumn);
        tableOfScouts.setOnMousePressed(new EventHandler<MouseEvent>() {@Override public void handle(MouseEvent event) {if (event.isPrimaryButtonDown() && event.getClickCount() >= 2) {processScoutSelected();}}});
        //--
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(115, 150);
        scrollPane.setContent(tableOfScouts);
        //--
        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent e) {clearErrorMessage();myModel.stateChangeRequest("CancelUpdate", null);}});
        //--
        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(submitButton);
        btnContainer.getChildren().add(cancelButton);
        vbox.getChildren().add(grid);
        vbox.getChildren().add(scrollPane);
        vbox.getChildren().add(btnContainer);
        return vbox;
    }
    //-------------------------------------------------------------
    protected void processScoutSelected() {
        ScoutTableModel selectedItem = tableOfScouts.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            String selectedScout = selectedItem.getTroopID();
            myModel.stateChangeRequest("ScoutChosen", selectedScout);
        }
    }
        protected void getEntryTableModelValues() {
            ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();
            try {
                ScoutCollection scoutCollection = (ScoutCollection)myModel.getState("ScoutList");
                Vector entryList = (Vector)scoutCollection.getState("Scouts");
                Enumeration entries = entryList.elements();
                while (entries.hasMoreElements() == true) {
                    Scout nextScout = (Scout)entries.nextElement();
                    Vector<String> view = nextScout.getEntryListView();
                    // add this list entry to the list
                    ScoutTableModel nextTableRowData = new ScoutTableModel(view);
                    tableData.add(nextTableRowData);
                }
                tableOfScouts.setItems(tableData);
            } catch (Exception e) {//SQLException e) {
                // Need to handle this exception
            }
        }
    protected void populateFields() {getEntryTableModelValues();}
    public void updateState(String key, Object value) {
        clearErrorMessage();
        if (key.equals("TransactionError")) {
            String messageToDisplay = (String)value;
            if (messageToDisplay.startsWith("ERR")) {
                displayErrorMessage(messageToDisplay);
            } else {
                displayMessage(messageToDisplay);
            }
        }
    }
    //------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }
    public void displayMessage(String message) {statusLog.displayMessage(message);}
    public void displayErrorMessage(String message) {statusLog.displayErrorMessage(message);}
    public void clearErrorMessage() {statusLog.clearErrorMessage();}
}
package userinterface;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
// project imports
import impresario.IModel;
import model.*;
//==============================================================
public class UpdateTreeFormView extends View {
    private TextField barcode;
    private TextField treeType;
    private TextField notes;
    private ComboBox<String> status; // Changed to ComboBox
    private TextField dateStatusUpdate;
    private Button submitButton;
    private Button cancelButton;
    private MessageView statusLog;
    //-------------------------------------------------------------
    public UpdateTreeFormView(IModel trans) {
        super(trans, "UpdateTreeFormView");
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());
        container.getChildren().add(createStatusLog("                          "));
        getChildren().add(container);
        populateFields();
        myModel.subscribe("TransactionError", this);
    }
    //-------------------------------------------------------------
    private Node createTitle() {
        Text titleText = new Text("       Troop 209          ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        return titleText;
    }
    // Create the main data entry fields
    //-------------------------------------------------------------
    private VBox createFormContent() {
        //-----------------------------------------------------------
        // Container Padding
        VBox vbox = new VBox(10);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
//-------------------------------------------------------------------
        //Barcode Label, Box and Handler
        Label barcodeLabel = new Label("Barcode : ");
        grid.add(barcodeLabel, 0, 0);
        barcode = new TextField();
        barcode.setEditable(false);
        grid.add(barcode, 1, 0);
        //-------------------------------------------------------------------
        //Tree Type Label, Box and Handler
        Label treeTypeLabel = new Label("Tree Type : ");
        grid.add(treeTypeLabel, 0, 2);
        treeType = new TextField();
        treeType.setEditable(false);
        grid.add(treeType, 1, 2);
        //-------------------------------------------------------------------
        //Notes Label, Box and Handler
        Label notesLabel = new Label("notes : ");
        grid.add(notesLabel, 0, 3);
        notes = new TextField();
        grid.add(notes, 1, 3);
        //-------------------------------------------------------------------
        //Status Label, Box and Handler
        Label statusLabel = new Label("Status : ");
        grid.add(statusLabel, 0, 4);
        status = new ComboBox<String>(); // Changed to ComboBox
        status.getItems().addAll( "Damaged", "Available");
        grid.add(status, 1, 4);
        //-------------------------------------------------------------------
        //dateStatusUpdate Label, Box and Handler
        Label dateStatusUpdateLabel = new Label("Date of last status update : ");
        grid.add(dateStatusUpdateLabel, 0, 5);
        dateStatusUpdate = new TextField();
        dateStatusUpdate.setEditable(false);
        grid.add(dateStatusUpdate, 1, 5);
//------------------------------------------------------------------
        //Submit Button and Event Handler
        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent e) {clearErrorMessage();processAction(e);}});
//----------------------------------------------------------------
        //Cancel Button and Event Handler
        cancelButton = new Button("Back");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent e) { clearErrorMessage(); myModel.stateChangeRequest("CancelDeposit", null);}});
        //--------------------------------------------------------
        //Place Entities within Container
        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(submitButton);
        btnContainer.getChildren().add(cancelButton);
        vbox.getChildren().add(grid);
        vbox.getChildren().add(btnContainer);
        return vbox;
    }
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }
    //-------------------------------------------------------------
    public void populateFields() {
        barcode.setText((String)myModel.getState("barcode"));
        treeType.setText((String)myModel.getState("treeType"));
        status.setValue((String)myModel.getState("status"));
        notes.setText((String)myModel.getState("notes"));
        dateStatusUpdate.setText((String)myModel.getState("dateStatusUpdate"));
    }
    //----------------------------------------------------------
    private void processAction(Event e) {
        clearErrorMessage();
        LocalDateTime now = LocalDateTime.now();
        String barcodeEntered = barcode.getText();
        //String treeTypeEntered = treeType.getText();
        String statusEntered = status.getValue();
        String notesEntered = notes.getText();
        String dateStatusUpdateEntered = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        //System.out.println(barcodeEntered + " " + treeTypeEntered + statusEntered);
        if ((barcodeEntered == null) || (barcodeEntered.length() != 5)) {
            displayErrorMessage("Please enter a valid barcode");
        } else if ((statusEntered == null)) {
            displayErrorMessage("Please enter a valid status");
        } else if ((notesEntered == null) || (notesEntered.length() == 0) || (notesEntered.length() >= 201)) {
            displayErrorMessage("Please enter valid notes");
        } else if ((dateStatusUpdateEntered == null) || (dateStatusUpdateEntered.length() == 0) || (dateStatusUpdateEntered.length() >= 13)) {
            displayErrorMessage("Please enter a valid date");
        } else{
            processTreeInfo(barcodeEntered, statusEntered,notesEntered, dateStatusUpdateEntered);
        }
    }
    private void processTreeInfo(String barcode,  String status, String notes, String dateStatusUpdate) {
        // modify to make update tree
        System.out.println(barcode+treeType+status+notes+dateStatusUpdate);
        Properties props = new Properties();
        props.setProperty("barcode", barcode);
        props.setProperty("status", status);
        props.setProperty("notes", notes);
        props.setProperty("dateStatusUpdate",dateStatusUpdate);
        myModel.stateChangeRequest("RegisterTreeInfo", props);
        //Tree tree = new Tree(props);
        //tree.update();
        //displayMessage("Successfully updated Tree");
    }
    //-------------------------------------------------------------
    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }
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


    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }
}
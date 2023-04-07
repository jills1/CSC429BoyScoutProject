package userinterface;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Properties;

// project imports
import impresario.IModel;
import model.*;
//==============================================================
public class UpdateTreeFormTransactionView extends View {
    //protected TableView<AccountTableModel> tableOfAccounts;
    private TextField barcode;
    private TextField treeType;
    private TextField notes;
    private ComboBox<String> status; // Changed to ComboBox
    private TextField dateStatusUpdate;
    private Button submitButton;
    private Button cancelButton;
    private MessageView statusLog;
    //-------------------------------------------------------------
    public UpdateTreeFormTransactionView(IModel trans) {
        super(trans, "UpdateTreeFormTransactionView");
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
    private VBox createFormContent() {
        //-----------------------------------------------------------
        populateFields();
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
        grid.add(barcode, 1, 0);
        //-------------------------------------------------------------------
        //Tree Type Label, Box and Handler
        Label treeTypeLabel = new Label("Tree Type : ");
        grid.add(treeTypeLabel, 0, 2);
        treeType = new TextField();
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
        status.getItems().addAll("Active", "Inactive");
        grid.add(status, 1, 4);
        //-------------------------------------------------------------------
        //dateStatusUpdate Label, Box and Handler
        Label dateStatusUpdateLabel = new Label("Date of last status update : ");
        grid.add(dateStatusUpdateLabel, 0, 5);
        dateStatusUpdate = new TextField();
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
    private void processAction(Event evt) {
        //clearErrorMessage();
        String barcodeEntered = barcode.getText();
        if ((barcodeEntered == null) || (barcodeEntered.length() == 0)) {
            displayErrorMessage("Please enter a barcode");
        } else {
            processTreeInfo(barcodeEntered);
        }
    }
    private void processTreeInfo(String barcode) {
        // modify to make update tree
        Properties props = new Properties();
        props.setProperty("barcode", barcode);
        myModel.stateChangeRequest("RegisterTreeInfo", props);
        Tree tree = new Tree(props);
        tree.update();
        displayMessage("Successfully added Tree");
    }
    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }
    //---------------------------------------------------------
    public void updateState(String key, Object value) {
    }
    //----------------------------------------------------------
    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }
}
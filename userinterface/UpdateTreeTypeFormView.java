package userinterface;
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
import model.*;
//==============================================================
public class UpdateTreeTypeFormView extends View {
    //protected TableView<AccountTableModel> tableOfAccounts;
    private TextField treeTypeID;
    private TextField typeDescription;
    private TextField cost;
    private TextField barcodePrefix;
    private Button submitButton;
    private Button cancelButton;
    private MessageView statusLog;
    //-------------------------------------------------------------
    public UpdateTreeTypeFormView(IModel trans) {
        super(trans, "UpdateTreeTypeFormView");
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
        // Container Padding
        VBox vbox = new VBox(10);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
//-------------------------------------------------------------------
        //Barcode Label, Box and Handler
        Label barcodeLabel = new Label("TreeTypeID : ");
        grid.add(barcodeLabel, 0, 0);
        treeTypeID = new TextField();
        treeTypeID.setEditable(false);
        grid.add(treeTypeID, 1, 0);
        //-------------------------------------------------------------------
        //Tree Type Label, Box and Handler
        Label descriptionLabel = new Label("Description : ");
        grid.add(descriptionLabel, 0, 2);
        typeDescription = new TextField();
        grid.add(typeDescription, 1, 2);
        //-------------------------------------------------------------------
        //Notes Label, Box and Handler
        Label costLabel = new Label("Cost : ");
        grid.add(costLabel, 0, 3);
        cost = new TextField();
        grid.add(cost, 1, 3);
        //-------------------------------------------------------------------
        //dateStatusUpdate Label, Box and Handler
        Label barcodePrefixLabel = new Label("Barcode Prefix : ");
        grid.add(barcodePrefixLabel, 0, 5);
        barcodePrefix = new TextField();
        barcodePrefix.setEditable(false);
        grid.add(barcodePrefix, 1, 5);
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
        treeTypeID.setText((String)myModel.getState("treeTypeID"));
        typeDescription.setText((String)myModel.getState("description"));
        cost.setText((String)myModel.getState("cost"));
        barcodePrefix.setText((String)myModel.getState("barcodePrefix"));
    }
    //----------------------------------------------------------
    private void processAction(Event evt) {
        //clearErrorMessage();
        LocalDateTime now = LocalDateTime.now();
        String treeTypeIDEntered = treeTypeID.getText();
        String typeDescriptionEntered = typeDescription.getText();
        String costEntered = cost.getText();
        String barcodePrefixEntered = barcodePrefix.getText();
        if ((treeTypeIDEntered == null) || (treeTypeIDEntered.length() == 0)) {
            displayErrorMessage("Please enter a valid treeTypeID");
        } else if ((typeDescriptionEntered == null) || (typeDescriptionEntered.length() == 0) || (typeDescriptionEntered.length() >= 26)) {
            displayErrorMessage("Please enter a valid type description");
        } else if ((costEntered == null) || (costEntered.length() == 0) || (costEntered.length() >= 21)) {
            displayErrorMessage("Please enter a valid price for treeType");
        } else if ((barcodePrefixEntered == null) || (barcodePrefixEntered.length() >= 3) || (barcodePrefixEntered.length() == 0)) {
            displayErrorMessage("Please enter a valid barcodePrefix");
        } else{
            processTreeInfo(treeTypeIDEntered,typeDescriptionEntered,costEntered, barcodePrefixEntered);
        }
    }
    private void processTreeInfo(String treeTypeID, String typeDescription, String cost, String barcodePrefix) {
        // modify to make update tree
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        Properties props = new Properties();
        props.setProperty("treeTypeID", treeTypeID);
        props.setProperty("typeDescription", typeDescription);
        props.setProperty("cost", cost);
        props.setProperty("barcodePrefix",barcodePrefix);
        //myModel.stateChangeRequest("RegisterTreeInfo", props);
        TreeType treeID = new TreeType(props);
        treeID.update();
        displayMessage("Successfully updated TreeType");
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
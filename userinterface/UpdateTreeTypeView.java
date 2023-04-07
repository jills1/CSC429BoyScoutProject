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
public class UpdateTreeTypeView extends View {
    private TextField treeTypeID;
    private Button submitButton;
    private Button cancelButton;
    private MessageView statusLog;
    //-------------------------------------------------------------
    public UpdateTreeTypeView(IModel trans) {
        super(trans, "UpdateTreeTypeView");
        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());
        // Error message area
        container.getChildren().add(createStatusLog("                          "));
        getChildren().add(container);
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
        // Container Padding
        VBox vbox = new VBox(10);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
//-------------------------------------------------------------------
        //Barcode Label, Box and Handler
        Label treeTypeIDLabel = new Label("TreeTypeID: ");
        grid.add(treeTypeIDLabel, 0, 0);
        treeTypeID = new TextField();
        grid.add(treeTypeID, 1, 0);
//------------------------------------------------------------------
        //Submit Button and Event Handler
        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                processAction(e);
            }});
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
    }
    //----------------------------------------------------------
    private void processAction(Event evt) {
        String treeTypeIDEntered = treeTypeID.getText();
        if ((treeTypeIDEntered == null) || (treeTypeIDEntered.length() == 0)) {
            displayErrorMessage("Please enter a treeTypeID");
        } else {
            processTreeTypeInfo(treeTypeIDEntered);
        }
    }
    private void processTreeTypeInfo(String treeTypeID) {
        Properties props = new Properties();
        props.setProperty("treeTypeID", treeTypeID);
        myModel.stateChangeRequest("UpdateTreeTypeFormView", props);
        //Tree tree = new Tree(props);
        //tree.update();
        displayMessage("Retrieving Tree Information");
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
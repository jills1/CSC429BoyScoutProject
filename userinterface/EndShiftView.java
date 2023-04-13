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
import java.util.Properties;
// project imports
import impresario.IModel;
public class EndShiftView extends View {
    private ComboBox<String> sessionID; // Changed to ComboBox
    private Button submitButton;
    private Button cancelButton;
    private MessageView statusLog;
    //-------------------------------------------------------------
    public EndShiftView(IModel trans) {
        super(trans, "EndShiftView");
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
//-------------------------------------------------------------------
        //Barcode Label, Box and Handler
        Label sessionIDLabel = new Label("Select SessionID : ");
        grid.add(sessionIDLabel, 0, 0);
        sessionID = new ComboBox<String>(); // Changed to ComboBox
        sessionID.getItems().addAll( "Damaged", "Available");//change to accept dynamically retrieved sessionID's
        grid.add(sessionID, 1, 0);
//------------------------------------------------------------------
        //Submit Button and Event Handler
        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent e) {clearErrorMessage();
                processAction(e);}});
//----------------------------------------------------------------
        //Cancel Button and Event Handler
        cancelButton = new Button("Back");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("CancelDeposit", null);
            }
        });
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
    private void processAction(Event evt) {
        String sessionIDEntered = sessionID.getValue();
        if ((sessionIDEntered == null)) {
            displayErrorMessage("Please enter a valid sessionID");
        } else {
            processSessionInfo(sessionIDEntered);
        }
    }
    private void processSessionInfo(String sessionID) {
        Properties props = new Properties();
        props.setProperty("sessionID", sessionID);
        myModel.stateChangeRequest("EndShiftDataViewTrans", props);
    }
    protected void populateFields() {

    }
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
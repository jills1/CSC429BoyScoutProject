package userinterface;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import jdk.net.SocketFlow;
//import model.EndShiftDataTransaction;
import model.EndShiftTransaction;
import userinterface.MessageView;
import userinterface.View;
import java.util.Properties;
import model.Session;

import javax.swing.*;

public class EndShiftDataView extends View {
    protected Button submitButton;
    protected Button backButton;
    protected TextField endCash;
    protected TextField notes;
    protected TextField endTime;
    protected TextField totalCheck;
    protected MessageView statusLog;
    protected TextField sessionID;
    public EndShiftDataView(IModel session) {
        super(session, "EndShiftDataView");
        VBox cont = new VBox(10);
        cont.setPadding(new Insets(15, 5, 5, 5));

        cont.getChildren().add(createTitle());
        cont.getChildren().add(createFormContent());
        cont.getChildren().add(createStatusLog(""));
        getChildren().add(cont);
        populateFields();
        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("TransactionError", this);
    }
    private Node createTitle(){
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("End a Shift");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }
    private VBox createFormContent() {
        VBox vbox = new VBox(10);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text prompt = new Text("SHIFT INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);
        //--
        Text sessionIDLabel = new Text("Shift ID: ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        sessionIDLabel.setFont(myFont);
        sessionIDLabel.setWrappingWidth(150);
        sessionIDLabel.setTextAlignment(TextAlignment.RIGHT);
        //grid.add(sessionIDLabel, 0 , 1);
        sessionID = new TextField();
        sessionID.setEditable(false);
        //grid.add(sessionID, 1,1);

        Text endCashLabel = new Text("Ending Cash: ");
        endCashLabel.setFont(myFont);
        endCashLabel.setWrappingWidth(150);
        endCashLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(endCashLabel, 0 , 2);
        endCash = new TextField();
        endCash.setEditable(true);
        grid.add(endCash, 1,2);

        Text totalCheckLabel = new Text("Check Total: ");
        totalCheckLabel.setFont(myFont);
        totalCheckLabel.setWrappingWidth(150);
        totalCheckLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(totalCheckLabel, 0 , 3);
        totalCheck = new TextField();
        totalCheck.setEditable(true);
        grid.add(totalCheck, 1,3);

        Text endTimeLabel = new Text("Ending Time: ");
        endTimeLabel.setFont(myFont);
        endTimeLabel.setWrappingWidth(150);
        endTimeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(endTimeLabel, 0, 4);
        endTime = new TextField();
        endTime.setEditable(true);
        grid.add(endTime, 1, 4);
        //--
        Text notesLabel = new Text("Notes: ");
        notesLabel.setFont(myFont);
        notesLabel.setWrappingWidth(150);
        notesLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(notesLabel, 0, 5);
        notes = new TextField();
        notes.setEditable(true);
        grid.add(notes, 1, 5);
        //--
        HBox cont = new HBox(10);
        cont.setAlignment(Pos.CENTER);
        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent e) {clearErrorMessage();processAction(e);}});
        //--
        backButton = new Button("Back");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backButton.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent event) {System.out.println("The back Button is being clicked");clearErrorMessage();myModel.stateChangeRequest("CancelTransaction", null);}});
        cont.getChildren().add(submitButton);
        cont.getChildren().add(backButton);
        vbox.getChildren().add(grid);
        vbox.getChildren().add(cont);
        //--
        return vbox;
    }
    public void updateState(String key, Object value) {
        clearErrorMessage();
        if (key.equals("TransactionError")) {
            String messageToDisplay = (String)value;
            if (messageToDisplay.startsWith("ERR")) {
                displayErrorMessage(messageToDisplay);
            }
            else {
                displayMessage(messageToDisplay);
            }
        }
    }
    public void processAction(ActionEvent event) {
        Properties p = new Properties();
        String s = endTime.getText();
        String x = notes.getText();
        p.setProperty("endTime", s);
        p.setProperty("notes", x);
        System.out.println(x);
        System.out.println(s);

        if((p.getProperty("endTime")).equals("")) {
            displayErrorMessage("There was an error with getting the time");
            return;
        } else if ((p.getProperty("notes")).length() > 200) {
            displayErrorMessage("Notes are bigger than 200 letters.");
            return;
        } else {
            Alert successPopUp = new Alert(Alert.AlertType.INFORMATION,
                    "Confirm close shift: ");
            successPopUp.showAndWait();
                myModel.stateChangeRequest("EndShift", p);

            }

        }

    public void displayErrorMessage(String message){
        statusLog.displayErrorMessage(message);
    }
    public void displayMessage(String message){
        statusLog.displayMessage(message);
    }
    public void clearErrorMessage(){
        statusLog.clearErrorMessage();
    }
    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }
    public void populateFields() {
        String shiftOpenMessage = (String)myModel.getState("TransactionError");
        if ((shiftOpenMessage != null) && (shiftOpenMessage.length() > 0) && (shiftOpenMessage.startsWith("ERR")))
        {
            displayErrorMessage(shiftOpenMessage);
        }
        else {
            String date = String.valueOf(java.time.LocalDateTime.now());
            String dUS = date.substring(11, 16);
            endCash.setText((String) myModel.getState("endingCash"));
            totalCheck.setText((String) myModel.getState("totalCheckTransactionAmount"));
            sessionID.setText(EndShiftTransaction.getSessionID('i'));
            endTime.setText(dUS);
        }
    }
}

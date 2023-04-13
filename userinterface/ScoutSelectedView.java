package userinterface;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import impresario.IModel;
public class ScoutSelectedView extends View {
    protected TextField firstName;
    protected TextField middleName;
    protected TextField lastName;
    protected TextField troopID;
    protected Button submitButton;
    protected Button cancelButton;
    protected MessageView statusLog;
    public ScoutSelectedView(IModel account) {
        super(account, "ScoutSelectedView");
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());
        container.getChildren().add(createStatusLog("             "));
        getChildren().add(container);
        populateFields();
        myModel.subscribe("UpdateStatusMessage", this);
    }
    private Node createTitle() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        Text titleText = new Text(" Troop 209 ");
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

        Text prompt = new Text("Click Submit to Inactivate Scout");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text fnLabel = new Text(" First Name : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        fnLabel.setFont(myFont);
        fnLabel.setWrappingWidth(150);
        fnLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(fnLabel, 0, 1);

        firstName = new TextField();
        firstName.setEditable(false);
        grid.add(firstName, 1, 1);

        Text mnLabel = new Text(" Middle Name : ");
        mnLabel.setFont(myFont);
        mnLabel.setWrappingWidth(150);
        mnLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(mnLabel, 0, 2);

        middleName = new TextField();
        middleName.setEditable(false);
        grid.add(middleName, 1, 2);

        Text lnLabel = new Text(" Last Name : ");
        lnLabel.setFont(myFont);
        lnLabel.setWrappingWidth(150);
        lnLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(lnLabel, 0, 3);

        lastName = new TextField();
        lastName.setEditable(false);
        grid.add(lastName, 1, 3);

        Text troopLabel = new Text(" Troop ID : ");
        troopLabel.setFont(myFont);
        troopLabel.setWrappingWidth(150);
        troopLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(troopLabel, 0, 4);

        troopID = new TextField();
        troopID.setEditable(false);
        grid.add(troopID, 1, 4);



        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);

        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();

                processAction(e);
            }
        });

        cancelButton = new Button("Back");
        cancelButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                myModel.stateChangeRequest("CancelScoutList", null);
            }
        });
        doneCont.getChildren().add(submitButton);
        doneCont.getChildren().add(cancelButton);
        vbox.getChildren().add(grid);
        vbox.getChildren().add(doneCont);

        return vbox;
    }
    //-----------------------------------------------------------------
    private void processAction(ActionEvent e) {
        myModel.stateChangeRequest("UpdateStatusInactive",this);
        displayMessage("Successfully Removed");
    }
    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }
    public void populateFields() {
        firstName.setText((String)myModel.getState("firstName"));
        middleName.setText((String)myModel.getState("middleName"));
        lastName.setText((String)myModel.getState("lastName"));
        troopID.setText((String)myModel.getState("troopID"));
    }
    public void updateState(String key, Object value) {clearErrorMessage();}
    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }
    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }
}





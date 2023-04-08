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
import java.util.Properties;
import impresario.IModel;
public class SearchUpdateScoutView extends View {
    protected Properties myProps;
    protected TextField firstName;
    protected TextField lastName;
    protected Button cancelButton;
    private Button submitButton;
    protected MessageView statusLog;
    public SearchUpdateScoutView(IModel account) {
        super(account, "SearchUpdateScoutView");
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());
        container.getChildren().add(createStatusLog("                                            "));
        getChildren().add(container);
        populateFields();
    }
    private Node createTitle() {
        Text titleText = new Text("       Search for Scout          ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        return titleText;
    }
    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        //---------------------------------------------------------------
        Text prompt = new Text("Please enter the first name and last name of the desired Scout below");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);
        //---------------------------------------------------------------
        Text scoutFNLabel = new Text("  First Name        : ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        scoutFNLabel.setFont(myFont);
        grid.add(scoutFNLabel, 0, 1);
        firstName = new TextField();
        firstName.setEditable(true);
        firstName.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent e) {clearErrorMessage();myModel.stateChangeRequest("FirstNameEntered", firstName.getText());}});
        grid.add(firstName, 1, 1);
        //---------------------------------------------------------------
        Text scoutLNLabel = new Text("  Last Name        : ");
        scoutLNLabel.setFont(myFont);
        grid.add(scoutLNLabel, 0, 2);
        lastName = new TextField();
        lastName.setEditable(true);
        lastName.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent e) {clearErrorMessage();myModel.stateChangeRequest("LastNameEntered", firstName.getText());}});
        grid.add(lastName, 1, 2);
        //---------------------------------------------------------------
        HBox doneCont = new HBox(10);
        doneCont.setAlignment(Pos.CENTER);
        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent e) {clearErrorMessage();processAction(e);}});
        //---------------------------------------------------------------
        cancelButton = new Button("Back");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {@Override public void handle(ActionEvent e) {clearErrorMessage();myModel.stateChangeRequest("CancelUpdate", null);}});
        //---------------------------------------------------------------
        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(submitButton);
        btnContainer.getChildren().add(cancelButton);
        vbox.getChildren().add(grid);
        vbox.getChildren().add(btnContainer);
        return vbox;
    }
    private void processAction(ActionEvent e) {
        // Getting the results from the text fields
        String lastNameEntry = lastName.getText();
        String firstNameEntry = firstName.getText();
        //Send the properties
        sendScoutSearch(lastNameEntry, firstNameEntry);
    }
    private void sendScoutSearch(String lastName, String firstName) {

        myProps = new Properties();
        //Setting up the search parameter in the
        myProps.setProperty("lastName", lastName);
        myProps.setProperty("firstName", firstName);
        System.out.println("Props collected");
        System.out.println("Props sent");
        //Sending the search
        myModel.stateChangeRequest("SubmitSearchUpdateScout", myProps);
    }
    //-------------------------------------------------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage) {
        statusLog = new MessageView(initialMessage);
        return statusLog;
    }
    public void populateFields() {}
    public void updateState(String key, Object value) {}
    public void displayErrorMessage(String message) {statusLog.displayErrorMessage(message);}
    public void displayMessage(String message) {statusLog.displayMessage(message);}
    public void clearErrorMessage() {statusLog.clearErrorMessage();}

}
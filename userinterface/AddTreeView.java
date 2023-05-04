package userinterface;

import impresario.IModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import userinterface.MessageView;
import userinterface.View;
import model.Tree;
import java.util.Properties;

public class AddTreeView extends View
{

    protected Button submitButton;
    protected Button backButton;
    protected TextField barcode;
    protected TextField notes;
    protected ComboBox<String> status;

    protected MessageView statusLog;
    protected String treeType;
    protected String treeTypeLastDigit;

    public AddTreeView(IModel tree)
    {
        super(tree, "AddTreeView");
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());
        container.getChildren().add(createStatusLog("             "));
        getChildren().add(container);

        populateFields();

        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("TransactionError", this);
    }

    private Node createTitle(){
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text("Insert New Tree");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }

    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text prompt = new Text("TREE INFROMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        Text treeBarcodeLabel = new Text("Barcode: ");
        Font myFont = Font.font("Helvetica", FontWeight.BOLD, 12);
        treeBarcodeLabel.setFont(myFont);
        treeBarcodeLabel.setWrappingWidth(150);
        treeBarcodeLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(treeBarcodeLabel, 0 , 1);

        barcode = new TextField();
        barcode.setEditable(true);
        grid.add(barcode, 1,1);

        Text treeNotesLabel = new Text("Notes: ");
        treeNotesLabel.setFont(myFont);
        treeNotesLabel.setWrappingWidth(150);
        treeNotesLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(treeNotesLabel, 0, 2);

        notes = new TextField();
        notes.setEditable(true);
        grid.add(notes, 1, 2);

        //Text treeStatusLabel = new Text("Status: ");
        //treeStatusLabel.setFont(myFont);
        //treeStatusLabel.setWrappingWidth(150);
        //treeStatusLabel.setTextAlignment(TextAlignment.RIGHT);
        //grid.add(treeStatusLabel, 0 , 3);

        //status = new ComboBox();
        //ObservableList options = status.getItems();
        //options.add("Available");
        //options.add("Sold");
        //options.add("Damaged");
        //status.setItems(options);
        //status.getSelectionModel().selectFirst();
        //grid.add(status, 1,3);

        HBox cont = new HBox(10);
        cont.setAlignment(Pos.CENTER);

        submitButton = new Button("Submit");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                treeType = barcode.getText().substring(0,2);
                treeTypeLastDigit = barcode.getText().substring(1,2);
                String date = String.valueOf(java.time.LocalDateTime.now());
                String dUS = date.substring(0,10);
                String status = "Available";
                Properties p = new Properties();

                p.setProperty("barcode", barcode.getText());
                p.setProperty("treeType", treeType);
                p.setProperty("notes", notes.getText());
                p.setProperty("status", status);
                p.setProperty("dateStatusUpdate", dUS);
                System.out.println("The time is " + dUS);
                System.out.println(treeType);
                clearErrorMessage();


                if((p.getProperty("barcode")).equals("") || (p.getProperty("barcode")).length() != 5) {
                    displayErrorMessage("Barcode is empty or bigger than 5 numbers.");
                    return;
                } else if ((p.getProperty("notes")).length() > 200) {
                    displayErrorMessage("Notes is bigger than 200 charcters.");
                    return;

                } else {myModel.stateChangeRequest("AddTreeInfo", p);
                    Tree tree = new Tree(p);
                    tree.update();
                    //displayMessage("Tree Successfully Added.");
                }

            }
        });

        backButton = new Button("Back");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("The back button is being clicked");
                clearErrorMessage();
                myModel.stateChangeRequest("CancelTransaction", null);
            }
        });

        cont.getChildren().add(submitButton);
        cont.getChildren().add(backButton);
        vbox.getChildren().add(grid);
        vbox.getChildren().add(cont);

        return vbox;
    }

    public void populateFields()
    {

    }


    public void updateState(String key, Object value){
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

    public void displayErrorMessage(String message){
        statusLog.displayErrorMessage(message);
    }

    public void displayMessage(String message){
        statusLog.displayMessage(message);
    }

    public void clearErrorMessage(){
        statusLog.clearErrorMessage();
    }

    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }
}

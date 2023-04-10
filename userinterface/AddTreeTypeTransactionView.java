// specify the package
package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

// project imports
import impresario.IModel;
import model.Scout;

/** The class containing the Add Tree Type Transaction View  for the Tree Sales application */
//==============================================================
public class AddTreeTypeTransactionView extends View
{

    // Model

    // GUI components
    private TextField typeDescription;
    private TextField cost;
    private TextField barcodePrefix;


    private Button submitButton;
    private Button cancelButton;

    // For showing error message
    private MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public AddTreeTypeTransactionView(IModel trans)
    {
        super(trans, "AddTreeTypeTransactionView");

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


    // Create the label (Text) for the title
    //-------------------------------------------------------------
    private Node createTitle()
    {

        Text titleText = new Text("       Troop 209          ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);

        return titleText;
    }

    // Create the main data entry fields
    //-------------------------------------------------------------
    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label typeDescriptionLabel = new Label("Tree Type Description : ");
        grid.add(typeDescriptionLabel, 0, 0);

        typeDescription = new TextField();
        typeDescription.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(typeDescription, 1, 0);

        Label costLabel = new Label("Cost : ");
        grid.add(costLabel, 0, 1);

        cost = new TextField();
        cost.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(cost, 1, 1);

        Label bpLabel = new Label("Barcode Prefix : ");
        grid.add(bpLabel, 0, 2);

        barcodePrefix = new TextField();
        barcodePrefix.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(barcodePrefix, 1, 2);



        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();

                processAction(e);
            }
        });

        cancelButton = new Button("Back");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                //----------------------------------------------------------
                clearErrorMessage();
                myModel.stateChangeRequest("CancelAddTreeType", null);
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(submitButton);
        btnContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }


    // Create the status log field
    //-------------------------------------------------------------
    private MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }

    //-------------------------------------------------------------
    public void populateFields()
    {

    }



    //----------------------------------------------------------
    private void processAction(Event evt)
    {
        //clearErrorMessage();

        String descriptionEntered = typeDescription.getText();
        String costEntered = cost.getText();
        String bpEntered = barcodePrefix.getText();


        if ((descriptionEntered == null) || (descriptionEntered.length() == 0))
        {
            displayErrorMessage("Please enter a description");
        }
        else if ((costEntered == null) || (costEntered.length() == 0))
        {
            displayErrorMessage("Please enter a cost");
        }
        else if (!isDoubleValue(costEntered))
        {
            displayErrorMessage("Please enter a valid cost(double)");
        }
        else if ((bpEntered == null) || (bpEntered.length()!=2))
        {
            displayErrorMessage("Please enter a valid barcode prefix");
        }

        else
            processScoutInfo(descriptionEntered,costEntered,bpEntered);
    }
    private void processScoutInfo(String description, String cost, String barcodePrefix)
    {
        Properties props = new Properties();
        props.setProperty("typeDescription", description);
        props.setProperty("cost", cost);
        props.setProperty("barcodePrefix", barcodePrefix);

        myModel.stateChangeRequest("AddTreeTypeWithInfo", props);

        displayMessage("Successfully added new Tree Type");
    }

    public static boolean isDoubleValue(String input) {
        // Regular expression for a double value
        String doubleRegex = "^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$";
        Pattern pattern = Pattern.compile(doubleRegex);
        return pattern.matcher(input).matches();
    }


    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }


    /**
     * Required by interface, but has no role here
     */
    //---------------------------------------------------------
    public void updateState(String key, Object value)
    {

    }

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message)
    {
        statusLog.displayErrorMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }

}

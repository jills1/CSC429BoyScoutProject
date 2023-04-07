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

// project imports
import impresario.IModel;
import model.Tree;

/** The class containing the Deposit Transaction View  for the ATM application */
//==============================================================
public class RemoveTreeTransactionView extends View
{

    // Model

    // GUI components
    private TextField barcode;
    //private TextField treeType;

    //private TextField status;
    //private TextField notes;
    //private TextField dateStatusUpdated;



    private Button submitButton;
    private Button cancelButton;

    // For showing error message
    private MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public RemoveTreeTransactionView(IModel trans)
    {
        super(trans, "RemoveTreeTransactionView");

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
        myModel.subscribe("TransactionError",this);
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

        Label barcodeLabel = new Label("barcode : ");
        grid.add(barcodeLabel, 0, 0);
        System.out.println("view found in Remove tree transaction view");

        barcode = new TextField();
        barcode.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(barcode, 1, 0);



        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();

                processAction(e);
            }
        });

        cancelButton = new Button("Back");
        cancelButton.setOnAction(new EventHandler <ActionEvent> () {

            @Override
            public void handle(ActionEvent e) {
                /**
                 * Process the Cancel button.
                 * The ultimate result of this action is that the transaction will tell the teller to
                 * to switch to the transaction choice view. BUT THAT IS NOT THIS VIEW'S CONCERN.
                 * It simply tells its model (controller) that the deposit transaction was canceled, and leaves it
                 * to the model to decide to tell the teller to do the switch back.
                 */
                //----------------------------------------------------------
                clearErrorMessage();
                myModel.stateChangeRequest("CancelDeposit", null);
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
    private Node createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;

    }


    //-------------------------------------------------------------
    public void populateFields()
    {

    }


    /**
     * Process barcode number selected by user.
     * Action is to pass this info on to the transaction object.
     */
    //----------------------------------------------------------
    private void processAction(Event evt)
    {
        clearErrorMessage();

        String barcodeEntered = barcode.getText();
        if(barcodeEntered.length()!= 5){
            displayErrorMessage("Enter a valid barcode");
        }



                    processTreeInfo(barcodeEntered);
    }
    private void processTreeInfo(String barcode)
    {
        Properties props = new Properties();
        props.setProperty("barcode", barcode);



        // call RemoveView here
        myModel.stateChangeRequest("searchTree", props);
        Tree tree = new Tree(props);

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
        if ((key.equals("TransactionError")))
            displayErrorMessage((String)value);

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

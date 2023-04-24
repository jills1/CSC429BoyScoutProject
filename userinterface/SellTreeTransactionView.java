// specify the package
package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

// project imports
import impresario.IModel;
import model.Tree;
import model.TreeType;

/** The class containing the Deposit Transaction View  for the ATM application */
//==============================================================
public class SellTreeTransactionView extends View
{

    // Model
    public Tree aTree;
    public TreeType aTreeType;
    // GUI components
    private TextField barcode;
    private TextField cost;
    private TextField notes;
    private ComboBox<String> transType;
    private TextField custName;
    private TextField custPhoneNumber;
    private TextField custEmail;





    private Button submitButton;
    private Button cancelButton;
    private Button enterButton;

    // For showing error message
    private MessageView statusLog;

    // constructor for this class -- takes a model object
    //----------------------------------------------------------
    public SellTreeTransactionView(IModel trans)
    {
        super(trans, "SellTreeTransactionView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                          "));

        getChildren().add(container);


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


        barcode = new TextField();
        barcode.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(barcode, 1, 0);

        Label costLabel = new Label("Cost : ");
        grid.add(costLabel, 0, 1);

        enterButton = new Button("EnterBarcode");
        grid.add(enterButton, 2, 0);
        enterButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();

                processAction2(e);
            }
        });


        cost = new TextField();
        cost.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(cost, 1, 1);

        Label notesLabel = new Label("Notes : ");
        grid.add(notesLabel, 0, 2);


        notes = new TextField();
        notes.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(notes, 1, 2);

        Label transTypeLabel = new Label("Transaction Type : ");
        grid.add(transTypeLabel, 0, 3);


        transType = new ComboBox<String>();
        transType.getItems().addAll("Check", "Cash");


        grid.add(transType, 1, 3);

        Label custNameLabel = new Label("Customer  Name : ");
        grid.add(custNameLabel, 0, 4);


        custName = new TextField();
        custName.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(custName, 1, 4);

        Label custPhoneNumberLabel = new Label("Customer Phone Number : ");
        grid.add(custPhoneNumberLabel, 0, 5);


        custPhoneNumber = new TextField();
        custPhoneNumber.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(custPhoneNumber, 1, 5);

        Label custEmailLabel = new Label("Customer Email : ");
        grid.add(custEmailLabel, 0, 6);


        custEmail = new TextField();
        custEmail.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                processAction(e);
            }
        });
        grid.add(custEmail, 1, 6);


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

                //----------------------------------------------------------
                clearErrorMessage();
                myModel.stateChangeRequest("CancelSale", null);
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
        String treeStatus = (String)myModel.getState("status");
        if ( treeStatus.equals("Sold"))
        {
            displayErrorMessage("This tree is already sold");
        }
        else {
            cost.setText((String) myModel.getState("cost"));
            notes.setText((String) myModel.getState("notes"));
        }
    }


    /**
     * Process barcode number selected by user.
     * Action is to pass this info on to the transaction object.
     */
    //----------------------------------------------------------
    private void processAction(Event evt)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        Integer hours = now.getHour();
        Integer minutes = now.getMinute();
        String hoursAndMinutes = (hours.toString()+":" + minutes.toString());
        System.out.println(hoursAndMinutes);

        String barcodeEntered = barcode.getText();
        System.out.println(barcodeEntered);
        String costEntered = cost.getText();
        System.out.println(costEntered);
        String paymentMethod = transType.getValue();
        System.out.println(paymentMethod);

        String custNameEntered = custName.getText();
        System.out.println(custNameEntered);
        String custPhoneEntered = custPhoneNumber.getText();
        System.out.println(custPhoneEntered);
        String custEmailEntered = custEmail.getText();
        System.out.println(custEmailEntered);
        String transactionDate = dtf.format(now);
        System.out.println(transactionDate);
        String transactionTime = hoursAndMinutes;
        System.out.println(transactionTime);
        String dateStatusUpdated = dtf.format(now);
        System.out.println(dateStatusUpdated);


        if((costEntered == null) || !isNumeric(costEntered)) {
            displayErrorMessage("Enter a valid cost");
        }

        else if ((custNameEntered.length() > 25)) {
            displayErrorMessage("Name too long for the database");
        }
        else if ((custNameEntered == null) || (isNumeric(custNameEntered)== true)) {
            displayErrorMessage("Enter a valid customer name");
        }
        else if((custPhoneEntered.length()!=10) || (!isNumeric(custPhoneEntered) ))
        {
            displayErrorMessage("Phone number must be in numeric format xxxxxxxxxx");
        }
        else if ((custEmailEntered == null) || (custEmailEntered.length() == 0))
        {
            displayErrorMessage("Please enter a valid email");
        }
        else if (( !custEmailEntered.contains("@")))
        {
            displayErrorMessage("Please enter a valid the email");
        }
        else
            processTransactionInfo(barcodeEntered, costEntered, paymentMethod, custNameEntered,
                    custPhoneEntered, custEmailEntered, transactionDate, transactionTime, dateStatusUpdated);
            //displayErrorMessage("tests are working !");

    }
    private void processAction2(Event evt)
    {
        clearErrorMessage();

        String barcodeEntered = barcode.getText();
        if(barcodeEntered.length()!= 5){
            displayErrorMessage("Enter a valid barcode");
        }
        String notesEntered = notes.getText();


        processTreeInfo(barcodeEntered,notesEntered);
    }
    private void processTreeInfo(String barcode, String notesEntered)
    {

        Properties props = new Properties();
        Properties props2 = new Properties();
        String prefix = firstTwo(barcode);
        System.out.println(prefix);
        props.setProperty("barcode", barcode);

        props2.setProperty("barcodePrefix", prefix);




        myModel.stateChangeRequest("SubmitSellTree", props);
        myModel.stateChangeRequest("SubmitSellTreeType", props2);

        populateFields();
        Tree tree = new Tree(props);
        TreeType treeType = new TreeType(props2);


    }

    private void processTransactionInfo (String barcodeEntered, String costEntered, String paymentMethod,
                                         String custNameEntered, String custPhoneEntered, String custEmailEntered,
                                         String transactionDate, String transactionTime,String dateStatusUpdated)
    {
        Properties props = new Properties();
        Properties props2 = new Properties();

        props.setProperty("transactionType", "Tree Sale");
        props.setProperty("paymentMethod", paymentMethod);
        props.setProperty("barcode", barcodeEntered);
        props.setProperty("transactionAmount", costEntered);
        props.setProperty("customerName", custNameEntered);
        props.setProperty("customerPhone", custPhoneEntered);
        props.setProperty("customerEmail", custEmailEntered);
        props.setProperty("transactionDate", transactionDate);
        props.setProperty("transactionTime", transactionTime);
        props.setProperty("dateStatusUpdate", dateStatusUpdated);
        System.out.println("collection of transaction info");
        props2.setProperty("barcode", barcodeEntered);
        props2.setProperty("status", "Sold");
        props2.setProperty("dateStatusUpdate", dateStatusUpdated);
        System.out.println("collection of tree info");
        myModel.stateChangeRequest("UpdateTreeInfo", props2);
        System.out.println("tree info sent");
        myModel.stateChangeRequest("UpdateTransactionInfo", props);

        displayMessage("Successfully sold Tree");
    }
    public String firstTwo(String str) {
        return str.length() < 2 ? str : str.substring(0,2);
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

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}

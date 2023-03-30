package userinterface;

// system imports
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Vector;
import java.util.Enumeration;

// project imports
import impresario.IModel;
import model.Account;
import model.AccountCollection;
import model.Tree;
import model.TreeCollection;

//==============================================================================
public class TreeCollectionView extends View
{
    protected TableView<TreeTableModel> tableOfTrees;
    protected Button cancelButton;
    protected Button submitButton;

    protected MessageView statusLog;


    //--------------------------------------------------------------------------
    public TreeCollectionView(IModel wsc)
    {
        super(wsc, "TreeCollectionView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(createTitle());
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                                            "));

        getChildren().add(container);

        populateFields();
    }

    //--------------------------------------------------------------------------
    protected void populateFields()
    {
        getEntryTableModelValues();
    }

    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {

        ObservableList<TreeTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            TreeCollection treeCollection = (TreeCollection)myModel.getState("TreeList");

            Vector entryList = (Vector)treeCollection.getState("Trees");
            Enumeration entries = entryList.elements();

            while (entries.hasMoreElements() == true)
            {
                Tree nextTree = (Tree)entries.nextElement();
                Vector<String> view = nextTree.getEntryListView();

                // add this list entry to the list
                TreeTableModel nextTableRowData = new TreeTableModel(view);
                tableData.add(nextTableRowData);

            }

            tableOfTrees.setItems(tableData);
        }
        catch (Exception e) {//SQLException e) {
            // Need to handle this exception
        }
    }

    // Create the title container
    //-------------------------------------------------------------
    private Node createTitle()
    {
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

    // Create the main form content
    //-------------------------------------------------------------
    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text prompt = new Text("Select From list Of Trees");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        tableOfTrees = new TableView<TreeTableModel>();
        tableOfTrees.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn bcColumn = new TableColumn("barcode") ;
        bcColumn.setMinWidth(100);
        bcColumn.setCellValueFactory(
                new PropertyValueFactory<TreeTableModel, String>("bacode"));

        TableColumn sColumn = new TableColumn("status") ;
        sColumn.setMinWidth(100);
        sColumn.setCellValueFactory(
                new PropertyValueFactory<TreeTableModel, String>("status"));

        TableColumn nColumn = new TableColumn("notes") ;
        nColumn.setMinWidth(100);
        nColumn.setCellValueFactory(
                new PropertyValueFactory<TreeTableModel, String>("notes"));

        TableColumn dsuColumn = new TableColumn("Date Status Updated") ;
        dsuColumn.setMinWidth(100);
        dsuColumn.setCellValueFactory(
                new PropertyValueFactory<TreeTableModel, String>("dateStatusUpdated"));





        tableOfTrees.getColumns().addAll(bcColumn,
                sColumn, nColumn, dsuColumn);

        tableOfTrees.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
                    processTreeSelected();
                }
            }
        });
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(115, 150);
        scrollPane.setContent(tableOfTrees);

        submitButton = new Button("Submit");
        submitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                clearErrorMessage();
                // do the inquiry
                processTreeSelected();

            }
        });

        cancelButton = new Button("Back");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                /**
                 * Process the Cancel button.
                 * The ultimate result of this action is that the transaction will tell the teller to
                 * to switch to the transaction choice view. BUT THAT IS NOT THIS VIEW'S CONCERN.
                 * It simply tells its model (controller) that the transaction was canceled, and leaves it
                 * to the model to decide to tell the teller to do the switch back.
                 */
                //----------------------------------------------------------
                clearErrorMessage();
                myModel.stateChangeRequest("CancelTreeList", null);
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(submitButton);
        btnContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(scrollPane);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }



    //--------------------------------------------------------------------------
    public void updateState(String key, Object value)
    {
    }

    //--------------------------------------------------------------------------
    protected void processTreeSelected()
    {
       TreeTableModel selectedItem = tableOfTrees.getSelectionModel().getSelectedItem();

        if(selectedItem != null)
        {
            String selectedTree = selectedItem.getbarcode();

            myModel.stateChangeRequest("TreeSelected", selectedTree);
        }
    }

    //--------------------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }


    /**
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }

	//--------------------------------------------------------------------------
	public void mouseClicked(MouseEvent click)
	{
		if(click.getClickCount() >= 2)
		{
			processTreeSelected();
		}
	}


}

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
import model.Scout;
import model.ScoutCollection;
import model.Scout;
import model.ScoutCollection;
import model.ScoutTableModel;

//==============================================================================
public class SelectUpdateScoutView extends View
{
	protected TableView<ScoutTableModel> tableOfScouts;
	protected Button cancelButton;
	protected Button submitButton;

	protected MessageView statusLog;


	//--------------------------------------------------------------------------
	public SelectUpdateScoutView(IModel wsc)
	{
		super(wsc, "SelectUpdateScoutView");

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
		
		ObservableList<ScoutTableModel> tableData = FXCollections.observableArrayList();
		try
		{
			ScoutCollection scoutCollection = (ScoutCollection)myModel.getState("ScoutList");

	 		Vector entryList = (Vector)scoutCollection.getState("Scouts");
			Enumeration entries = entryList.elements();

			while (entries.hasMoreElements() == true)
			{
				Scout nextScout = (Scout)entries.nextElement();
				Vector<String> view = nextScout.getEntryListView();

				// add this list entry to the list
				ScoutTableModel nextTableRowData = new ScoutTableModel(view);
				tableData.add(nextTableRowData);
				
			}
			
			tableOfScouts.setItems(tableData);
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
        
        Text prompt = new Text("Select from the scout list");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

		tableOfScouts = new TableView<ScoutTableModel>();
		tableOfScouts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		TableColumn firstNameColumn = new TableColumn("First Name") ;
		firstNameColumn.setMinWidth(100);
		firstNameColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, String>("firstName"));
		
		TableColumn lastNameColumn = new TableColumn("Last Name") ;
		lastNameColumn.setMinWidth(100);
		lastNameColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, String>("lastName"));
		
		TableColumn middleNameColumn = new TableColumn("Middle Name") ;
		middleNameColumn.setMinWidth(100);
		middleNameColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, String>("middleName"));

		TableColumn dateOfBirthColumn = new TableColumn("Date of Birth") ;
		dateOfBirthColumn.setMinWidth(100);
		dateOfBirthColumn.setCellValueFactory(
	                new PropertyValueFactory<ScoutTableModel, String>("dateOfBirth"));

		TableColumn phoneNumberColumn = new TableColumn("Phone Number") ;
		phoneNumberColumn.setMinWidth(100);
		phoneNumberColumn.setCellValueFactory(
								new PropertyValueFactory<ScoutTableModel, String>("phoneNumber"));

		TableColumn emailColumn = new TableColumn("Email") ;
		emailColumn.setMinWidth(100);
		emailColumn.setCellValueFactory(
								new PropertyValueFactory<ScoutTableModel, String>("email"));
		
		TableColumn troopIDColumn = new TableColumn("Troop ID") ;
		troopIDColumn.setMinWidth(100);
		troopIDColumn.setCellValueFactory(
								new PropertyValueFactory<ScoutTableModel, String>("troopID"));

		tableOfScouts.getColumns().addAll(
			lastNameColumn, firstNameColumn, middleNameColumn, dateOfBirthColumn, phoneNumberColumn, emailColumn, troopIDColumn);

		tableOfScouts.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event)
			{
				if (event.isPrimaryButtonDown() && event.getClickCount() >=2 ){
					processScoutSelected();
				}
			}
		});
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPrefSize(115, 150);
		scrollPane.setContent(tableOfScouts);

		submitButton = new Button("Submit");
 		submitButton.setOnAction(new EventHandler<ActionEvent>() {

       		     @Override
       		     public void handle(ActionEvent e) {
       		     	clearErrorMessage(); 
					// do the inquiry
					processScoutSelected();
					//testProcessAction(e);
					
            	 }
        	});

		cancelButton = new Button("Cancel");
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
       		     	myModel.stateChangeRequest("CancelUpdate", null);
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
	protected void processScoutSelected()
	{
		ScoutTableModel selectedItem = tableOfScouts.getSelectionModel().getSelectedItem();
		
		if(selectedItem != null)
		{
			String selectedScout = selectedItem.getTroopID();

			myModel.stateChangeRequest("ScoutChosen", selectedScout);
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
	/*
	//--------------------------------------------------------------------------
	public void mouseClicked(MouseEvent click)
	{
		if(click.getClickCount() >= 2)
		{
			processScoutSelected();
		}
	}
   */
  private void testProcessAction(ActionEvent e) {
	myModel.stateChangeRequest("UpdateScoutInfo", null);
}
	
}
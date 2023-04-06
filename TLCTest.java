import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import event.Event;
import model.TLC;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;
/** Main Class Containing Main Stage Running 'variable' */
//==============================================================
public class TLCTest extends Application {
	private TLC myTLC;
	private Stage mainStage;
	public void start(Stage primaryStage) {
	   System.out.println("Tree Sales Program Alpha 0.1");
	   //Create Main Stage
	   MainStageContainer.setStage(primaryStage, "Tree Sales 2.0");
	   mainStage = MainStageContainer.getInstance();
	   mainStage.setOnCloseRequest(new EventHandler <javafx.stage.WindowEvent>() {
		   @Override
		   public void handle(javafx.stage.WindowEvent event) { System.exit(0);}});
	   try {
		myTLC = new TLC();
	   } catch(Exception exc) {
		System.err.println("Program Contain could not be made!");
		new Event(Event.getLeafLevelClassName(this), "ATM.<init>", "Unable to create Seller object", Event.ERROR);
		exc.printStackTrace();
	   }
  	   WindowPosition.placeCenter(mainStage);
           mainStage.show();
	}
	//----------------------------------------------------------
	public static void main(String[] args) {
		launch(args);
	}
}

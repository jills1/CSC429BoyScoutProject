// tabs=4
//************************************************************
//	COPYRIGHT Sandeep Mitra, 2015 - ALL RIGHTS RESERVED
//
//
//*************************************************************
package userinterface;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.stage.Screen;
//==============================================================
public class WindowPosition {
	 private static Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	//--------------------------------------------------------------------------
	public static void placeCenter(Stage s) {
		if (s != null) {
			s.centerOnScreen();
		}
	}
	//--------------------------------------------------------------------------
	public static void placeTopLeft(Stage s) {
		if (s != null) {
			s.setX(primaryScreenBounds.getMinX());
			s.setY(primaryScreenBounds.getMinY());
		}
	}
	//--------------------------------------------------------------------------
	public static void placeTopRight(Stage s) 
	{
		if (s != null) 
		{
			s.setX(primaryScreenBounds.getMinX() + 
				primaryScreenBounds.getWidth() - s.getWidth());
			s.setY(primaryScreenBounds.getMinY());
		}
	}
	//--------------------------------------------------------------------------
	public static void placeBottomLeft(Stage s) 
	{
		if (s != null) 
		{
			s.setX(primaryScreenBounds.getMinX());
			s.setY(primaryScreenBounds.getMinY() +
				primaryScreenBounds.getHeight() - s.getHeight());
		}
		
	}
	//--------------------------------------------------------------------------
	public static void placeBottomRight(Stage s) 
	{
		if (s != null) 
		{
			s.setX(primaryScreenBounds.getMinX() + 
				primaryScreenBounds.getWidth() - s.getWidth());
			s.setY(primaryScreenBounds.getMinY() +
				primaryScreenBounds.getHeight() - s.getHeight());
		}
	}
}
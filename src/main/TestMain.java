package main;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import main.view.MainSceneController;

public class TestMain extends Application {
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
	  FXMLLoader loader=new FXMLLoader();
	  loader.setLocation(TestMain.class.getResource("view/MainScene.fxml"));
	try {
		Pane pane = (Pane)loader.load();
		MainSceneController controller=loader.getController();
	    // Create a circle and set its properties
	    
	    // Create a pane to hold the circle 
	    //pane.getChildren().add(circle);
	    // Create a scene and place it in the stage
	    Scene scene = new Scene(pane);
	    primaryStage.setTitle("ShowCircle"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
	    controller.initEvent();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  /**
   * The main method is only needed for the IDE with limited
   * JavaFX support. Not needed for running from the command line.
   */
  public static void main(String[] args) {
    launch(args);
  }
}


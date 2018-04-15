package main;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import main.view.MainSceneController;
import reader.SimpleConf;

public class TestMain extends Application {
	public static String settingFile="setting.txt";
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
	  FXMLLoader loader=new FXMLLoader();
	  loader.setLocation(TestMain.class.getResource("view/MainScene.fxml"));
	try {
		
		Pane pane = (Pane)loader.load();
		MainSceneController controller=loader.getController();
		// if no file write one.
		if(!Files.exists(Paths.get(settingFile)))
		{
			writeSetting();
		}
		//read setting
		Map<String,String>data=SimpleConf.read(settingFile);
		Color color=Color.web(data.get("color"));
		controller.setColor(color);
	    Scene scene = new Scene(pane);
	    primaryStage.setTitle("NoteBook"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
	    controller.initEvent();
	    primaryStage.setOnCloseRequest((e)->
	    {
	    		writeSetting(controller.getColor());
	    });
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  public void writeSetting(Color color)
  {
		Map<String,String> def=new TreeMap<String,String>();
		def.put("color", color.toString());
		SimpleConf.write(def, settingFile);
  }
  //default setting
  public void writeSetting()
  {
	  writeSetting(Color.BLACK);
  }
  public static void main(String[] args) {
    launch(args);
  }
}


package main.view;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.TestMain;
import main.model.Book;

public class MainSceneController 
{
	@FXML
	private Pane pane;
	@FXML
	private Menu menu;

	private Book book;

	private ImageView iv;

	private WritableImage image;

	private boolean erase=false;



	@FXML
	public void open()
	{
		System.out.println("open");
		String filename=select();
		if(filename==null)
			return;
		book=new Book(filename);
		pane.setPrefWidth(book.getWidth());
		pane.setPrefHeight(book.getHeight());
		Path p=Paths.get(book.getFile());
		System.out.println(p.toAbsolutePath().toString());
		if(!Files.exists(p))
			System.out.println("Image source is corrupted");
		else
		{
			setImage();
			iv.setImage(image);
			iv.setPreserveRatio(true);
			iv.setSmooth(true);
			iv.setCache(true);
			pane.getChildren().add(iv);
		}
	}
	public void setImage()
	{
		image=new WritableImage(new Image(new File(book.getFile()).toURI().toString()).getPixelReader(),book.getWidth(),book.getHeight());
		iv.setImage(image);
	}
	public void saveImage()
	{
		book.saveImage(SwingFXUtils.fromFXImage(image, null));
	}
	public void write(int x,int y)
	{
		PixelWriter writer=image.getPixelWriter();
		for(int i=-1;i<2;i++)
		{
			for(int j=-1;j<2;j++)
			{
				int alterX=x+i;
				int alterY=y+j;
				if(alterX>=0&&alterX<book.getWidth()&&alterY>=0&&alterY<book.getHeight())
					writer.setColor(alterX, alterY, Color.BLACK);
			}
		}
	}
	public void clear(int x,int y)
	{
		PixelWriter writer=image.getPixelWriter();
		for(int i=-10;i<11;i++)
		{
			for(int j=-10;j<11;j++)
			{
				int alterX=x+i;
				int alterY=y+j;
				if(alterX>=0&&alterX<book.getWidth()&&alterY>=0&&alterY<book.getHeight())
					writer.setColor(alterX, alterY, Color.WHITE);
			}

		}
	}
	public void toggleErase()
	{
		erase=!erase;
		setCursor();
	}
	private void setCursor()
	{
		if(erase)
			pane.getScene().setCursor(Cursor.CROSSHAIR);
		else
			pane.getScene().setCursor(Cursor.DEFAULT);
	}
	@FXML
	public void close()
	{
		saveImage();
		System.out.println("close");
		book=null;

	}
	public void initialize()
	{
		book=null;
		iv = new ImageView();
	}
	public void initEvent()
	{
		pane.getScene().addEventHandler(KeyEvent.KEY_PRESSED, e->
		{
			switch(e.getCode())
			{
			case A:
				previous();
				break;
			case D:
				next();
				break;
			case B:
				toggleErase();
				break;
			case S:
				saveImage();
				break;
			default:
				break;
			}
		});
		pane.setOnMouseDragged(e->{
			if(book!=null)
			{
				if(!erase)
				{
					write((int)e.getX(),(int)e.getY());
				}
				else
				{
					clear((int)e.getX(),(int)e.getY());
				}
			}
		});
		setCursor();
		/*
		pane.setOnMouseClicked(e->{
			if(book!=null)
			{
				write((int)e.getX(),(int)e.getY());
			}
		});
		 */


	}
	@FXML
	public void next()
	{
		saveImage();
		if(book!=null)
		{
			if(book.next())
			{
				setImage();
				setShowPage();
			}
		}
	}
	public void setShowPage()
	{
		menu.setText("Page: "+book.getIndex());
	}
	@FXML
	public void previous()
	{
		saveImage();
		if(book!=null)
		{
			if(book.previous())
			{
				setImage();
				setShowPage();
			}
		}
	}
	public void goTo(int index)
	{
		saveImage();
		if(book!=null)
		{
			if(book.goTo(index))
			{
				setImage();
				setShowPage();
			}
		}
	}
	@FXML
	public void remove()
	{
		book.remove();
		setImage();
		setShowPage();
	}
	public static String select()
	{
		Stage stage=new Stage();
		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showSaveDialog(stage);
		if(file==null)
			return null;
		return file.getAbsolutePath();
	}
}

package main.view;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
	@FXML
	private Menu colorMenu;

	private Book book;

	private ImageView iv;

	private WritableImage image;

	private boolean erase=false;
	
	private boolean drag=false;
	
	private Color paintColor=Color.BLACK;
	
	private int x,y;



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
			setShowPage();
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
	public void drawStroke(int x1,int y1,int x2,int y2)
	{
		PixelWriter writer=image.getPixelWriter();
		if(x1==x2)
		{
			int minY,maxY;
			if(y1>y2)
			{
				minY=y2;maxY=y1;
			}
			else
			{
				minY=y1;maxY=y2;
			}
			for(int i=0;i<=maxY-minY;i++)
			{
				writer.setColor(x1, minY+i, paintColor);
			}
		}
		else
		{
			double slope=(y1-y2)/(x1-x2);
			if(Math.abs(x1-x2)>Math.abs(y1-y2))
			{
				int minX,maxX,originY;
				if(x1>x2)
				{
					minX=x2;maxX=x1;originY=y2;
				}
				else
				{
					minX=x1;maxX=x2;originY=y1;
				}
				for(int i=0;i<=maxX-minX;i++)
				{
					double centerY=originY+slope*i;
					writer.setColor(minX+i,(int) Math.floor(centerY),paintColor);
				}
			}
			else
			{
				double qSlope=1/slope;
				int minY,maxY,originX;
				if(y1>y2)
				{
					minY=y2;maxY=y1;originX=x2;
				}
				else
				{
					minY=y1;maxY=y2;originX=x1;
				}
				for(int i=0;i<=maxY-minY;i++)
				{
					double centerX=originX+qSlope*i;
					writer.setColor((int) Math.floor(centerX),minY+i,paintColor);
				}
			}
		}
	}
	public void rubber(int x,int y)
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
	@FXML
	public void clear()
	{
		image=new WritableImage(book.getWidth(),book.getHeight());
		PixelWriter writer=image.getPixelWriter();
		for(int i=0;i<book.getWidth();i++)
		{
			for(int j=0;j<book.getHeight();j++)
			{
				writer.setColor(i, j, Color.WHITE);
			}
		}
		iv.setImage(image);
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
		book = null;
		iv = new ImageView();
	}
	@FXML
	public void quickDraw()
	{
		drawStroke(book.getWidth()/2,0,book.getWidth()/2,book.getHeight());
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
		pane.setOnMousePressed(e->{
			if(book!=null)
			{
				if(!erase)
				{
					drag=true;
					x=(int) e.getX();
					y=(int) e.getY();
				}
				else
				{
					rubber((int)e.getX(),(int)e.getY());
				}
			}
		});
		pane.setOnMouseClicked(e->
		{
			if(book!=null)
			{
				if(!erase)
				{
					PixelWriter writer=image.getPixelWriter();
					writer.setColor((int)e.getX(),(int)e.getY(), Color.BLACK);
				
				}
			}
		});
		pane.setOnMouseDragged(e->{
			if(book!=null)
			{
				if(!erase)
				{
					if(drag)
					{
						drawStroke(x,y,(int)e.getX(),(int)e.getY());
						x=(int) e.getX();
						y=(int) e.getY();
					}
				}
				else
				{
					rubber((int)e.getX(),(int)e.getY());
				}
			}
		});
		pane.setOnMouseReleased(e->{
			drag=false;});
		setCursor();
		colorMenu.getItems().add(new ColorOption(Color.RED));
		colorMenu.getItems().add(new ColorOption(Color.ORANGE));
		colorMenu.getItems().add(new ColorOption(Color.YELLOW));
		colorMenu.getItems().add(new ColorOption(Color.GREEN));
		colorMenu.getItems().add(new ColorOption(Color.BLUE));
		colorMenu.getItems().add(new ColorOption(Color.INDIGO));
		colorMenu.getItems().add(new ColorOption(Color.web("9400B3")));
		colorMenu.getItems().add(new ColorOption(Color.BLACK));
		

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
	public class ColorOption extends CustomMenuItem
	{
		private Rectangle rectangle;
		public ColorOption(Color color)
		{
			rectangle=new Rectangle(20,20,color);
			this.setContent(rectangle);
			this.setOnAction((e)->{
				paintColor=color;
			});
		}
	}
}

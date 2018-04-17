package main.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import javafx.scene.image.Image;
import reader.MapControl;
import reader.SimpleConf;

public class Book 
{
	public static final int MAXPAGE=1000;
	private MapControl mc;
	private int maxPage;
	private int width;
	private int height;
	private String filename;
	public Book(String filename)
	{
		Map<String,String> defaultMap=new TreeMap<String,String>();
		defaultMap.put("width", Integer.toString(1200));
		defaultMap.put("height", Integer.toString(800));
		defaultMap.put("maxPage", Integer.toString(500));
		defaultMap.put("current", Integer.toString(0));
		defaultMap.put("lastOpen", Integer.toString(1));
		mc=new MapControl(filename,defaultMap);
		width=Integer.parseInt(mc.read("width"));
		height=Integer.parseInt(mc.read("height"));
		maxPage=Integer.parseInt(mc.read("maxPage"));
		this.filename=filename;
		if(!hasPage())
			generatePage(getIndex());
	}
	public int getIndex() {return Integer.parseInt(mc.read("lastOpen"));}
	public void addPage()
	{
		addPage(getIndex());
	}
	private int current() {return Integer.parseInt(mc.read("current"));}
	public void addPage(int index)
	{
		if(index<1||index>current())
			return;
		else if(current()==maxPage)
			return;
		else
		{
			for(int i=current();i>=index;i--)
			{
				File tempfile=new File(getFile(i));
				tempfile.renameTo(new File(getFile(i+1)));
			}
			generatePage(index);
		}
	}
	public boolean setIndex(int index) 
	{
		if(index>0&&index<=current())
		{
			mc.change("lastOpen", Integer.toString(index));
			return true;
		}
		return false;
	}
	public void remove()
	{
		remove(getIndex());
		if(current()<getIndex())
			setIndex(getIndex()-1);
	}
	public void remove(int index)
	{
		if(index<1||index>current())
			return;
		else
		{
			File file=new File(getFile(index));
			file.delete();
			for(int i=index+1;i<current()+1;i++)
			{
				File tempfile=new File(getFile(i));
				tempfile.renameTo(new File(getFile(i-1)));
			}
			reduceCurrent();
		}
	}
	public String getFolder()
	{
		return Paths.get(filename).toAbsolutePath().getParent().toString();
	}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public boolean hasPage()
	{
		File file=new File(getFile());
		return file.exists();
	}
	private void generatePage(int index)
	{
		BufferedImage output=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D gr=output.createGraphics();
		gr.setColor(Color.white);
		gr.fillRect(0, 0, width, height);
        try {
			ImageIO.write(output, "png", new File(getFile(index)));
			addCurrent();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Image getImage()
	{
		Path p=Paths.get(getFile());
		System.out.println(p.toAbsolutePath().toString());
		if(!Files.exists(p))
		{
			System.out.println("Image source is corrupted");
			return null;
		}
		return new Image(new File(getFile()).toURI().toString());
	}
	private String getFile()
	{
		return getFolder()+"/page"+getIndex()+".png";
	}
	private String getFile(int index)
	{
		return getFolder()+"/page"+index+".png";
	}
	private void addCurrent()
	{
		if(current()!=maxPage)
		{
			mc.change("current", Integer.toString(current()+1));
		}
	}
	private void reduceCurrent()
	{
		if(current()!=0)
		{
			mc.change("current", Integer.toString(current()-1));
		}
	}
	public void saveImage(BufferedImage input)
	{
		if(input.getWidth()==width&&input.getHeight()==height)
		{
			try {
				ImageIO.write(input, "png", new File(getFile()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
			return;
	}
	public void saveSetting()
	{
		mc.write();
	}
	public boolean next()
	{
		if(getIndex()==maxPage)
			return false;
		else
		{
			setIndex(getIndex()+1);
			if(!hasPage())
				generatePage(getIndex());
			return true;
		}
	}
	public boolean previous()
	{
		if(getIndex()==1)
			return false;
		else
		{
			setIndex(getIndex()-1);
			return true;
		}
		
	}
	public boolean goTo(int index)
	{
		return setIndex(index);
	}
}

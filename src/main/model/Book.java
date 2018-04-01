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

import reader.SimpleConf;

public class Book 
{
	public static final int MAXPAGE=1000;
	private int maxPage;
	private int width;
	private int height;
	private int current;
	private int index;
	private String filename;
	public Book(String filename)
	{
		if(!Files.exists(Paths.get(filename)))
		{
			//int width=Integer.parseInt(JOptionPane.showInputDialog("Width:"));
			//int height=Integer.parseInt(JOptionPane.showInputDialog("Height:"));
			//int maxPage=Integer.parseInt(JOptionPane.showInputDialog("MaxPage"));
			//TODO javafx joptionpanel
			int width=1200;
			int height=800;
			int maxPage=500;
			int current=0;
			Map<String,String> def=new TreeMap<String,String>();
			def.put("width", Integer.toString(width));
			def.put("height", Integer.toString(height));
			def.put("maxPage", Integer.toString(maxPage));
			def.put("current", Integer.toString(current));
			SimpleConf.write(def, filename);
		}
		Map<String,String>data=SimpleConf.read(filename);
		width=Integer.parseInt(data.get("width"));
		height=Integer.parseInt(data.get("height"));
		maxPage=Integer.parseInt(data.get("maxPage"));
		current=Integer.parseInt(data.get("current"));
		this.filename=filename;
		index=1;
		if(!hasPage())
			generatePage();
	}
	public static void main(String[] args)
	{
		Book book=new Book("book.txt");
	}
	/*
	public void insert()
	{
		
	}
	public void insert()
	{
		
	}
	*/
	public void remove()
	{
		remove(index);
		if(current<index)
			index--;
	}
	public void remove(int index)
	{
		if(index<1||index>current)
			return;
		else
		{
			File file=new File(getFile(index));
			file.delete();
			for(int i=index+1;i<current+1;i++)
			{
				File tempfile=new File(getFile(i));
				tempfile.renameTo(new File(getFile(i-1)));
			}
			setCurrent(current-1);
		}
	}
	public String getFolder()
	{
		return Paths.get(filename).toAbsolutePath().getParent().toString();
	}
	public int getIndex() {return index;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public boolean hasPage()
	{
		File file=new File(getFile());
		return file.exists();
	}
	private void generatePage()
	{
		BufferedImage output=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D gr=output.createGraphics();
		gr.setColor(Color.white);
		gr.fillRect(0, 0, width, height);
        try {
			ImageIO.write(output, "png", new File(getFile()));
			setCurrent(current+1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getFile()
	{
		return getFolder()+"/page"+index+".png";
	}
	public String getFile(int index)
	{
		return getFolder()+"/page"+index+".png";
	}
	public void setCurrent(int current)
	{
		this.current=current;
		Map<String,String> def=new TreeMap<String,String>();
		def.put("width", Integer.toString(width));
		def.put("height", Integer.toString(height));
		def.put("maxPage", Integer.toString(maxPage));
		def.put("current", Integer.toString(current));
		SimpleConf.write(def, filename);
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
	public boolean next()
	{
		if(index==maxPage)
			return false;
		else
		{
			index++;
			if(!hasPage())
				generatePage();
			return true;
		}
	}
	public boolean previous()
	{
		if(index==1)
			return false;
		else
		{
			index--;
			if(!hasPage())
				generatePage();
			return true;
		}
		
	}
	public boolean goTo(int index)
	{
		if(index<0||index>maxPage)
			return false;
		else
		{
			this.index=index;
			return true;
		}
	}
	public int getCurrent() {return current;}
	@Override
	protected void finalize() throws Throwable {
		System.out.println("current: "+current);
		super.finalize();
	}
	
}

package main.model;

import java.util.Map;
import java.util.TreeMap;

import reader.MapControl;

public class BookReader 
{
	private Book book;
	private MapControl mc;
	
	public BookReader(String filename)
	{
		Map<String,String> defaultMap=new TreeMap<String,String>();
		defaultMap.put("lastOpen", Integer.toString(1));
		defaultMap.put("imageCount", Integer.toString(2));
		mc=new MapControl(filename,defaultMap);
		book=new Book(filename);
		
	}
	public void next()
	{
		
	}
	

}

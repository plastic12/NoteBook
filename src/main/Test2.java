package main;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Test2 
{
	public static void main(String[] args) throws IOException
	{
		//drawImage();
		fileConvert();
		/*
		Font font = new Font("TimesRoman", Font.BOLD, 20);
		gr.setFont(font);
		String message = "www.java2s.com!";
		FontMetrics fontMetrics = gr.getFontMetrics();
		int stringWidth = fontMetrics.stringWidth(message);
		int stringHeight = fontMetrics.getAscent();
		gr.setPaint(Color.black);
		gr.drawString(message, (100 - stringWidth) / 2, 100 / 2 + stringHeight / 4);
		gr.dispose();
		*/

		/*
	    int width = 100;
	    int height = 100;

	    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	    Graphics2D g2d = bufferedImage.createGraphics();

	    g2d.setColor(Color.white);
	    g2d.fillRect(0, 0, width, height);
	    g2d.setColor(Color.black);
	    g2d.fillOval(0, 0, width, height);

	    g2d.dispose();
	    RenderedImage rendImage = bufferedImage;

	    File file = new File("newimage.png");
	    ImageIO.write(bufferedImage, "png", file);

	    file = new File("newimage.jpg");
	    ImageIO.write(bufferedImage, "jpg", file);
	    */
	}
	public static void drawImage()
	{
		JFXPanel jfxPanel = new JFXPanel();
		BufferedImage output=new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
		Graphics2D gr=output.createGraphics();
		gr.setColor(java.awt.Color.white);
		gr.fillRect(0, 0, 100, 100);
		gr.setColor(java.awt.Color.blue);
		gr.fillOval(10, 10, 20, 20);
		gr.dispose();
		/*
		Image image=SwingFXUtils.toFXImage(output, null);
		output=SwingFXUtils.fromFXImage(image, null);
		*/
		try {
			ImageIO.write(output, "jpg", new File("while.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void fileConvert()
	{
		JFXPanel jfxPanel = new JFXPanel();
		//System.out.println(new File("while.png").toURI().toString());
		Image image=new Image(new File("while.png").toURI().toString());
		WritableImage wi=new WritableImage(image.getPixelReader(),100,100);
		PixelWriter writer=wi.getPixelWriter();
		for(int i=0;i<100;i++)
		writer.setColor(i, 10,Color.ANTIQUEWHITE);
		BufferedImage output=SwingFXUtils.fromFXImage(wi, null);
		try {
			ImageIO.write(output, "png", new File("while.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

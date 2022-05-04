package main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Window {
	
	int width, height;
	String title;
	JFrame frame;
	
	Canvas canvas;	
	ClassLoader classLoader = getClass().getClassLoader();
	Image img = new ImageIcon("src/main/resources/images/BarIcon.png").getImage();
	
	//Constructor creates window with certain properties
	public Window (String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		
		frame = new JFrame(title);
		frame.setIconImage(img);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setFocusable(false);
		
		frame.add(canvas);
		frame.pack();
	}
	
	//Getters
	public int getWidth() {
		return frame.getWidth();
	}
	
	public int getHeight() {
		return frame.getHeight();
	}
	
//	@SuppressWarnings("deprecation")
//	public int getX() {
//		return frame.location().x;
//	}
//	
//	@SuppressWarnings("deprecation")
//	public int getY() {
//		return frame.location().y;
//	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
}

package gui;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class Intersection extends JPanel{
	private static final long serialVersionUID = -3855850303665219282L;
	public Point center;
	//Intersection bounds. Mainly used for finding correct intersection where to draw a stone
	//when user clicks mouse button. 
	private Rectangle bounds;
	
	public Intersection(Point center, int width){
		this.center = new Point(center);
		bounds = new Rectangle(center.x - width/2, center.y - width/2, width, width);
	}
	
	public boolean contains(Point p){
		return bounds.contains(p);
	}
	
	public Rectangle getBounds(){
		return new Rectangle(bounds);
	}
	
	public int getTopLeftX(){
		return bounds.x;
	}
	
	public int getTopLeftY(){
		return bounds.y;
	}
	
	public int getWidth(){
		return bounds.width;
	}
	
	public int getHeight(){
		return bounds.height;
	}

}

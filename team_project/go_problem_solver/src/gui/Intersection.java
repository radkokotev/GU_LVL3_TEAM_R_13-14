package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Intersection extends JPanel{
	private Point center;
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

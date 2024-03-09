package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import view_controller.GUI;

/**
 * Bucket holds information about the fill function. Each object holds all pixels it 
 * needs to draw along with other color infomration. Fast drawing is avaiable and will
 * skip the animation phase for drawing the pixels to the screen.
 */
public class Bucket implements Drawable {
	
	private ArrayList<Point2D> pixels = new ArrayList<>();
	private ArrayList<Point2D> edges = new ArrayList<>();
	
	public static boolean drawFast = false;
	
	int startX, startY;
	Paint baseColor;
	
	public Bucket(int startX, int startY, PixelReader pr, Paint baseColor) {
		this.startX = startX;
		this.startY = startY;
		this.baseColor = baseColor;
		
		// we need to see which pixels need to be filled
		collectData(pr);
	}
	
	
	/**
	 * collectData fills the arraylist of pixels so we have a record of which pixels need to be filled in for the drawing phase.
	 * PixelReader object must not be null.
	 * @param pr The pixel reader of the canvas to read from
	 */
	public void collectData(PixelReader pr) {

		// keep track of which pixels we have colored and which are not visited yet
		Queue<Point2D> colors = new LinkedList<>();
		Set<String> colored = new HashSet<>();
		
		// get the starting color
		Color baseColor = pr.getColor(startX,startY);
		
		// create the starting point and add to the queue and the set of colored pixels
		Point2D start = new Point2D(startX, startY);
		colors.add(start);
		
		int lineWidth = 1;
		
		// keep going until the queue is empty, meaning there are no more pixels to fill
		while(colors.isEmpty() == false) {

			// get the most recent pixel off the queue
			Point2D p = colors.remove();
			Color curColor = pr.getColor((int)p.getX(),(int)p.getY());
			
			// check if the colors match and the pixel is not already colored
			if(curColor.equals(baseColor) && !colored.contains(String.valueOf(((int)p.getX() + " " + (int)p.getY())))) {
				// we want to stagger how many pixels are added so we mod to see if its a multiple of fillWidth
				if(p.getX() % GUI.fillWidth == 0 && p.getY() % GUI.fillWidth == 0) {
					pixels.add(new Point2D(p.getX(),p.getY()));
					
				}
				// add the pixel to colored since we have visited it
				colored.add(String.valueOf((int)p.getX()+ " " +(int)p.getY()));
//				
				// Now add each direction to the queue if applicable
				
//				// X direction
				if(p.getX()-lineWidth < 0 == false) {
					Point2D left = new Point2D(p.getX()-lineWidth,p.getY());
					colors.add(left);
				}
				if(p.getX()+lineWidth > GUI.width -1 == false) {
					Point2D left = new Point2D(p.getX()+lineWidth,p.getY());
					colors.add(left);
				}
				// Y direction
				if(p.getY()-lineWidth < 0 == false) {
					Point2D up = new Point2D(p.getX(),p.getY()-lineWidth);
					colors.add(up);
				}
				if(p.getY()+lineWidth > GUI.height -1 == false) {
					Point2D left = new Point2D(p.getX(),p.getY()+lineWidth);
					colors.add(left);
				}
			}else {
				// add the edge just to have a nice border
				if(p.getX() % GUI.fillWidth == 0 && p.getY() % GUI.fillWidth == 0) {
					edges.add(new Point2D(p.getX(),p.getY()));
					
				}

			}
		}
		
		// Shuffle the pixels and then add the edges on the end
		Collections.shuffle(pixels);
		pixels.addAll(edges);
		edges.clear();
		
	}

	// keep track of which pixel we are processing
	public int indexPixels = 0;
	
	@Override
	public void draw(GraphicsContext gc) {
		
		
		indexPixels = 0;
		
		// If draw fast, no animation timer, just fill
		if(drawFast == true) {
			// save current info
	    	double curWidth = gc.getLineWidth();
	    	Paint curPaint = gc.getStroke();
	    	Paint curFill = gc.getFill();
	    	gc.setLineWidth(1);
	    	gc.setFill(baseColor);
	    	gc.setStroke(baseColor);

	    	// go until out of pixels
	    	while(indexPixels < pixels.size()) {
	    		Point2D p = pixels.get(indexPixels);
	    		indexPixels++;
	    		gc.fillRect(p.getX(),p.getY(),GUI.fillWidth,GUI.fillWidth);
	    	
	    	}
	    	// and reset old values
	    	gc.setLineWidth(curWidth);
	    	gc.setStroke(curPaint);
	    	gc.setFill(curFill);
		}else {
			/**
			 * at is our animation timer object which runs in the background
			 */
			AnimationTimer at = new AnimationTimer() {
				

				@Override
				public void handle(long arg0) {
			    	int counter = 0;
			    	double curWidth = gc.getLineWidth();
			    	Paint curPaint = gc.getStroke();
			    	Paint curFill = gc.getFill();
			    	gc.setLineWidth(1);
			    	gc.setFill(baseColor);
			    	gc.setStroke(baseColor);

			    	// keep going until out of pixels or we hit 1000 drawn pixels, which then we exit to refresh the frame
			    	while(counter < 1000 && indexPixels < pixels.size()) {
			    		Point2D p = pixels.get(indexPixels);
			    		indexPixels++;
			    		gc.fillRect(p.getX(),p.getY(),GUI.fillWidth,GUI.fillWidth);
			    	
			    		counter++;
			    	}
			    	
			    	// if out of pixels, then kill the animation timer object
			    	if(indexPixels < pixels.size() == false) {
		    			gc.setLineWidth(curWidth);
				    	gc.setStroke(curPaint);
				    	gc.setFill(curFill);
		    			this.stop();
		    		}
			    	
			    	gc.setLineWidth(curWidth);
			    	gc.setStroke(curPaint);
			    	gc.setFill(curFill);
				}
				
				
			};
			at.start();
		}
		
	}

}

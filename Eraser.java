package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Eraser is the eraser object for removing pixels from the screen.
 * Erasers are just a regular pen stroke but the stroke is always white.
 * The size is adjustable by the user and dpeneds on the line with selector 
 */
public class Eraser implements Drawable {
	
	private double startX, startY, endX, endY;
	
	double size;
	
	public Eraser(double startX, double startY, double endX, double endY, double size) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.size = size;
	}


	@Override
	public void draw(GraphicsContext gc) {
		// secretly its just a line thats white
		Paint stroke = gc.getStroke();
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(size);
		gc.strokeLine(startX, startY, endX, endY);
		gc.setStroke(stroke);
		
	}
}

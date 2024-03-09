package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Line is a line object for the shapes drawing functions. 
 * 
 */
public class Line implements Drawable {
	
	private Color color;
	private double size;
	
	private double startX, startY, endX, endY;

	@Override
	public void draw(GraphicsContext gc) {
		
		// Just the usual save, draw and reload old params
		Paint temp_ = gc.getStroke();
		double size_ = gc.getLineWidth();
		
		gc.setStroke(color);
		gc.setLineWidth(size);
		
		gc.strokeRect(startX, startY, endX, endY);
		
		gc.setStroke(temp_);
		gc.setLineWidth(size_);
		
	}

}

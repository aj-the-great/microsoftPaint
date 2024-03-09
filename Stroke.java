package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * This is a legacy class from when we started working on the project.
 * Its basically the pen class. Only not removed incase of adding inheritance from
 * a stroke object.
 */
public class Stroke implements Drawable{
	
	private Color color;
	private double size;
	
	private double startX, startY;
	private double endX, endY;

	
	public Stroke(Color color, double size, double startX, double startY, double endX, double endY) {
		this.color = color;
		this.size = size;
		this.startX=startX;
		this.startY = startY;
		this.endX = endX;
		this.endY=endY;
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		Paint temp = gc.getStroke();
		double size_ = gc.getLineWidth();
		
		gc.setStroke(color);
		gc.setLineWidth(size);
		
		gc.strokeLine(startX, startY, endX, endY);
		
		gc.setStroke(temp);
		gc.setLineWidth(size_);
	}
}

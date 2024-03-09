package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Rectangle is just a rectangle drawn to the screen. It is not filled in.
 */
public class Rectangle implements Drawable {
	
	private Paint paint;
	private double size;
	
	private double startX, startY, endX, endY;
	
	public Rectangle(double startX, double startY, double endX, double endY, double size, Paint paint) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.size = size;
		this.paint = paint;
	}

	@Override
	public void draw(GraphicsContext gc) {
		
		// regular save, stroke and reload
		Paint temp_ = gc.getStroke();
		double size_ = gc.getLineWidth();
		
		gc.setStroke(paint);
		gc.setLineWidth(size);
		
		if (endX > 0 && endY > 0) {
			gc.strokeRect(startX, startY, endX, endY);
		}
		else if (endX < 0 && endY < 0){
			gc.strokeRect(startX + endX, startY + endY, -endX, -endY);
		}
		else if (endX < 0 && endY > 0){
			gc.strokeRect(startX + endX, startY, -endX, endY);
		}
		else if (endX > 0 && endY < 0){
			gc.strokeRect(startX, startY + endY, endX, -endY);
		}
		
		gc.setStroke(temp_);
		gc.setLineWidth(size_);
		
	}
}

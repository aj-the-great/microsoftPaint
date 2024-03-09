package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Circle represents a circle object along with relevant information for drawing it.
 */
public class Circle implements Drawable {

	private Paint paint;
	private double size;
	
	private double startX, startY, endX, endY;
	
	public Circle(double startX, double startY, double endX, double endY, double size, Paint paint) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.size = size;
		this.paint = paint;
	}

	@Override
	public void draw(GraphicsContext gc) {
		Paint temp_ = gc.getStroke();
		double size_ = gc.getLineWidth();
		
		gc.setStroke(paint);
		gc.setLineWidth(size);

		//draw the oval
		if (endX > 0 && endY > 0) {
			gc.strokeOval(startX, startY, endX, endY);
		}
		else if (endX < 0 && endY < 0){
			gc.strokeOval(startX + endX, startY + endY, -endX, -endY);
		}
		else if (endX < 0 && endY > 0){
			gc.strokeOval(startX + endX, startY, -endX, endY);
		}
		else if (endX > 0 && endY < 0){
			gc.strokeOval(startX, startY + endY, endX, -endY);
		}
		
		gc.setStroke(temp_);
		gc.setLineWidth(size_);
		
	}

}

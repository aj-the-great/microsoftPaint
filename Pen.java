package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

/**
 * Pen is a basic drawing tool for the user. It is just a single line being draw from a point to another point
 */
public class Pen implements Drawable {
	
	double startX, startY, uiX, uiY;
	double penSize;
	Paint paint;
	
	public Pen(double startX, double startY, double uiX, double uiY, double penSize, Paint paint) {
		this.startX = startX;
		this.startY = startY;
		this.uiX = uiX;
		this.uiY = uiY;
		this.penSize = penSize;
		this.paint = paint;
	}

	@Override
	public void draw(GraphicsContext gc) {
		
		// regular save, stroke, reload params
		double size = gc.getLineWidth();
		Paint p = gc.getStroke();
		gc.setLineWidth(penSize);
		gc.setStroke(paint);
		gc.strokeLine(startX, startY, uiX, uiY);
		
		gc.setStroke(p);
		gc.setLineWidth(size);
		
	}
}

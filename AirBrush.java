package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Random;

/**
 * AirBrush holds all information and methods regarding to drawing an airbrush stroke.
 * Airbrush strokes have several small dots about a certain radius drawn to the screen.
 */
public class AirBrush implements Drawable {
	
	double startX, startY;
	double size;
	
	double[] xPos;
	double[] yPos;
	
	Paint fill;
	
	int density;
	
	public AirBrush(double startX, double startY, double size, int density, Paint fill) {
		this.startX= startX;
		this.startY = startY;
		this.size = size;
		this.density = density;
		this.fill = fill;
		
		// Have two arrays for the x and y position of the random spots
		xPos = new double[density];
		yPos = new double[density];
		
		Random rand = new Random();
		
		// fill with our random points
		for (int i = 0; i < density; i++) {
            double offsetX = rand.nextDouble() * size * 2;
            double offsetY = rand.nextDouble() * size * 2;
            xPos[i] = offsetX;
            yPos[i] = offsetY;
            
        }
	}
	
	
	
	/**
	 * brush draws all airbrush particles to the screen from the buffer
	 * @param gc The graphics context to draw to
	 */
	public void brush(GraphicsContext gc) {
		
		// save old values
        double alpha = gc.getGlobalAlpha();
        Paint curFill = gc.getFill();
        gc.setGlobalAlpha(.05);
        gc.setFill(fill);
        
        // now draw each oval to the screen from our saved buffer
        for (int i = 0; i < density; i++) {
            double offsetX = xPos[i];
            double offsetY = yPos[i];
            gc.fillOval(startX + offsetX-size, startY + offsetY-size, (size+3) / 4.0, (size+3) / 4.0);
            
        }
        
        // reset values before leaving
        gc.setGlobalAlpha(alpha);
        gc.setFill(curFill);
    }

	@Override
	public void draw(GraphicsContext gc) {
		brush(gc);
		
	}
}

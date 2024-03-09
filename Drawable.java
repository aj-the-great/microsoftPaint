package model;

import javafx.scene.canvas.GraphicsContext;

/**
 * Drawable is our interface to allow all objects to be drawn for our undo redo system.
 * Every implementing class of Drawable can be part of the undo redo system. The main requirements
 * are that each draw method being overridden must adjust its own color and line width since those
 * will not preserve from when the object was origionally drawn.
 */
public interface Drawable {
	
	/**
	 * draw is the basic function all objects must have. When called, it draws the object to the screen
	 * based on its own data in the object
	 * @param gc The graphics context to draw to
	 */
	public void draw(GraphicsContext gc);

}

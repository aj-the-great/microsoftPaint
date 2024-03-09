package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Selection is the class for the selection tool. Unfortunetally it was not implemented in time
 * but this class will stay incase it gets implemented later
 */
public class Selection implements Drawable {
    private double x;
    private double y;
    private WritableImage image;
    private Paint strokeColor;

    public Selection(double x, double y, WritableImage image, Paint paint) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.strokeColor = paint;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x, y);
    }
    
}

package view_controller;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import model.AirBrush;
import model.Bucket;
import model.Circle;
import model.Eraser;
import model.Pen;
import model.Stroke;
import model.Rectangle;
import model.Selection;
import model.Drawable;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

/**
 * GUI is the base starting class for the paint project.
 * It handles all keyboard input and drawing related events.
 */
public class GUI extends Application {
	
	public static final Color backgroundColor = Color.WHITE;
	
	public static double width = 575;
	public static double height = 500;
	double headerSpace = 200;
	Canvas cv = new Canvas(width, height);
	volatile GraphicsContext gc = cv.getGraphicsContext2D();
	double currBrushSize = 1.0;
	String currBrushType = "Pen";
	
	LinkedList<Drawable> actions = new LinkedList<>();
	LinkedList<Drawable> redo = new LinkedList<>();
	
	GridPane controlPanel = new GridPane();
	
    private double rectX, rectY, rectWidth, rectHeight;
    private boolean isSelecting = false;

	public static int fillWidth = 1;
	
	TextField fontSize;
	
	public Stage stage;
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Initializes and sets up the JavaFX application window.
	 * 
	 * @param stage The primary stage for the JavaFX application.
	 */
	@Override
	public void start(Stage stage) {
		this.stage = stage;
		GridPane panel = new GridPane();
		gc.setLineWidth(1);
		
		//panel.add(new Label("Header goes here"), 0, 0);
		panel.add(cv, 0, 2);
		
		// Initialize button images and set dimensions
		Image penImage = new Image("/documents/pen.jpeg");
		ImageView penView = new ImageView(penImage);
		penView.setFitWidth(20);
		penView.setFitHeight(20);
		
		Image eraserImage = new Image("/documents/eraser.jpeg");
		ImageView eraserView = new ImageView(eraserImage);
		eraserView.setFitWidth(20);
		eraserView.setFitHeight(20);
		
		Image airBrushImage = new Image("/documents/airbrush.jpg");
		ImageView airBrushView = new ImageView(airBrushImage);
		airBrushView.setFitWidth(20);
		airBrushView.setFitHeight(20);
		
		Image lineImage = new Image("/documents/line.png");
		ImageView lineView = new ImageView(lineImage);
		lineView.setFitWidth(20);
		lineView.setFitHeight(20);
		
		Image rectImage = new Image("/documents/rectangle.jpeg");
		ImageView rectView = new ImageView(rectImage);
		rectView.setFitWidth(20);
		rectView.setFitHeight(20);
		
		Image cirImage = new Image("/documents/circle.png");
		ImageView cirView = new ImageView(cirImage);
		cirView.setFitWidth(20);
		cirView.setFitHeight(20);
		
		Image clearImage = new Image("/documents/clear.jpg");
		ImageView clearView = new ImageView(clearImage);
		clearView.setFitWidth(20);
		clearView.setFitHeight(20);
		
		Image saveImage = new Image("/documents/save.jpeg");
		ImageView saveView = new ImageView(saveImage);
		saveView.setFitWidth(20);
		saveView.setFitHeight(20);
		
		Image openImage = new Image("/documents/openFile.png");
		ImageView openView = new ImageView(openImage);
		openView.setFitWidth(20);
		openView.setFitHeight(20);
		
		Image selectImage = new Image("/documents/select.png");
		ImageView selectView = new ImageView(selectImage);
		selectView.setFitWidth(20);
		selectView.setFitHeight(20);
		
		Image fillImage = new Image("/documents/fill.jpeg");
		ImageView fillView = new ImageView(fillImage);
		fillView.setFitWidth(20);
		fillView.setFitHeight(20);
		
		// Button to clear canvas
		Button clrButton = new Button("Clear");
		clrButton.setOnMouseClicked(e -> {
			clearCanvas(gc);
		});
		
		// Button to toggle rectangle selection tool
		Button selectButton = new Button("Select tool");
        //selectButton.setOnAction(e -> toggleSelectionTool());

		// Buttons for changing the stroke size and brush type
		Button btnSmall = new Button("Small");
		Button btnMedium = new Button("Medium");
		Button btnLarge = new Button("Large");
		Button btnPen = new Button("Pen");
		Button btnEraser = new Button("Eraser");
		Button btnAir = new Button("Air Brush");
		Button btnLine = new Button("Line");
		Button btnRect = new Button("Rectangle");
		Button btnCirc = new Button("Circle");
		Button clrSelBtn = new Button("Clear Selection");
		Button saveImgButton = new Button();
		Button openImgButton = new Button();
		Button bucket = new Button();
		
		// Set button graphics
		btnPen.setGraphic(penView);
		btnEraser.setGraphic(eraserView);
		btnAir.setGraphic(airBrushView);
		btnLine.setGraphic(lineView);
		btnRect.setGraphic(rectView);
		btnCirc.setGraphic(cirView);
		clrButton.setGraphic(clearView);
		saveImgButton.setGraphic(saveView);
		openImgButton.setGraphic(openView);
		selectButton.setGraphic(selectView);
		bucket.setGraphic(fillView);
		
		ColorPicker colorPicker = new ColorPicker(Color.BLACK);
		
		// Set button actions
		btnSmall.setOnAction(e -> setBrush(currBrushType, 1));
		btnMedium.setOnAction(e -> setBrush(currBrushType, 5));
		btnLarge.setOnAction(e -> setBrush(currBrushType, 10));
		btnPen.setOnAction(e -> setBrush("Pen", currBrushSize));
		btnEraser.setOnAction(e -> setBrush("Eraser", currBrushSize));
		btnAir.setOnAction(e -> setBrush("AirBrush", currBrushSize));
		btnLine.setOnAction(e -> setBrush("Line", currBrushSize));
		btnRect.setOnAction(e -> setBrush("Rectangle", currBrushSize));
		btnCirc.setOnAction(e -> setBrush("Circle", currBrushSize));
		bucket.setOnAction(e -> setBrush("fill", currBrushSize));
		
		saveImgButton.setOnAction(e-> saveImageDialog(stage));
		openImgButton.setOnAction(e -> openImageDialog(stage));
		
		selectButton.setOnAction(e -> {
			setBrush("Select", 1);
			drawing = false;
			isSelecting = !isSelecting;
		});
		
		clrSelBtn.setOnAction(e -> clearSelection());

		// set the color picking function so we get the main color updated
		colorPicker.setOnAction((a) -> {
			gc.setStroke(colorPicker.getValue());
		});

		Label fontSizeLabel = new Label("Brush Size:");
		fontSize = new TextField("1");
		fontSize.setOnAction(
				(EventHandler) -> {
					try {
						String s = fontSize.getText();
						double size = Double.valueOf(s);
						if(size > 0) {
							gc.setLineWidth(size);
							currBrushSize = size;
						}
					} catch (Exception e) {
						
					}
					panel.requestFocus();
				}
		);
		
		// Add nodes to GridPane
		controlPanel.setHgap(4);
		controlPanel.setVgap(2);
		
		controlPanel.add(btnSmall, 0, 0);
		controlPanel.add(btnMedium, 1, 0);
		controlPanel.add(btnLarge, 2, 0);
		controlPanel.add(fontSizeLabel, 3, 0);
		controlPanel.add(fontSize, 4, 0);
		
		controlPanel.add(btnPen, 0, 1);
		controlPanel.add(btnEraser, 1, 1);
		controlPanel.add(btnAir, 2, 1);
		controlPanel.add(clrButton, 3, 1);
		
		controlPanel.add(btnLine, 0, 2);
		controlPanel.add(btnRect, 1, 2);
		controlPanel.add(btnCirc, 2, 2);
		controlPanel.add(clrSelBtn, 3, 2);
		
		controlPanel.add(colorPicker, 4, 1);
		controlPanel.add(selectButton, 4, 2);
		controlPanel.add(saveImgButton, 5, 1);
		controlPanel.add(openImgButton, 5, 2);
		controlPanel.add(bucket, 5, 0);
		
		
		panel.add(controlPanel, 0, 1);

		// add large displacement to show canvas location
		Scene s = new Scene(panel, width, height);

		cv.setOnMouseDragged(new MouseDragHandler());
		
		// allow for the bucket function to work if its selected
		cv.setOnMouseClicked((arg0) -> {
			// if type is fill then call fill 
			if(currBrushType.equals("fill")) {
				fill();
			}
		});
		
		panel.setOnKeyReleased(new Keyboard());
		
		panel.setOnKeyPressed((arg0) -> {
			if(arg0.getCode() == KeyCode.U) {
				// undo so remove top action
				System.out.println("Size: " + actions.size());
				if(actions.size() != 0) {
					System.out.println("Removing and redrawing");
					Drawable s_ = actions.removeLast();
					redo.addLast(s_);
					
					Paint p = gc.getStroke();
					double size = gc.getLineWidth();
					gc.setStroke(Color.BLACK);
					gc.setFill(Color.WHITE);
					gc.fillRect(0, 0, width, height);
					Bucket.drawFast = true;
					for(Drawable stroke : actions) {
						stroke.draw(gc);
					}
					
					gc.setStroke(p);
					gc.setLineWidth(size);
					Bucket.drawFast = false;
				}
			}
			
			if(arg0.getCode() == KeyCode.R) {
				Bucket.drawFast = true;
				if(redo.isEmpty()==false) {
					Drawable s_ = redo.removeLast();
					actions.addLast(s_);
					s_.draw(gc);
				}
				Bucket.drawFast = false;
				
			}
		});
		
		cv.setOnMouseMoved(new MouseMoveHandler());
		cv.setOnMousePressed(new MousePressHandler());
		cv.setOnMouseReleased(new MouseReleaseHandler());
		
		//gc.setStroke(backgroundColor);
		//gc.setStroke(Color.WHITE);
		gc.setFill(Color.WHITE);
		
		// extra 100 for header
		gc.fillRect(0, 0, width, height);
		
		stage.setScene(s);
		stage.show();
		
//		stage.setResizable(false);
		
		s.widthProperty().addListener( (obs, oldVal, newVal) -> {
			System.out.println("Resizing");
			width = newVal.doubleValue();
			cv.setWidth(width);
			
			// fill in the spot we just opened up
			Paint p = gc.getStroke();
			gc.setStroke(backgroundColor);
			gc.fillRect(oldVal.doubleValue(), 0, newVal.doubleValue(), height);
			gc.setStroke(p);
		});
		
		s.heightProperty().addListener( (obs, oldVal, newVal) -> {
			System.out.println("Resizing");
			height = newVal.doubleValue();
			cv.setHeight(height);
			
			// fill in the spot we just opened up
			Paint p = gc.getStroke();
			gc.setStroke(backgroundColor);
			gc.fillRect(0,oldVal.doubleValue(), newVal.doubleValue(), width);
			gc.setStroke(p);
		});
//		System.out.println(controlPanel.getHeight());
		
	}
	
	/**
	 * Sets the current brush type and size for application.
	 * 
	 * @param brushType The type of brush to be set (e.g., "Pen", "Eraser", "AirBrush", etc.).
	 * @param size      The size of the brush to be set.
	 */
	private void setBrush(String brushType, double size) {
		currBrushType = brushType;
		currBrushSize = size;
		fontSize.setText(String.valueOf(size));
	}
	
	/**
	 * Clears the content of the provided GraphicsContext, effectively clearing the canvas.
	 * 
	 * @param gc The GraphicsContext associated with the canvas to be cleared.
	 */
	private void clearCanvas(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
  		gc.setFill(Color.WHITE);
  		gc.fillRect(0, 0, width, height);
    }
	
	/**
	 * Clears the selected area on the canvas by filling it with a white color.
	 * The selected area is determined by the rectangle defined by rectX, rectY, rectWidth, and rectHeight.
	 */
	private void clearSelection() {
	    gc.setFill(Color.WHITE);
	    gc.fillRect(rectX-1, rectY-1, rectWidth+2, rectHeight+2);
	}
	
	/**
	 * Draws a selection rectangle on the canvas with the specified coordinates and dimensions.
	 * The appearance of the rectangle is defined by using a blue stroke color.
	 */
	private void drawSelectionRectangle() {
        gc.setStroke(Color.BLUE);
        gc.strokeRect(rectX, rectY, rectWidth, rectHeight);
    }
	
//	private void undoSelection() {
//        gc.setStroke(Color.WHITE);
//        gc.strokeRect(rectX, rectY, rectWidth, rectHeight);
//    }
	
	private boolean drawing = false;
	private double lastX = width/2;
	private double lastY = height/2;
	/**
	 * Handles mouse drag events for the drawing application, updating the canvas based on the user's actions.
	 */
	class MouseDragHandler implements EventHandler<MouseEvent> {
		 /**
	     * Handles the mouse drag event.
	     *
	     * @param arg0 The MouseEvent representing the mouse drag argument.
	     */
		@Override
		public void handle(MouseEvent arg0) {
			if (isSelecting) {
				rectWidth = arg0.getX() - startX;
	            rectHeight = arg0.getY() - startY;
			}
			
			// Update mouse coordinates
			UserInput.mouseX = arg0.getX();
			UserInput.mouseY = arg0.getY();
			
			// Determine the starting coordinates based on drawing state
			double startX = 0;
			double startY = 0;
			if(drawing == false) {
				startX = UserInput.mouseX;
				startY = UserInput.mouseY;
				
			} else {
				startX = lastX;
				startY = lastY;
			}
			
			// Draw the appropriate drawable based on the current brush type
			Drawable s = null;
			
			if (currBrushType.equals("Pen")) {
				s = new Pen(startX, startY, UserInput.mouseX, UserInput.mouseY, currBrushSize, gc.getStroke());
				s.draw(gc);
			}
			else if (currBrushType.equals("Eraser")) {
				s = new Eraser(startX, startY, UserInput.mouseX, UserInput.mouseY, currBrushSize);
				s.draw(gc);
			}
			else if (currBrushType.equals("AirBrush")) {
				s = new AirBrush(UserInput.mouseX, UserInput.mouseY, currBrushSize, 10, gc.getStroke());
				s.draw(gc);
			}
			// Add the drawn object to the actions list
			if(s != null) {
				actions.addLast(s);
			}
			// Update the last mouse coordinates and set drawing state to true
			lastX = UserInput.mouseX;
			lastY = UserInput.mouseY;
			drawing = true;
			
		}
	}
	/**
	 * Handles mouse move events for the drawing application, updating the mouse coordinates.
	 */
	class MouseMoveHandler implements EventHandler<MouseEvent> {
		/**
	     * Handles the mouse move event, updating the mouse coordinates in the UserInput class.
	     *
	     * @param arg0 The MouseEvent representing the mouse move argument.
	     */
		@Override
		public void handle(MouseEvent arg0) {
			// Update mouse coordinates in the UserInput class
			UserInput.mouseX = arg0.getX();
			UserInput.mouseY = arg0.getY();
			
			System.out.println("X: " + UserInput.mouseX + " Y: " + UserInput.mouseY);
			// TODO Auto-generated method stub
		}
	}
	
	private double startX = width/2;
	private double startY = height/2;
	/**
	 * Handles mouse press events for the drawing application, updating mouse coordinates and initializing selection parameters if applicable.
	 */
	class MousePressHandler implements EventHandler<MouseEvent> {
		/**
	     * Handles the mouse press event, updating the mouse coordinates in the UserInput class and initializing
	     * selection parameters if the application is in selection mode.
	     *
	     * @param arg0 The MouseEvent representing the mouse press event.
	     */
		@Override
		public void handle(MouseEvent arg0) {
			// Update mouse coordinates in the UserInput class
			UserInput.mouseX = arg0.getX();
			UserInput.mouseY = arg0.getY();
			
			// Set the starting coordinates for drawing
			startX = UserInput.mouseX;
			startY = UserInput.mouseY;
			
			// Initialize selection params if selecting
			if (isSelecting) {
				startX = arg0.getX();
		        startY = arg0.getY();
		        rectX = startX;
	            rectY = startY;
	            rectWidth = 0;
	            rectHeight = 0;
			}
		}
	}
	
	/**
	 * Handles mouse release events for the drawing application, performing actions based on the current brush type.
	 */
	class MouseReleaseHandler implements EventHandler<MouseEvent> {
		/**
	     * Handles the mouse release event, performing actions based on the current brush type and updating the canvas.
	     *
	     * @param arg0 The MouseEvent representing the mouse release argument.
	     */
		@Override
		public void handle(MouseEvent arg0) {
			Drawable s = null;
			
			// Check the current brush type and perform corresponding actions
			if (currBrushType.equals("Line")) {
				s = new Pen(startX, startY, UserInput.mouseX, UserInput.mouseY, currBrushSize,gc.getStroke());
				s.draw(gc);
				actions.addLast(s);
			}
			else if (currBrushType.equals("Rectangle")) {
				//gc.strokeRect(startX, startY, 10, 8);
				s = new Rectangle(startX, startY, UserInput.mouseX - startX, UserInput.mouseY - startY, gc.getLineWidth(), gc.getStroke());
				s.draw(gc);
				actions.addLast(s);
//				gc.strokeRect(startX, startY, UserInput.mouseX - startX, UserInput.mouseY - startY);
			}
			else if (currBrushType.equals("Select")) {
				drawSelectionRectangle();
				isSelecting = false;
				gc.setStroke(Color.BLACK);
				setBrush("Pen", 1);
			}
			else if (currBrushType.equals("Circle")) {
				s = new Circle(startX, startY, UserInput.mouseX - startX, UserInput.mouseY - startY, gc.getLineWidth(), gc.getStroke());
				s.draw(gc);
				actions.addLast(s);
			}
			
			// Reset drawing state
			drawing = false;
		}
	}
	
	/**
	 * Displays a file chooser dialog for opening an image file and calls the 'openImage' method with the selected file.
	 *
	 * @param stage The JavaFX Stage to which the file chooser dialog is associated.
	 */
	private void openImageDialog(Stage stage) {
		// Get the current directory
		File curDir = new File(System.getProperty("user.dir"));
		
		// Create a file chooser with the initial directory set to the current directory
		FileChooser openImg = new FileChooser();
		openImg.setInitialDirectory(curDir);
		
		// Show the file chooser dialog and get the file
		File file = openImg.showOpenDialog(stage);
		System.out.println(file);
		openImage(file);
	}
	
	/**
	 * Displays a file chooser dialog for saving the current canvas image and calls the 'saveImage' method with the selected file.
	 *
	 * @param stage The JavaFX Stage to which the file chooser dialog is associated.
	 */
	private void saveImageDialog(Stage stage) {
		// Get the current directory
		File curDir = new File(System.getProperty("user.dir"));
		
		// Create a file chooser with the initial directory set to the current directory
		FileChooser saveImg = new FileChooser();
		saveImg.setInitialDirectory(curDir);
		
		// SHow the file chooser dialog and get the selected file for saving
		File file = saveImg.showSaveDialog(stage);
		System.out.println(file);
		saveImage(file);
	}
	
	/**
	 * Saves the current canvas image to the specified file in PNG format.
	 *
	 * @param file The File object representing the location where the image will be saved.
	 *             If the file is null or the filename has no extension, the method returns without saving.
	 */
	private void saveImage(File file) {
		// Check if the file is null
		if(file == null) {
			return;
		}
		System.out.println("Saving now");
		System.out.println(file.getPath());
		int split = file.getPath().lastIndexOf(".");
		// in case of no extention
		if(split == -1) {
			System.out.println("Did not save, invalid file name");
			return;
		}
		// we only save png files right now
		String ext = file.getPath().substring(split+1);
		WritableImage wi = new WritableImage((int)width,(int)height);
		cv.snapshot(null, wi);
		BufferedImage ri = SwingFXUtils.fromFXImage(wi, null);
		try {
			ImageIO.write(ri, "png", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens and loads an image from the specified file onto the canvas.
	 *
	 * @param file The File object representing the location of the image to be opened.
	 *             If the file is null, the method returns without loading.
	 */
	private void openImage(File file) {
		// Check if the file is null
		if(file == null) {
			return;
		}
		System.out.println("Loading now");
		try {
			BufferedImage img = ImageIO.read(file);
			Image img_ = SwingFXUtils.toFXImage(img,null);
			gc.drawImage(img_, 0,0);
			System.out.println("Drawn");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * fill handles everything regarding the bucket function.
	 * There it creates the bucket object and adds it to the undo redo system
	 */
	private void fill() {
		WritableImage wi = new WritableImage((int)width,(int)height);
		cv.snapshot(null, wi);
		PixelReader pr = wi.getPixelReader();
		
		int startX = (int)UserInput.mouseX;
		int startY = (int)UserInput.mouseY;
		
		Drawable bc = new Bucket(startX, startY, pr, gc.getStroke());
		bc.draw(gc);
		actions.addLast(bc);
	}
	
	/**
	 * Handles keyboard events for the drawing application, allowing users to perform various actions using keyboard shortcuts.
	 */
	class Keyboard implements EventHandler<KeyEvent> {
		/**
	     * Handles the keyboard event, performing actions based on the pressed key.
	     *
	     * @param arg0 The KeyEvent representing the keyboard argument.
	     */
		@Override
		public void handle(KeyEvent arg0) {
			
			System.out.println("KeyCode: " + arg0.getCode());
			
			
			// Key F means fill
			if(arg0.getCode() == KeyCode.F) {
				fill();	
			}
			
			if(arg0.getCode() == KeyCode.S) {
				saveImageDialog(stage);
			}
			if(arg0.getCode() == KeyCode.O) {
				openImageDialog(stage);
			}
			
			
			
		}
		
	}
}
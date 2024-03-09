package model;
import java.util.Stack;

/**
 * UndoRedo is a legacy class for implementing an undo redo system for the GUI.
 * There are two stacks, one for undo and one for redo which keeps track of the user's inputs.
 * @param <J> The type of the object to undo/redo. This should be the drawable interface
 */
public class UndoRedo<J> {
	    private Stack<J> undoStack;
	    private Stack<J> redoStack;

	    public UndoRedo() {
	        undoStack = new Stack<>();
	        redoStack = new Stack<>();
	    }

	   /**
	    * addToUndoStack pushes an object to the stack
	    * @param object The object to add to the stack
	    */
	    public void addToUndoStack(J object) {
	        undoStack.push(object);
	        redoStack.clear(); // Clear the redo stack when a new action is performed
	    }

	    /**
	     * undo moves an object off the undo stack and onto the redo stack
	     * @return The object that was taken off
	     */
	    public J undo() {
	        if (!undoStack.isEmpty()) {
	            J object = undoStack.pop();
	            redoStack.push(object);
	            return object;
	        }
	        return null; // Nothing to undo
	    }

	    /**
	     * redo undos the undo and puts the object back on the undo stack
	     * @return The object undone
	     */
	    public J redo() {
	        if (!redoStack.isEmpty()) {
	            J object = redoStack.pop();
	            undoStack.push(object);
	            return object;
	        }
	        return null; // Nothing to redo
	    }
	}



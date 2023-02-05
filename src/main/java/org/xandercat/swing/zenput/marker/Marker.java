package org.xandercat.swing.zenput.marker;

import java.awt.Color;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Interface for UI markers to implement.  A marker decorates the UI components for
 * an input whenever marked invalid, and undoes the same decoration for an input
 * whenever marked valid.
 * 
 * @author Scott Arnold
 */
public interface Marker<T> {
	
	public static final Color DEFAULT_INVALID_COLOR = new Color(255, 200, 200);
	public static final Color DEFAULT_INVALID_TEXT_COLOR = new Color(225, 0, 0);
	
	/**
	 * Mark the field as valid.
	 */
	public void markValid();
	
	/**
	 * Mark the field as invalid.
	 * 
	 * @param error		validation error 
	 */
	public void markInvalid(ValidationException error);

	/**
	 * Returns whether or not the mark target is currently marked valid.
	 * 
	 * @return			whether or not the mark target is currently marked valid
	 */
	public boolean isMarkedValid();
}

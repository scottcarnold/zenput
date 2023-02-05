package org.xandercat.swing.zenput.marker;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * AbstractMarker is a self-replicating marker that combines the jobs of a Marker 
 * and MarkerBuilder into a single class.  The primary reason for this is to reduce 
 * the amount of code and number of classes required for new marker types.  This 
 * class also takes care of holding the mark target object and maintaining the state 
 * of whether or not the mark target is marked valid or invalid.
 * 
 * When constructing an instance to serve as a MarkerBuilder, the default constructor
 * should be used.  All other instances should be created using the constructor that
 * takes a mark target as an argument.
 * 
 * @author Scott Arnold
 *
 * @param <T>	mark target type
 */
public abstract class AbstractMarker<T> implements Marker<T>, MarkerBuilder<T> {

	private T markTarget;
	private boolean valid = true;
	
	public AbstractMarker() {
	}
	
	public AbstractMarker(T markTarget) {
		this.markTarget = markTarget;
	}
	
	protected abstract void markValid(T markTarget);
	
	protected abstract void markInvalid(T markTarget, ValidationException error);

	@Override
	public boolean isMarkedValid() {
		return valid;
	}

	@Override
	public void markInvalid(ValidationException error) {
		markInvalid(markTarget, error);
		this.valid = false;
	}

	@Override
	public void markValid() {
		markValid(markTarget);
		this.valid = true;
	}
}

package org.xandercat.swing.zenput.marker;

import java.util.Properties;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * AbstractMarker takes care of holding the mark target object and maintaining the state 
 * of whether or not the mark target is marked valid or invalid.
 * 
 * @author Scott Arnold
 *
 * @param <T>	mark target type
 */
public abstract class AbstractMarker<T> implements Marker<T> {

	private T markTarget;
	private boolean valid = true;
	
	public AbstractMarker(T markTarget) {
		this.markTarget = markTarget;
	}
	
	protected abstract void markValid(T markTarget);
	
	protected abstract void markInvalid(T markTarget, ValidationException error, Properties messageProperties);

	@Override
	public boolean isMarkedValid() {
		return valid;
	}

	@Override
	public void markInvalid(ValidationException error, Properties messageProperties) {
		markInvalid(markTarget, error, messageProperties);
		this.valid = false;
	}

	@Override
	public void markValid() {
		markValid(markTarget);
		this.valid = true;
	}
}

package org.xandercat.swing.zenput.marker;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * CompoundMarker applies the affects of two or more individual Markers
 * to a single mark target.
 * 
 * @author Scott Arnold
 *
 * @param <T>
 */
public class CompoundMarker<T> extends AbstractMarker<T> {
	
	private AbstractMarker<? super T>[] markers;

	public CompoundMarker(AbstractMarker<? super T>... markers) {
		super();
		this.markers = markers;
	}
	
	public CompoundMarker(T markTarget, AbstractMarker<? super T>... markers) {
		super(markTarget);
		this.markers = markers;
	}

	@Override
	protected void markInvalid(T markTarget, ValidationException error) {
		for (AbstractMarker<? super T> marker : markers) {
			marker.markInvalid(markTarget, error);
		}
	}

	@Override
	protected void markValid(T markTarget) {
		for (AbstractMarker<? super T> marker : markers) {
			marker.markValid(markTarget);
		}
	}

	@Override
	public Marker<T> newMarker(T markTarget) {
		AbstractMarker<? super T>[] newMarkers = new AbstractMarker[markers.length];
		for (int i=0; i<markers.length; i++) {
			newMarkers[i] = (AbstractMarker<? super T>) markers[i].newMarker(markTarget);
		}
		return new CompoundMarker<T>(markTarget, newMarkers);
	}
}

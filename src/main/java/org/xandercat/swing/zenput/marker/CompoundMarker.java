package org.xandercat.swing.zenput.marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * CompoundMarker applies the affects of two or more individual Markers.
 * This can be used to mark multiple targets as a group, mark a single 
 * target with multiple effects, or some combination of the two.
 * 
 * @author Scott Arnold
 *
 * @param <T> the input component type to be marked
 */
public class CompoundMarker<T> implements Marker<T> {
	
	private List<Marker<? super T>> markers = new ArrayList<>();
	private boolean valid = true;
	
	public CompoundMarker(Marker<? super T>... markers) {
		super();
		this.markers.addAll(Arrays.asList(markers));
	}

	public CompoundMarker(List<Marker<? super T>> markers) {
		super();
		this.markers = markers;
	}
	
	@Override
	public void markValid() {
		for (Marker<? super T> marker : markers) {
			marker.markValid();
		}
		valid = true;
	}

	@Override
	public void markInvalid(ValidationException error, Properties messageProperties) {
		for (Marker<? super T> marker : markers) {
			marker.markInvalid(error, messageProperties);
		}
		valid = false;
		
	}

	@Override
	public boolean isMarkedValid() {
		return valid;
	}
}

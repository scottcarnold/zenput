package org.xandercat.swing.zenput.marker;

import java.util.ArrayList;
import java.util.List;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * MarkTargetProviderMarker acts as a marker for all mark targets provided by
 * an input that acts as a MarkTargetProvider.
 * 
 * This class is used internally by the Zenput framework and should not be needed
 * by Zenput clients.
 * 
 * @author Scott Arnold
 */
public class MarkTargetProviderMarker implements Marker<MarkTargetProvider> {

	private List<Marker<?>> markers = new ArrayList<Marker<?>>();
	
	public MarkTargetProviderMarker(MarkTargetProvider provider, MarkerFactory factory) {
		for (Object target : provider.getMarkTargets()) {
			Marker<?> marker = factory.newMarker(target);
			if (marker != null) {
				markers.add(marker);
			}
		}
	}

	@Override
	public boolean isMarkedValid() {
		for (Marker<?> marker : markers) {
			if (!marker.isMarkedValid()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void markInvalid(ValidationException error) {
		for (Marker<?> marker : markers) {
			marker.markInvalid(error);
		}
	}

	@Override
	public void markValid() {
		for (Marker<?> marker : markers) {
			marker.markValid();
		}
	}
}

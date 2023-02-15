package org.xandercat.swing.zenput.marker;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * MarkerFactory constructs common marker builders for mark targets.
 * 
 * @author Scott Arnold
 */
public class MarkerFactory {

	public static MarkerBuilder<JComponent> backgroundMarkerBuilder() {
		return component -> new BackgroundMarker(component);
	}
	
	public static MarkerBuilder<JComponent> backgroundMarkerBuilder(Color invalidColor) {
		return component -> new BackgroundMarker(component, invalidColor);
	}

	public static MarkerBuilder<JComponent> foregroundMarkerBuilder() {
		return component -> new ForegroundMarker(component);
	}
	
	public static MarkerBuilder<JComponent> foregroundMarkerBuilder(Color invalidColor) {
		return component -> new ForegroundMarker(component, invalidColor);
	}
	
	public static MarkerBuilder<JComponent> toolTipMarkerBuilder() {
		return component -> new ToolTipMarker(component);
	}
	
	public static <T> MarkerBuilder<T> compoundMarkerBuilder(MarkerBuilder<? super T> ... markerBuilders) {
		return new MarkerBuilder<T>() {
			private MarkerBuilder<? super T>[] markerBuildersx = markerBuilders;
			@Override
			public Marker<T> newMarker(T markTarget) {
				List<Marker<? super T>> markers = new ArrayList<Marker<? super T>>();
				for (MarkerBuilder<? super T> markerBuilder : markerBuildersx) {
					markers.add(markerBuilder.newMarker(markTarget));
				}
				return new CompoundMarker<T>(markers);
			}
		};
	}
}

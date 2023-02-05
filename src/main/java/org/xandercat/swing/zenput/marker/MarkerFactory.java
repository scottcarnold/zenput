package org.xandercat.swing.zenput.marker;

import java.util.HashMap;
import java.util.Map;

/**
 * MarkerFactory constructs markers for mark targets based on the marker builders provided
 * to the factory.
 * 
 * @author Scott Arnold
 */
public class MarkerFactory {

	private Map<Class<?>, MarkerBuilder<?>> markerBuilders 
		= new HashMap<Class<?>, MarkerBuilder<?>>();
	
	/**
	 * Sets a marker builder for a particular mark target class type.
	 * 
	 * @param <T>				type of class to mark
	 * @param targetClass		type of class to mark
	 * @param markerBuilder		marker builder for building markers for of the given type
	 */
	public <T> void setMarkerBuilder(Class<T> targetClass, MarkerBuilder<? super T> markerBuilder) {
		markerBuilders.put(targetClass, markerBuilder);
	}
	
	/**
	 * Creates a new marker for the given mark target.  If there is no compatible marker builder
	 * registered with this factory, null is returned.
	 * 
	 * @param <T>				target type
	 * @param target			target to be marked
	 * @return					marker for the mark target
	 */
	public <T> Marker<? super T> newMarker(T target) {
		Class<?> c = target.getClass();
		MarkerBuilder<? super T> markerBuilder = null;
		while (markerBuilder == null && c != null) {
			markerBuilder = (MarkerBuilder<? super T>) markerBuilders.get(c);
			c = c.getSuperclass();
		}
		if (markerBuilder == null) {
			if (target instanceof MarkTargetProvider) {
				return (Marker<? super T>) new MarkTargetProviderMarker((MarkTargetProvider) target, this);
			} else {
				return null;
			}
		} else {
			return markerBuilder.newMarker(target);
		}
	}
}

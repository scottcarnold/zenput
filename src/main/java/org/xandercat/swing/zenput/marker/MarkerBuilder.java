package org.xandercat.swing.zenput.marker;

/**
 * A MarkerBuilder is responsible for building new instances of a particular type of Marker.
 * MarkerBuilders contain all state that should be replicated across multiple markers of 
 * the same type.
 * 
 * @author Scott Arnold
 *
 * @param <T>	marker target type
 */
public interface MarkerBuilder<T> {

	/**
	 * Constructs and returns a new Marker for the given mark target.
	 * 
	 * @param markTarget	mark target
	 * 
	 * @return				marker for mark target
	 */
	public Marker<T> newMarker(T markTarget);
}

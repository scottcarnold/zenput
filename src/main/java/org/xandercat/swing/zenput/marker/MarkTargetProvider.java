package org.xandercat.swing.zenput.marker;

/**
 * Implement this interface for complex components where it is better to mark one or more 
 * internal sub-components instead of the complex component.
 * 
 * @author Scott Arnold
 */
public interface MarkTargetProvider {

	public Object[] getMarkTargets();
}

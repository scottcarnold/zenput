package org.xandercat.swing.zenput.marker;

/**
 * Implement this interface for complex components where it is better to mark one or more 
 * internal sub-components instead of the complex component.
 * 
 * In addition, if using ValidateOnFocusLost, the InputProcessor will attempt to add FocusListeners
 * to the mark targets rather than the complex component. 
 * 
 * @author Scott Arnold
 */
public interface MarkTargetProvider {

	public Object[] getMarkTargets();
}

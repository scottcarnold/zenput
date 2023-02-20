package org.xandercat.swing.zenput.marker;

import java.awt.Color;
import java.util.Properties;

import javax.swing.JComponent;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Marker that changes the background color of a JComponent to mark it invalid.
 * 
 * @author Scott Arnold
 */
public class BackgroundMarker extends AbstractMarker<JComponent> {

	private Color defaultColor;
	private Color invalidColor;
	
	public BackgroundMarker(JComponent component) {
		this(component, Marker.DEFAULT_INVALID_COLOR);
	}
	
	public BackgroundMarker(JComponent component, Color invalidColor) {
		super(component);
		this.defaultColor = component.getBackground();
		this.invalidColor = invalidColor;
	}
	
	@Override
	protected void markInvalid(JComponent inputComponent, ValidationException error, Properties messageProperties) {
		inputComponent.setBackground(invalidColor);
	}

	@Override
	protected void markValid(JComponent inputComponent) {
		inputComponent.setBackground(defaultColor);	
	}
}

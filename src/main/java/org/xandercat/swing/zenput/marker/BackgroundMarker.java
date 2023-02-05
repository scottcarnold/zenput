package org.xandercat.swing.zenput.marker;

import java.awt.Color;

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
	
	public BackgroundMarker() {
		this(Marker.DEFAULT_INVALID_COLOR);
	}
	
	public BackgroundMarker(Color invalidColor) {
		super();
		this.invalidColor = invalidColor;
	}
	
	public BackgroundMarker(JComponent markTarget) {
		this(markTarget, Marker.DEFAULT_INVALID_COLOR);
	}
	
	public BackgroundMarker(JComponent markTarget, Color invalidColor) {
		super(markTarget);
		this.invalidColor = invalidColor;
		this.defaultColor = markTarget.getBackground();
	}

	@Override
	protected void markInvalid(JComponent inputComponent, ValidationException error) {
		inputComponent.setBackground(invalidColor);
	}

	@Override
	protected void markValid(JComponent inputComponent) {
		inputComponent.setBackground(defaultColor);	
	}

	@Override
	public Marker<JComponent> newMarker(JComponent markTarget) {
		return new BackgroundMarker(markTarget, invalidColor);
	}
	
	
}

package org.xandercat.swing.zenput.marker;

import java.awt.Color;

import javax.swing.JComponent;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Marker that changes the foreground color of a JComponent to mark it invalid.
 * 
 * @author Scott Arnold
 */
public class ForegroundMarker extends AbstractMarker<JComponent> {
	
	private Color defaultColor;
	private Color invalidColor;
	
	public ForegroundMarker() {
		this(Marker.DEFAULT_INVALID_TEXT_COLOR);
	}
	
	public ForegroundMarker(Color invalidColor) {
		super();
		this.invalidColor = invalidColor;
	}
	
	public ForegroundMarker(JComponent markTarget) {
		this(markTarget, Marker.DEFAULT_INVALID_TEXT_COLOR);
	}
	
	public ForegroundMarker(JComponent markTarget, Color invalidColor) {
		super(markTarget);
		this.defaultColor = markTarget.getForeground();	
		this.invalidColor = invalidColor;
	}

	@Override
	protected void markInvalid(JComponent inputComponent, ValidationException error) {
		inputComponent.setForeground(invalidColor);
	}

	@Override
	protected void markValid(JComponent inputComponent) {
		inputComponent.setForeground(defaultColor);	
	}

	@Override
	public Marker<JComponent> newMarker(JComponent markTarget) {
		return new ForegroundMarker(markTarget, invalidColor);
	}
}

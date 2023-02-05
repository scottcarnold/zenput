package org.xandercat.swing.zenput.marker;

import javax.swing.JComponent;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Marker that sets the tool tip of a JComponent to the validation error message when a 
 * validation error is present.  This Marker can be used with JComponents that have pre-set
 * fixed tool tips, but should not be used with components where the tool tip value may
 * be modified.
 * 
 * @author Scott Arnold
 */
public class ToolTipMarker extends AbstractMarker<JComponent> {

	private static final String TT_BEGIN = "<html><p><span style='color: white; background-color: red;'><b>&nbsp;X&nbsp;</b></span>&nbsp;&nbsp;";
	private static final String TT_END = "</p></html>";
	
	private String defaultToolTip;
	
	public ToolTipMarker() {
		super();
	}
	
	public ToolTipMarker(JComponent markTarget) {
		super(markTarget);
		this.defaultToolTip = markTarget.getToolTipText();
	}
	
	@Override
	protected void markInvalid(JComponent markTarget, ValidationException error) {
		markTarget.setToolTipText(TT_BEGIN + error.getMessage() + TT_END);		
	}

	@Override
	protected void markValid(JComponent markTarget) {
		markTarget.setToolTipText(defaultToolTip);	
	}

	@Override
	public Marker<JComponent> newMarker(JComponent markTarget) {
		return new ToolTipMarker(markTarget);
	}
}

package org.xandercat.swing.zenput.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.processor.InputProcessor;

/**
 * Helper class for showing validation errors.
 * 
 * @author Scott Arnold
 */
public class ValidationDialogUtil {
	
	@Deprecated
	private static String buildMessage(List<ValidationException> validationExceptions) {
		StringBuilder message = new StringBuilder();
		for (ValidationException ve : validationExceptions) {
			message.append("\n" + ve.getFieldName() + ": " + ve.getMessage());
		}
		return message.toString();
	}
	
	/**
	 * Builds and returns a JTable of the provided validation exceptions.
	 * 
	 * @param inputProcessor			input processor 
	 * @param validationExceptions		validation exceptions
	 * 
	 * @return							table of validation exceptions
	 */
	public static JTable buildValidationExceptionsTable(InputProcessor inputProcessor, List<ValidationException> validationExceptions) {
		DefaultTableModel model = new DefaultTableModel(new Object[] {"Field", "Error Message"}, 0) {
			private static final long serialVersionUID = 2010081601L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}	
		};
		for (ValidationException ve : validationExceptions) {
			String inputName = null;
			if (inputProcessor != null) {
				inputName = inputProcessor.getRegisteredFieldTitle(ve.getFieldName());
			} else {
				inputName = ve.getFieldName();
			}
			model.addRow(new Object[] {inputName, ve.getMessage()});
		}
		return new JTable(model);
	}
	
	/**
	 * Show a basic validation error message dialog.
	 * 
	 * @param parent					parent frame
	 * @param inputProcessor			input processor
	 * @param validationExceptions		validation exceptions
	 * @param message					message to show along with the validation exceptions
	 */
	public static void showMessageDialog(Component parent, InputProcessor inputProcessor, List<ValidationException> validationExceptions, String message) {
		JPanel panel = new JPanel(new BorderLayout());
		JTable table = buildValidationExceptionsTable(inputProcessor, validationExceptions);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setPreferredWidth(300);
		JLabel label = new JLabel(message);
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		panel.add(label, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(450, 150));
		panel.add(scrollPane, BorderLayout.CENTER);
		JOptionPane.showMessageDialog(parent, panel, "Validation Errors", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Show a basic validation error confirmation dialog.
	 * 
	 * @param parent					parent frame
	 * @param validationExceptions		validation exceptions
	 * 
	 * @return		JOptionPane result code of OK_OPTION or CANCEL_OPTION
	 */
	public static int showConfirmDialog(Frame parent, List<ValidationException> validationExceptions) {
		String message = buildMessage(validationExceptions);
		message = message + "\n\nChoose OK to continue or CANCEL to make changes.";
		return JOptionPane.showConfirmDialog(parent, message, "Validation Errors", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
	}
}

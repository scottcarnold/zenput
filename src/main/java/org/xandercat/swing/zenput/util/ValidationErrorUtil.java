package org.xandercat.swing.zenput.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.processor.InputProcessor;
import org.xandercat.swing.zenput.processor.Processor;

/**
 * Helper class for showing validation errors.
 * 
 * @author Scott Arnold
 */
public class ValidationErrorUtil {
	
	private static final Logger log = LogManager.getLogger(ValidationErrorUtil.class);
	
	public static Properties getDefaultMessageProperties() {
		Properties defaultProperties = new Properties();
		try {
			InputStream inputStream = ValidationErrorUtil.class.getResourceAsStream("/zenput.messages");
			if (inputStream == null) {
				log.warn("Unable to find default message properties.");
				return null;
			}
			defaultProperties.load(inputStream);
		} catch (IOException e) {
			log.error("Unable to load default message properties.", e);
			return null;
		}
		return defaultProperties;
	}
	
	public static String getMessage(Properties messageProperties, ValidationException validationException) {
		if (messageProperties == null) {
			log.warn("No message properties provided for formatting error messages.");
			return "Field is invalid.";
		}
		String messageTemplate = (String) messageProperties.get(validationException.getMessageKey());
		if (messageTemplate == null) {
			log.warn("No error message template exists for message key " + validationException.getMessageKey());
			return "Field is invalid.";
		}
		Map<String, Object> rawReplacementValues = validationException.getMessageParameters();
		// TODO: Consider how to add formatting for raw values; for now, just use toString() on them
		Map<String, String> replacementValues = rawReplacementValues.entrySet().stream()
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().toString()));
		return replaceTokens(messageTemplate, replacementValues);
	}
	
	private static String replaceTokens(String messageTemplate, Map<String, String> replacementValues) {
		Pattern pattern = Pattern.compile("\\{(.+?)\\}");
		Matcher matcher = pattern.matcher(messageTemplate);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while (matcher.find()) {
			String replacement = replacementValues.get(matcher.group(1));
			sb.append(messageTemplate.substring(i, matcher.start()));
			if (replacement == null) {
				sb.append(matcher.group(0));
			} else {
				sb.append(replacement);
			}
			i = matcher.end();
		}
		sb.append(messageTemplate.substring(i, messageTemplate.length()));
		return sb.toString();
	}
	
	/**
	 * Builds and returns a JTable of the provided validation exceptions.
	 * 
	 * @param processor					processor 
	 * @param validationExceptions		validation exceptions
	 * 
	 * @return							table of validation exceptions
	 */
	public static JTable buildValidationExceptionsTable(Processor processor, List<ValidationException> validationExceptions) {
		DefaultTableModel model = new DefaultTableModel(new Object[] {"Field", "Error Message"}, 0) {
			private static final long serialVersionUID = 2010081601L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}	
		};
		for (ValidationException ve : validationExceptions) {
			String inputName = null;
			Properties messageProperties = null;
			if (processor != null) {
				inputName = processor.getRegisteredFieldTitle(ve.getFieldName());
				messageProperties = processor.getMessageProperties();
			} else {
				inputName = ve.getFieldName();
			}
			model.addRow(new Object[] {inputName, getMessage(messageProperties, ve)});
		}
		return new JTable(model);
	}
	
	/**
	 * Show a basic validation error message dialog.
	 * 
	 * @param parent                    parent frame
	 * @param inputProcessor            input processor
	 * @param message                   message to show along with the validation exceptions
	 */
	public static void showMessageDialog(Component parent, InputProcessor inputProcessor, String message) {
		showMessageDialog(parent, inputProcessor, inputProcessor.getErrors(), message);
	}
	
	/**
	 * Show a basic validation error message dialog.
	 * 
	 * @param parent					parent frame
	 * @param inputProcessor			input processor
	 * @param validationExceptions		validation exceptions to show
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
}

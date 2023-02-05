package org.xandercat.swing.zenput.processor;

/**
 * Enum identifying what field values should be saved when an input processor
 * has validation errors.
 * <ul>
 * <li>COMMIT_NONE:  Field values are committed as a group when all fields are valid</li>
 * <li>COMMIT_VALID:  Valid fields are committed, invalid fields are not.</li>
 * <li>COMMIT_ALL:  All field values are committed whether that are valid or not.</li>
 * </ul>
 * @author Scott Arnold
 */
public enum CommitMode {
	COMMIT_NONE, COMMIT_VALID, COMMIT_ALL;
}

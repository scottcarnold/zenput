package org.xandercat.swing.zenput.condition;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NumericConditionTest {

	@Test
	public void testCompareGTEQ() {
		NumericCondition condition = new NumericCondition("field", Operator.GTEQ);
		assertTrue(condition.requiresFieldValue());
		assertTrue(condition.isMet(10, 9));
		assertTrue(condition.isMet(10,  10));
		assertFalse(condition.isMet(10,  11));
	}
	
	@Test
	public void testCompareLT() {
		NumericCondition condition = new NumericCondition("field", Operator.LT);
		assertTrue(condition.requiresFieldValue());
		assertFalse(condition.isMet(10.5f, 10.1f));
		assertTrue(condition.isMet(10.5f,  10.8f));		
	}
	
	@Test
	public void testCompareToFixedValue() {
		NumericCondition condition = new NumericCondition("field", Operator.GT, 10);
		assertFalse(condition.requiresFieldValue());
		assertTrue(condition.isMet(null, 11));
		assertFalse(condition.isMet(null, 10));
	}
	
	@Test
	public void testInvalidOperator() {
		assertThrows(IllegalArgumentException.class, () -> {
			NumericCondition condition = new NumericCondition("field", null);
		});
	}
}

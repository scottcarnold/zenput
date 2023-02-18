package org.xandercat.swing.zenput.validator;

public class NumericCondition implements DependentCondition<Number, Number> {

	public static enum Operator { LT, LTEQ, EQ, GT, GTEQ }

	private Operator operator;
	
	@Override
	public boolean isMet(Number fieldValue, Number conditionalValue) {
		// TODO Auto-generated method stub
		return false;
	}
	

}

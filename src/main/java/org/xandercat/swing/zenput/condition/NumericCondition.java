package org.xandercat.swing.zenput.condition;

public class NumericCondition implements DependentCondition<Number, Number> {

	private Operator operator;

	public NumericCondition(Operator operator) {
		this.operator = operator;
	}
	
	@Override
	public boolean isMet(Number fieldValue, Number conditionalValue) {
		// TODO Auto-generated method stub
		return false;
	}
	

}

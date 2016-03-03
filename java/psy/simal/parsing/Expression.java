package psy.simal.parsing;

public class Expression{
	private final Expression left, right;
	private final String operator;
	
	public Expression(Expression left, String operator, Expression right){
		this.left = left;
		this.right = right;
		this.operator = operator;
	}

	public double evalAsNumber(){
		double leftVal = left.evalAsNumber();
		double rightVal = right.evalAsNumber();
		
		switch (operator){
			case "+":
				return leftVal + rightVal;
			case "-":
				return leftVal - rightVal;
			case "*":
				return leftVal * rightVal;
			case "/":
				return leftVal / rightVal;
			case "%":
				return leftVal % rightVal;
			case "^":
				return Math.pow(leftVal, rightVal);
			default:
				return 0;
		}
	}
	
	private class TerminalValue extends Expression{
		private final String value;
		
		public TerminalValue(String value){
			super(null, "", null);
			this.value = value;
		}
		
		@Override
		public double evalAsNumber(){
			try{
				return Double.parseDouble(value);
			}catch(NumberFormatException e){
				return 0;
			}
		}
	}
}

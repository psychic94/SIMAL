package psy.simal.parsing;

import java.util.ArrayDeque;

import psy.simal.error.ParseException;


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
	
	public String toString(){
		return "(" + left + operator + right + ")";
	}
	
	public static void main(String[] args){
		String line = "";
		for(String arg : args){
			line += arg + " ";
		}
		line.trim();
		
		ArrayDeque<Token> tokens = Tokenizer.tokenizeLine(line, 1);
		
		try{
			Parser parser = new Parser(tokens);
			Expression expr = parser.parseExpression();
			System.out.println(expr + " = " + expr.evalAsNumber());
		}catch(ParseException e){
			System.out.println(e.getMessage());
		}	
	}
}

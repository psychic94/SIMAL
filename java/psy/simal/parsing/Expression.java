package psy.simal.parsing;

import java.util.ArrayDeque;

import psy.simal.Value;
import psy.simal.error.ParseException;


public class Expression implements Value{
	private final Value left, right;
	private final String operator;
	
	public Expression(Value left, String operator, Value right){
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
	
	public String evalAsString(){
		//TODO
		return "";
	}
	
	public boolean evalAsBoolean(){
		return evalAsNumber() > 0;
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
		
		try{
			ArrayDeque<Token> tokens = Tokenizer.tokenizeLine(line, 1);
			Parser parser = new Parser(tokens);
			Value expr = parser.parseExpression();
			System.out.println(expr + " = " + expr.evalAsNumber());
		}catch(ParseException e){
			System.out.println(e.getMessage());
		}	
	}
}

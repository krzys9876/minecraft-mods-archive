package com.krzyskrzys.logo;

public class LogoExprToken {
	String token;
	public LogoExprToken.tokenType type;
	public LogoExprToken.tokenOperator operator;
	public int boolValue;
	public Double numValue;
	private String description;
	
	public final static String operTokens="+-*/><=!&|()";
		
	public enum tokenType {
		NUMBER, BOOLEAN, ARITHMETIC_OPERATOR, RELATIVE_OPERATOR, LOGICAL_OPERATOR, OP_BRACKET, CL_BRACKET
	}
	
	public enum tokenOperator {
		ADD, SUB, MULTI, DIV, GT, LT, EQ, NEQ, AND, OR, NONE 
	}
	
	public LogoExprToken(String nToken) {
		token=nToken;
		boolValue=-1;
		numValue=Double.NaN;
		description="";
		switch (nToken) {
		case "+": 
			type=tokenType.ARITHMETIC_OPERATOR;
			operator=tokenOperator.ADD;
			description="OP+";
			break;
		case "-": 
			type=tokenType.ARITHMETIC_OPERATOR;
			operator=tokenOperator.SUB;
			description="OP-";
			break;
		case "*": 
			type=tokenType.ARITHMETIC_OPERATOR;
			operator=tokenOperator.MULTI;
			description="OP*";
			break;
		case "/": 
			type=tokenType.ARITHMETIC_OPERATOR;
			operator=tokenOperator.DIV;
			description="OP/";
			break;
		case ">": 
			type=tokenType.RELATIVE_OPERATOR;
			operator=tokenOperator.GT;
			description="OP>";
			break;
		case "<": 
			type=tokenType.RELATIVE_OPERATOR;
			operator=tokenOperator.LT;
			description="OP<";
			break;
		case "=": 
			type=tokenType.RELATIVE_OPERATOR;
			operator=tokenOperator.EQ;
			description="OP=";
			break;
		case "!": 
			type=tokenType.RELATIVE_OPERATOR;
			operator=tokenOperator.NEQ;
			description="OP<>";
			break;
		case "&": 
			type=tokenType.LOGICAL_OPERATOR;
			operator=tokenOperator.AND;
			description="OP:AND";
			break;
		case "|": 
			type=tokenType.LOGICAL_OPERATOR;
			operator=tokenOperator.OR;
			description="OP:OR";
			break;
		case "(": 
			type=tokenType.OP_BRACKET;
			operator=tokenOperator.NONE;
			description="BR(";
			break;
		case ")": 
			type=tokenType.CL_BRACKET;
			operator=tokenOperator.NONE;
			description="BR)";
			break;
		default:
			type=tokenType.NUMBER;
			numValue=parseNumber(nToken);
			operator=tokenOperator.NONE;
			description="NUM:"+numValue.toString();
		}
		// wartoœæ typu boolean nie pochodzi z tekstu, tylko z dalszych operacji		
	}
	
	public LogoExprToken(boolean nBoolToken) {
		boolValue=nBoolToken ? 1:0;
		type=tokenType.BOOLEAN;
		operator=tokenOperator.NONE;
		numValue=Double.NaN;
		description="BOOL:"+boolValue;	
	}
	
	public LogoExprToken(Double nNumToken) {
		boolValue=-1;
		type=tokenType.NUMBER;
		operator=tokenOperator.NONE;
		numValue=nNumToken;
		token=nNumToken.toString();
		description="NUM:"+numValue;	
	}

	public static Double parseNumber(String str) {
		Double val;
		try {
			val=Double.valueOf(str);			
		} catch(NumberFormatException e) {
			val=Double.NaN;
		}
		return val;
	}

	public Double parseNumber() {
		return parseNumber(token);
	}

	public String toString() {
		return description;
	}
	
	public boolean isBracket() {
		return type==tokenType.OP_BRACKET || type==tokenType.CL_BRACKET; 
	}

}

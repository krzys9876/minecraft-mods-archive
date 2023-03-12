package com.krzyskrzys.logo;

import java.util.ArrayList;

public class LogoExprTokens {
	private ArrayList<LogoExprToken> array;
	
	public LogoExprTokens() {
		array=new ArrayList<LogoExprToken>();
	}
	
	public LogoExprTokens(LogoExprTokens tokens, int indFrom, int indTo) {
		this();
		
		for(int i=indFrom;i<=indTo;i++) {
			array.add(tokens.array.get(i));
		}
	}
	
	public LogoExprTokens(LogoExprTokens tokens, int indFrom) {
		this(tokens,indFrom,tokens.size()-1);
	}

	
	public void replace(int indFrom,int indTo, LogoExprToken nToken) {
		for(int i=indTo;i>=indFrom;i--) {
			array.remove(i);
		}
		array.add(indFrom, nToken);
	}
	
	public int tokenIndex(LogoExprToken.tokenType type, int startPos) {
		for(int i=startPos;i<array.size();i++) {
			if(array.get(i).type==type) {
				return i;				
			}
		}		
		return -1;
	}
	
	public int tokenIndex(LogoExprToken.tokenType type) {
		return tokenIndex(type,0);
	}


	public boolean hasBrackets() {
		for(LogoExprToken token : array) {
			if(token.isBracket()) {
				return true;				
			}
		}		
		return false;
	}
	
	public boolean isArithmetic() {
		return 
			tokenIndex(LogoExprToken.tokenType.LOGICAL_OPERATOR)==-1 &&
			tokenIndex(LogoExprToken.tokenType.RELATIVE_OPERATOR)==-1 &&
			tokenIndex(LogoExprToken.tokenType.BOOLEAN)==-1 &&
			tokenIndex(LogoExprToken.tokenType.NUMBER)>=0;
	}
	
	public boolean isRelative() {
		// wyra¿enie w postaci: wyra¿enie arytmetyczne / operator relacyjny / wyra¿enie arytmetyczne
		if(tokenIndex(LogoExprToken.tokenType.LOGICAL_OPERATOR)>=0) {
			// operatory OR i AND
			return false;
		}
		
		int cnt=0;
		int pos=-1;
		
		for(int i=0;i<size(); i++) {
			if(get(i).type==LogoExprToken.tokenType.RELATIVE_OPERATOR) {
				if(i==0 || i==size()-1) {
					// operator na pocz¹tku lub na koñcu
					return false;
				}
				cnt=cnt+1;
				pos=i;
				if(cnt>1) {
					// wiêcej, ni¿ jeden operator relacyjny
					return false;
				}
			}
		}
		
		LogoExprTokens expr1=new LogoExprTokens(this,0,pos-1);
		if(!expr1.isArithmetic()) {
			// wyra¿enie 1 nie jest arytmetyczne
			return false;
		}
		LogoExprTokens expr2=new LogoExprTokens(this,pos+1,size()-1);
		if(!expr2.isArithmetic()) {
			// wyra¿enie 2 nie jest arytmetyczne
			return false;
		}

		return true;
	}
	
	public boolean isBoolean() {
		// wyra¿enie logiczne
		if(tokenIndex(LogoExprToken.tokenType.LOGICAL_OPERATOR)==-1) {
			// operatory OR i AND
			return false;
		}
		
		// nieoptymalne
		for(int i=0;i<size(); i++) {
			if(get(i).type==LogoExprToken.tokenType.LOGICAL_OPERATOR) {
				if(i==0 || i==size()-1) {
					// operator na pocz¹tku lub na koñcu
					return false;
				}
			}
		}
		
		return true;
	}

	
	public LogoExprToken get(int i) {
		return array.get(i);
	}

	public void add(LogoExprToken token) {
		array.add(token);
	}

	public int size() {
		return array.size();
	}
	
	public String toString() {
		return array.toString();
	}
}

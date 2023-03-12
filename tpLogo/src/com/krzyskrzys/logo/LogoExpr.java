package com.krzyskrzys.logo;

import java.util.StringTokenizer;

public class LogoExpr {
	private String expression;
	private boolean evaluated;
	private boolean isBoolean;
	private int boolValue; // -1 to wartoœæ odpowiadaj¹ca NaN w LogoExpression - czyli b³¹d, 0 - false, 1 - true
	private Double numValue; // NaN dzia³a tak samo, jak w LogoExpression - czyli b³¹d
	
	
	public LogoExpr(String nExpr,boolean nIsBool) {
		expression=nExpr;
		evaluated=false;
		isBoolean=nIsBool;
		boolValue=-1;
		numValue=Double.NaN;
	}	

	public String getExpr() {
		return expression;
	};
	
	private void setEvaluationErr() {
		boolValue=-1;
		numValue=Double.NaN;
		evaluated=true;
	}
		
	public String toString() {
		return expression+" is "+(isBoolean ? boolValue : numValue.toString());
	}
	
	public boolean evaluate() {
		return evaluate(expression,null);
	}
	
	
	private Double evaluateArithmetic(LogoExprTokens tokens) {
		// za³o¿enie - nie ma nawiasów!
		if(tokens.hasBrackets()) {
			return Double.NaN;
		}
		// wyra¿enie zawiera operatory logiczne, czyli nie jest to wyra¿enie arytmetyczne
		if(!tokens.isArithmetic()) {
			return Double.NaN;
		}
		
		
		Double finValue=0.0;
		Double workValue=0.0;
		Double parsedValue;

		int i=0;		
		LogoExprToken.tokenOperator waitingOperator=LogoExprToken.tokenOperator.ADD;
		LogoExprToken.tokenOperator lastOperator=LogoExprToken.tokenOperator.ADD;

		// pierwszy element - liczba lub znak "-", na razie nie ma innych opcji		
		if(tokens.get(i).operator==LogoExprToken.tokenOperator.SUB) {
			i=i+1;
			waitingOperator=LogoExprToken.tokenOperator.SUB;
		}

		// przetwarzanie na przemian - liczba / operator, zaczynamy od liczby
		boolean waitForNumber=true;
		
		while(i<tokens.size()) {
			if(waitForNumber) {
				parsedValue=tokens.get(i).parseNumber();
				// b³êdna wartoœæ
				if(parsedValue.isNaN()) {
					return Double.NaN;			
				}
				// sk³adanie roboczej wartoœci do póŸniejszego dodania lub odjêcia od wartoœci finalnej
				if(lastOperator==LogoExprToken.tokenOperator.MULTI) {
					workValue=workValue*parsedValue;
				} else if(lastOperator==LogoExprToken.tokenOperator.DIV) {
					workValue=workValue/parsedValue;
				} else if(lastOperator==LogoExprToken.tokenOperator.ADD || lastOperator==LogoExprToken.tokenOperator.SUB) {
					workValue=parsedValue;
				}
				// teraz kolej na operator
				waitForNumber=!waitForNumber;
			} else {
				if(tokens.get(i).type==LogoExprToken.tokenType.ARITHMETIC_OPERATOR) {
					lastOperator=tokens.get(i).operator;
					if(!workValue.isNaN()) {
						if(lastOperator==LogoExprToken.tokenOperator.ADD || lastOperator==LogoExprToken.tokenOperator.SUB) {
							// dodanie wartoœci roboczej do finalnej lub odjêcie wartoœci roboczej od finalnej
							// tylko operatory + i - powoduj¹ dodanie lub odjêcie wartoœci roboczej,
							// * i / powoduj¹ wy³¹cznie zapamiêtanie ostatniej operacji w lastOperator
							if(waitingOperator==LogoExprToken.tokenOperator.ADD) {
								finValue=finValue+workValue;
							} else if(waitingOperator==LogoExprToken.tokenOperator.SUB) {
								finValue=finValue-workValue;
							} 
							waitingOperator=lastOperator;						
							workValue=Double.NaN;
						}
					}
					waitForNumber=!waitForNumber;
				} else {
					// to mo¿e wynikaæ z nieprawid³owej sk³adni
					return Double.NaN;			
				}
			}			
			i=i+1;
		}
		
		// zakoñczenie - czêœciowe powtórzenie kodu z pêtli
		if(!workValue.isNaN()) {
			if(waitingOperator==LogoExprToken.tokenOperator.ADD) {
				finValue=finValue+workValue;
			} else if(waitingOperator==LogoExprToken.tokenOperator.SUB) {
				finValue=finValue-workValue;
			} 
		}
				
		System.out.println(finValue);
		return finValue; 
	}

	
	private int evaluateRelative(LogoExprTokens tokens) {
		// za³o¿enie: nie ma nawiasów
		if(tokens.hasBrackets()) {
			return -1;
		}
		// je¿eli nie jest to wyra¿enie relacyjne, to b³¹d
		if(!tokens.isRelative()) {
			return -1;			
		}		
		
		int operPos=tokens.tokenIndex(LogoExprToken.tokenType.RELATIVE_OPERATOR);
		
		if(operPos<=0 || operPos==tokens.size()) {
			// b³¹d formatu (teoretycznie wczeœniej zosta³o to sprawdzone)
			return -1;
		}
		
		LogoExprTokens expr1=new LogoExprTokens(tokens,0,operPos-1); 
		Double val1=evaluateArithmetic(expr1);
		if(val1.isNaN()) {
			// b³¹d w wyra¿eniu 1
			return -1;
		}
		
		LogoExprToken oper=tokens.get(operPos); 
		if(oper.type!=LogoExprToken.tokenType.RELATIVE_OPERATOR) {
			// b³¹d operatora
			return -1;
		}
		
		LogoExprTokens expr2=new LogoExprTokens(tokens,operPos+1); 
		Double val2=evaluateArithmetic(expr2);
		if(val2.isNaN()) {
			// b³¹d w wyra¿eniu 2
			return -1;
		}
		
		int finValue=-1;
		if(oper.operator==LogoExprToken.tokenOperator.GT) {
			finValue=val1.doubleValue()>val2.doubleValue() ? 1 : 0;
		} else if(oper.operator==LogoExprToken.tokenOperator.LT) {
			finValue=val1.doubleValue()<val2.doubleValue() ? 1 : 0;
		} else if(oper.operator==LogoExprToken.tokenOperator.EQ) {
			finValue=val1.doubleValue()==val2.doubleValue() ? 1 : 0;
		} else if(oper.operator==LogoExprToken.tokenOperator.NEQ) {
			finValue=val1.doubleValue()!=val2.doubleValue() ? 1 : 0;
		}
		// teoretycznie wczeœniej sprawdziliœmy, czy operator zawiera tylko ><=!
		if(finValue==-1) {
			// b³¹d operatora
			return -1;
		}
		
		System.out.println(val1.toString()+oper+val2.toString()+":"+finValue);
		
		return finValue;
	}
	
	
		
	private int evaluateSimpleBoolean(LogoExprTokens tokens) {
		if(tokens.size()!=1) {
			return -1;
		}
		if(tokens.get(0).type==LogoExprToken.tokenType.BOOLEAN) {
			return tokens.get(0).boolValue;		
		}
		return -1;
	}

	
	private int evaluateBoolean(LogoExprTokens tokens) {
		// za³o¿enie: nie ma nawiasów
		if(tokens.hasBrackets()) {
			return -1;
		}
				
		int finValue=0;
		int workValue=0;
		int parsedValue=-1;
		
		LogoExprToken.tokenOperator lastOperator=LogoExprToken.tokenOperator.OR;

		// przetwarzanie na przemian - wyra¿enie / operator, zaczynamy od wyra¿enia
		boolean waitForExpr=true;

		int pos=tokens.tokenIndex(LogoExprToken.tokenType.LOGICAL_OPERATOR);		
		LogoExprTokens workTokens;
		if(pos>=0) {
			workTokens=new LogoExprTokens(tokens,0,pos-1);
		} else {
			// wyrazenie nie zawiera operatorów logicznych
			workTokens=tokens;
		}
		
		// nieskoñczona pêtla
		while(true) {
			if(waitForExpr) {				
				int simpleVal=evaluateSimpleBoolean(workTokens);
				if(simpleVal!=-1) {
					parsedValue=simpleVal;
				} else if(workTokens.isRelative()) {
					parsedValue=evaluateRelative(workTokens);
				} else {
					// b³êdne wyra¿enie, to powinno byæ wyra¿enie logiczne
					return -1;
				}
				// b³êdne wyra¿enie
				if(parsedValue==-1) {
					return -1;			
				}
				
				// wyznaczanie wartoœci tymczasowej wyra¿enia logicznego
				if(lastOperator==LogoExprToken.tokenOperator.OR) {
					workValue=parsedValue;
				} else if(lastOperator==LogoExprToken.tokenOperator.AND) {
					workValue=parsedValue*workValue;
				}
				
				if(pos==-1) {
					// to by³ ostatni segment wyra¿enia
					break;
				} 
				waitForExpr=!waitForExpr;				
				workTokens=new LogoExprTokens(tokens,pos,pos);					
				
			} else {
				// ten warunek powinien byæ zawsze spe³niony
				if(workTokens.get(0).type==LogoExprToken.tokenType.LOGICAL_OPERATOR) {
					lastOperator=workTokens.get(0).operator;
					if(workValue!=-1) {
						if(lastOperator==LogoExprToken.tokenOperator.OR) {
							//suma logiczna wartoœci roboczej i finalnej
							// tylko operator | powoduje wykonani dzia³ania,
							// & powoduje wy³¹cznie zapamiêtanie ostatniej operacji w lastOperator
							finValue=finValue+workValue==0 ? 0 : 1;
							workValue=-1;
						}
					}
					waitForExpr=!waitForExpr;
				} else {
					// b³¹d w sk³adni (np. dwa operatory nastêpuj¹ce po sobie)
					return -1;			
				}
				int lastPos=pos;
				pos=tokens.tokenIndex(LogoExprToken.tokenType.LOGICAL_OPERATOR,lastPos+1);
				if(pos>=0) {
					workTokens=new LogoExprTokens(tokens,lastPos+1,pos-1);
				} else {
					// to by³ ostatni operator
					workTokens=new LogoExprTokens(tokens,lastPos+1);
				}
			}			
		}
		
		// zakoñczenie - czêœciowe powtórzenie kodu z pêtli
		if(workValue!=-1) {
			finValue=finValue+workValue==0 ? 0 : 1;
		}

		return finValue;
	}
	
	
	private boolean evaluate(LogoExprTokens tokens) {
		// brak elementów - puste wyra¿enie
		if(tokens.size()==0) {
			setEvaluationErr();
			return false;
		}

		System.out.println(tokens+";start");
		
		
		int opBrPos=-1;
		int clBrPos=-1;
				
		// redukcja nawiasów
		//TODO: rozwi¹zanie tymczasowe z _, nale¿y raczej redukowaæ nawiasy w tablicy ci¹gów, nie jako String
		while(tokens.hasBrackets()) {
			clBrPos=tokens.tokenIndex(LogoExprToken.tokenType.CL_BRACKET);
			int pos=clBrPos-1;
			// szukamy nawiasu otwieraj¹cego
			while(pos>=0) {
				if(tokens.get(pos).type==LogoExprToken.tokenType.OP_BRACKET) {
					opBrPos=pos;
					break;
				}
				pos=pos-1;
			}
			if(clBrPos<0 || opBrPos<0) {
				// b³¹d w uk³adzie nawiasów
				setEvaluationErr();
				return true;
			}
			// wnêtrze nawiasu
			LogoExprTokens brTokens=new LogoExprTokens(tokens,opBrPos+1, clBrPos-1);
			System.out.println(brTokens);			
			if(brTokens.isArithmetic()) {
				Double exprValue=evaluateArithmetic(brTokens);
				if(exprValue.isNaN()) {
					// b³¹d w wyra¿eniu
					setEvaluationErr();
					return true;					
				}
				// zamiana nawiasu na wartoœæ wyra¿enia
				tokens.replace(opBrPos,clBrPos,new LogoExprToken(exprValue));
				System.out.println(tokens);
				
			} else if(brTokens.isRelative()){
				int relValue=evaluateRelative(brTokens);
				tokens.replace(opBrPos,clBrPos,new LogoExprToken(relValue==1 ? true : false));
				System.out.println(tokens);
			} else if(brTokens.isBoolean()) {
				int boolValue=evaluateBoolean(brTokens);
				// TODO: rozwi¹zanie tymczasowo, nale¿y redukowaæ wyra¿enie na tablicy obiektów (obiekty typu operator i liczba)
				tokens.replace(opBrPos,clBrPos,new LogoExprToken(boolValue==1 ? true : false));
				System.out.println(tokens);
			}
		}
		
		// wyra¿enie zredukowane, bez nawiasów
		System.out.println("zredukowane:"+tokens);		
		if(isBoolean) {			
			boolValue=evaluateBoolean(tokens);
		} else {
			numValue=evaluateArithmetic(tokens);
		}
		System.out.println("value:"+(isBoolean ? boolValue : numValue.toString()));
				
		evaluated=true;

		return true;

	}

	private boolean evaluate(String expr, LogoOutput logoOut) {
		System.out.println(expr+";start");
		
		if(expr.equals("")) {
			setEvaluationErr();
			return false;
		}

		StringTokenizer strt=new StringTokenizer(expr,LogoExprToken.operTokens,true);			
		
		LogoExprTokens tokens=new LogoExprTokens();
		
		LogoExprToken token;
				
		while(strt.hasMoreTokens()) {
			String strToken=strt.nextToken();
			// liczba lub operator - wartoœæ NaN sugeruje, ¿e jest to zmienna 
			// (bo nie uda³a siê konwersja do liczby, a nie jest to operator) 
			token=new LogoExprToken(strToken);
			
			if(token.type==LogoExprToken.tokenType.NUMBER && token.numValue.isNaN()) {
				// nieprawid³owa wartoœæ - nale¿y sprawdziæ, czy to nie jest zmienna
				String var="";
				// wyszukanie wartoœci zmiennej
				if(logoOut.getVariables().getLocal().containsKey(strToken)) {
					var=logoOut.getVariables().getLocal().get(strToken).getString();				
				} else if(logoOut.getVariables().getGlobal().containsKey(strToken)) {
					var=logoOut.getVariables().getGlobal().get(strToken).getString();
				} else {
					// nie znaleziony symbol
					setEvaluationErr();
					return false;					
				}
				token=new LogoExprToken(var);
			}
			
			tokens.add(token);			
		}
				
		System.out.println(tokens);
		return evaluate(tokens);
		
	}
	
	public int getBoolValue() {
		return boolValue;
	}

	public Double getNumValue() {
		return numValue;
	}
	
	public boolean isBoolean() {
		return isBoolean;
	}

	public boolean evaluate(LogoOutput logoOut) {
		return evaluate(expression,logoOut);
	}
	
	public boolean isEvaluated() {
		return evaluated;
	}

}

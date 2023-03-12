package com.krzyskrzys.logo;

import java.util.ArrayList;

public abstract class LogoStmt {
	
	protected String cmd;	
	protected ArrayList<String> paramArr;
	protected int lineNum;
	
	private LogoStmt next;	
	
	protected LogoStmt(String nCmd, int nLineNum) {
		cmd=nCmd;
		lineNum=nLineNum;
		paramArr=new ArrayList<String>();
		//params=paramArr.size();
	}

	abstract boolean execute(LogoOutput logoOut);
	
	public void setParams(String[] nParams) {
		paramArr.clear();
		for(int i=0;i<nParams.length;i++) {
			paramArr.add(nParams[i]);				
		}
	}
	
	public String getCmd() {
		return cmd;
	}	
	
	public LogoStmt getNext() {
		return next;
	}
	
	public void setNext(LogoStmt nnext) {
		next=nnext;
	}

	public String toString() {
		String str=lineNum+".CMD:"+cmd+";params:"+paramArr;
		return str;		
	}
	
	// tylko tymczasowe u�ycie	przy wyznaczaniu warto�ci parametr�w
	protected double parseVal=0; 
	protected boolean parseValBool=false;
	
	public boolean getParValue(int i, LogoOutput logoOut) {
		// warto�� numeryczna 
		return getParValue(i, logoOut, false);
	}
	
	public boolean getParBoolValue(int i, LogoOutput logoOut) {
		// warto�� logiczna
		return getParValue(i, logoOut, true);
	}

	private boolean getParValue(int i, LogoOutput logoOut, boolean isBoolean) {
		parseVal=0;		
		
		// warto�� parametru traktowana jako wyra�enie arytmetyczne
		LogoExpr logoExp=new LogoExpr(paramArr.get(i),isBoolean);
		// b��d przy wyznaczaniu warto�ci
		if(!logoExp.evaluate(logoOut)) {
			return false;				
		}

		// Ustawianie parametru w zale�no�ci od typu wyra�enia
		// Po wywo�aniu nale�y odczyta� odpowiedni parametr
		if(isBoolean) {
			int pBoolVal=logoExp.getBoolValue();
			if(pBoolVal==-1) {
				// nieprawid�owa warto�� wyra�enia
				return false;
			}
			parseValBool=pBoolVal==1 ? true : false;
		} else {
			Double pVal=logoExp.getNumValue();
			if(pVal.isNaN() || pVal.isInfinite()) {
				// b��d warto�ci, np. dzielenie przez 0
				return false;				
			}
			parseVal=pVal.doubleValue();
		}
		
		return true;		
	}
}
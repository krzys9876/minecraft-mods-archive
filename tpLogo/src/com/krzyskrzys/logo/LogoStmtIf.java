package com.krzyskrzys.logo;

public class LogoStmtIf extends LogoStmt {
	
	private LogoStmtEndIf endIfStat;
	private LogoStmtElse elseStat;
	private int lastResult; // ostatni wynik warunku
	
	public LogoStmtIf(int nLineNum, String nParam) {
		super("IF",nLineNum);
		setParams(new String[] {nParam});
		endIfStat=null;
		elseStat=null;
		resetResult();
	}
	
	public boolean execute(LogoOutput logoOut) {
		if(!getParBoolValue(0,logoOut)) {
			return false;
		}
		lastResult=parseValBool ? 1: 0;
		
		return true;
	}
	
	public void resetResult() {
		lastResult=-1;
	}
	
	public void setEndIf(LogoStmtEndIf nEndIfStat) {
		endIfStat=nEndIfStat;
	}

	public LogoStmtEndIf getEndIf() {
		return endIfStat;
	}	

	public void setElse(LogoStmtElse nElseStat) {
		elseStat=nElseStat;
	}

	public LogoStmtElse getElse() {
		return elseStat;
	}	
	
	public LogoStmt getNext() {
		if(lastResult==0) {
			if(elseStat!=null) {
				return elseStat; 
			} 
			return endIfStat;			 
		}
		return super.getNext();
	}
	
	public int getLastResult() {
		return lastResult;
	}


}





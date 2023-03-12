package com.krzyskrzys.logo;

public class LogoStmtProc extends LogoStmt {
	
	private LogoStmtProcEnd procEndStat;
	
	public LogoStmtProc(int nLineNum, String[] args) {
		super("PROC",nLineNum);
		setParams(args);
		procEndStat=null;
	}	
	
	public boolean execute(LogoOutput logoOut) {
		return true;
	}	
	
	
	public void setEnd(LogoStmtProcEnd procEnd) {
		procEndStat=procEnd;
	}
	
	public LogoStmtProcEnd getEnd() {
		return procEndStat;
	}
	
	public String getName() {
		return paramArr.get(0);
	}
	
	public String toString() {
		return super.toString()+";name:"+getName();
	}

}

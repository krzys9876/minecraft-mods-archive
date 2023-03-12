package com.krzyskrzys.logo;

public class LogoStmtProcEnd extends LogoStmt {
	
	private LogoStmtProc procStat;
	
	public LogoStmtProcEnd(int nLineNum) {
		super("ENDPROC",nLineNum);
		procStat=null;
	}	
	
	public boolean execute(LogoOutput logoOut) {
		return true;
	}	
	
	
	public void setStart(LogoStmtProc nProcStat) {
		procStat=nProcStat;
	}
	
	public LogoStmtProc getStart() {
		return procStat;
	}


}

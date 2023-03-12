package com.krzyskrzys.logo;

public class LogoStmtRepEnd extends LogoStmt {
	
	private LogoStmtRepeat repeatStat;
	
	public LogoStmtRepEnd(int nLineNum) {
		super("ENDREPEAT",nLineNum);
		repeatStat=null;
	}
	
	public boolean execute(LogoOutput logoOut) {
		if(repeatStat==null) {
			return false;
		}
		
		LogoLoop loop=repeatStat.getLoop();
		boolean nextResult=loop.next();
		if(!nextResult) {
			loop.reset();
		}
		
		return true;
	}	

	public LogoStmt getNext() {
		LogoLoop loop=repeatStat.getLoop();
		if(loop.isRunning()) {
			return repeatStat.getNext(); // nadpisanie domyœlnego nastêpnego polecenia 
		}
		return super.getNext();
	}
	
	public void setStart(LogoStmtRepeat repStat) {
		repeatStat=repStat;
		//repeatStat.setEnd(this);
	}
	
	public String toString() {
		String str=super.toString();
		if(repeatStat!=null) {
			str=str+" "+repeatStat.getLoop().toString();
		}
		return str;
	}
}
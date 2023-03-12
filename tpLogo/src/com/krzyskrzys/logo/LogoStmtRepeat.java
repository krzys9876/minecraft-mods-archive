package com.krzyskrzys.logo;

public class LogoStmtRepeat extends LogoStmt {
	
	private LogoLoop loop;
	private LogoStmtRepEnd repEndStat; 
	
	public LogoLoop getLoop() {
		return loop;
	}
	
	public LogoStmtRepeat(int nLineNum, String nParam) {
		super("REPEAT",nLineNum);
		setParams(new String[] {nParam});
		loop=new LogoLoop();
		repEndStat=null;
	}
	
	public boolean execute(LogoOutput logoOut) {
		if(!getParValue(0,logoOut)) {			
			return false;
		}		
		// Sprawdzamy, czy wartoœæ jest ca³kowita
		if(Math.rint(parseVal)!=parseVal) {
			return false;
		}
		loop.setCount((int)parseVal);
		
		loop.start();
		return true;
	}	
	
	public void setEnd(LogoStmtRepEnd repEnd) {
		repEndStat=repEnd;
	}
	
	public LogoStmtRepEnd getEnd() {
		return repEndStat;
	}	
	
	public String toString() {
		return super.toString()+";"+loop.toString();
	}		
}
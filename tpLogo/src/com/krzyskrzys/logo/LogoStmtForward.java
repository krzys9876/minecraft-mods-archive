package com.krzyskrzys.logo;

public class LogoStmtForward extends LogoStmt {
	
	public LogoStmtForward(int nLineNum, String nParam) {
		super("FORWARD",nLineNum);
		setParams(new String[] {nParam});
	}
	
	public boolean execute(LogoOutput logoOut) {
		if(!getParValue(0,logoOut)) {
			return false;
		}		
		logoOut.drawLine(parseVal);
		return true;
	}	
}

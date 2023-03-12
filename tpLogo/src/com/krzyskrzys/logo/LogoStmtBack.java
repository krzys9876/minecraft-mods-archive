package com.krzyskrzys.logo;

public class LogoStmtBack extends LogoStmt {
	
	public LogoStmtBack(int nLineNum, String nParam) {
		super("BACK",nLineNum);
		setParams(new String[] {nParam});
	}
	
	public boolean execute(LogoOutput logoOut) {
		if(!getParValue(0,logoOut)) {
			return false;
		}
		
		logoOut.drawLine(-parseVal);
		return true;
	}	
}

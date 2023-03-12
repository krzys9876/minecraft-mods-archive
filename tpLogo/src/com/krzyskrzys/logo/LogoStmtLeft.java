package com.krzyskrzys.logo;

public class LogoStmtLeft extends LogoStmt {

	public LogoStmtLeft(int nLineNum, String nParam) {
		super("LEFT",nLineNum);
		setParams(new String[] {nParam});
	}

	public boolean execute(LogoOutput logoOut) {
		if(!getParValue(0,logoOut)) {
			return false;
		}

		logoOut.turn(-parseVal,0.0);
		return true;
	}	
}





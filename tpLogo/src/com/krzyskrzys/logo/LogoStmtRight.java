package com.krzyskrzys.logo;

public class LogoStmtRight extends LogoStmt {

	public LogoStmtRight(int nLineNum, String nParam) {
		super("RIGHT",nLineNum);
		setParams(new String[] {nParam});
	}

	public boolean execute(LogoOutput logoOut) {
		if(!getParValue(0,logoOut)) {
			return false;
		}

		logoOut.turn(parseVal,0.0);
		return true;
	}	

}

package com.krzyskrzys.logo;

public class LogoStmtDown extends LogoStmt {

	public LogoStmtDown(int nLineNum, String nParam) {
		super("DOWN",nLineNum);
		setParams(new String[] {nParam});
	}

	public boolean execute(LogoOutput logoOut) {
		if(!getParValue(0,logoOut)) {
			return false;
		}
		
		logoOut.turn(0.0,-parseVal);
		return true;
	}	

}

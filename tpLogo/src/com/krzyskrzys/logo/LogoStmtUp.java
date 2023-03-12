package com.krzyskrzys.logo;

public class LogoStmtUp extends LogoStmt {
	
	public LogoStmtUp(int nLineNum, String nParam) {
		super("UP",nLineNum);
		setParams(new String[] {nParam});
	}

	public boolean execute(LogoOutput logoOut) {
		if(!getParValue(0,logoOut)) {
			return false;
		}
		
		logoOut.turn(0.0,parseVal);
		return true;
	}	

}

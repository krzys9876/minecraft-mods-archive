package com.krzyskrzys.logo;

public class LogoStmtPenUp extends LogoStmt {
	
	public LogoStmtPenUp(int nLineNum) {
		super("PENUP",nLineNum);
		// parametry: pióro
		setParams(new String[] {});
	}

	public boolean execute(LogoOutput logoOut) {
		logoOut.setPenDown(false);
		return true;
	}	

}

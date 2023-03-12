package com.krzyskrzys.logo;

public class LogoStmtPenDown extends LogoStmt {
	
	public LogoStmtPenDown(int nLineNum) {
		super("PENDOWN",nLineNum);
		// parametry: pi�ro
		setParams(new String[] {});
	}

	public boolean execute(LogoOutput logoOut) {
		logoOut.setPenDown(true);
		return true;
	}	

}

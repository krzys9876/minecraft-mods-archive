package com.krzyskrzys.logo;

public class LogoStmtEndIf extends LogoStmt {
	
	public LogoStmtEndIf(int nLineNum) {
		super("ENDIF",nLineNum);
	}
	
	public boolean execute(LogoOutput logoOut) {
		// polecenie nic nie robi, pe�ni rol� wska�nika na kolejne polecenia
		return true;
	}


}

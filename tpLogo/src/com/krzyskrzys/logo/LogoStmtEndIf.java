package com.krzyskrzys.logo;

public class LogoStmtEndIf extends LogoStmt {
	
	public LogoStmtEndIf(int nLineNum) {
		super("ENDIF",nLineNum);
	}
	
	public boolean execute(LogoOutput logoOut) {
		// polecenie nic nie robi, pe³ni rolê wskaŸnika na kolejne polecenia
		return true;
	}


}

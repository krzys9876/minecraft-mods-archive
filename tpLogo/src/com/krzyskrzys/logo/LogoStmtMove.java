package com.krzyskrzys.logo;

public class LogoStmtMove extends LogoStmt {

	public LogoStmtMove(int nLineNum, String nParam) {
		super("MOVE",nLineNum);
		setParams(new String[] {nParam});		
	}
		
	public boolean execute(LogoOutput logoOut) {
		if(!getParValue(0,logoOut)) {
			return false;
		}

		logoOut.move(parseVal);
		return true;
	}	

}



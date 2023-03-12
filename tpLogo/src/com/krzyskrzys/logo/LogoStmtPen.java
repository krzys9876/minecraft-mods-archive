package com.krzyskrzys.logo;

public class LogoStmtPen extends LogoStmt {
	
	public LogoStmtPen(int nLineNum, String nParam1) {
		super("PEN",nLineNum);
		// parametry: pióro
		setParams(new String[] {nParam1});
	}

	public boolean execute(LogoOutput logoOut) {
		System.out.println(paramArr.get(0));
		logoOut.setPen(paramArr.get(0));
		return true;
	}	

}

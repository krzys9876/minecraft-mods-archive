package com.krzyskrzys.logo;

public class LogoStmtPos extends LogoStmt {
	
	public LogoStmtPos(int nLineNum, String nParam1, String nParam2, String nParam3) {
		super("POS",nLineNum);
		// parametry: x, y, z
		setParams(new String[] {nParam1, nParam2, nParam3});
	}

	public boolean execute(LogoOutput logoOut) {
		double xVal,yVal,zVal;
		
		if(!getParValue(0,logoOut)) {
			return false;
		}
		xVal=parseVal;
		
		if(!getParValue(1,logoOut)) {
			return false;
		}
		yVal=parseVal;

		if(!getParValue(2,logoOut)) {
			return false;
		}
		zVal=parseVal;
		
		logoOut.setPos(xVal, yVal, zVal);
		return true;
	}	

}

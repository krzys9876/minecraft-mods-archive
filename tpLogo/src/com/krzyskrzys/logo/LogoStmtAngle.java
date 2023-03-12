package com.krzyskrzys.logo;

public class LogoStmtAngle extends LogoStmt {
	
	public LogoStmtAngle(int nLineNum, String nParam1, String nParam2) {
		super("ANGLE",nLineNum);
		// parametry: aH, aV
		setParams(new String[] {nParam1, nParam2});
	}

	public boolean execute(LogoOutput logoOut) {
		double aHval,aVval;
		
		if(!getParValue(0,logoOut)) {
			return false;
		}
		aHval=parseVal;
		
		if(!getParValue(1,logoOut)) {
			return false;
		}
		aVval=parseVal;
				
		logoOut.setAngle(aHval, aVval);
		return true;
	}	
	
}

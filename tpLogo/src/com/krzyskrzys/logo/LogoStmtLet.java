package com.krzyskrzys.logo;

import java.util.HashMap;

public class LogoStmtLet extends LogoStmt {
	
	private boolean isGlobal;
	
	public LogoStmtLet(int nLineNum, String nParam1, String nParam2) {
		super("LET",nLineNum);
		// 1 - zmienna, 2 - wartoœæ (wyra¿enie)
		// Do dodania - sprawdzanie kontekstu, czy zmienne lokalne (wnêtrze procedury), czy globalne (program g³ówny)
		setParams(new String[] {nParam1,nParam2});
		setLocal(); // domyœlnie
	}	
	
	public boolean execute(LogoOutput logoOut) {
		LogoExpr expr=new LogoExpr(paramArr.get(1),false);
		if(!expr.evaluate(logoOut)) {
			// b³¹d w wyra¿eniu
			return false;
		}		

		HashMap<String,LogoVariable> var;
		// albo jawne wskazanie na zmienn¹ globaln¹, albo zmienna ju¿ jest globalna
		if(isGlobal || logoOut.getVariables().getGlobal().containsKey(paramArr.get(0))) {
			var=logoOut.getVariables().getGlobal();
		} else {
			var=logoOut.getVariables().getLocal();
		}
		var.put(paramArr.get(0), new LogoVariable(expr.getNumValue().doubleValue()));
				
		return true;
	}	
	
	public void setGlobal() {
		isGlobal=true;
	}

	public void setLocal() {
		isGlobal=false;
	}
	
	public String toString() {
		String str=lineNum+".CMD:"+cmd+"("+(isGlobal?"global":"local")+");params:"+paramArr;
		return str;		
	}

}

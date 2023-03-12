package com.krzyskrzys.logo;

import java.util.ArrayList;

public class LogoStmtCall extends LogoStmt {
	
	private LogoStmtProc procStat;
	
	public LogoStmtCall(int nLineNum, String[] nParams) {
		super("CALL",nLineNum);
		setParams(nParams);
		procStat=null; // referencja do procedury musi zosta� ustawiona p�niej, nie w konstruktorze
	}	
	
	
	@SuppressWarnings("unchecked") // chodzi o lini� z kopiowaniem listy parametr�w, clone zwraca wy��cznie Object
	public boolean execute(LogoOutput logoOut) {
		if(procStat==null) {
			return false;
		}
		// utworzenie procedury - nowy �a�cuch wywo�a� wskazuj�cy na lini� z deklaracj� procedury		
		ArrayList<String> paramArrCopy=(ArrayList<String>)paramArr.clone();
		for(int i=1;i<paramArrCopy.size();i++) {
			if(!getParValue(i,logoOut)) {			
				return false;
			}
			paramArrCopy.set(i,Double.toString(parseVal));
		}
		
		LogoProcedure proc=new LogoProcedure(procStat,false,getName(),paramArrCopy);
		System.out.println("call:"+proc);
		
		return proc.execute(logoOut);
	}	
	
	public void setProc(LogoStmtProc nProcStat) {
		procStat=nProcStat;
	}
	
	public LogoStmtProc getProc() {
		return procStat;
	}

	public String getName() {
		return paramArr.get(0);
	}
	
	public String toString() {
		return super.toString()+" name: "+getName()+ " stat: "+procStat;
	}

}

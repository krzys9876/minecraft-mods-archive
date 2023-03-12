package com.krzyskrzys.logo;

public class LogoStmtElse extends LogoStmt {
	
	private LogoStmtIf ifStat;
	
	public LogoStmtElse(int nLineNum) {
		super("ELSE",nLineNum);
		ifStat=null;
	}
	
	public boolean execute(LogoOutput logoOut) {
		// polecenie nic nie robi, pe³ni rolê wskaŸnika na kolejne polecenia
		if(ifStat==null) {
			// brak If - b³¹d w kodzie, teoretycznie sprawdzone wczeœniej
			return false;
		}
		return true;
	}
	
	public void setIf(LogoStmtIf nIfStat) {
		ifStat=nIfStat;
	}

	public LogoStmtIf getIf() {
		return ifStat;
	}	
	
	public LogoStmt getNext() {
		// je¿eli warunek by³ spe³niony, skocz na koniec
		if(ifStat.getLastResult()==1) {
			return ifStat.getEndIf(); 
		} 			 
		return super.getNext();
	}


	
}

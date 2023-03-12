package com.krzyskrzys.logo;

import java.util.ArrayList;
import java.util.HashMap;

public class LogoProcedure {
	
	private LogoStmt startStat;
	//private boolean isMain; // nadmiarowe, nie jest wykorzystywane
	private String name;
	private ArrayList<String> procParArr;	
	
	
	public LogoProcedure(LogoStmt start) {
		init(start,true,"MAIN",new ArrayList<String>());
	}
	
	public LogoProcedure(LogoStmt start, boolean nIsMain, String nName, ArrayList<String> nPars) {
		init(start,nIsMain,nName,nPars);
	}
	
	private void init(LogoStmt start, boolean nIsMain, String nName, ArrayList<String> nPars) {
		startStat=start;
		//isMain=nIsMain;
		name=nName;
		procParArr=nPars;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean execute(LogoOutput logoOut) {
				
		boolean execResult=false;
		
		LogoStmt execStat=startStat;
					
		// stworzy� dwa tryby wywo�ywania procedury - jedna rzeczywista, po wywo�aniu CALL, druga skip
		// ewentualnie przenie�� wszystkie deklaracje do innego �a�cucha wywo�a� (tak nawet lepiej)

		// Przechowanie warto�ci zmiennych lokalnych
		LogoVariables oldVariables=logoOut.getVariables();		
		// kopia z pustymi zmiennymi lokalnymi i tymi samymi globalnymi
		LogoVariables newVariables=new LogoVariables(oldVariables); 
		
		HashMap<String,Double> newLocalVar=new HashMap<String,Double>();
		for(int i=1;i<procParArr.size();i++) {
			// przepisanie paramer�w (pierwszy to nazwa procedury, wi�c zaczynamy od 1, nie od 0
			newLocalVar.put(execStat.paramArr.get(i), Double.valueOf(procParArr.get(i)));
			newVariables.getLocal().put(execStat.paramArr.get(i), new LogoVariable(Double.valueOf(procParArr.get(i))));
		}
		
		// Ustawienie nowych warto�ci zmiennych lokalnych
		logoOut.setVariables(newVariables);
				
		try {
					
		while(execStat!=null ) { // zako�czenie programu
			
			System.out.println(execStat.toString()); // polecenie
				
			execResult=execStat.execute(logoOut);
			if(!execResult) {
				return false; // przerwanie przy b��dzie
			}
			System.out.println(logoOut.toString()); // wynik
				
			execStat=execStat.getNext();
		}
		return true;
		
		} finally {
			// Przywr�cenie warto�ci zmiennych lokalnych
			logoOut.setVariables(oldVariables);
		}
	}
	
	public String toString() {
		return "start:"+startStat+"name:"+name+"params:"+procParArr.toString();
	}
	
}

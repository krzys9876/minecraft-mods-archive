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
					
		// stworzyæ dwa tryby wywo³ywania procedury - jedna rzeczywista, po wywo³aniu CALL, druga skip
		// ewentualnie przenieœæ wszystkie deklaracje do innego ³añcucha wywo³añ (tak nawet lepiej)

		// Przechowanie wartoœci zmiennych lokalnych
		LogoVariables oldVariables=logoOut.getVariables();		
		// kopia z pustymi zmiennymi lokalnymi i tymi samymi globalnymi
		LogoVariables newVariables=new LogoVariables(oldVariables); 
		
		HashMap<String,Double> newLocalVar=new HashMap<String,Double>();
		for(int i=1;i<procParArr.size();i++) {
			// przepisanie paramerów (pierwszy to nazwa procedury, wiêc zaczynamy od 1, nie od 0
			newLocalVar.put(execStat.paramArr.get(i), Double.valueOf(procParArr.get(i)));
			newVariables.getLocal().put(execStat.paramArr.get(i), new LogoVariable(Double.valueOf(procParArr.get(i))));
		}
		
		// Ustawienie nowych wartoœci zmiennych lokalnych
		logoOut.setVariables(newVariables);
				
		try {
					
		while(execStat!=null ) { // zakoñczenie programu
			
			System.out.println(execStat.toString()); // polecenie
				
			execResult=execStat.execute(logoOut);
			if(!execResult) {
				return false; // przerwanie przy b³êdzie
			}
			System.out.println(logoOut.toString()); // wynik
				
			execStat=execStat.getNext();
		}
		return true;
		
		} finally {
			// Przywrócenie wartoœci zmiennych lokalnych
			logoOut.setVariables(oldVariables);
		}
	}
	
	public String toString() {
		return "start:"+startStat+"name:"+name+"params:"+procParArr.toString();
	}
	
}

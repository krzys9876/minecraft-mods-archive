package com.krzyskrzys.logo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class LogoProgram {
	private ArrayList<String> fileArr;
	private boolean prepStatus;
	private boolean execResult;
	//private LogoOutput logoOut;
	private ArrayList<LogoParser> statParserArr; // by� mo�e niepotrzebne, wykorzystywane tylko na pocz�tku
	private ArrayList<LogoStmt> statArr;
	private HashMap<String,LogoStmtProc> procMap;
	private LogoProcedure mainProc;
	private LogoStmt startStatement;
	
	public String getLine(int num) {
		return fileArr.get(num);
	}
	
	public boolean getResult() {
		return execResult;
	}

	public boolean getPrepStatus() {
		return prepStatus;
	}
	
	public LogoProgram(String[] progArr) {
		init();
		
		// przepisanie programu
		for(int i=0;i<progArr.length;i++) {
			fileArr.add(progArr[i]);
		}
		
		//exec();
	}
	
	private void init() {
		fileArr=new ArrayList<String>();
		statParserArr=new ArrayList<LogoParser>();
		statArr=new ArrayList<LogoStmt>();
		procMap=new HashMap<String,LogoStmtProc>();
		mainProc=null;
		execResult=false; // warto�ci inicjalne
		prepStatus=false; // warto�ci inicjalne
	}
	
	public void exec(LogoOutput logoOut) {
		if(logoOut==null) {
			System.out.println("No output");
			return;
		}
		
		prepStatus=prepare();
		
		if(!prepStatus) {
			System.out.println("Program preparation error");
			return;
		}
		
		//logoOut=new LogoOutput();
		
		// Uruchomienie g��wnej procedury
		execResult=mainProc.execute(logoOut);
		
		System.out.println("status: "+execResult);		
	}

	
	public LogoProgram(File prFile) {
		System.out.println("LogoProgram "+prFile.getPath()+" "+prFile.getName());
		
		init();
		
		FileInputStream in=null;
		BufferedReader bReader=null;
		
		try {
			File file=prFile;
			in=new FileInputStream(file);
			bReader=new BufferedReader(new InputStreamReader(in));
			String ln=null;
			while((ln=bReader.readLine())!=null) {
				fileArr.add(ln);
			}		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}					
            }
		}
		System.out.println(fileArr);
		
		//exec();
	}
	
	public boolean prepare() {
		LogoParser logoStatPar=null;
		LogoStmt logoStat=null;
		LogoStmtRepeat logoStatRep=null;
		LogoStmtIf logoStatIf=null;
		LogoStmtProc logoStatProc=null;
		LogoStmtProcEnd logoStatProcEnd=null;
		LogoStmtCall logoStatCall=null;
		Stack<LogoStmtRepeat> repeatStack=new Stack<LogoStmtRepeat>();
		Stack<LogoStmtIf> ifStack=new Stack<LogoStmtIf>();
		
		// KROK 1. Analiza pliku
		for(int i=0;i<fileArr.size();i++) {			
			logoStatPar=new LogoParser(this,i);
			statParserArr.add(logoStatPar);
			logoStat=logoStatPar.getStatement();
			if(logoStat!=null) {
				statArr.add(logoStat);
				// Obs�uga szczeg�lnych polece� - procedur i p�tli
				switch(logoStat.getCmd()) {
				case "REPEAT" : 
					repeatStack.push((LogoStmtRepeat)logoStat);
					System.out.println(repeatStack);
					continue;
				case "ENDREPEAT" :
					if(repeatStack.isEmpty()) {
						System.out.println("Brakuj�ca p�tla "+logoStat);
						logoStatPar.setError("Missing REPEAT for ENDREPEAT");
						return false;
					}
					logoStatRep=repeatStack.pop();
					((LogoStmtRepEnd)logoStat).setStart(logoStatRep);
					logoStatRep.setEnd((LogoStmtRepEnd)logoStat); 
					System.out.println(repeatStack);
					continue;					
				case "PROC" :
					if(logoStatProc!=null) {
						//b��d - nieprawid�owy uk�ad deklaracji PROC ... ENDPROC
						System.out.println("Nieprawid�owe polecenie PROC "+logoStat);
						logoStatPar.setError("PROC without ENDPROC");
						return false;
					}
					logoStatProc=(LogoStmtProc)logoStat;
					procMap.put(logoStatProc.getName(), logoStatProc);
					System.out.println(procMap);
					continue;
				case "ENDPROC" :
					logoStatProcEnd=(LogoStmtProcEnd)logoStat;
					// zak�adamy, �e logoStatProc zosta�o zainicjalozowane w case "PROC"
					if(logoStatProc==null) {
						// b��d - nieprawid�owy uk�ad deklaracji PROC ... ENDPROC
						System.out.println("Nieprawid�owe polecenie ENDPROC "+logoStat);
						logoStatPar.setError("Missing PROC for ENDPROC");
						return false;						
					}
					logoStatProcEnd.setStart(logoStatProc);
					logoStatProc.setEnd(logoStatProcEnd);
					logoStatProc=null;
					continue;
				case "IF" : 
					ifStack.push((LogoStmtIf)logoStat);
					System.out.println(ifStack);
					continue;
				case "ELSE" :
					if(ifStack.isEmpty()) {
						System.out.println("Brakuj�ce polecenie IF "+logoStat);
						logoStatPar.setError("Missing IF for ENDIF");
						return false;
					}
					// zostawiamy IF na stosie wywo�a�, czekamy na ENDIF
					logoStatIf=ifStack.peek();
					if(logoStatIf.getElse()!=null) {
						System.out.println("Wielokrotne ELSE "+logoStat);
						logoStatPar.setError("Multiple ELSE for IF");
						return false;						
					}
					logoStatIf.setElse((LogoStmtElse)logoStat); 
					((LogoStmtElse)logoStat).setIf(logoStatIf);
					System.out.println(ifStack);
					continue;					
				case "ENDIF" :
					if(ifStack.isEmpty()) {
						System.out.println("Brakuj�ce polecenie IF "+logoStat);
						logoStatPar.setError("Missing IF for ENDIF");
						return false;
					}
					// zdejmujemy IF ze stosu
					logoStatIf=ifStack.pop();
					logoStatIf.setEndIf((LogoStmtEndIf)logoStat); 
					System.out.println(ifStack);
					continue;					
				}
			} else {
				if(logoStatPar.getError()!="Not parsed") {					
					return false;
				}
			}
		}
		
		if(statArr.size()==0) {
			System.out.println("Brak polece� do wykonania");
			return false;
		}
		
		if(!repeatStack.isEmpty()) {
			System.out.println("Niedomkni�ta p�tla");
			return false;
		}
		
		if(!ifStack.isEmpty()) {
			System.out.println("Niefomkni�te polecenie IF");
			return false;
		}
		// KROK 2. U�o�enie kolejki polece�
		for(int i=0;i<statArr.size()-1;i++) { // bez ostatniego elementu
			statArr.get(i).setNext(statArr.get(i+1));
		}
		statArr.get(statArr.size()-1).setNext(null); // ostatni element
		
		// KROK 3. Przypisanie procedur do wywo�a�
		// Wcze�niej nie ma sensu z powodu mo�liwych krzy�owych wywo�a�
		logoStat=statArr.get(0);
		while(logoStat!=null) {
			if(logoStat.getCmd()=="CALL") {
				logoStatCall=(LogoStmtCall)logoStat;
				logoStatProc=procMap.get(logoStatCall.getName());				
				logoStatCall.setProc(logoStatProc);
			}
			logoStat=logoStat.getNext();
		}
		
		// KROK 4. Znalezienie pierwszego polecenia poza deklaracjami procedur
		// Mo�liwe by�oy po��czenie z innymi krokami, ale takie podej�cie jest bardziej przejrzyste
		logoStat=statArr.get(0);
		while(logoStat!=null) {
			while(logoStat.getCmd()=="PROC") {
				// przesuwamy wska�nik na koniec - omijamy procedur�
				// w tym miejscu �a�cuch wywo�a� nie jest przerwany i mo�emy to wykona�
				logoStat=((LogoStmtProc)logoStat).getEnd().getNext(); 
			} 
			if(logoStat.getCmd()!=null) {
				startStatement=logoStat;
				break;
			}
			logoStat=logoStat.getNext();
		}
		
		// KROK 5. Omini�cie deklaracji procedur w �a�cuchu wywo�a�
		// - czyli wyizolowanie g��wnego programu
		logoStat=startStatement;
		LogoStmt logoStatPrev=null;
		LogoStmt procEndStat=null;
		while(logoStat!=null) {
			if(logoStat.getCmd()=="PROC") {
				// przesuwamy wska�nik na koniec - omijamy procedur�
				procEndStat=((LogoStmtProc)logoStat).getEnd();
				// pobieramy kolejne polecenie po ENDPROC
				if(logoStatPrev==null) {
					logoStat=procEndStat.getNext();
				} else {
					logoStatPrev.setNext(procEndStat.getNext()); 
					logoStat=logoStatPrev.getNext();					
				}
			} else {
				logoStatPrev=logoStat;
				if(logoStat.getCmd()=="LET") {
					((LogoStmtLet)logoStat).setGlobal();
				}
				logoStat=logoStat.getNext();
			}
		}
		
		// KROK 6. Przerwanie �acucha wywo�a� dla ENDPROC - procedura musi si� sko�czy� na ENDPROC
		// To by�oby mo�liwe wcze�niej, ale wykonanie tego na koniec upraszcza inne kroki 
		for(int i=0;i<statArr.size();i++) {
			logoStat=statArr.get(i);
			if(logoStat.getCmd()=="ENDPROC") {
				logoStat.setNext(null);
			}
		}
	
		// Utworzenie g��wnej procedury ze wskazaniem na pierwsze polecenie
		System.out.println("START:"+startStatement);
		mainProc=new LogoProcedure(startStatement);
		
		return true;
	}
	
}
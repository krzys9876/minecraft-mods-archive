package com.krzyskrzys.logo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.HashMap;
//import java.lang.reflect.Method;


public class LogoParser {
	
	static HashMap<String,String> stmtMap;
	static {
		stmtMap=new HashMap<String,String>();
		stmtMap.put("FORWARD","setCmdForward");
		stmtMap.put("MOVE","setCmdMove");
		stmtMap.put("BACK","setCmdBack");
		stmtMap.put("LEFT","setCmdLeft");
		stmtMap.put("RIGHT","setCmdRight");								
		stmtMap.put("UP","setCmdUp");								
		stmtMap.put("DOWN","setCmdDown");
		stmtMap.put("POS","setCmdPos");
		stmtMap.put("ANGLE","setCmdAngle");
		stmtMap.put("REPEAT","setCmdRepeat");
		stmtMap.put("ENDREPEAT","setCmdRepEnd");
		stmtMap.put("PROC","setCmdProc");
		stmtMap.put("ENDPROC","setCmdProcEnd");
		stmtMap.put("CALL","setCmdCall");
		stmtMap.put("PEN","setCmdPen");		
		stmtMap.put("PENDOWN","setCmdPenDown");		
		stmtMap.put("PENUP","setCmdPenUp");
		stmtMap.put("LET","setCmdLet");
		stmtMap.put("IF","setCmdIf");
		stmtMap.put("ELSE","setCmdElse");
		stmtMap.put("ENDIF","setCmdEndIf");
}

	
	String cmd;	
	private boolean status;
	private String err;
	ArrayList<String> params;
	private LogoStmt stat; 
	
	public boolean getStatus() {
		return status;
	}
	
	public String getError() {
		return err;
	}
	
	public void setError(String nErr) {
		err=nErr;
	}

	public LogoStmt getStatement() {
		return stat;
	}

	
	public LogoParser(LogoProgram prog, int lineNum) {
		cmd=null; // wstêpnie brak komendy
		status=false; // wstêpnie status=b³¹d
		err="Not parsed";
		params=new ArrayList<String>();
		stat=null;
		
		setStatement(prog,lineNum);
		
		System.out.println(toString());
	}
	
	public void setStatement(LogoProgram prog, int lineNum) {
		String line=prog.getLine(lineNum);
		StringTokenizer strTok=new StringTokenizer(line);
		if(!strTok.hasMoreTokens()) {
			status=false;
			return; // pozostaje status b³¹d i pusta komenda
		}
		cmd=strTok.nextToken().toUpperCase(); // komenda
		if(line!=null && !stmtMap.containsKey(cmd)) {
			// je¿eli s³owo kluczowe nie jest rozpoznane, to zak³adamy, ¿e to jest polecenie LET
			// w ten sposób LET staje siê opcjonalne, a pierwszy ci¹g staje siê parametrem polecenia LET
			params.add(cmd.toUpperCase());
			cmd="LET";
		}
		while(strTok.hasMoreTokens()) {
			params.add(strTok.nextToken().toUpperCase());
		}

		// szukamy odpowiedniej metody i wywo³ujemy j¹ dynamicznie
		if(stmtMap.containsKey(cmd)) {
			try {
				Method m=this.getClass().getDeclaredMethod((String)stmtMap.get(cmd), int.class);
				// konieczne, bo metoda jest prywatna
				m.setAccessible(true);
				// wywo³anie
				status=(boolean)(m.invoke(this,lineNum));
				// wyj¹tki nie powinny zdarzyæ, je¿eli nazwy metod s¹ prawid³owe
			} catch(NoSuchMethodException e) {e.printStackTrace();} 
				catch(IllegalAccessException e) {e.printStackTrace();} 
				catch (IllegalArgumentException e) {e.printStackTrace();} 
				catch (InvocationTargetException e) {e.printStackTrace();}
		}
		// zamiast powy¿szego mo¿na u¿yæ klasycznej instrukcji switch..case
		// nie jest to rozwi¹zanie optymalne, ale przy wykonywaniu na serwerze minecrafta jest to pomijalne
			
			
		/*
		switch(cmd) {
		case "FORWARD" : status=setCmdForward(lineNum); break;
		case "MOVE" : status=setCmdMove(lineNum); break;
		case "BACK" : status=setCmdBack(lineNum); break;
		case "LEFT" : status=setCmdLeft(lineNum); break;
		case "RIGHT" : status=setCmdRight(lineNum); break;								
		case "UP" : status=setCmdUp(lineNum); break;								
		case "DOWN" : status=setCmdDown(lineNum); break;
		case "POS" : status=setCmdPos(lineNum); break;
		case "ANGLE" : status=setCmdAngle(lineNum); break;
		case "REPEAT" : status=setCmdRepeat(lineNum); break;
		case "ENDREPEAT" : status=setCmdRepEnd(lineNum); break;
		case "PROC" : status=setCmdProc(lineNum); break;
		case "ENDPROC" : status=setCmdProcEnd(lineNum); break;
		case "CALL" : status=setCmdCall(lineNum); break;
		case "PEN" : status=setCmdPen(lineNum); break;		
		case "PENDOWN" : status=setCmdPenDown(lineNum); break;		
		case "PENUP" : status=setCmdPenUp(lineNum); break;
		case "LET" : status=setCmdLet(lineNum); break;
		}*/
		
		return;
	}
	
	@SuppressWarnings("unused")
	private boolean setCmdForward(int lineNum) {
		if(params.size()!=1) {
			// b³êdna liczba parametrów
			err="Incorrect parameters";
			return false;
		}
		err=null;
		stat=new LogoStmtForward(lineNum,params.get(0));
		return true;
	}
	
	@SuppressWarnings("unused")
	private boolean setCmdMove(int lineNum) {
		if(params.size()!=1) {
			// b³êdna liczba parametrów
			err="Incorrect parameters";
			return false;
		}
		err=null;
		stat=new LogoStmtMove(lineNum,params.get(0));
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdBack(int lineNum) {
		if(params.size()!=1) {
			// b³êdna liczba parametrów
			err="Incorrect parameters";
			return false;
		}
		err=null;
		stat=new LogoStmtBack(lineNum, params.get(0));
		return true;
	}
		
	@SuppressWarnings("unused")
	private boolean setCmdLeft(int lineNum) {
		if(params.size()!=1) {
			// b³êdna liczba parametrów
			err="Incorrect parameters";
			return false;
		}
		err=null;
		stat=new LogoStmtLeft(lineNum, params.get(0));
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdRight(int lineNum) {
		if(params.size()!=1) {
			// b³êdna liczba parametrów
			err="Incorrect parameters";
			return false;
		}
		err=null;
		stat=new LogoStmtRight(lineNum, params.get(0));
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdUp(int lineNum) {
		if(params.size()!=1) {
			// b³êdna liczba parametrów
			err="Incorrect parameters";
			return false;
		}
		err=null;
		stat=new LogoStmtUp(lineNum, params.get(0));
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdDown(int lineNum) {
		if(params.size()!=1) {
			// b³êdna liczba parametrów
			err="Incorrect parameters";
			return false;
		}
		err=null;
		stat=new LogoStmtDown(lineNum, params.get(0));
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdPos(int lineNum) {
		if(params.size()!=3) {
			// b³êdna liczba parametrów
			err="Incorrect parameters";
			return false;
		}
		err=null;
		stat=new LogoStmtPos(lineNum, params.get(0),params.get(1),params.get(2));
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdAngle(int lineNum) {
		if(params.size()!=2) {
			// b³êdna liczba parametrów
			err="Incorrect parameters";
			return false;
		}
		err=null;
		stat=new LogoStmtAngle(lineNum, params.get(0),params.get(1));
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdRepeat(int lineNum) {
		if(params.size()!=1) {
			// b³êdna liczba parametrów
			err="Incorrect parameters";
			return false;
		}
		err=null;
		stat=new LogoStmtRepeat(lineNum, params.get(0));
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdRepEnd(int lineNum) {
		if(params.size()!=0) {
			// b³êdna liczba parametrów
			err="Incorrect parameters";
			return false;
		}
		err=null;
		stat=new LogoStmtRepEnd(lineNum); 
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdProc(int lineNum) {
		if(params.size()<1) { // na razie jedynym parametrem jest nazwa
			// b³êdna liczba parametrów
			err="Incorrect parameters";
			return false;
		}
		err=null;
		stat=new LogoStmtProc(lineNum, params.toArray(new String[params.size()]));
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdProcEnd(int lineNum) {
		if(params.size()!=0) {
			// b³êdna liczba parametrów
			err="Incorrect parameters";
			return false;
		}
		err=null;
		stat=new LogoStmtProcEnd(lineNum); 
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdCall(int lineNum) {
		if(params.size()<1) {
			// b³êdna liczba parametrów
			err="Incorrect parameters (0)";
			return false;
		}
		err=null;
		String[] pars=params.toArray(new String[] {});
		
		stat=new LogoStmtCall(lineNum,pars); 
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdPen(int lineNum) {
		if(params.size()!=1) {
			// b³êdna liczba parametrów
			err="Incorrect parameters (1)";
			return false;
		}
		err=null;
		stat=new LogoStmtPen(lineNum, params.get(0));
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdPenDown(int lineNum) {
		if(params.size()>0) {
			// b³êdna liczba parametrów
			err="Incorrect parameters (0)";
			return false;
		}
		err=null;
		stat=new LogoStmtPenDown(lineNum);
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdPenUp(int lineNum) {
		if(params.size()>0) {
			// b³êdna liczba parametrów
			err="Incorrect parameters (0)";
			return false;
		}
		err=null;
		stat=new LogoStmtPenUp(lineNum);
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdLet(int lineNum) {
		String paramsConcat="";
		for(int i=0;i<params.size();i++) {
			paramsConcat=paramsConcat+params.get(i).trim();
		}
		StringTokenizer strt=new StringTokenizer(paramsConcat,"=");
		
		if(strt.countTokens()!=2) {
			// b³êdna liczba parametrów
			err="Incorrect syntax (var=value)";
			return false;
		}
		String par1=strt.nextToken();
		String par2=strt.nextToken();
		err=null;
		stat=new LogoStmtLet(lineNum,par1,par2);
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdIf(int lineNum) {
		String paramsConcat="";
		// z³¹czenie w jeden parametr (wyra¿enie) z pominiêciem ostatniego
		for(int i=0;i<params.size()-1;i++) {
			paramsConcat=paramsConcat+params.get(i).trim();
		}
		
		String par1=paramsConcat;
		String par2=params.get(params.size()-1);
		
		if(par1.equals("") || par1==null || !par2.equalsIgnoreCase("THEN")) {
			// nieprawid³owa sk³adnia
			err="Incorrect syntax (IF expression THEN)";
			return false;			
		}

		err=null;
		// par2 ma tylko znaczenie formalne
		stat=new LogoStmtIf(lineNum,par1);
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdElse(int lineNum) {
		if(params.size()>0) {
			// b³êdna liczba parametrów
			err="Incorrect parameters (0)";
			return false;
		}
		err=null;
		stat=new LogoStmtElse(lineNum);
		return true;
	}

	@SuppressWarnings("unused")
	private boolean setCmdEndIf(int lineNum) {
		if(params.size()>0) {
			// b³êdna liczba parametrów
			err="Incorrect parameters (0)";
			return false;
		}
		err=null;
		stat=new LogoStmtEndIf(lineNum);
		return true;
	}

	/*private boolean checkIntParameter(int paramInd) {
		try {
			Integer.parseInt(params.get(0)); // tylko sprawdzenie			
		} catch(NumberFormatException e) {
			return false;
		}
		return true;		
	}*/
	
	/*private boolean checkDoubleParameter(int paramInd) {
		try {
			Double.parseDouble(params.get(0)); // tylko sprawdzenie			
		} catch(NumberFormatException e) {
			return false;
		}
		return true;		
	}*/
	
	public String toString() {
		return "parseCMD:"+cmd+";params:"+params+";status:"+status+";error:"+err;		
	}
}

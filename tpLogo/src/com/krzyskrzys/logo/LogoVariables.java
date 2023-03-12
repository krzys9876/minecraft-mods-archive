package com.krzyskrzys.logo;

import java.util.HashMap;

public class LogoVariables {
	
	private HashMap<String,LogoVariable> globalVar; // zmienne globalne
	private HashMap<String,LogoVariable> localVar; // zmienne lokalne
	
	public HashMap<String,LogoVariable> getGlobal() {
		return globalVar;
	}
	
	public HashMap<String,LogoVariable> getLocal() {
		return localVar;
	}

	public LogoVariables() {
		globalVar=new HashMap<String,LogoVariable>(); 
		localVar=new HashMap<String,LogoVariable>();
	}
	
	public LogoVariables(LogoVariables toCopy) {
		globalVar=toCopy.globalVar; // referencja do zmiennych globalnych 
		localVar=new HashMap<String,LogoVariable>(); // nowa tablica zmiennych lokalnych
	}
	
	public String toString() {
		return "local:"+localVar.toString()+";global:"+globalVar.toString();
	}

}

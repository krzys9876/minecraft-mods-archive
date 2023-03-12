package com.krzyskrzys.logo;

public class LogoLoop {
	private int stepCount; // na p�niej do rozwa�enia warunkowe obliczenie liczby powt�rze�
	private int currentStep;
	
	public LogoLoop() {
		init();
	}
	
	private void init() {
		setCount(0);
		reset();
	}
	
	// W momencie tworzenia p�tli w trakcie analizy programu nie znamy liczby powt�rze� (mo�e by� parametrem)
	// wi�c ten konstruktor nie powinien by� u�ywany
	public LogoLoop(int cnt) {
		init();
		setCount(cnt);
	}
	
	public void setCount(int cnt) {
		stepCount=cnt;
	}
		
	public boolean isCorrect() {
		return stepCount>0;
	}
	
	public boolean isRunning() {
		return currentStep>-1;
	}
	
	public void start() {
		if(stepCount>0) { // p�tla startuje tylko wtedy, gdy ma >=1 krok
			currentStep=0;
		} else {
			currentStep=-1;
		}
	}
	
	public boolean next() {
		if(currentStep>=stepCount-1) {
			// koniec p�tli (currentStep=stepCount-1, znak '>' jest nadmiarowy)
			reset();
			return false;				
		}
		// p�tla trwa
		currentStep=currentStep+1;
		return true;
	}

	public void reset() {
		currentStep=-1; // liczymy od 0
	}
		
	public String toString() {
		return "loop: steps="+stepCount+" curr.step="+currentStep;
	}
}

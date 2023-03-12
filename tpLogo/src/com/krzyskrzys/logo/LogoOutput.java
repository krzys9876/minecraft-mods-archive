package com.krzyskrzys.logo;

public class LogoOutput {
	protected double x;
	protected double y;
	protected double z; // pozycja
	
	protected double aH;
	protected double aV; // k¹t na p³aszczyŸnie (aH) i w pionie (aV)
	
	protected String pen;
	
	protected boolean penDown;
	
	protected LogoVariables variables;
	
	public LogoOutput() {
		setPos(0,0,0);
		setAngle(0,0);
		variables=new LogoVariables();
		pen="A"; // cokolwiek, przy innych implementacjach to mo¿e byæ kolor
		penDown=true; // pióro opuszczone
	}
	
	public void setPen(String nPen) {
		pen=nPen;
	}
	
	public void setPenDown(boolean pd) {
		penDown=pd;
	}
		
	public String getPen() {
		return pen;
	}
	
	public boolean getPenDown() {
		return penDown;
	}	

	public void drawLine(double len) {
		movePos(len);
	}
	
	public void move(double len) {
		movePos(len);
		
	}
	
	public void turn(double naH, double naV) {
		setAngle((aH+naH),(aV+naV));		
	}
	
	protected void movePos(double len) {
		// poruszamy siê na p³aszczyŸnie X-Z, Y to wysokoœæ
		
		double nx=x-len*Math.sin(Math.toRadians(aH))*Math.cos(Math.toRadians(aV));
		double nz=z+len*Math.cos(Math.toRadians(aH))*Math.cos(Math.toRadians(aV));
		double ny=y+len*Math.sin(Math.toRadians(aV));
		
		setPos(nx,ny,nz);

	}
	
	
	
	public void setPos(double nx, double ny, double nz) {
		x=nx;
		y=ny;
		z=nz;
	}
	
	public void setAngle(double naH, double naV) {
		aH=naH % 360.0;
		aV=naV % 360.0;
	}
		
	public LogoVariables getVariables() {
		return variables; 
	}
	
	// Do rozwa¿enia, czy to ma tak dzia³aæ przy wywo³ywaniu procedur
	public void setVariables(LogoVariables nVariables) {
		variables=nVariables; 
	}
	
	public String toString() {
		return "X="+Math.rint(x*10000)/10000+";Y="+Math.rint(y*10000)/10000+";Z="+Math.rint(z*10000)/10000+";aH="+aH+";aV="+aV+";var:"+variables;
	}	
}

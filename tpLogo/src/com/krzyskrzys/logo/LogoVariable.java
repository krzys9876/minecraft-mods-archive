package com.krzyskrzys.logo;

public class LogoVariable {
	
	private String valueStr;
	private Double valueDouble;
	private Long valueInt;
	private String type;
	
	public LogoVariable(String val) {
		setValue(val);
	}
	
	public LogoVariable(int val) {
		setValue(val);
	}

	public LogoVariable(double val) {
		setValue(val);
	}

	
	public String getString() {
		return valueStr;
	}
	
	public boolean isString() {
		return type=="S";
	}
	
	public Double getDouble() {
		return valueDouble;
	}	

	public boolean isDouble() {
		return type=="D" || type=="I";
	}
	
	public boolean isInt() {
		return type=="I";
	}

	public Long getInt() {
		return valueInt;
	}	

	public void setValue(String nValue) {
		valueStr=nValue;
		type="S";
		if(checkInteger()) {
			checkDouble(); // tylko sprawdzenie
			type="I";
		} else {
			if(checkDouble()) {
				type="D";
			}
		}		
	}
	
	public void setValue(double nValue) {
		valueStr=(new Double(nValue)).toString();
		valueDouble=new Double(nValue);
		if(checkInteger()) {
			type="I";
		} else {
			type="D";
		}
	}
	
	public void setValue(int nValue) {
		valueStr=(new Long(nValue)).toString();
		valueInt=new Long(nValue);
		checkDouble(); // tylko sprawdzenie
		type="I";
	}	

	public boolean checkDouble() {
		try {
			valueDouble=Double.parseDouble(valueStr); // tylko sprawdzenie			
		} catch(NumberFormatException e) {
			return false;
		}
		return true;		
	}
	
	public boolean checkInteger() {
		// Je¿eli nie jest Double, to nie jest te¿ liczb¹ ca³kowit¹
		if(!checkDouble()) {
			return false;
		}
		
		if(Math.rint(valueDouble.doubleValue())==valueDouble.doubleValue()) {
			valueInt=new Long(Math.round(valueDouble));
			return true;
		} 
		return false;		
	}
	
	public String toString() {
		return "("+type+")"+valueStr;
	}
}

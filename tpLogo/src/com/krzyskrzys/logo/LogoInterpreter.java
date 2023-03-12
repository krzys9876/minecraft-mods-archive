package com.krzyskrzys.logo;

import java.io.File;
//import java.util.ArrayList;

public class LogoInterpreter {

	public static void main(String[] args) {
		System.out.println("Hello logo");		
		LogoProgram prog=new LogoProgram(new File("c:\\MinecraftServer\\KR\\workspace\\tpLogo","prog6.logo"));
		//LogoProgram prog=new LogoProgram(new String[] {"POS 0 0 0","RIGHT 45","UP 45","FORWARD 50"});
		prog.exec(new LogoOutput());
		if(prog.getResult()) {
			System.out.println("ok");
		} else {
			System.out.println("exec error");
		}
		
		//LogoExpression ex=new LogoExpression("-5+8*(4-3)/((4*2+5)-7*(2+8))");
		//LogoExpression ex=new LogoExpression("(5+8*(4+(-3)))/((4*(-2)+5)-2*(2+1))");
		/*LogoOutput lo=new LogoOutput();
		
		lo.getVariables().getLocal().put("QQ", new LogoVariable("-2.0"));
		lo.getVariables().getLocal().put("Q", new LogoVariable("3.0"));
		LogoExpression ex=new LogoExpression("(1+QQ)*(5+Q)");
		
		if(ex.evaluate(lo)) {
			System.out.println(ex+";"+ex.getValue());
		} else {		
			System.out.println(ex+";"+"b³¹d");
		}*/
		
		/*LogoExpr ex=new LogoExpr("QQ-(12+1)<3*(-Q)&(1=1&3=3)&(10-3)>-9",true);
		//LogoBoolExpression ex=new LogoBoolExpression("QQ-(12+1)+3*(-Q)",false);
		//LogoExpression ex=new LogoExpression("(5+8*(4+(-3)))/((4*(-2)+5)-2*(2+1))");
		LogoOutput lo=new LogoOutput();
		
		lo.getVariables().getLocal().put("QQ", new LogoVariable("-2.0"));
		lo.getVariables().getLocal().put("Q", new LogoVariable("3.0"));
		//LogoExpression ex=new LogoExpression("(1+QQ)*(5+Q)");
		
		
		if(ex.evaluate(lo)) {
			System.out.println(ex+";"+ex.getBoolValue());
		} else {		
			System.out.println(ex+";"+"b³¹d");
		}*/

	}
	
}



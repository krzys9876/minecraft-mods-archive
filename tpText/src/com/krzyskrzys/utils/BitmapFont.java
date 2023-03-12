package com.krzyskrzys.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.IOException;

import com.krzyskrzys.utils.TpTextUtils;


public class BitmapFont {
	private byte[] fileArr;
	public int bmpOffset=0;
	public int bmpFileSize=0;
	public int bmpWidth=0;
	public int bmpHeight=0;
	public int fontH=0;
	public int fontW=0;
	public String fontMap="";
	public int mapH=0;
	public int mapW=0; 
	
	public BitmapFont(File bmpFile, File metaFile) {
		fileArr=new byte[0];
		
		FileInputStream in=null;
				
		try {
			File file=bmpFile;
			int size=(int)file.length();
			fileArr=new byte[size];			
			in=new FileInputStream(file);
			DataInputStream din=new DataInputStream(in);
			// plik jest ma³y, wiêc mo¿emy czytaæ go w ca³oœci
			din.readFully(fileArr);
		
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
		
		bmpFileSize=(int)TpTextUtils.getLong(fileArr,2);
		bmpOffset=(int)TpTextUtils.getLong(fileArr,10);
		bmpWidth=(int)TpTextUtils.getLong(fileArr,18);
		bmpHeight=(int)TpTextUtils.getLong(fileArr,22);
				
		FileReader reader=null;
		try {
			File file=metaFile;
			reader=new FileReader(file);
			BufferedReader bReader=new BufferedReader(reader);
			fontW=Integer.parseInt(bReader.readLine());
			fontH=Integer.parseInt(bReader.readLine());
			fontMap=bReader.readLine();
		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}					
            }
		}
		
		mapH=(int)bmpHeight/fontH;
		mapW=(int)bmpWidth/fontW;

	}
	
	public byte[] getArr() {
		return fileArr;
	}
	
	public boolean[][] getCharArr(String ch) {
		boolean[][] charArr=new boolean[fontW][fontH];
		int charInd=fontMap.indexOf(ch);
		if(charInd==-1) {
			charInd=
					fontMap.indexOf(" "); // nierozpoznany znak zamieniany jest na spacjê
		}
		int charR=charInd/mapW; //wiersz w bitmapie
		int charC=charInd%mapW; //kolumna w bitmapie
		int bitInd=charC*fontW+(mapH-1-charR)*mapW*fontW*fontH; // bit pocz¹tkowy
		int ind;
		
		for(int h=0;h<fontH;h++) {
			for(int w=0;w<fontW;w++) {
				ind=bitInd+(fontH-1-h)*mapW*fontW+w;
				int bit=TpTextUtils.getBit(fileArr[bmpOffset+ind/8],7-(ind)%8);
				charArr[w][h]=bit==0;
			}			
		}
		return charArr;
	}
	
	public boolean[][] getStringArr(String str) {
		boolean[][] charArr=new boolean[fontW][fontH];		
		boolean[][] strArr=new boolean[fontW*str.length()][fontH];
		
		for(int i=0;i<str.length();i++) {
			charArr=getCharArr(str.substring(i, i+1));
			for(int h=0;h<fontH;h++) {
				for(int w=0;w<fontW;w++) {
					strArr[i*fontW+w][h]=charArr[w][h];
				}
			}
		}
		return strArr;
	}
	
	
}

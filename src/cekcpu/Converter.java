package cekcpu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {
	
	public static int converterDecimal(String hex){
		return Integer.valueOf(hex, 16);
	}
	public static String findOpCode(String data,String opName){
		String[] result=data.split("\n");
		int index=0;
		for (String temp : result){
			index++;
			if(temp.indexOf(opName)>-1)
				break;
		}
		Pattern r=Pattern.compile("([0-9]x([0-9]|[A-F])*)");
		Matcher got=r.matcher(result[index]);
		if(got.find())
			return got.group();
		return null;
	}
	public static ArrayList<String> getOpHex(String opCode, String program){
		String[] result=null;
		ArrayList<String> returning=new ArrayList<String>();
		for (String temp : program.split("\n")){
			result=temp.split("\\s+");
			if(result.length != 1)
				returning.add(findOpCode(opCode,result[1].replace(";", "").toUpperCase()));
		}
		return returning;
	}
	public static void main(String[] args){
		maindua();
	}
	public static void main3(){
		String program=ReadFile.loadFile("program2.mcd");
		int index=0;
		ArrayList<String> result=new ArrayList<String>();
		for (String temp:program.split("\n")){
			if(temp.length() >1){
				if(index<10){
					result.add("[0"+index+"] "+temp);
				}
				else {
					result.add("["+index+"] "+temp);
				}
				index+=4;
			}
		}
		for(String i : result){
			System.out.println(i);
		}
	}
	public static void maindua(){
		String opCode=ReadFile.loadFile("instruction.txt");
		String program=ReadFile.loadFile("program.txt");
		ArrayList<String> result=getOpHex(opCode,program);
		String[] after=null;
		String exception=null,stringResult=null,stringFormatted=null;
		int index=0,tempLength=0;
		for (String temp : program.split("\n")){
			after=temp.split("\\s+");
			if(after.length != 1){
				try {
					for (String temporary : after[2].replace(";", "").split(",")){
						if(temporary.indexOf("r")>-1){
							temporary=temporary.replace("r","");
							result.set(index-1, result.get(index-1).concat(String.valueOf(Integer.toHexString(Integer.valueOf(temporary)))));
						}
						else {
							tempLength=result.get(index-1).length()-2;
							tempLength=8-tempLength;
							stringResult=String.valueOf(Integer.toHexString(Integer.valueOf(temporary)));
							tempLength=tempLength-stringResult.length();
							stringFormatted=new String("");
							for(int i=0;i<tempLength;i++)
								stringFormatted+="0";
							stringFormatted+=stringResult;
							result.set(index-1,result.get(index-1).concat(stringFormatted));
						}
					}
				} catch (Exception e){
					//System.out.println(result.size());
					//result.get(index).concat("000000");
				}
			}
			index++;
		}
		int tempIndex=0;
		for (String temp : result){
			if(temp.length() != 10){
				tempIndex=10-temp.length();
				for(int i=0;i<tempIndex;i++){
					temp+="0";
				}
			}
			System.out.println(temp);
		}
		System.out.println();
		for (String temp : result){
			if(temp.length() != 10){
				tempIndex=10-temp.length();
				for(int i=0;i<tempIndex;i++){
					temp+="0";
				}
			}
			System.out.println(converterDecimal(temp.replace("0x", "")));
		}
	}
}

package com.nosliw.datasource.school;

import java.io.BufferedReader;
import java.io.FileReader;

import com.nosliw.common.utils.HAPFileUtility;

public class HAPProcessRawSchoolData {

	private static String startToken = "new SchoolMapData(";
	private static String endToken = ");";
	
	public static void main(String[] argus){
		
		StringBuffer out = new StringBuffer();
		out.append("[\n");
		try{
			FileReader inputFile = new FileReader("elementSchool.js");
		    BufferedReader bufferReader = new BufferedReader(inputFile);
            String line;
            int i = 0;
            while ((line=bufferReader.readLine())!=null){
            	int index = line.indexOf(startToken);
            	if(index!=-1){
            		if(i!=0)  out.append(",\n");
            		int index1 = line.lastIndexOf(endToken);
            		String content = line.substring(index+startToken.length(), index1);
            		out.append("       [").append(content).append("]");
            		i++;
            	}
            }
            out.append("\n]");
            bufferReader.close(); 			
		}
		catch(Exception e){
			e.printStackTrace();
		}

		HAPFileUtility.writeFile("elementSchoolArray.js", out.toString());
		
	}
	
}

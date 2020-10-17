package com.nosliw.test.criteria;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.nosliw.data.core.data.criteria.HAPCriteriaUtility;

public class HAPCriteriaEscape {

	public static void main(String[] args) throws Exception{

		InputStreamReader r=new InputStreamReader(System.in);  
		BufferedReader br=new BufferedReader(r);  
		  
		System.out.println("Enter your string to escpate");  
		String content=br.readLine();  
		String escpateConent = HAPCriteriaUtility.escape(content);
		
		System.out.println("Output "+escpateConent);  		
	}
	
}

package com.nosliw.test.criteria;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.nosliw.core.data.criteria.HAPUtilityCriteria;

public class HAPCriteriaEscape {

	public static void main(String[] args) throws Exception{

		InputStreamReader r=new InputStreamReader(System.in);  
		BufferedReader br=new BufferedReader(r);  
		  
		System.out.println("Enter your string to escpate");  
		String content=br.readLine();  
		String escpateConent = HAPUtilityCriteria.escape(content);
		
		System.out.println("Output "+escpateConent);  		
	}
	
}

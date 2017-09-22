package com.nosliw.test.criteria;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.nosliw.data.core.criteria.HAPCriteriaUtility;

public class HAPCriteriaDeescape {

	public static void main(String[] args) throws Exception {
		InputStreamReader r=new InputStreamReader(System.in);  
		BufferedReader br=new BufferedReader(r);  
		  
		System.out.println("Enter your string to escpate");  
		String content=br.readLine();  
		String deEscpateConent = HAPCriteriaUtility.deescape(content);
		
		System.out.println("Output : ");
		System.out.println(deEscpateConent);  		
	}

}

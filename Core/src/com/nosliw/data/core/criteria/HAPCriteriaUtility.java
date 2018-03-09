package com.nosliw.data.core.criteria;

import java.util.Iterator;

import org.json.JSONObject;

import com.nosliw.data.core.expression.HAPVariableInfo;

public class HAPCriteriaUtility {

	public static String[][] mapping = {
		{":", ";;;"},
		{",", ";;"}
	};
	
	public static String escape(String content){
		String out = content;
		for(int i=0; i<mapping.length; i++){
			out = out.replaceAll(mapping[i][0], mapping[i][1]);
		}
		return out;
	}
	
	public static String deescape(String content){
		String out = content;
		for(int i=0; i<mapping.length; i++){
			out = out.replaceAll(mapping[i][1], mapping[i][0]);
		}
		return out;
	}
	
	  public static HAPDataTypeCriteria parseCriteria(String criteria){
		  return HAPCriteriaParser.getInstance().parseCriteria(criteria);
	  }	

}

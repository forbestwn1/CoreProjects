package com.nosliw.data.core.expression;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPExpressionUtility {

	
	  public static Map<String, HAPVariableInfo> buildVariablesInfoMapFromJson(JSONObject jsonObj) {
		  Map<String, HAPVariableInfo> out = new LinkedHashMap<String, HAPVariableInfo>(); 
			Iterator<String> its = jsonObj.keys();
			while(its.hasNext()){
				String name = its.next();
				String criteriaStr = jsonObj.optString(name);
				HAPDataTypeCriteria criteria = HAPCriteriaUtility.parseCriteria(criteriaStr);
				out.put(name, new HAPVariableInfo(criteria));
			}
		  return out;
	  }

	
}

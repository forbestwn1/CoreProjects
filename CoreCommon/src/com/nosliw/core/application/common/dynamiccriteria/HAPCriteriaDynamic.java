package com.nosliw.core.application.common.dynamiccriteria;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public interface HAPCriteriaDynamic extends HAPSerializable{

	public final static String TYPE = "type"; 

	public final static String TYPE_FACADE_SIMPLE = "facadeSimple";
	
	String getCriteriaType();
	
	public static HAPCriteriaDynamic parseDynamicCriteria(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPCriteriaDynamic out = null;
		
		String type = jsonObj.getString(TYPE);
		
		switch(type) {
		case TYPE_FACADE_SIMPLE:
			out = HAPCriteriaDynamicSimpleoFacade.parse(jsonObj, dataRuleMan); 
			break;
		}
		return out;
	}
}

package com.nosliw.core.application.dynamic;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.core.application.entity.brickcriteria.facade.HAPCriteriaBrickFacade;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public interface HAPDynamicDefinitionCriteria extends HAPSerializable{

	public final static String TYPE = "type"; 

	public final static String TYPE_FACADE_SIMPLE = "facadeSimple";
	
	String getCriteriaType();
	
	public static HAPDynamicDefinitionCriteria parseDynamicCriteria(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPDynamicDefinitionCriteria out = null;
		
		String type = jsonObj.getString(TYPE);
		
		switch(type) {
		case TYPE_FACADE_SIMPLE:
			out = HAPCriteriaBrickFacade.parse(jsonObj, dataRuleMan); 
			break;
		}
		return out;
	}
}

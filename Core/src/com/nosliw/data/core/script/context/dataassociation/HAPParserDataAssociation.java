package com.nosliw.data.core.script.context.dataassociation;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPDefinitionDataAssociationMapping;

public class HAPParserDataAssociation {

	public static HAPDefinitionDataAssociation buildObjectByJson(JSONObject asJson){
		String type = (String)asJson.opt(HAPDefinitionDataAssociation.TYPE);
		if(HAPBasicUtility.isStringEmpty(type))  type = HAPConstant.DATAASSOCIATION_TYPE_MAPPING;
		
		switch(type) {
		case HAPConstant.DATAASSOCIATION_TYPE_MAPPING:
			HAPDefinitionDataAssociationMapping out = new HAPDefinitionDataAssociationMapping();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			return out;
		}

		return null;
	}
	
}

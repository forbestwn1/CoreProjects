package com.nosliw.core.application.common.dataassociation.definition;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPDefinitionParserDataAssociation {

	public static HAPDefinitionDataAssociation buildDefinitionByJson(JSONObject asJson, HAPManagerDataRule dataRuleMan){
		if(asJson==null) {
			return null;
		}

		if(!HAPUtilityEntityInfo.isEnabled(asJson)) {
			return null;
		}
		
		String type = (String)asJson.opt(HAPDefinitionDataAssociation.TYPE);
		if(HAPUtilityBasic.isStringEmpty(type)) {
			type = HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING;
		}

		switch(type) {
		case HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING:
		{
			HAPDefinitionDataAssociationMapping out = HAPDefinitionParserMapping.parse(asJson, dataRuleMan); 
			return out;
		}
		case HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR:
		{
			HAPDefinitionDataAssociationMirror out = new HAPDefinitionDataAssociationMirror();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			return out;
		}
		case HAPConstantShared.DATAASSOCIATION_TYPE_NONE:
		{
			HAPDefinitionDataAssociationNone out = new HAPDefinitionDataAssociationNone();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			return out;
		}
		}

		return null;
	}
	
	public static void buildToDataAssociation(HAPDefinitionDataAssociation dataAssociationDef, JSONObject daJsonObj) {
		dataAssociationDef.buildEntityInfoByJson(daJsonObj);
		Object dirObj = daJsonObj.opt(HAPDefinitionDataAssociation.DIRECTION);
		if(dirObj!=null) {
			dataAssociationDef.setDirection((String)dirObj);
		}
	}
	
}

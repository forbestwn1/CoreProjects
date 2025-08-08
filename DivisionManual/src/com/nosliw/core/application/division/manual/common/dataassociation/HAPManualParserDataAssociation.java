package com.nosliw.core.application.division.manual.common.dataassociation;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionDataAssociation;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionDataAssociationMapping;

public class HAPManualParserDataAssociation {

	public static HAPDefinitionDataAssociation buildDefinitionByJson(JSONObject asJson){
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
			HAPDefinitionDataAssociationMapping out = new HAPDefinitionDataAssociationMapping();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			return out;
		}
		case HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR:
		{
			HAPManualDataAssociationMirror out = new HAPManualDataAssociationMirror();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			return out;
		}
		case HAPConstantShared.DATAASSOCIATION_TYPE_NONE:
		{
			HAPManualDataAssociationNone out = new HAPManualDataAssociationNone();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			return out;
		}
		}

		return null;
	}
	
}

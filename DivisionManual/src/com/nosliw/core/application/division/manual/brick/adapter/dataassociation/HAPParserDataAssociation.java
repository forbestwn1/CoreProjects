package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;

public class HAPParserDataAssociation {

	public static HAPDefinitionDataAssociation buildDefinitionByJson(JSONObject asJson){
		if(asJson==null) {
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
	
}

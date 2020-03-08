package com.nosliw.data.core.script.context.dataassociation;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPExecutableDataAssociationMapping;
import com.nosliw.data.core.script.context.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.script.context.dataassociation.mirror.HAPExecutableDataAssociationMirror;
import com.nosliw.data.core.script.context.dataassociation.none.HAPDefinitionDataAssociationNone;
import com.nosliw.data.core.script.context.dataassociation.none.HAPExecutableDataAssociationNone;

public class HAPParserDataAssociation {

	public static HAPExecutableDataAssociation buildExecutalbeByJson(JSONObject asJson) {
		if(asJson==null)   return null;
		
		String type = (String)asJson.opt(HAPDefinitionDataAssociation.TYPE);
		if(HAPBasicUtility.isStringEmpty(type))  type = HAPConstant.DATAASSOCIATION_TYPE_MAPPING;
		
		HAPExecutableDataAssociation out = null;
		switch(type) {
		case HAPConstant.DATAASSOCIATION_TYPE_MAPPING:
		{
			out = new HAPExecutableDataAssociationMapping();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			break;
		}
		case HAPConstant.DATAASSOCIATION_TYPE_MIRROR:
		{
			out = new HAPExecutableDataAssociationMirror();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			break;
		}
		case HAPConstant.DATAASSOCIATION_TYPE_NONE:
		{
			out = new HAPExecutableDataAssociationNone();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			break;
		}
		}

		return out;
	}
	
	public static HAPDefinitionDataAssociation buildDefinitionByJson(JSONObject asJson){
		if(asJson==null)   return null;
		
		String type = (String)asJson.opt(HAPDefinitionDataAssociation.TYPE);
		if(HAPBasicUtility.isStringEmpty(type))  type = HAPConstant.DATAASSOCIATION_TYPE_MAPPING;
		
		switch(type) {
		case HAPConstant.DATAASSOCIATION_TYPE_MAPPING:
		{
			HAPDefinitionDataAssociationMapping out = new HAPDefinitionDataAssociationMapping();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			return out;
		}
		case HAPConstant.DATAASSOCIATION_TYPE_MIRROR:
		{
			HAPDefinitionDataAssociationMirror out = new HAPDefinitionDataAssociationMirror();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			return out;
		}
		case HAPConstant.DATAASSOCIATION_TYPE_NONE:
		{
			HAPDefinitionDataAssociationNone out = new HAPDefinitionDataAssociationNone();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			return out;
		}
		}

		return null;
	}
	
}

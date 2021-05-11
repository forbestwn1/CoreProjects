package com.nosliw.data.core.dataassociation;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.dataassociation.mapping.HAPExecutableDataAssociationMapping;
import com.nosliw.data.core.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.dataassociation.mirror.HAPExecutableDataAssociationMirror;
import com.nosliw.data.core.dataassociation.none.HAPDefinitionDataAssociationNone;
import com.nosliw.data.core.dataassociation.none.HAPExecutableDataAssociationNone;

public class HAPParserDataAssociation {

	public static <T extends HAPExecutableTask> HAPExecutableWrapperTask<T> buildExecutableWrapperTask(JSONObject jsonObj, T task) {
		HAPExecutableWrapperTask<T> out = new HAPExecutableWrapperTask<T>();
		
		//process in task
		JSONObject taskJsonObj = jsonObj.optJSONObject(HAPExecutableWrapperTask.TASK);
		task.buildObject(taskJsonObj, HAPSerializationFormat.JSON);
		out.setTask(task);
		return out;
	}
	
	public static HAPExecutableDataAssociation buildExecutalbeByJson(JSONObject asJson) {
		if(asJson==null)   return null;
		
		String type = (String)asJson.opt(HAPDefinitionDataAssociation.TYPE);
		if(HAPBasicUtility.isStringEmpty(type))  type = HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING;
		
		HAPExecutableDataAssociation out = null;
		switch(type) {
		case HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING:
		{
			out = new HAPExecutableDataAssociationMapping();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			break;
		}
		case HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR:
		{
			out = new HAPExecutableDataAssociationMirror();
			out.buildObject(asJson, HAPSerializationFormat.JSON);
			break;
		}
		case HAPConstantShared.DATAASSOCIATION_TYPE_NONE:
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
		if(HAPBasicUtility.isStringEmpty(type))  type = HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING;
		
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

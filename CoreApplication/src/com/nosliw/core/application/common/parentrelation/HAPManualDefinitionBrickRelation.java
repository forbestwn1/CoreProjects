package com.nosliw.core.application.common.parentrelation;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

public abstract class HAPManualDefinitionBrickRelation extends HAPSerializableImp{

	public static String TYPE = "type";
	
	private String m_type;
	
	public HAPManualDefinitionBrickRelation(String type) {
		this.m_type = type;
	}
	
	public String getType() {    return this.m_type;    }
	
	public static HAPManualDefinitionBrickRelation parseRelation(JSONObject jsonObj) {
		HAPManualDefinitionBrickRelation out = null;
		String type = jsonObj.getString(HAPManualDefinitionBrickRelation.TYPE);
		if(type.equals(HAPConstantShared.MANUAL_RELATION_TYPE_VALUECONTEXT)) {
			out = new HAPManualDefinitionBrickRelationValueContext();
		}
		else if(type.equals(HAPConstantShared.MANUAL_RELATION_TYPE_ATTACHMENT)) {
			out = new HAPManualDefinitionBrickRelationAttachment();
		}
		else if(type.equals(HAPConstantShared.MANUAL_RELATION_TYPE_AUTOPROCESS)) {
			out = new HAPManualDefinitionBrickRelationAutoProcess();
		}
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}

}

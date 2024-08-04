package com.nosliw.core.application.common.parentrelation;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualDefinitionBrickRelationAttachment extends HAPManualDefinitionBrickRelation{

	public static final String MODE = "mode";
	
	private String m_mode;

	public HAPManualDefinitionBrickRelationAttachment() {
		super(HAPConstantShared.MANUAL_RELATION_TYPE_ATTACHMENT);
	}

	//way to merge with parent
	public String getMode() {   return this.m_mode;    }

	public HAPManualDefinitionBrickRelationAttachment mergeHard(HAPManualDefinitionBrickRelationAttachment relation) {
		if(relation!=null && relation.m_mode!=null) {
			this.m_mode = relation.m_mode;
		}
		return this;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(MODE, this.m_mode);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_mode = (String)jsonObj.opt(MODE);
		return true;  
	}
}

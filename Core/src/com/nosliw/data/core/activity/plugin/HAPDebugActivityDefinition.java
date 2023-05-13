package com.nosliw.data.core.activity.plugin;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivityNormal;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.mirror.HAPDefinitionDataAssociationMirror;

public class HAPDebugActivityDefinition extends HAPDefinitionActivityNormal{

	public HAPDebugActivityDefinition(String type) {
		super(type);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.setInputDataAssociation(new HAPDefinitionDataAssociationMirror());
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPDebugActivityDefinition out = new HAPDebugActivityDefinition(this.getActivityType());
		this.cloneToNormalActivityDefinition(out);
		return out;
	}
}

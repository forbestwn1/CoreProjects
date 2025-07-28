package com.nosliw.data.core.cronjob;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPDefinitionSchedule extends HAPSerializableImp{

	private JSONObject m_definition;
	
	public JSONObject getDefinition() {   return this.m_definition;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		this.m_definition = (JSONObject)json;
		return true;  
	}

}

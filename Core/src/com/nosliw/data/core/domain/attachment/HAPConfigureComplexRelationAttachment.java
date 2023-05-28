package com.nosliw.data.core.domain.attachment;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPConfigureComplexRelationAttachment extends HAPSerializableImp{

	public static final String MODE = "mode";
	
	private String m_mode;
	
	//way to merge with parent
	public String getMode() {   return this.m_mode;    }

	public void mergeHard(HAPConfigureComplexRelationAttachment configure) {
		if(configure.m_mode!=null)   this.m_mode = configure.m_mode;
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

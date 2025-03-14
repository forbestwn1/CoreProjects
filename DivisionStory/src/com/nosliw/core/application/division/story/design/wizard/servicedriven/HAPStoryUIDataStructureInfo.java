package com.nosliw.core.application.division.story.design.wizard.servicedriven;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;

@HAPEntityWithAttribute
public class HAPStoryUIDataStructureInfo extends HAPSerializableImp{

	@HAPAttribute
	public static final String CONTEXT = "context";

	private HAPValueStructureDefinitionGroup m_context;
	
	public HAPStoryUIDataStructureInfo() {
		this.m_context = new HAPValueStructureDefinitionGroup();
	}
	
	public HAPValueStructureDefinitionGroup getContext() {	return this.m_context;	}
	
	public void setContext(HAPValueStructureDefinitionGroup context) {   this.m_context = context;      }

	public HAPStoryUIDataStructureInfo cloneUIDataStructureInfo() {
		HAPStoryUIDataStructureInfo out = new HAPStoryUIDataStructureInfo();
//		if(this.m_context!=null) {
//			out.m_context = this.m_context.cloneValueStructureGroup();
//		}
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		JSONObject contextObj = jsonObj.optJSONObject(CONTEXT);
		if(contextObj!=null) {
//			this.m_context = new HAPValueStructureDefinitionGroup();
//			this.m_context.buildObject(contextObj, HAPSerializationFormat.JSON);
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, HAPUtilityJson.buildJson(this.m_context, HAPSerializationFormat.JSON));
	}
}

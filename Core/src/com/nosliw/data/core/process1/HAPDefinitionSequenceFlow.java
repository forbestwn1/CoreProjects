package com.nosliw.data.core.process1;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

//connection between two activity
@HAPEntityWithAttribute
public class HAPDefinitionSequenceFlow extends HAPSerializableImp{

	@HAPAttribute
	public static String TARGET = "target";
	
	private String m_target;
	
	public String getTarget() {   return this.m_target;  }
	
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			this.m_target = (String)jsonObj.opt(TARGET);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TARGET, m_target);
	}
	
	public HAPDefinitionSequenceFlow cloneSequenceFlowDefinition() {
		HAPDefinitionSequenceFlow out = new HAPDefinitionSequenceFlow();
		out.m_target = this.m_target;
		return out;
	}
}

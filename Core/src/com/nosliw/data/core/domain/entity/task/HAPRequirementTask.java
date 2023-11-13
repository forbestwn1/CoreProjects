package com.nosliw.data.core.domain.entity.task;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPRequirementTask extends HAPSerializableImp{

	@HAPAttribute
	public static final String INTERFACE = "interface";

	private String m_interface;
	
	public String getInterface() {   return this.m_interface;     }
	public void setInterface(String interface1) {    this.m_interface = interface1;      }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INTERFACE, this.m_interface);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_interface = (String)jsonObj.opt(INTERFACE);
		return true;  
	}

}

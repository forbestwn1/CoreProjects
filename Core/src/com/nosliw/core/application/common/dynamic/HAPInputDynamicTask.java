package com.nosliw.core.application.common.dynamic;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPInputDynamicTask extends HAPEntityInfoImp{

	@HAPAttribute
	public final static String DYNAMICTASK = "dynamicTask"; 
	
	private HAPRefDynamicTask m_dynamicTaskRef;

	public HAPRefDynamicTask getDyanmicTaskReference() {		return this.m_dynamicTaskRef;	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DYNAMICTASK, this.m_dynamicTaskRef.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_dynamicTaskRef = HAPRefDynamicTask.parse(jsonObj.getJSONObject(DYNAMICTASK)); 
		return true;  
	}
}

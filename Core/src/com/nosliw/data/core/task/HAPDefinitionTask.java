package com.nosliw.data.core.task;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

/**
 * Task is unit that can execute 
 * All information required when describe a task
 * Task can be a sequence of step or data source
 */
@HAPEntityWithAttribute
public abstract class HAPDefinitionTask extends HAPDefinitionComponent{

	@HAPAttribute
	public static String TYPE = "type";
	
	@HAPAttribute
	public static String INFO = "info";

	private HAPInfo m_info;
	
	public HAPDefinitionTask(){
	}
	
	public abstract String getType();
	
	//related information, for instance, description, 
	public HAPInfo getInfo(){  return this.m_info;  }
	
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		
		JSONObject infoObj = jsonObj.optJSONObject(INFO);
		if(infoObj!=null){
			this.m_info = new HAPInfoImpSimple();
			this.m_info.buildObject(infoObj, HAPSerializationFormat.JSON);
		}
		
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}
	
	public void cloneTo(HAPDefinitionTask taskDef){
		
	}
}

package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

@HAPEntityWithAttribute
public abstract class HAPExecutableActivity extends HAPExecutableImp{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String DEFINITION = "definition";
	
	private String m_id;
	
	private HAPDefinitionActivity m_activityDefinition;
	
	public HAPExecutableActivity(String id, HAPDefinitionActivity activityDef) {
		this.m_id = id;
		this.m_activityDefinition = activityDef;
	}
	
	public String getType() {   return this.getActivityDefinition().getType();  }
	
	public String getId() {  return this.m_id;  }
	
	public HAPDefinitionActivity getActivityDefinition() {   return this.m_activityDefinition;   }
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(DEFINITION, this.m_activityDefinition.toStringValue(HAPSerializationFormat.JSON));
	}

	//build map specific for resource
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {}
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		//build general json map first
		this.buildJsonMap(jsonMap, typeJsonMap);
		//build json map for resource, it may override general json map
		this.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	} 
}

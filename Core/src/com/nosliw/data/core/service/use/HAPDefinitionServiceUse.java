package com.nosliw.data.core.service.use;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPDefinitionServiceUse extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String PROVIDER = "provider";

	@HAPAttribute
	public static String SERVICEMAPPING = "serviceMapping";

	private String m_provider;

	private HAPDefinitionMappingService m_serviceMapping;
	
	public HAPDefinitionServiceUse() {}
	
	public HAPDefinitionMappingService getServiceMapping() {   return this.m_serviceMapping;    }
	
	public String getProvider() {   return this.m_provider;   }
	
	public void cloneBasicTo(HAPDefinitionServiceUse command) {
		this.cloneToEntityInfo(command);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROVIDER, this.m_provider);
		jsonMap.put(SERVICEMAPPING, HAPJsonUtility.buildJson(this.m_serviceMapping, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_serviceMapping = new HAPDefinitionMappingService();
		this.m_serviceMapping.buildObject(jsonObj.optJSONObject(SERVICEMAPPING), HAPSerializationFormat.JSON);
		this.m_provider = (String)jsonObj.opt(PROVIDER);
		return true;  
	}
}

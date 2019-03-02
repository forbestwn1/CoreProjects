package com.nosliw.data.core.service.use;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;

@HAPEntityWithAttribute
public class HAPDefinitionServiceUse extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String PROVIDER = "provider";

	@HAPAttribute
	public static String SERVICEMAPPING = "serviceMapping";

	private String m_provider;

	private HAPDefinitionMappingService m_serviceMapping;
	
	public HAPDefinitionServiceUse() {
		this.m_serviceMapping = new HAPDefinitionMappingService();
	}
	
	public HAPDefinitionMappingService getServiceMapping() {   return this.m_serviceMapping;    }
	public void setServiceMapping(HAPDefinitionMappingService serviceMapping) {  this.m_serviceMapping = serviceMapping;  }
	public void setParmMapping(HAPDefinitionDataAssociation parmMapping) {   this.m_serviceMapping.setParmMapping(parmMapping);    }
	public void addResultMapping(String name, HAPDefinitionDataAssociation result) {  this.m_serviceMapping.addResultMapping(name, result);   }
	
	public String getProvider() {   return this.m_provider;   }
	public void setProvider(String provider) {   this.m_provider = provider;   }
	
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

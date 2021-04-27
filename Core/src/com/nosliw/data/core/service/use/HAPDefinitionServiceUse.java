package com.nosliw.data.core.service.use;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.structure.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.structure.dataassociation.HAPDefinitionDataMappingTask;

@HAPEntityWithAttribute
public class HAPDefinitionServiceUse extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String INTERFACE = "interface";

	@HAPAttribute
	public static String PROVIDER = "provider";

	@HAPAttribute
	public static String DATAMAPPING = "dataMapping";

	private String m_provider;

	private HAPResourceId m_interfaceId;

	private HAPDefinitionDataMappingTask m_dataMapping;
	
	public HAPDefinitionServiceUse() {
		this.m_dataMapping = new HAPDefinitionDataMappingTask();
	}
	
	public HAPDefinitionDataMappingTask getDataMapping() {   return this.m_dataMapping;    }
	public void setServiceMapping(HAPDefinitionDataMappingTask dataMapping) {  this.m_dataMapping = dataMapping;  }
	public void setParmMapping(HAPDefinitionDataAssociation parmMapping) {   this.m_dataMapping.setInputMapping(parmMapping);    }
	public void addResultMapping(String name, HAPDefinitionDataAssociation result) {  this.m_dataMapping.addOutputMapping(name, result);   }
	
	public String getProvider() {   return this.m_provider;   }
	public void setProvider(String provider) {   this.m_provider = provider;   }
	
	public HAPResourceId getInterfaceId() {    return this.m_interfaceId;    }
	public void setInterfaceId(HAPResourceId resourceId) {   this.m_interfaceId = resourceId;   }
	
	public void cloneBasicTo(HAPDefinitionServiceUse command) {
		this.cloneToEntityInfo(command);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROVIDER, this.m_provider);
		jsonMap.put(DATAMAPPING, HAPJsonUtility.buildJson(this.m_dataMapping, HAPSerializationFormat.JSON));
		jsonMap.put(INTERFACE, HAPSerializeManager.getInstance().toStringValue(this.m_interfaceId, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_interfaceId = HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, jsonObj.opt(INTERFACE));
		this.m_dataMapping = new HAPDefinitionDataMappingTask();
		this.m_dataMapping.buildObject(jsonObj.optJSONObject(DATAMAPPING), HAPSerializationFormat.JSON);
		this.m_provider = (String)jsonObj.opt(PROVIDER);
		return true;  
	}
}

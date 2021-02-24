package com.nosliw.data.core.service.use;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataMappingTask;
import com.nosliw.data.core.service.interfacee.HAPUtilityServiceInterface;

@HAPEntityWithAttribute
public class HAPDefinitionServiceUse extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String INTERFACE = "interface";

	@HAPAttribute
	public static String PROVIDER = "provider";

	@HAPAttribute
	public static String DATAMAPPING = "dataMapping";

	private String m_provider;

	private HAPEntityOrReference m_interface;

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
	
	public HAPEntityOrReference getInterface() {    return this.m_interface;    }
	
	public void cloneBasicTo(HAPDefinitionServiceUse command) {
		this.cloneToEntityInfo(command);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROVIDER, this.m_provider);
		jsonMap.put(DATAMAPPING, HAPJsonUtility.buildJson(this.m_dataMapping, HAPSerializationFormat.JSON));
		jsonMap.put(INTERFACE, HAPSerializeManager.getInstance().toStringValue(this.m_interface, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_interface = HAPUtilityServiceInterface.parseInterface(jsonObj.opt(INTERFACE));
		this.m_dataMapping = new HAPDefinitionDataMappingTask();
		this.m_dataMapping.buildObject(jsonObj.optJSONObject(DATAMAPPING), HAPSerializationFormat.JSON);
		this.m_provider = (String)jsonObj.opt(PROVIDER);
		return true;  
	}
}

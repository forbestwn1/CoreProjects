package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.service.use.HAPDefinitionServiceInEntity;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceProvider;

public class HAPComponent extends HAPWithExternalMappingEntityInfoImp implements HAPWithServiceProvider{

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String CONTEXT = "context";
	
	@HAPAttribute
	public static String SERVICE = "services";
	
	private String m_id;

	//context definition within this component
	private HAPContextGroup m_context;
	
	//service definition
	private HAPDefinitionServiceInEntity m_serviceDefinition;

	public HAPComponent(String id) {
		this.m_id = id;
		this.m_serviceDefinition = new HAPDefinitionServiceInEntity();
		this.m_context = new HAPContextGroup();
	}
	
	public String getId() {   return this.m_id;   }
	 
	public HAPContextGroup getContext() {  return this.m_context;   }
	public void setContext(HAPContextGroup context) {  this.m_context = context;   }
	
	public void addServiceUseDefinition(HAPDefinitionServiceUse def) {  this.m_serviceDefinition.addServiceUseDefinition(def);   }
	@Override
	public void addServiceProviderDefinition(HAPDefinitionServiceProvider def) {  this.m_serviceDefinition.addServiceProviderDefinition(def);   }

	public Map<String, HAPDefinitionServiceUse> getServiceUseDefinitions(){   return this.m_serviceDefinition.getServiceUseDefinitions();    }
	@Override
	public Map<String, HAPDefinitionServiceProvider> getServiceProviderDefinitions(){  return this.m_serviceDefinition.getServiceProviderDefinitions();   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(this.m_context, HAPSerializationFormat.JSON));
		jsonMap.put(SERVICE, HAPJsonUtility.buildJson(this.m_serviceDefinition, HAPSerializationFormat.JSON));
	}
}

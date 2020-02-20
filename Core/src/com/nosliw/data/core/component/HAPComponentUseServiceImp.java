package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.service.use.HAPDefinitionServiceInEntity;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPWithServiceUse;

public abstract class HAPComponentUseServiceImp extends HAPComponentImp implements HAPWithServiceUse{

	//service definition
	private HAPDefinitionServiceInEntity m_serviceDefinition;

	public HAPComponentUseServiceImp(String id, HAPManagerActivityPlugin activityPluginMan) {
		super(id, activityPluginMan);
		this.m_serviceDefinition = new HAPDefinitionServiceInEntity();
	}
	
	@Override
	public void addServiceUseDefinition(HAPDefinitionServiceUse def) {  this.m_serviceDefinition.addServiceUseDefinition(def);   }
	@Override
	public void addServiceProviderDefinition(HAPDefinitionServiceProvider def) {  this.m_serviceDefinition.addServiceProviderDefinition(def);   }

	@Override
	public Map<String, HAPDefinitionServiceUse> getServiceUseDefinitions(){   return this.m_serviceDefinition.getServiceUseDefinitions();    }
	@Override
	public Map<String, HAPDefinitionServiceProvider> getServiceProviderDefinitions(){  return this.m_serviceDefinition.getServiceProviderDefinitions();   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SERVICE, HAPJsonUtility.buildJson(this.m_serviceDefinition, HAPSerializationFormat.JSON));
	}

}

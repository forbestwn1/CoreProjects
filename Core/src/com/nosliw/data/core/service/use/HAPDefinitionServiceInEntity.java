package com.nosliw.data.core.service.use;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPDefinitionServiceInEntity extends HAPSerializableImp{

	@HAPAttribute
	public static String USE = "use";

	@HAPAttribute
	public static String PROVIDER = "provider";

	//service requirement definition
	private Map<String, HAPDefinitionServiceUse> m_servicesUseDefinition;
	//service provider definition
	private Map<String, HAPDefinitionServiceProvider> m_servicesProviderDefinition;

	public HAPDefinitionServiceInEntity() {
		this.m_servicesUseDefinition = new LinkedHashMap<String, HAPDefinitionServiceUse>();
		this.m_servicesProviderDefinition = new LinkedHashMap<String, HAPDefinitionServiceProvider>();
	}
	
	public void addServiceUseDefinition(HAPDefinitionServiceUse def) {  this.m_servicesUseDefinition.put(def.getName(), def);   }
	public void addServiceProviderDefinition(HAPDefinitionServiceProvider def) {  this.m_servicesProviderDefinition.put(def.getName(), def);   }

	public Map<String, HAPDefinitionServiceUse> getServiceUseDefinitions(){  return this.m_servicesUseDefinition;   }
	public Map<String, HAPDefinitionServiceProvider> getServiceProviderDefinitions(){  return this.m_servicesProviderDefinition;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(USE, HAPUtilityJson.buildJson(this.m_servicesUseDefinition, HAPSerializationFormat.JSON));
		jsonMap.put(PROVIDER, HAPUtilityJson.buildJson(this.m_servicesProviderDefinition, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			JSONArray useJsonArray = jsonObj.optJSONArray(USE);
			if(useJsonArray!=null) {
				for(int i=0; i<useJsonArray.length(); i++) {
					HAPDefinitionServiceUse serviceUse = new HAPDefinitionServiceUse();
					serviceUse.buildObject(useJsonArray.getJSONObject(i), HAPSerializationFormat.JSON);
					this.m_servicesUseDefinition.put(serviceUse.getName(),serviceUse);
				}
			}

			JSONArray providerJsonArray = jsonObj.optJSONArray(PROVIDER);
			if(providerJsonArray!=null) {
				for(int i=0; i<providerJsonArray.length(); i++) {
					HAPDefinitionServiceProvider serviceProvider = new HAPDefinitionServiceProvider();
					serviceProvider.buildObject(providerJsonArray.getJSONObject(i), HAPSerializationFormat.JSON);
					this.m_servicesProviderDefinition.put(serviceProvider.getName(),serviceProvider);
				}
			}

			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}

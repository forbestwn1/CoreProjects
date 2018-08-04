package com.nosliw.miniapp.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.miniapp.data.HAPDefinitionData;
import com.nosliw.miniapp.service.HAPDefinitionService;

@HAPEntityWithAttribute
public class HAPRequireMiniApp extends HAPSerializableImp{

	@HAPAttribute
	public static final String DATA = "data";
	
	@HAPAttribute
	public static final String SERVICE = "service";

	//stateful data definition (the data that can retrieve next time you use the app)
	private Map<String, HAPDefinitionData> m_data;
	
	//service needed
	private Map<String, HAPDefinitionService> m_service;
	
	public HAPRequireMiniApp() {
		this.m_data = new LinkedHashMap<String, HAPDefinitionData>();
		this.m_service = new LinkedHashMap<String, HAPDefinitionService>();
	}

	public HAPDefinitionData getData(String name) {  return this.m_data.get(name);   }
	
	public HAPDefinitionService getService(String serviceName) {  return this.m_service.get(serviceName);  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(SERVICE, HAPJsonUtility.buildJson(this.m_service, HAPSerializationFormat.JSON));
		jsonMap.put(DATA, HAPJsonUtility.buildJson(this.m_data, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		JSONObject servicesJsonObj = jsonObj.optJSONObject(SERVICE);
		if(servicesJsonObj!=null) {
			for(Object key : servicesJsonObj.keySet()) {
				String serviceName = (String)key; 
				HAPDefinitionService service = HAPDefinitionService.buildObject(servicesJsonObj.getJSONObject(serviceName));
				this.m_service.put(serviceName, service);
			}
		}
		JSONObject dataJsonObj = jsonObj.optJSONObject(DATA);
		if(dataJsonObj!=null) {
			for(Object key : dataJsonObj.keySet()) {
				String dataName = (String)key; 
				HAPDefinitionData dataDef = HAPDefinitionData.buildObject(dataJsonObj.getJSONObject(dataName));
				this.m_data.put(dataName, dataDef);
			}
		}

		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}
}

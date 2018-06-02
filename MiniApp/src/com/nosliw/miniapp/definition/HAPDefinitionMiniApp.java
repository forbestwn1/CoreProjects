package com.nosliw.miniapp.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.miniapp.data.HAPDefinitionMiniAppData;
import com.nosliw.miniapp.service.HAPDefinitionMiniAppService;

@HAPEntityWithAttribute
public class HAPDefinitionMiniApp extends HAPSerializableImp{

	@HAPAttribute
	public static final String ID = "id";
	
	@HAPAttribute
	public static final String UIMODULES = "uiModules";
	
	@HAPAttribute
	public static final String UIENTRIES = "uiEntries";
	
	@HAPAttribute
	public static final String DATA = "data";
	
	@HAPAttribute
	public static final String SERVICES = "services";

	private String m_id;
	
	private Map<String, String> m_uiModules;

	private Map<String, HAPDefinitionMiniAppUIEntry> m_uiEntries;
	
	private Map<String, HAPDefinitionMiniAppData> m_data;
	
	private Map<String, HAPDefinitionMiniAppService> m_services;
	
	public HAPDefinitionMiniApp() {
		this.m_uiModules = new LinkedHashMap<String, String>();
		this.m_uiEntries = new LinkedHashMap<String, HAPDefinitionMiniAppUIEntry>();
		this.m_data = new LinkedHashMap<String, HAPDefinitionMiniAppData>();
		this.m_services = new LinkedHashMap<String, HAPDefinitionMiniAppService>();
	}
	
	public void setId(String id) {  this.m_id = id;   }
	
	public HAPDefinitionMiniAppUIEntry getUIEntry(String uiEntry) {  return this.m_uiEntries.get(uiEntry);  }

	public String getModuleIdByName(String moduleName) {  return this.m_uiModules.get(moduleName);   }
	
	public HAPDefinitionMiniAppData getData(String name) {  return this.m_data.get(name);   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(UIMODULES, HAPJsonUtility.buildJson(this.m_uiModules, HAPSerializationFormat.JSON));
		jsonMap.put(UIENTRIES, HAPJsonUtility.buildJson(this.m_uiEntries, HAPSerializationFormat.JSON));
		jsonMap.put(SERVICES, HAPJsonUtility.buildJson(this.m_services, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_uiModules =  HAPSerializeUtility.buildMapFromJsonObject(String.class.getName(), jsonObj.optJSONObject(UIMODULES));
		this.m_uiEntries =  HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionMiniAppUIEntry.class.getName(), jsonObj.optJSONObject(UIENTRIES));
		JSONObject servicesJsonObj = jsonObj.optJSONObject(SERVICES);
		if(servicesJsonObj!=null) {
			for(Object key : servicesJsonObj.keySet()) {
				String serviceName = (String)key; 
				HAPDefinitionMiniAppService service = HAPDefinitionMiniAppService.buildObject(servicesJsonObj.getJSONObject(serviceName));
				this.m_services.put(serviceName, service);
			}
		}
		JSONObject dataJsonObj = jsonObj.optJSONObject(DATA);
		if(dataJsonObj!=null) {
			for(Object key : dataJsonObj.keySet()) {
				String dataName = (String)key; 
				HAPDefinitionMiniAppData dataDef = HAPDefinitionMiniAppData.buildObject(servicesJsonObj.getJSONObject(dataName));
				this.m_data.put(dataName, dataDef);
			}
		}

		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}
	
}

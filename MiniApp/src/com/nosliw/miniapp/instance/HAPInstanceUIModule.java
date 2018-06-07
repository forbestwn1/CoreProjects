package com.nosliw.miniapp.instance;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.miniapp.service.HAPDefinitionMiniAppService;
import com.nosliw.uiresource.module.HAPUIModuleEntry;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitResource;

@HAPEntityWithAttribute
public class HAPInstanceUIModule  extends HAPSerializableImp{

	@HAPAttribute
	public static final String UIMODULEENTRY = "uiModuleEntry";
	
	@HAPAttribute
	public static final String DATA = "data";

	@HAPAttribute
	public static final String SERVICE = "service";

	private HAPUIModuleEntry m_moduleEntry;
	
	private Map<String, String> m_data;

	private Map<String, HAPDefinitionMiniAppService> m_service;

	public HAPInstanceUIModule(HAPUIModuleEntry moduleEntry) {
		this.m_data = new LinkedHashMap<String, String>();
		this.m_service = new LinkedHashMap<String, HAPDefinitionMiniAppService>();
		this.m_moduleEntry = moduleEntry;
	}
	
	public void setData(Map<String, String> data) {	this.m_data.putAll(data);	}
	
	public void addService(String serviceName, HAPDefinitionMiniAppService serviceDef) {	this.m_service.put(serviceName, serviceDef);	}

	public Map<String, HAPUIDefinitionUnitResource> getPages(){ return this.m_moduleEntry.getPages();   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DATA, HAPJsonUtility.buildJson(this.m_data, HAPSerializationFormat.JSON));
		jsonMap.put(SERVICE, HAPJsonUtility.buildJson(this.m_service, HAPSerializationFormat.JSON));
		jsonMap.put(UIMODULEENTRY, HAPJsonUtility.buildJson(this.m_moduleEntry, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_data =  HAPSerializeUtility.buildMapFromJsonObject(String.class.getName(), jsonObj.optJSONObject(DATA));
		this.m_service =  HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionMiniAppService.class.getName(), jsonObj.optJSONObject(SERVICE));
		this.m_moduleEntry = (HAPUIModuleEntry)HAPSerializeManager.getInstance().buildObject(HAPUIModuleEntry.class.getName(), jsonObj.optJSONObject(UIMODULEENTRY), HAPSerializationFormat.JSON);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}

}

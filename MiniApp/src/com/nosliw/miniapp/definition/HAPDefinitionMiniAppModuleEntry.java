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

@HAPEntityWithAttribute
public class HAPDefinitionMiniAppModuleEntry extends HAPSerializableImp{

	@HAPAttribute
	public static final String MODULE = "module";
	
	@HAPAttribute
	public static final String ENTRY = "entry";
	
	@HAPAttribute
	public static final String DATA = "data";
	
	@HAPAttribute
	public static final String SERVICE = "service";

	private String m_module;
	
	private String m_entry;
	
	private Map<String, String> m_data; 
	
	private Map<String, String> m_service;
	
	public HAPDefinitionMiniAppModuleEntry() {
		this.m_data = new LinkedHashMap<String, String>();
		this.m_service = new LinkedHashMap<String, String>();
	}
	
	public String getModule() {   return this.m_module;   }
	
	public String getEntry() {   return this.m_entry;    }
	
	public Map<String, String> getData(){   return this.m_data;  }
	
	public Map<String, String> getService(){   return this.m_service;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(MODULE, this.m_module);
		jsonMap.put(ENTRY, this.m_entry);
		jsonMap.put(DATA, HAPJsonUtility.buildJson(this.m_data, HAPSerializationFormat.JSON));
		jsonMap.put(SERVICE, HAPJsonUtility.buildJson(this.m_service, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_module = (String)jsonObj.opt(MODULE);
		this.m_entry = (String)jsonObj.opt(ENTRY);
		this.m_data =  HAPSerializeUtility.buildMapFromJsonObject(String.class.getName(), jsonObj.optJSONObject(DATA));
		this.m_service =  HAPSerializeUtility.buildMapFromJsonObject(String.class.getName(), jsonObj.optJSONObject(SERVICE));
		
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return this.buildObjectByFullJson(json);
	}
	
}

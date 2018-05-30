package com.nosliw.miniapp.definition;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPDefinitionMiniAppModuleEntry extends HAPSerializableImp{

	@HAPAttribute
	public static final String MODULE = "module";
	
	@HAPAttribute
	public static final String ENTRY = "entry";
	
	private String m_module;
	
	private String m_entry;
	
	private Map<String, String> m_data; 
	
	
	public String getModule() {   return this.m_module;   }
	
	public String getEntry() {   return this.m_entry;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(MODULE, this.m_module);
		jsonMap.put(ENTRY, this.m_entry);
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_module = (String)jsonObj.opt(MODULE);
		this.m_entry = (String)jsonObj.opt(ENTRY);
		
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return this.buildObjectByFullJson(json);
	}
	
}

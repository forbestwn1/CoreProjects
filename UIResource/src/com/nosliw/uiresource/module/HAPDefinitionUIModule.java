package com.nosliw.uiresource.module;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;

@HAPEntityWithAttribute
public class HAPDefinitionUIModule extends HAPSerializableImp{

	@HAPAttribute
	public static final String ID = "id";
	
	@HAPAttribute
	public static final String PAGES = "pages";
	
	@HAPAttribute
	public static final String MODULEENTRIES = "moduleEntries";
	
	private String m_id;
	
	private Map<String, String> m_pages;
	
	private Map<String, HAPDefinitionUIModuleEntry> m_moduleEntries;

	public String getId() {   return this.m_id;   }
	public void setId(String id) {  this.m_id = id;   }
	
	public Map<String, String> getPages(){  return this.m_pages;   }
	
	public HAPDefinitionUIModuleEntry getModuleEntry(String name) {  return this.m_moduleEntries.get(name);   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(PAGES, HAPJsonUtility.buildJson(this.m_pages, HAPSerializationFormat.JSON));
		jsonMap.put(MODULEENTRIES, HAPJsonUtility.buildJson(this.m_moduleEntries, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = (String)jsonObj.opt(ID);
		this.m_pages =  HAPSerializeUtility.buildMapFromJsonObject(String.class.getName(), jsonObj.optJSONObject(PAGES));
		this.m_moduleEntries =  HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionUIModuleEntry.class.getName(), jsonObj.optJSONObject(MODULEENTRIES));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return this.buildObjectByFullJson(json);
	}
	
}

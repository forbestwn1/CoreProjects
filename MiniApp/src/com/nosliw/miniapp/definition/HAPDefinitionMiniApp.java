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
public class HAPDefinitionMiniApp extends HAPSerializableImp{

	@HAPAttribute
	public static final String ID = "id";
	
	@HAPAttribute
	public static final String NAME = "name";

	@HAPAttribute
	public static final String ENTRY = "entry";
	
	@HAPAttribute
	public static final String REQUIRE = "require";
	
	private String m_id;

	private String m_name;

	//one mini app may have different entry for different senario. 
	private Map<String, HAPDefinitionMiniAppEntry> m_entries;

	//require (data, service)
	private HAPRequireMiniApp m_require;

	public HAPDefinitionMiniApp() {
		this.m_entries = new LinkedHashMap<String, HAPDefinitionMiniAppEntry>();
		this.m_require = new HAPRequireMiniApp();
	}
	
	public void setId(String id) {  this.m_id = id;   }
	
	public HAPDefinitionMiniAppEntry getEntry(String entry) {  return this.m_entries.get(entry);  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, m_name);
		jsonMap.put(ID, m_id);
		jsonMap.put(ENTRY, HAPJsonUtility.buildJson(this.m_entries, HAPSerializationFormat.JSON));
		jsonMap.put(REQUIRE, HAPJsonUtility.buildJson(this.m_require, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = jsonObj.optString(ID);
		this.m_name = jsonObj.optString(NAME);
		this.m_entries =  HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionMiniAppEntry.class.getName(), jsonObj.optJSONObject(ENTRY));
		this.m_require.buildObjectByJson(jsonObj.optJSONObject(REQUIRE));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}
	
}

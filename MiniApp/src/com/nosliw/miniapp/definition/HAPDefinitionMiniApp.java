package com.nosliw.miniapp.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.data.core.service.HAPQueryService;

@HAPEntityWithAttribute
public class HAPDefinitionMiniApp extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static final String ID = "id";
	
	@HAPAttribute
	public static final String ENTRY = "entry";
	
	@HAPAttribute
	public static final String SERVICEMAPPING = "serviceMapping";

	@HAPAttribute
	public static final String DATADEFINITION = "dataDefinition";
	
	private String m_id;

	//one mini app may have different entry for different senario. 
	private Map<String, HAPDefinitionMiniAppEntry> m_entries;

	//mapping between service name required by module and real service 
	private Map<String, HAPQueryService> m_serviceMapping;
	
	//stateful data definition (the data that can retrieve next time you use the app)
	private Map<String, HAPDefinitionMiniAppData> m_dataDefinition;

	public HAPDefinitionMiniApp() {
		this.m_entries = new LinkedHashMap<String, HAPDefinitionMiniAppEntry>();
		this.m_serviceMapping = new LinkedHashMap<String, HAPQueryService>();
		this.m_dataDefinition = new LinkedHashMap<String, HAPDefinitionMiniAppData>();
	}
	
	public void setId(String id) {  this.m_id = id;   }
	
	public HAPDefinitionMiniAppEntry getEntry(String entry) {  return this.m_entries.get(entry);  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, m_id);
		jsonMap.put(ENTRY, HAPJsonUtility.buildJson(this.m_entries, HAPSerializationFormat.JSON));
		jsonMap.put(SERVICEMAPPING, HAPJsonUtility.buildJson(this.m_serviceMapping, HAPSerializationFormat.JSON));
		jsonMap.put(DATADEFINITION, HAPJsonUtility.buildJson(this.m_dataDefinition, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = jsonObj.optString(ID);
		this.m_entries =  HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionMiniAppEntry.class.getName(), jsonObj.optJSONObject(ENTRY));
		this.m_serviceMapping =  HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionMiniAppEntry.class.getName(), jsonObj.optJSONObject(SERVICEMAPPING));
		this.m_dataDefinition =  HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionMiniAppEntry.class.getName(), jsonObj.optJSONObject(DATADEFINITION));
		return true;
	}
}

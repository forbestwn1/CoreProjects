package com.nosliw.miniapp.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.uiresource.common.HAPComponentWithConfiguration;

@HAPEntityWithAttribute
public class HAPDefinitionMiniApp extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static final String ID = "id";
	 
	@HAPAttribute
	public static final String ENTRY = "entry";
	
	@HAPAttribute
	public static final String DATADEFINITION = "dataDefinition";
	

	private String m_id;

	//one mini app may have different entry for different senario. 
	private Map<String, HAPDefinitionMiniAppEntry> m_entries;

	//global data definition 
	//it can be stateful data(the data that can retrieve next time you use the app)
	//or stateless (data generate during runtime)
	private Map<String, HAPDefinitionMiniAppData> m_dataDefinition;


	
	private HAPComponentWithConfiguration m_configure;
	
	public HAPDefinitionMiniApp() {
		this.m_entries = new LinkedHashMap<String, HAPDefinitionMiniAppEntry>();
		this.m_dataDefinition = new LinkedHashMap<String, HAPDefinitionMiniAppData>();
	}
	
	public void setId(String id) {  this.m_id = id;   }
	
	public HAPDefinitionMiniAppEntry getEntry(String entry) {  return this.m_entries.get(entry);  }
	public void addEntry(HAPDefinitionMiniAppEntry entry) {
		String name = entry.getName();
		if(HAPBasicUtility.isStringEmpty(name))  name = HAPUtilityMiniApp.ENTRY_DEFAULT;
		this.m_entries.put(name, entry);
	}
	

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, m_id);
		jsonMap.put(ENTRY, HAPJsonUtility.buildJson(this.m_entries, HAPSerializationFormat.JSON));
		jsonMap.put(DATADEFINITION, HAPJsonUtility.buildJson(this.m_dataDefinition, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = jsonObj.optString(ID);
	
		this.m_dataDefinition =  HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionMiniAppData.class.getName(), jsonObj.optJSONObject(DATADEFINITION));

		JSONArray entryArray = jsonObj.optJSONArray(HAPDefinitionMiniApp.ENTRY);
		if(entryArray!=null) {
			for(int i=0; i<entryArray.length(); i++) {
				HAPDefinitionMiniAppEntry entry = new HAPDefinitionMiniAppEntry();
				entry.buildObject(entryArray.get(i), HAPSerializationFormat.JSON);
				this.addEntry(entry);
			}
		}
		return true;
	}
}

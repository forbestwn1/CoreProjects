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
import com.nosliw.data.core.service.HAPQueryService;
import com.nosliw.uiresource.module.HAPInfoPage;

@HAPEntityWithAttribute
public class HAPDefinitionMiniApp extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static final String ID = "id";
	
	@HAPAttribute
	public static final String ENTRY = "entry";
	
	@HAPAttribute
	public static final String PAGEINFO = "pageInfo";
	
	@HAPAttribute
	public static final String SERVICEMAPPING = "serviceMapping";

	@HAPAttribute
	public static final String DATADEFINITION = "dataDefinition";
	
	private String m_id;

	//one mini app may have different entry for different senario. 
	private Map<String, HAPDefinitionMiniAppEntry> m_entries;

	// all the page required by app (name -- page id)
	private Map<String, HAPInfoPage> m_pagesInfo;

	//mapping between service name required by module and real service 
	private Map<String, HAPQueryService> m_serviceMapping;
	
	//global data definition 
	//it can be stateful data(the data that can retrieve next time you use the app)
	//or stateless (data generate during runtime)
	private Map<String, HAPDefinitionMiniAppData> m_dataDefinition;

	public HAPDefinitionMiniApp() {
		this.m_entries = new LinkedHashMap<String, HAPDefinitionMiniAppEntry>();
		this.m_serviceMapping = new LinkedHashMap<String, HAPQueryService>();
		this.m_dataDefinition = new LinkedHashMap<String, HAPDefinitionMiniAppData>();
		this.m_pagesInfo = new LinkedHashMap<String, HAPInfoPage>();
	}
	
	public void setId(String id) {  this.m_id = id;   }
	
	public HAPDefinitionMiniAppEntry getEntry(String entry) {  return this.m_entries.get(entry);  }
	public void addEntry(HAPDefinitionMiniAppEntry entry) {
		String name = entry.getName();
		if(HAPBasicUtility.isStringEmpty(name))  name = HAPUtilityMiniApp.ENTRY_DEFAULT;
		this.m_entries.put(name, entry);
	}
	
	public HAPInfoPage getPageInfo(String pageName) {  return this.m_pagesInfo.get(pageName);   }
	public void addPageInfo(HAPInfoPage pageInfo) {   this.m_pagesInfo.put(pageInfo.getName(), pageInfo);   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, m_id);
		jsonMap.put(ENTRY, HAPJsonUtility.buildJson(this.m_entries, HAPSerializationFormat.JSON));
		jsonMap.put(SERVICEMAPPING, HAPJsonUtility.buildJson(this.m_serviceMapping, HAPSerializationFormat.JSON));
		jsonMap.put(DATADEFINITION, HAPJsonUtility.buildJson(this.m_dataDefinition, HAPSerializationFormat.JSON));
		jsonMap.put(PAGEINFO, HAPJsonUtility.buildJson(this.m_pagesInfo, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = jsonObj.optString(ID);
	
		this.m_serviceMapping =  HAPSerializeUtility.buildMapFromJsonObject(HAPQueryService.class.getName(), jsonObj.optJSONObject(SERVICEMAPPING));
		this.m_dataDefinition =  HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionMiniAppData.class.getName(), jsonObj.optJSONObject(DATADEFINITION));

		JSONArray entryArray = jsonObj.optJSONArray(HAPDefinitionMiniApp.ENTRY);
		if(entryArray!=null) {
			for(int i=0; i<entryArray.length(); i++) {
				HAPDefinitionMiniAppEntry entry = new HAPDefinitionMiniAppEntry();
				entry.buildObject(entryArray.get(i), HAPSerializationFormat.JSON);
				this.addEntry(entry);
			}
		}

		JSONArray pageInfoArray = jsonObj.optJSONArray(HAPDefinitionMiniApp.PAGEINFO);
		if(pageInfoArray!=null) {
			for(int i=0; i<pageInfoArray.length(); i++) {
				HAPInfoPage pageInfo = new HAPInfoPage();
				pageInfo.buildObject(pageInfoArray.get(i), HAPSerializationFormat.JSON);
				this.addPageInfo(pageInfo);
			}
		}
		return true;
	}
}

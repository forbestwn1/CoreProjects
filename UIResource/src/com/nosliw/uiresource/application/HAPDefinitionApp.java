package com.nosliw.uiresource.application;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.uiresource.common.HAPComponentWithConfiguration;

@HAPEntityWithAttribute
public class HAPDefinitionApp extends HAPComponentWithConfiguration{

	@HAPAttribute
	public static final String ID = "id";

	@HAPAttribute
	public static final String CONTEXT = "context";

	@HAPAttribute
	public static final String ENTRY = "entry";
	
	@HAPAttribute
	public static final String APPLICATIONDATA = "applicationData";
	
	private String m_id;

	//one mini app may have different entry for different senario. 
	private Map<String, HAPDefinitionAppEntryUI> m_entries;

	//global data structure, logical
	private HAPContextGroup m_context;
	
	//global data definition 
	//it can be stateful data(the data that can retrieve next time you use the app)
	private Map<String, HAPDefinitionAppData> m_applicationData;

	public HAPDefinitionApp() {
		this.m_entries = new LinkedHashMap<String, HAPDefinitionAppEntryUI>();
		this.m_applicationData = new LinkedHashMap<String, HAPDefinitionAppData>();
	}
	
	public void setId(String id) {  this.m_id = id;   }
	
	public Map<String, HAPDefinitionAppData> getApplicationData(){   return this.m_applicationData;   }
	public void setApplicationData(Map<String, HAPDefinitionAppData> dataDef) {		if(dataDef!=null)   this.m_applicationData = dataDef;	}
	
	public HAPContextGroup getContext() {
		if(this.m_context==null)  this.m_context = new HAPContextGroup();
		return this.m_context;   
	}
	public void setContext(HAPContextGroup context) {   this.m_context = context;    }
	
	public HAPDefinitionAppEntryUI getEntry(String entry) {  return this.m_entries.get(entry);  }
	public void addEntry(HAPDefinitionAppEntryUI entry) {
		String name = entry.getName();
		if(HAPBasicUtility.isStringEmpty(name))  name = HAPUtilityApp.ENTRY_DEFAULT;
		this.m_entries.put(name, entry);
	}
	

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, m_id);
		jsonMap.put(ENTRY, HAPJsonUtility.buildJson(this.m_entries, HAPSerializationFormat.JSON));
		jsonMap.put(APPLICATIONDATA, HAPJsonUtility.buildJson(this.m_applicationData, HAPSerializationFormat.JSON));
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(this.m_context, HAPSerializationFormat.JSON));
	}
}

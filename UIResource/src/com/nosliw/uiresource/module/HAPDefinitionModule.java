package com.nosliw.uiresource.module;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionEmbededProcess;
import com.nosliw.data.core.script.context.HAPContextGroup;

/**
Module is a independent entity that is runnable within a container
It is a simple application which contains pages and transition between pages
Also be aware that module can be run in different container (pc browser, mobile browser, ...), try to make it platform independent
for instance, for a module that shows a school information, it contains two pages: list of school, school info
    when in mobile phone, the school info should overlap on top of list page
    when in pc, the list page and the school page should diplay sid by side

Don't need to define service information here, as service information will be gathered from all the mdoule ui definition
 */
@HAPEntityWithAttribute
public class HAPDefinitionModule extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String PAGEINFO = "pageInfo";

	@HAPAttribute
	public static String CONTEXT = "context";
	
	@HAPAttribute
	public static String UI = "ui";
	
	@HAPAttribute
	public static String PROCESS = "process";

	private String m_id;
	
	// all the page required by module (name -- page id)
	private Map<String, HAPInfoPage> m_pagesInfo;

	// data definition
	// hook up with real data during runtime
	private HAPContextGroup m_contextGroup;
	
	// all the module uis (name -- definition)
	private Set<HAPDefinitionModuleUI> m_uis;

	//processes (used for lifecycle, module command)
	private Map<String, HAPDefinitionEmbededProcess> m_processes;

	public HAPDefinitionModule(String id) {
		this.m_id = id;
		this.m_pagesInfo = new LinkedHashMap<String, HAPInfoPage>();
		this.m_uis = new HashSet<HAPDefinitionModuleUI>();
		this.m_processes = new LinkedHashMap<String, HAPDefinitionEmbededProcess>();
	}
	
	public String getId() {   return this.m_id;   }
	
	public HAPInfoPage getPageInfo(String pageName) {  return this.m_pagesInfo.get(pageName);   }
	public void addPageInfo(HAPInfoPage pageInfo) {   this.m_pagesInfo.put(pageInfo.getName(), pageInfo);   }
	
	public HAPContextGroup getContext() {  return this.m_contextGroup;   }
	public void setContext(HAPContextGroup contextGroup) {  this.m_contextGroup = contextGroup;   }
	
	public Map<String, HAPDefinitionEmbededProcess> getProcesses(){  return this.m_processes;  }
	public void addProcess(HAPDefinitionEmbededProcess processDef) {  this.m_processes.put(processDef.getName(), processDef);  }
	
	public Set<HAPDefinitionModuleUI> getUIs(){  return this.m_uis;  }
	public void addUI(HAPDefinitionModuleUI ui) {   this.m_uis.add(ui);   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(this.m_contextGroup, HAPSerializationFormat.JSON));
		jsonMap.put(PAGEINFO, HAPJsonUtility.buildJson(this.m_pagesInfo, HAPSerializationFormat.JSON));
		jsonMap.put(UI, HAPJsonUtility.buildJson(this.m_uis, HAPSerializationFormat.JSON));
		jsonMap.put(PROCESS, HAPJsonUtility.buildJson(this.m_processes, HAPSerializationFormat.JSON));
	}

}

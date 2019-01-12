package com.nosliw.uiresource.module;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionDataAssociationGroup;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPDefinitionSequenceFlow;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.script.context.HAPUtilityContext;
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

	// all the page required by module (name -- page id)
	private Map<String, HAPInfoPage> m_pagesInfo;

	// data definition
	// hook up with real data during runtime
	private HAPContextGroup m_contextGroup;
	
	// all the module uis (name -- definition)
	private Set<HAPDefinitionModuleUI> m_uis;

	//processes (used for lifecycle, module command)
	private Map<String, HAPDefinitionProcess> m_processes;

	public HAPDefinitionModule() {
		this.m_pagesInfo = new LinkedHashMap<String, HAPInfoPage>();
		this.m_uis = new HashSet<HAPDefinitionModuleUI>();
		this.m_processes = new LinkedHashMap<String, HAPDefinitionProcess>();
	}
	
	public HAPInfoPage getPageInfo(String pageName) {  return this.m_pagesInfo.get(pageName);   }
	
	public HAPContextGroup getContext() {  return this.m_contextGroup;   }
	public void setContext(HAPContextGroup contextGroup) {  this.m_contextGroup = contextGroup;   }
	
	public Map<String, HAPDefinitionProcess> getProcesses(){  return this.m_processes;  }
	public void addProcess(HAPDefinitionProcess processDef) {  this.m_processes.put(processDef.getName(), processDef);  }
	
	public Set<HAPDefinitionModuleUI> getUIs(){  return this.m_uis;  }
	public void addUI(HAPDefinitionModuleUI ui) {   this.m_uis.add(ui);   }
	
}

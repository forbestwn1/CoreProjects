package com.nosliw.uiresource.module;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.data.core.script.context.HAPContext;

//each module ui is page unit in module that is alive in a module
//as it defined:
//		what it look like
//		where data come from: service provider
//		how to interact with page : page event handler
@HAPEntityWithAttribute
public class HAPDefinitionModuleUI extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String PAGE = "page";
	
	@HAPAttribute
	public static String CONTEXTMAPPING = "contextMapping";
	
	@HAPAttribute
	public static String EVENTHANDLER = "eventHandler";
	
	
	//ui page
	private String m_page;

	//event handlers
	private Map<String, HAPDefinitionModuleUIEventHander> m_eventHandlers;
	
	//data mapping (from data definition in module to public data definition in page)
	private HAPContext m_contextMapping;
	
	//service name mapping (  service name in page -- service name in module   )
	private Map<String, String> m_serviceMapping;
	
	//provide extra information about this module ui so that container can render it properly
	private String m_type;
	
	public HAPDefinitionModuleUI() {
		this.m_eventHandlers = new LinkedHashMap<String, HAPDefinitionModuleUIEventHander>();
		this.m_serviceMapping = new LinkedHashMap<String, String>();
		this.m_contextMapping = new HAPContext();
	}
	
	public String getPage() {   return this.m_page;    }
	public void setPage(String page) {   this.m_page = page;   }
	   
	public HAPContext getContextMapping() {   return this.m_contextMapping;   }
	public void setContextMapping(HAPContext contextMapping) {   this.m_contextMapping = contextMapping;   }
	
	public Map<String, HAPDefinitionModuleUIEventHander> getEventHandlers(){   return this.m_eventHandlers;   }
	public void addEventHandler(String name, HAPDefinitionModuleUIEventHander eventHandler) {  this.m_eventHandlers.put(name, eventHandler);   }
	
	public Map<String, String> getServiceMapping(){    return this.m_serviceMapping;  }
	
}

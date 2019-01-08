package com.nosliw.uiresource.module;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;

//each module ui is page unit in module that is alive in a module
//as it defined:
//		what it look like
//		where data come from: service provider
//		how to interact with page : page event handler
public class HAPDefinitionModuleUI extends HAPEntityInfoImp{

	//provide extra information about this module ui so that container can render it properly
	private String m_type;
	
	//ui page
	private String m_page;
	
	//event handlers
	private Map<String, HAPDefinitionModuleUIEventHander> m_eventHandlers;
	
	//service name mapping (  service name in page -- service name in module   )
	private Map<String, String> m_serviceMap;

}

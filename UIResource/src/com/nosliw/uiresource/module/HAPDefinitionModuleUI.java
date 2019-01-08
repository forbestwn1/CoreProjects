package com.nosliw.uiresource.module;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.script.context.HAPContextGroup;

//each module ui is page unit in module that is alive in a module
//as it defined:
//		what it look like
//		where data come from: service provider
//		how to interact with page : page event handler
@HAPEntityWithAttribute
public class HAPDefinitionModuleUI extends HAPEntityInfoImp{

	@HAPAttribute
	public static String PAGE = "page";

	
	//ui page
	private String m_page;

	//data mapping (from data definition in module to data definition in page)
	private HAPContextGroup m_contextMapping;
	
	//event handlers
	private Map<String, HAPDefinitionModuleUIEventHander> m_eventHandlers;
	
	//service name mapping (  service name in page -- service name in module   )
	private Map<String, String> m_serviceMapping;
	
	//provide extra information about this module ui so that container can render it properly
	private String m_type;
	

}

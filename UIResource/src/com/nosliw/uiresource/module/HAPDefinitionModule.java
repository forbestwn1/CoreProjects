package com.nosliw.uiresource.module;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.script.context.HAPContext;

//Module is a independent entity that is runnable
//It is a simple application which contains pages and transition between pages
//Also be aware that module can be run in different container (pc browser, mobile browser, ...), try to make it platform independent
//Don't need to define service information here, as service information will be gathered from all the mdoule ui definition
public class HAPDefinitionModule extends HAPEntityInfoImp{

	// all the page required by module
	private Map<String, String> m_pages;
	
	// what to do when module just start
	private HAPDefinitionProcess m_initProcess;
	
	// all the module uis
	private Map<String, HAPDefinitionModuleUI> m_uis;

	// data definition
	// hook up with real data during runtime
	private HAPContext m_context;
}
